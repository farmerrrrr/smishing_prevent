package com.example.myapplication;

import java.util.Date;

public class SMSItem {

     private String sender;
    private String Date_send;
    private String Content;
     public SMSItem(){}

    public void setsender(String sender) {
        this.sender=sender;
    }
    public String getsender() {
        return sender;
    }
    public void setDate_send(String Date_send) {
        this.Date_send=Date_send;
    }
    public String getDate_send() {
        return Date_send;
    }
    public void setContent(String Content) {
        this.Content=Content;
    }
    public String getContent() {
        return Content;
    }
}

