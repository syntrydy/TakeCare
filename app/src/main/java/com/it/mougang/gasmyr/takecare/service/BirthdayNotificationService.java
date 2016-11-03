package com.it.mougang.gasmyr.takecare.service;

import android.app.IntentService;
import android.content.Intent;
import android.widget.SpinnerAdapter;

import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import io.realm.Realm;
import io.realm.RealmResults;


public class BirthdayNotificationService extends IntentService {
    private RealmResults<Birthday> realmResults;

    public BirthdayNotificationService() {
        super("BirthdayNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            realmResults = Realm.getDefaultInstance().where(Birthday.class).findAll();
            for (Birthday birthday : realmResults) {
                int remaingdays = Math.abs(Utils.daysBetweenUsingJoda(birthday.getBirthdate(), birthday.getNextbirthdate()));
                if(remaingdays<=5){
                    notifyForBirthdayWith(birthday);
                }
            }
        }
    }

    private void notifyForBirthdayWith(Birthday birthday) {
        Utils.sendNewSms("new birthday comming soon","696534361");
    }
}
