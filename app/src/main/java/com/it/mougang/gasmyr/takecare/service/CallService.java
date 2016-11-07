package com.it.mougang.gasmyr.takecare.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by gamyr on 10/28/16.
 */

public class CallService extends IntentService {
    public CallService() {
        super("CALLService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Log.d("CALL", "Inside service");
            Bundle bundle = intent.getExtras();
        }
    }

}
