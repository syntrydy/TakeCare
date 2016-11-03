package com.it.mougang.gasmyr.takecare.domain;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by gamyr on 10/16/16.
 */

public class SMS extends RealmObject{

    public SMS(){

    }

    @PrimaryKey
    private int id;
    @Required
    private String senderNumber;
    @Required
    private String receiverNumber;
    @Required
    private Date currentDate;
    private String senderName;
    private String receiverName;

    public SMS(String senderNumber, String receiverNumber, Date currentDate, String senderName, String receiverName) {
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.currentDate = currentDate;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public SMS(int id, String senderNumber, String receiverNumber, Date currentDate, String receiverName, String senderName) {
        this.id = id;
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.currentDate = currentDate;
        this.receiverName = receiverName;
        this.senderName = senderName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
