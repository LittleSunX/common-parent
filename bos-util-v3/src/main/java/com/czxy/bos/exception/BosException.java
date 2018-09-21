package com.czxy.bos.exception;

public class BosException extends  RuntimeException {
    public BosException() {
    }

    public BosException(String message) {
        super(message);
    }

    public BosException(String message, Throwable cause) {
        super(message, cause);
    }

    public BosException(Throwable cause) {
        super(cause);
    }

    public BosException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
