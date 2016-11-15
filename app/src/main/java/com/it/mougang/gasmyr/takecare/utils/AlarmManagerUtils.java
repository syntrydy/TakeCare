package com.it.mougang.gasmyr.takecare.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.it.mougang.gasmyr.takecare.alarm.BirthdayAlarmReceiver;

/**
 * Created by gamyr on 10/28/16.
 */

public class AlarmManagerUtils {

    public static void scheduleBirthdayAlarm(Context context, @NonNull AlarmManager alarmManager) {
        Log.e("ME","start===============================>");
        if (alarmManager != null) {
            Log.e("ME","start1===============================>");
            Intent intent = new Intent(context, BirthdayAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, BirthdayAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long firstMillis = System.currentTimeMillis();
            Log.e("ME","start2===============================>");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis, AlarmManager.INTERVAL_HALF_HOUR, pIntent);
            Log.e("ME","start2===============================>");
        }
    }

    public static void cancelBirthDayAlarm(@NonNull Context context, @NonNull AlarmManager alarmManager) {
        Log.e("ME","stop===============================>");
        if (alarmManager != null) {
            Log.e("ME","stop1===============================>");
            Intent intent = new Intent(context.getApplicationContext(), BirthdayAlarmReceiver.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, BirthdayAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pIntent);
            Log.e("ME","stop2===============================>");
        }
    }
}
