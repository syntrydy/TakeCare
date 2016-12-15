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

    private String key;
    @Required
    private String text;

    public BirthdayMessageModel(String key, String text) {
        this.key = key;
        this.text = text;
    }

    public BirthdayMessageModel(String currentBirthdayMessage) {
        this.text=currentBirthdayMessage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
