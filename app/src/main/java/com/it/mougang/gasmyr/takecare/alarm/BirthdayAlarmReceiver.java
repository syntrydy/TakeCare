package com.it.mougang.gasmyr.takecare.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.it.mougang.gasmyr.takecare.BirthdayDetailActivity;
import com.it.mougang.gasmyr.takecare.MainActivity;
import com.it.mougang.gasmyr.takecare.MyApplication;
import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.domain.BirthdayMessageModel;
import com.it.mougang.gasmyr.takecare.service.SpeechService;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class BirthdayAlarmReceiver extends WakefulBroadcastReceiver {

    public static final int REQUEST_CODE = 2444;
    public static RealmConfiguration realmConfiguration;
    private SharedPreferences sharedPreferences;
    private int delay = 0;
    private Realm realm;
    private RealmResults<Birthday> realmResults;
    private int counter = 0;
    private Birthday birthdayToUseForNotification;
    private SimpleDateFormat dateFormat;

    public BirthdayAlarmReceiver() {
    }

    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        Log.e("ME", "===============================>");
        init(context);
        dateFormat = Utils.getDateFormatter();
        initRealmInstance();
        realmResults = realm.where(Birthday.class).equalTo("isrealm", true).findAll();
        for (Birthday birthday : realmResults) {
            if (Utils.daysBetweenUsingJoda(Utils.adjustDate(birthday.getBirthdate()), new Date()) <= delay) {
                birthdayToUseForNotification = birthday;
                counter++;
            }
        }
        if (counter >= 1) {
            showNotification(context, counter, birthdayToUseForNotification);
            startSpeakerService(context, context.getString(R.string.birthday_notifone) + counter + context.getString(R.string.birthday_notiftwo));
        }

    }

    private void showNotification(Context context, int numberOfBirthdays, Birthday birthday) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_add_alert_black_24dp);
        builder.setContentTitle(context.getString(R.string.birthday_notification_title));
        builder.setContentText(context.getString(R.string.birthday_notification_textOne) + numberOfBirthdays + context.getString(R.string.birthday_notification_textTwo));
        builder.setSound(Utils.getDefaultNotificationSoundUri());
        builder.setAutoCancel(true);
        builder.setWhen(new Date().getTime());
        builder.setTicker(context.getResources().getString(R.string.app_tab_title_birtdays));

        Intent intent = new Intent(context, BirthdayDetailActivity.class);
        intent.putExtra(GlobalConstants.BIRTHDAY_DATE, dateFormat.format(birthday.getBirthdate()));
        intent.putExtra(GlobalConstants.BIRTHDAY_NEXT_DATE, dateFormat.format(Utils.getNextBirthdate(birthday.getBirthdate())));
        intent.putExtra(GlobalConstants.BIRTHDAY_FULLNAME, birthday.getFullName());
        intent.putExtra(GlobalConstants.BIRTHDAY_NUMBER, birthday.getPhonenumber());
        intent.putExtra(GlobalConstants.BIRTHDAY_REMAINING_DAYS, Utils.getRemainingsDays(birthday.getBirthdate()));
        intent.putExtra(GlobalConstants.BIRTHDAY_ID, String.valueOf(birthday.getId()));


        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(BirthdayDetailActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(125, notification);
    }

    private void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        delay = Integer.valueOf(sharedPreferences.getString(GlobalConstants.TAKECARE_BIRTHDAY_NOTIFICATIONS_PERIODS, "2"));
    }

    private void startSpeakerService(@NonNull Context context, String message) {
        Intent speakerServiceIntent = new Intent(context, SpeechService.class);
        speakerServiceIntent.putExtra(GlobalConstants.TAKECARE_TEXTTOSPEECH_Message, message);
        speakerServiceIntent.putExtra(GlobalConstants.TAKECARE_TEXTTOSPEECH_TARGET, false);
        context.startService(speakerServiceIntent);
    }

    public void initRealmInstance() {
        realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
    }
}
