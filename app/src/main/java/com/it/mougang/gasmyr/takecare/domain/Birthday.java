package com.it.mougang.gasmyr.takecare.domain;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by gamyr on 10/16/16.
 */

public class Birthday extends RealmObject {
    @PrimaryKey
    private Long id;
    @Required
    private String fullName;
    @Required
    private String phonenumber;
    @Required
    private Date birthdate;
    @Ignore
    private int eventcode;
    boolean isrealm;


    public Birthday() {
    }

    public Birthday(Long id, String fullName, String phonenumber, Date birthdate, boolean isrealm) {
        this.id = id;
        this.fullName = fullName;
        this.phonenumber = phonenumber;
        this.birthdate = birthdate;
        this.isrealm = isrealm;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
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

    public boolean isrealm() {
        return isrealm;
    }

    public void setIsrealm(boolean isrealm) {
        this.isrealm = isrealm;
    }


    @Override
    public boolean equals(Object firstBirthday) {
        boolean resultValue = false;
        if (firstBirthday instanceof Birthday) {
            Birthday birthday = (Birthday) firstBirthday;
            resultValue = birthday.id == this.id;
        }
        return resultValue;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
