package eu.cdevreeze.mybank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldValidationError {

    @JsonProperty("field_name")
    private String fieldName;

    @JsonProperty("validation_error")
    private String validationError;

    public FieldValidationError(String fieldName, String validationError) {
        this.fieldName = fieldName;
        this.validationError = validationError;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValidationError() {
        return validationError;
    }

    public void setValidationError(String validationError) {
        this.validationError = validationError;
    }
}
