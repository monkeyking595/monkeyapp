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

import java.util.Optional;



@RestController
@RequestMapping("/payment")
public class PaymentController {

    private String webhookSecret;

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService=paymentService;
    }
    @GetMapping("/{paymentId}")
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
            Event event = Webhook.constructEvent( payload, sigHeader, webhookSecret);
            String eventType=event.getType();
            switch(eventType) {
                case "payment_intent.succeeded":
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
    

   
    
    

