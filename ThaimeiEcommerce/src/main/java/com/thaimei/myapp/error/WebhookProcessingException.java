package com.thaimei.myapp.error;

public class WebhookProcessingException extends AppException{
    public WebhookProcessingException(String message) {
        super(message,500);
    }
    
}
