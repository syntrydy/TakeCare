package com.it.mougang.gasmyr.takecare.receivers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import org.joda.time.LocalDateTime;

public class GreetingAlarmReceiver extends WakefulBroadcastReceiver {
    public static final int REQUEST_CODE = 1357;
    private SharedPreferences sharedPreferences;
    private String phoneOwnerName;
    public GreetingAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("GreetingAlarmReceiver", "===============================>");
        init(context);
        handleGreeting(context);
    }
    private void handleGreeting(Context context){
        StringBuilder message=new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHourOfDay();
        int minute = now.getMinuteOfHour();
        int second = now.getSecondOfMinute();
        if(hour>=6 && hour<=8){
            message.append("Bonjour "+phoneOwnerName+" bien dormir j'espère");
        }
        else if(hour>=13 && hour <=15){
            message.append(phoneOwnerName+" bon après midi");
        }
        else if(hour>=18 && hour<=20) {
            message.append(phoneOwnerName+" bonsoir ta journée à été?    la mienne étais troop toop");
        }
        Utils.startSpeakerService(context,message.toString());
    }

    private void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        phoneOwnerName=sharedPreferences.getString(GlobalConstants.APPLICATION_PHONE_OWNER_NAME,"");
    }
}
