package com.thaimei.myapp.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.service.PaymentService;
import com.thaimei.myapp.dto.PaymentDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.stripe.net.Webhook;
import com.stripe.model.Event;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;



@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService=paymentService;
    }

    @GetMapping("/customer/{paymentId}")
    public ResponseEntity<?> getPaymentDetails(@PathVariable String paymentId) {
        Optional<PaymentDto> dto= paymentService.getPaymentDetailsByPaymentId(paymentId);
        if(dto.isPresent()) {
            return ResponseEntity.ok(dto.get());
        }
        else {
            return ResponseEntity.status(404).body("Payment Details not found for this ID: "+ paymentId);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handlePaymentWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature")String sigHeader) {
        
        try {
            //Event is a class provided by Stripe to represent the webhook event received from Stripe, this is the result produced by webhook.
            //webhook is a also a class provided by Stripe to construct the event from the payload and signature header, everything happens here Deserialization of the data, recomputation of the signature and verification of the signature.
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            //event.getType() returns the String representation of the event type, which is sent by Stripe in the payload. This is used to determine what kind of event was received, so that we can handle it accordingly.
            String eventType=event.getType();

            //idempotency check: if the event has already been processed, we don't want to process it again. This is important because Stripe may send the same event multiple times in case of network issues or retries.
            if(paymentService.eventExists(event.getId())) {
                return ResponseEntity.ok("event already exists!");
            }

            switch(eventType) {
                
                case "payment_intent.succeeded":
                    //event.getDataObjectDeserializer().getObject(), this returns an generic object --Stripe doesn't know in advance what kind of thing is inside data.object, since it depends on the object type. so it needs to be casted.
                    //event.getDataObjectDeserializer().getObject(), this returns the actual object inside data.object, but it is wrapped in an Optional, so we need to call .orElse(null) to get the actual object or null if it is not present.
                    PaymentIntent successIntent=(PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

                    if(successIntent!=null) {
                        paymentService.savePaymentDetails(successIntent);
                    }
                break;

                case "payment_intent.payment_failed": 
                    PaymentIntent failedIntent=(PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                    if(failedIntent!=null) {
                        paymentService.savePaymentDetails(failedIntent);
                    }
                break;

                case "charge.refunded":
                    Charge refundedCharge=(Charge) event.getDataObjectDeserializer().getObject().orElse(null);
                    if(refundedCharge!=null) {
                        paymentService.savePaymentDetails(refundedCharge);
                    }
                    
                break;

                default: 
                    System.out.println("Unhandled event type: " + eventType);
                break;
            }

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("invalid signature" + e.getMessage());
        }

    }
    
        
}
    

   
    
    

