package com.nekromant.twitch.content;

public class Message {
    private String tagUser;
    private String messageText;

    public Message(String tagUser, String messageText) {
        this.tagUser = tagUser;
        this.messageText = messageText;
    }

    public String getMessage() {
        return "@" + tagUser + " " + messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
