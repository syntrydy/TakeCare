package com.it.mougang.gasmyr.takecare.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

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
        Bundle bundle = intent.getExtras();
    }

}
