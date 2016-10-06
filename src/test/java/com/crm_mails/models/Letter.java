package com.crm_mails.models;

/**
 * Created by stako on 22.09.2016.
 */
public class Letter {
    private String sender;
    private String subject;

    public Letter(String sender, String subject) {
        this.sender = sender;
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "sender: " + this.sender + ", subject: " + this.subject;
    }
}
