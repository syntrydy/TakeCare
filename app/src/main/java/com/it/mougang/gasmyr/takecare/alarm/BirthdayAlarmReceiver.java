package com.it.mougang.gasmyr.takecare.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.it.mougang.gasmyr.takecare.service.BirthdayNotificationService;

public class BirthdayAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 2444;

    public BirthdayAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent birtdayIntent = new Intent(context, BirthdayNotificationService.class);
        context.startService(birtdayIntent);
    }
}
