package edu.sdccd.cisc191.library.message;

import java.io.Serializable;

public class RequestWrapper<T> implements Serializable {
    private final RequestType requestType;
    private final T payload;

    public RequestWrapper(RequestType requestType, T payload) {
        this.requestType = requestType;
        this.payload = payload;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public T getPayload() {
        return payload;
    }
}
