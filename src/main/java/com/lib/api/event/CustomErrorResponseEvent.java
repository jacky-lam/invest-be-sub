package com.lib.api.event;

public class CustomErrorResponseEvent extends AbstractResponseEvent {

    private final String message;

    public CustomErrorResponseEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return "UserRegisterResponseEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
