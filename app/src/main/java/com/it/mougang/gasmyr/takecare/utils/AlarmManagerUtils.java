package com.it.mougang.gasmyr.takecare.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.it.mougang.gasmyr.takecare.alarm.BirthdayAlarmReceiver;
import com.it.mougang.gasmyr.takecare.receivers.GreetingAlarmReceiver;

/**
 * Created by gamyr on 10/28/16.
 */

public class AlarmManagerUtils {

    public static void scheduleBirthdayAlarm(Context context, @NonNull AlarmManager alarmManager) {
        if (alarmManager != null) {
            Intent intent = new Intent(context, BirthdayAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, BirthdayAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long firstMillis = System.currentTimeMillis();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis, AlarmManager.INTERVAL_HOUR, pIntent);
        }
    }

    public static void cancelBirthDayAlarm(@NonNull Context context, @NonNull AlarmManager alarmManager) {
        if (alarmManager != null) {
            Intent intent = new Intent(context.getApplicationContext(), BirthdayAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, BirthdayAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pIntent);
        }
    }

    public static void GreetingAlarm(Context context, @NonNull AlarmManager alarmManager) {
        if (alarmManager != null) {
            Intent intent = new Intent(context, BirthdayAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, GreetingAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long firstMillis = System.currentTimeMillis();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis, AlarmManager.INTERVAL_HALF_DAY, pIntent);
        }
    }

    public static void cancelGreetingAlarm(@NonNull Context context, @NonNull AlarmManager alarmManager) {
        if (alarmManager != null) {
            Intent intent = new Intent(context.getApplicationContext(), BirthdayAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, GreetingAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pIntent);
        }
    }
}
