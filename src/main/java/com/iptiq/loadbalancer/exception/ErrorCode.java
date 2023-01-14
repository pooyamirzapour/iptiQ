package com.iptiq.loadbalancer.exception;

/**
 * Enum for all possible error codes.
 *
 * @author Pooya Mirzapour (pooyamirzapour@gmail.com)
 */
public enum ErrorCode {

    PROVIDER_NOT_FOUND("Registered provider not found", 1001),
    QUEUE_IS_FULL("Queue is full", 1002),
    QUEUE_IS_EMPTY("Queue is empty", 1003),
    LOAD_BALANCER_IS_OUT_OF_CAPACITY("Load balancer is out of capacity", 1004);


    private int value;
    private String message;

    ErrorCode(String message, int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }
}
