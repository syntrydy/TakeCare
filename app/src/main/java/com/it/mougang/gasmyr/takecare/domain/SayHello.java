package com.it.mougang.gasmyr.takecare.domain;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by gamyr on 10/28/16.
 */

public class SayHello extends RealmObject {

    public SayHello() {

    }

    @PrimaryKey
    private long id;
    @Required
    private String fullName;
    @Required
    private String phonenumber;
    @Ignore
    private int eventcode;
    @Ignore
    private boolean status;
    private boolean isSheduled;

    public SayHello(long id, String fullName, String phonenumber, boolean isSheduled) {
        this.id = id;
        this.fullName = fullName;
        this.phonenumber = phonenumber;
        this.isSheduled = isSheduled;
    }

    public SayHello(String fullName, String phonenumber, boolean isSheduled) {
        this.fullName = fullName;
        this.phonenumber = phonenumber;
        this.isSheduled = isSheduled;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getEventcode() {
        return eventcode;
    }

    public void setEventcode(int eventcode) {
        this.eventcode = eventcode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSheduled() {
        return isSheduled;
    }

    public void setSheduled(boolean sheduled) {
        isSheduled = sheduled;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
