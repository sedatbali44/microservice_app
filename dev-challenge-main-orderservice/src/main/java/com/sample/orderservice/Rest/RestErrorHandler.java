package com.sample.orderservice.Rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.sample.orderservice.Exception.NotFoundException;
import com.sample.orderservice.Exception.OrderServiceException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestError> handleResourceNotAvailable(HttpServletRequest request, RuntimeException e) {
        RestError restError = new RestError(e.getMessage());
        return new ResponseEntity<RestError>(restError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = OrderServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestError> handlePersonServiceException(HttpServletRequest request, RuntimeException e) {
        RestError restError = new RestError(e.getMessage());
        return new ResponseEntity<RestError>(restError, HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    public static class RestError {
        private String message;
    }

}