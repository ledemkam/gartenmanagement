package de0.backend.gartenmanagement.exceptions;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(String message) {
        super(message);
    }

    public InvalidPriceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPriceException(Throwable cause) {
        super(cause);
    }

    public InvalidPriceException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

