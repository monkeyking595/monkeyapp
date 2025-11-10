package com.thaimei.myapp.configurations;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import com.stripe.Stripe;
@Configuration
public class StripeConfig {
    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey=secretKey; //sets the api key with the secretKey we pulled from the application properties
    }


    
}
