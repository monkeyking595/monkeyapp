package com.thaimei.myapp.error;

public class UnAuthorizeException extends AppException {
    public UnAuthorizeException (String message) {
        super(message, 401);
    }
}
