package com.github.kerner1000.terra;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import feign.codec.DecodeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
public class ExceptionConfiguration extends ResponseEntityExceptionHandler {

    @ExceptionHandler({JsonMappingException.class, MismatchedInputException.class, DecodeException.class}) // Or whatever exception type you want to handle
    public ResponseEntity<Object> handleConverterErrors(JsonMappingException exception) { // Or whatever exception type you want to handle
        return ResponseEntity.internalServerError().build();
    }



}
