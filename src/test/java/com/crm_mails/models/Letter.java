package com.crm_mails.models;

/**
 * Created by stako on 22.09.2016.
 */
public class Letter {
    private String sender;
    private String subject;
    private String bulkId;

    public Letter(String sender, String subject, String bulkId) {
        this.sender = sender;
        this.subject = subject;
        this.bulkId = bulkId;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

//    public void setBulkId(String bulkId){
//        this.bulkId = bulkId;
//    }

    public String getBulkId(){
        return bulkId;
    }

    @Override
    public String toString() {
        return "sender: " + this.sender + ", subject: " + this.subject + ", bulkID: " + bulkId;
    }
}
