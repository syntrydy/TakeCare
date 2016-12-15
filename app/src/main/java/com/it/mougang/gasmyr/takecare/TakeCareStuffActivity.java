package com.it.mougang.gasmyr.takecare;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.it.mougang.gasmyr.takecare.utils.AlarmManagerUtils;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

public class TakeCareStuffActivity extends AppCompatActivity {
    private Switch birthdayAlrm, greetingAlarm;
    private ImageView imageView;
    private AlarmManager alarm;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean notify, greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this,2);
        setContentView(R.layout.activity_take_care_stuff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        sharedPreferences=getSharedPreferences(GlobalConstants.APPLICATION_SHAREPRFERENCE,MODE_PRIVATE);
        setupSwitchers();
        birthdayAlrm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startBirthdayNotificationAlarm();
                    editor=sharedPreferences.edit();
                    editor.putBoolean(GlobalConstants.TAKECARE_NOTIF_SPEAKER,true);
                    editor.apply();
                } else {
                    cancelBirthdayNotificationAlarm();
                    editor=sharedPreferences.edit();
                    editor.putBoolean(GlobalConstants.TAKECARE_NOTIF_SPEAKER,false);
                    editor.apply();
                }
            }
        });
        greetingAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startGreetingNotificationAlarm();
                    editor=sharedPreferences.edit();
                    editor.putBoolean(GlobalConstants.TAKECARE_GREETING,true);
                    editor.apply();

                } else {
                    cancelGreetingNotificationAlarm();
                    editor=sharedPreferences.edit();
                    editor.putBoolean(GlobalConstants.TAKECARE_GREETING,false);
                    editor.apply();
                }
            }
        });
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageResource(Utils.getResourceID("a"+Utils.getRandom(20),"drawable",getApplicationContext()));

    }

    private void setupSwitchers() {
        notify=sharedPreferences.getBoolean(GlobalConstants.TAKECARE_NOTIF_SPEAKER,false);
        greeting=sharedPreferences.getBoolean(GlobalConstants.TAKECARE_GREETING,false);
        birthdayAlrm = (Switch) findViewById(R.id.birthdayAlarm);
        birthdayAlrm.setChecked(notify);
        greetingAlarm = (Switch) findViewById(R.id.greetingAlarm);
        greetingAlarm.setChecked(greeting);

    }

    private void startBirthdayNotificationAlarm() {
        AlarmManagerUtils.scheduleBirthdayAlarm(getApplicationContext(), alarm);
    }

    private void cancelBirthdayNotificationAlarm() {
        AlarmManagerUtils.cancelBirthDayAlarm(getApplicationContext(), alarm);
    }

    private void startGreetingNotificationAlarm() {
        AlarmManagerUtils.GreetingAlarm(getApplicationContext(), alarm);
    }

    private void cancelGreetingNotificationAlarm() {
        AlarmManagerUtils.cancelGreetingAlarm(getApplicationContext(), alarm);
    }
}
