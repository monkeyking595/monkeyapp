package com.thaimei.myapp.error;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;
//global exception handler
@RestControllerAdvice
public class GlobalExceptionHandler  {
    //handles unchecked exceptions thrown from any part of the app
    //method level, handle specific exception passed as parameter here @ExceptionHandler(AppException.class)
    @ExceptionHandler(AppException.class)
    public ResponseEntity <?> handleException(AppException ex) {
        return ResponseEntity.status(ex.getStatusCode())
        .body(Map.of("error",ex.getMessage()));
    } 
    //handle unexpected errors (checked) used as a safety net 
    @ExceptionHandler(Exception.class) 
    public ResponseEntity<?> genericExceptionHandler(Exception ex) {
        return ResponseEntity.status(500)
        .body(Map.of("error","Something went wrong"));
    }
}
