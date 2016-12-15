package com.it.mougang.gasmyr.takecare.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by gasmyr.mougang on 12/12/16.
 */

public class SmsForLife extends RealmObject{

    @PrimaryKey
    private String key;
    @Required
    private String text;
    public SmsForLife() {
    }

    public SmsForLife(String key, String text) {
        this.key = key;
        this.text = text;
    }

    public SmsForLife(String currentBirthdayMessage) {
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
