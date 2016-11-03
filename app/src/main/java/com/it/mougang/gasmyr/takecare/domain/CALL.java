package com.it.mougang.gasmyr.takecare.domain;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by gamyr on 10/16/16.
 */

public class CALL extends RealmObject {
    public CALL(){

    }

    @PrimaryKey
    private int id;
    @Required
    private String callerNumber;
    @Required
    private String receiverNumber;
    @Required
    private Date callDate;
    private String callerName;
    private String receiverName;

    public CALL(String callerNumber, String receiverNumber, Date callDate, String callerName, String receiverName) {
        this.callerNumber = callerNumber;
        this.receiverNumber = receiverNumber;
        this.callDate = callDate;
        this.callerName = callerName;
        this.receiverName = receiverName;
    }

    public CALL(int id, String callerNumber, String receiverNumber, Date callDate, String callerName, String receiverName) {
        this.id = id;
        this.callerNumber = callerNumber;
        this.receiverNumber = receiverNumber;
        this.callDate = callDate;
        this.callerName = callerName;
        this.receiverName = receiverName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public Date getCallDate() {
        return callDate;
    }

    public void setCallDate(Date callDate) {
        this.callDate = callDate;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
