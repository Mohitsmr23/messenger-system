package com.messengersystem.exceptions;

public class InvalidRequestException extends BaseException {

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(Exception ex) {
        super(ex);
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, String errCode) {
        super(message, errCode);
    }

    public InvalidRequestException(String message, String errCode, String param) {
        super(message, errCode, param);
    }
}
