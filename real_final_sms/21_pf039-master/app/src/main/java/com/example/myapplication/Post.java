package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {
    @SerializedName("id")
    private String id;
    @SerializedName("Sender")
    private String Sender;
    @SerializedName("Date_send")
    private Date Date_send;
    @SerializedName("Content")
    private String Content;
    @SerializedName("smishing_sort")
    private Boolean smishing_sort;

    public String getMember_id_id(){
        return id;
    }
    public void setMember_id_id(String id){
        this.id=id;
    }
    public String getSender(){
        return Sender;
    }
    public void setSender(String Sender){
        this.Sender=Sender;
    }
    public Date getDate_send(){
        return  Date_send;
    }
    public void setDate_send(Date Date_send){
        this.Date_send=Date_send;
    }
    public String getContent(){
        return Content;
    }
    public void setContent(String Content){
        this.Content=Content;
    }
    public boolean getsmishing_sort(){
        return smishing_sort;
    }
    public void setSmishing_sort(Boolean smishing_sort){
        this.smishing_sort=smishing_sort;
    }

}
