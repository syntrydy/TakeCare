package com.it.mougang.gasmyr.takecare.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.it.mougang.gasmyr.takecare.alarm.BirthdayAlarmReceiver;

/**
 * Created by gamyr on 10/28/16.
 */

public class AlarmManagerUtils {

    public static void scheduleBirthdayAlarm(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), BirthdayAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, BirthdayAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_HALF_HOUR, pIntent);
    }

    public static void cancelBirthDayAlarm(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), BirthdayAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, BirthdayAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}
