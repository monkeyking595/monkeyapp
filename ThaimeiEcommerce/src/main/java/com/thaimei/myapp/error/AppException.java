package com.thaimei.myapp.error;
public class AppException extends RuntimeException {
    private int statusCode;
    public AppException(String message, int  statusCode) {
        //calling the parent constructor, the super() needs to be innitialized first, we're doing this because the parent constructor is a constructor with parameter
        //if the parent constructor were no-argument constructor we don't have to innitialized it manually 
        //java insert no-argument constructor first even if we don't manually write it in the code
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
    
}
