package com.it.mougang.gasmyr.takecare.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

/**
 * Created by gamyr on 10/28/16.
 */

public class SMSService extends IntentService {

    public SMSService(String name) {
        super(name);
    }

    public SMSService() {
        super("smsservice");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();

        }
    }

    private void startSpeakerService(@NonNull Context context, String message) {
        Intent speakerServiceIntent = new Intent(context, SpeechService.class);
        speakerServiceIntent.putExtra(GlobalConstants.SPEAKER_SERVICE_MESSAGE, message);
        speakerServiceIntent.putExtra(GlobalConstants.SPEAKER_SERVICE_TARGET, false);
        context.startService(speakerServiceIntent);
    }

}
