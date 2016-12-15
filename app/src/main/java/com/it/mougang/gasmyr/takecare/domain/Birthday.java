package com.it.mougang.gasmyr.takecare.domain;

import android.support.annotation.NonNull;
import android.util.Log;

import com.it.mougang.gasmyr.takecare.utils.Utils;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
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
    private BirthdayMessageModel messageModel;




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

    public BirthdayMessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(BirthdayMessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public Date getNextBirthDate(){
        Log.d("BIRTHDAY","before "+getBirthdate());

        Date date=improveDate();
        Log.d("BIRTHDAY","improve date "+date);
        if(date.before(new Date())){
            Log.d("BIRTHDAY","is before");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, 1);
            Log.d("BIRTHDAY","improve date "+calendar.getTime());
            return calendar.getTime();
        }
        else{
            Log.d("BIRTHDAY","is not before");
            return date;
        }
    }

    private Date improveDate() {
        int nbyears = Math.abs(birthdate.getYear() - new Date().getYear());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthdate);
        calendar.add(Calendar.YEAR, nbyears);
        return calendar.getTime();
    }

    public int  getRemainingsDays() {
            return daysBetweenUsingJoda(getNextBirthDate(), new Date());
    }
    public static int daysBetweenUsingJoda(@NonNull Date d1, @NonNull Date d2) {
        return Math.abs(Days.daysBetween(new LocalDate(d1.getTime()), new LocalDate(d2.getTime())).getDays());
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
