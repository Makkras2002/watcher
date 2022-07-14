package com.currency.watcher.exception;

public class RequestSendingException extends Exception{
    public RequestSendingException() {
        super();
    }
    public RequestSendingException(String message) {
        super(message);
    }
    public RequestSendingException(String message, Throwable cause) {
        super(message, cause);
    }
    public RequestSendingException(Throwable cause) {
        super(cause);
    }
}