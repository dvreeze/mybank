package eu.cdevreeze.mybank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ErrorObject {

    private String message;

    @JsonProperty("validation_errors")
    private List<FieldValidationError> validationErrors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<FieldValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
