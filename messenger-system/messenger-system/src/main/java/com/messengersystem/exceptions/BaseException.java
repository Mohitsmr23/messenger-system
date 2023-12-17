package com.messengersystem.exceptions;

import lombok.Getter;
import lombok.Setter;

public class BaseException extends RuntimeException {
    @Getter
    @Setter
    private String errorCode;
    @Getter
    @Setter
    private String param;

    public BaseException() {
        super();
    }

    public BaseException(Exception ex) {
        super(ex);
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String msg, String errCode) {
        super(msg);
        errorCode = errCode;
    }

    /**
     * Base exception.
     *
     * @param msg message.
     * @param errCode errCode.
     * @param parameter parameter.
     */
    public BaseException(String msg, String errCode, String parameter) {
        super(msg);
        errorCode = errCode;
        param = parameter;
    }
}
