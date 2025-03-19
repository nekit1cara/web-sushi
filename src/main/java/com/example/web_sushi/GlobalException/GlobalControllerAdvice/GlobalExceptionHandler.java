package com.example.web_sushi.GlobalException.GlobalControllerAdvice;

import com.example.web_sushi.GlobalException.Exceptions.AlreadyExistException;
import com.example.web_sushi.GlobalException.Exceptions.CartExceptions;
import com.example.web_sushi.GlobalException.Exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleCartNotFoundException(NotFoundException ex ) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handleAlreadyExistException(AlreadyExistException ex ) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Произошла ошибка: отсутствует необходимый объект или поле", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CartExceptions.class)
    public ResponseEntity<String> handleCartException(CartExceptions ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
//    public ResponseEntity<String> handleCategoryException() {
//        return new ResponseEntity<>("Page not found" + ex, HttpStatus.BAD_REQUEST);
//    }

}
