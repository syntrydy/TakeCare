package com.it.mougang.gasmyr.takecare.domain;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by gamyr on 11/2/16.
 */

public class Todo extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String title;
    private String description;
    @Required
    private Date startdate;
    @Required
    private Date endate;
    private int frequency;
    @Ignore
    private int eventcode;

    public Todo(){

    }

    public Todo(String title, String description, Date startdate, Date endate, int frequency) {
        this.title = title;
        this.description = description;
        this.startdate = startdate;
        this.endate = endate;
        this.frequency = frequency;
    }

    public Todo(String title, String description, Date startdate, Date endate, int frequency, int id) {
        this.title = title;
        this.description = description;
        this.startdate = startdate;
        this.endate = endate;
        this.frequency = frequency;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEndate() {
        return endate;
    }

    public void setEndate(Date endate) {
        this.endate = endate;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getEventcode() {
        return eventcode;
    }

    public void setEventcode(int eventcode) {
        this.eventcode = eventcode;
    }
}
