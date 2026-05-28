package com.thaimei.myapp.error;

public class ResourceNotFoundException  extends AppException{
    public ResourceNotFoundException (String message) {
        //ResourceNotFoundException, this will always be 404 no matter what
        //status code is hardcoded because it never changes like message
        //message is passed as variable since the message will keep changing on the context of the error
        super(message, 404);
    }
    
}
