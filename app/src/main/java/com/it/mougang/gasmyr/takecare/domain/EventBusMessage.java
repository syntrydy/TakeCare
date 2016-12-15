package com.it.mougang.gasmyr.takecare.domain;

/**
 * Created by gasmyr.mougang on 12/13/16.
 */

public class EventBusMessage {
    private String messageCode;
    private String messageText;

    public EventBusMessage(String messageCode, String messageText) {
        this.messageCode = messageCode;
        this.messageText = messageText;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
