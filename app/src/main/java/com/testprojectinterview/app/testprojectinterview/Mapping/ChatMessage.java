package com.testprojectinterview.app.testprojectinterview.Mapping;

import java.util.Date;

/**
 * Created by skyshi on 13/01/17.
 */

public class ChatMessage {
    private String message;
    private String user;
    private String time;
    public ChatMessage(){

    }
    public ChatMessage(String messageText, String messageUser) {
        this.message = messageText;
        this.user = messageUser;

        // Initialize to current time
        time = new Date().getTime()+"";
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
