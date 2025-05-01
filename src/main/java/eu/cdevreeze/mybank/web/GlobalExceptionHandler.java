package eu.cdevreeze.mybank.web;

import eu.cdevreeze.mybank.dto.ErrorObject;
import eu.cdevreeze.mybank.dto.FieldValidationError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorObject handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setMessage(exception.getMessage());
        errorObject.setValidationErrors(
                exception.getFieldErrors()
                        .stream()
                        .map(err -> new FieldValidationError(err.getField(), err.getDefaultMessage()))
                        .toList()
        );
        return errorObject;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorObject handleConstraintViolation(ConstraintViolationException exception) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setMessage(exception.getMessage());
        errorObject.setValidationErrors(
                exception.getConstraintViolations()
                        .stream()
                        .map(err -> new FieldValidationError(err.getPropertyPath().toString(), err.getMessage()))
                        .toList()
        );
        return errorObject;
    }
}
