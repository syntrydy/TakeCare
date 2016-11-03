package com.it.mougang.gasmyr.takecare.domain;

/**
 * Created by gamyr on 10/28/16.
 */

public class SayHello {
    private String fullName;
    private String phonenumber;
    private  int eventcode;

    public SayHello(String fullName, String phonenumber) {
        this.fullName = fullName;
        this.phonenumber = phonenumber;
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
}
