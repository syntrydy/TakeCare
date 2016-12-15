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
    private boolean running;
    private String sorthelper;
    public SayHello(long id, String fullName, String phonenumber, boolean running, String sorthelper) {
        this.id = id;
        this.fullName = fullName;
        this.phonenumber = phonenumber;
        this.running = running;
        this.sorthelper = sorthelper;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getSorthelper() {
        return sorthelper;
    }

    public void setSorthelper(String sorthelper) {
        this.sorthelper = sorthelper;
    }
}
