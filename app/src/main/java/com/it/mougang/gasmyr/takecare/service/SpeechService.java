package com.it.mougang.gasmyr.takecare.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;

import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;

import java.util.Locale;

public class SpeechService extends Service implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private String message;
    private boolean isCallMessage;
    private boolean isInit;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        tts = new TextToSpeech(getApplicationContext(), this);
        handler = new Handler();
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        handler.removeCallbacksAndMessages(null);
        Bundle data = intent.getExtras();
        message = data.getString(GlobalConstants.TAKECARE_TEXTTOSPEECH_Message, message);
        isCallMessage = data.getBoolean(GlobalConstants.TAKECARE_TEXTTOSPEECH_TARGET, false);
        if (isInit) {
            speak(isCallMessage);
        }
        waitAwhile();
        return SpeechService.START_NOT_STICKY;
    }

    private void waitAwhile() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 15 * 1000);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.getDefault());
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                speak(isCallMessage);
                isInit = true;
            }
        }
    }

    private void speak(boolean isCallMessage) {
        if (tts != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
                if(isCallMessage){
                    tts.speak(message, TextToSpeech.QUEUE_ADD, null, null);
                }
            } else {
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                if(isCallMessage){
                    tts.speak(message, TextToSpeech.QUEUE_ADD, null);
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}