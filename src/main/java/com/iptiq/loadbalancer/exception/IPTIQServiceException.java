package com.iptiq.loadbalancer.exception;

/**
 * A customized exception.
 *
 * @author Pooya Mirzapour (pooyamirzapour@gmail.com)
 */

public class IPTIQServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * Build a new exception with the specified error code, detail message.
     *
     * @param message   - Message
     * @param errorCode - Error Code
     */
    public IPTIQServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public IPTIQServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public IPTIQServiceException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
