package com.npd.core.errors.exceptions;

import com.npd.core.errors.BusinessError;

import java.util.List;

public class InvalidObjectForCreationException extends Exception{

    public List<BusinessError> errors;

    public InvalidObjectForCreationException(String message) {
        super(message);
    }

    public InvalidObjectForCreationException(List<BusinessError> errors) {
        super(errors.toString());
    }
}
