package com.thaimei.myapp.configurations;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import com.stripe.Stripe;
@Configuration
public class StripeConfig {
    @Value("${stripe.secret.key}")
    private String secretKey;

    // The @PostConstruct annotation is used to indicate that the init() method should be executed after the bean's properties have been set. This is a good place to perform any initialization logic that depends on the injected properties.
    // why do this? because of sequence followed by Spring which call the constructor first and then injection of the properties, so if we try to use the secretKey in the constructor, it will be null. So we use @PostConstruct to ensure that the secretKey is set before we use it. 
    @PostConstruct
    public void init() {
        //calls the Static method of Stripe class and set it with secret key from the application properties.
        Stripe.apiKey=secretKey; 
    }

    // this will be called when creating a new PaymentIntent, so we need to attach the secretKey to the request for authentication purposes.
}
