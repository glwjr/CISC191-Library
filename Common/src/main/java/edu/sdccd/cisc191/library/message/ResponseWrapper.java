package edu.sdccd.cisc191.library.message;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ResponseWrapper<T> implements Serializable {
    private final ResponseStatus responseStatus;
    private String message;
    private T data;
    private final LocalDateTime timestamp;

    public ResponseWrapper(ResponseStatus responseStatus, T data) {
        this.responseStatus = responseStatus;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseWrapper(ResponseStatus responseStatus, String message) {
        this.responseStatus = responseStatus;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
