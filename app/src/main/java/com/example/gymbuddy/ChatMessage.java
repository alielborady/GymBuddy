package com.example.gymbuddy;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class ChatMessage {
    private String firstUser;
    private String secondUser;
    private String messageText;
    private String messageUser;
    private long messageTime;

    public ChatMessage(String firstUser, String secondUser, String messageText, String messageUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(String firstUser) {
        this.firstUser = firstUser;
    }

    public String getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(String secondUser) {
        this.secondUser = secondUser;
    }
}
