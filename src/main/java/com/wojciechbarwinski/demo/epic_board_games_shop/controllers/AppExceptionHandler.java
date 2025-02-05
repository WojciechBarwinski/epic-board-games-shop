package com.wojciechbarwinski.demo.epic_board_games_shop.controllers;


import com.wojciechbarwinski.demo.epic_board_games_shop.exceptions.*;
import com.wojciechbarwinski.demo.epic_board_games_shop.security.exceptions.ApplicationSecurityException;
import com.wojciechbarwinski.demo.epic_board_games_shop.validations.ValidationError;
import com.wojciechbarwinski.demo.epic_board_games_shop.validations.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class AppExceptionHandler {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductsNotFoundException.class)
    public ErrorResponse<String> productsNotFoundException(ProductsNotFoundException exception) {

        return new ErrorResponse<>(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ApplicationSecurityException.class)
    public ErrorResponse<String> applicationSecurityException(ApplicationSecurityException exception) {

        return new ErrorResponse<>(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingCredentialsException.class)
    public ErrorResponse<String> missingCredentialsException(MissingCredentialsException exception) {

        return new ErrorResponse<>(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MappingToDTOException.class)
    public ErrorResponse<String> mappingToDTOException(MappingToDTOException exception) {

        return new ErrorResponse<>(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse<List<ValidationError>> validationException(ValidationException exception) {

        return new ErrorResponse<>(exception.getMessage(), exception.getErrors());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse<List<ValidationError>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<ValidationError> errors = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(new ValidationError(
                    error.getField(),
                    error.getDefaultMessage(),
                    error.getRejectedValue()));
        }

        return new ErrorResponse<>("Validation errors", errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductStockCouldNotBeDecreasedException.class)
    public ErrorResponse<String> productDecreaseException(ProductStockCouldNotBeDecreasedException exception) {

        return new ErrorResponse<>(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OrderStatusChangeException.class)
    public ErrorResponse<String> orderStatusChangeException(OrderStatusChangeException exception) {

        return new ErrorResponse<>(exception.getMessage());
    }
}
