package edu.sdccd.cisc191.library.message;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestWrapper<T> {
    private RequestType requestType;
    private T payload;
    private final LocalDateTime timestamp;
    private final String requestId;

    public RequestWrapper(RequestType requestType, T payload) {
        this.requestType = requestType;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
        this.requestId = UUID.randomUUID().toString();
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getRequestId() {
        return requestId;
    }
}
