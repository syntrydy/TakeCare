package com.it.mougang.gasmyr.takecare.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by bao on 11/13/16.
 */

public class BirthdayMessageModel extends RealmObject {

    public BirthdayMessageModel() {
    }

    @PrimaryKey
    private int id;
    @Required
    private String text;

    public BirthdayMessageModel(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
