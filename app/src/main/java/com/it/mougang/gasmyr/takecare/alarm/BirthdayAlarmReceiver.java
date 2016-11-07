package com.it.mougang.gasmyr.takecare.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.it.mougang.gasmyr.takecare.service.BirthdayNotificationService;
import com.it.mougang.gasmyr.takecare.utils.Utils;

public class BirthdayAlarmReceiver extends WakefulBroadcastReceiver {

    public static final int REQUEST_CODE = 2444;

    public BirthdayAlarmReceiver() {
    }

    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        Ringtone ringtone = RingtoneManager.getRingtone(context, Utils.getDefaultAlarmSoundUri());
        ringtone.play();
        Utils.vibrate(context);
    }
}
