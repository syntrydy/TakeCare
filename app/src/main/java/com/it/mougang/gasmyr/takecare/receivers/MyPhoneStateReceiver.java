package com.it.mougang.gasmyr.takecare.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.it.mougang.gasmyr.takecare.service.SpeechService;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

public class MyPhoneStateReceiver extends BroadcastReceiver {

    private static final String TAG = "MyPhoneStateReceiver";
    private static Context context;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences globalPreferences;
    private static boolean phoneHasAlreadyRing = false;
    private static boolean phoneIsReceivingOutgoingCall = false;


    private static boolean canUseSpeakerFeature = false;
    private static boolean speakerIsOn = false;
    private static boolean canSpeakWhenNewIncomingCallIsDetected = false;
    private static boolean canReplyToNewCallWhenPhoneOwnerIsBusy = false;

    @Nullable
    private String CALL_MAKER_NUMBER;
    private String CALL_MAKER_NUMBER_NAME;
    @Nullable
    private String SPEAKER_DEFINED_MESSAGE;
    @Nullable
    private String RESPONDER_REPLY_MESSAGE;
    @Nullable
    private String PHONE_OWNER_NAME;
    private String FULL_MESSAGE;

    public MyPhoneStateReceiver() {
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        try {
            MyPhoneStateReceiver.context = context;
            init();
            makeLog("1" + canUseSpeakerFeature + canSpeakWhenNewIncomingCallIsDetected);
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            CALL_MAKER_NUMBER = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            CALL_MAKER_NUMBER_NAME = Utils.getContactName(context, CALL_MAKER_NUMBER);
            makeLog("2");
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                phoneHasAlreadyRing = true;
                if (canUseSpeakerFeature && speakerIsOn && canSpeakWhenNewIncomingCallIsDetected) {
                    FULL_MESSAGE = SPEAKER_DEFINED_MESSAGE.replace("{name}", PHONE_OWNER_NAME);
                    FULL_MESSAGE = FULL_MESSAGE.replace("{sname}", CALL_MAKER_NUMBER_NAME);
                    startSpeakerService(context, FULL_MESSAGE);
                }
                if (canReplyToNewCallWhenPhoneOwnerIsBusy) {
                    Utils.sendNewSms(RESPONDER_REPLY_MESSAGE, CALL_MAKER_NUMBER);
                }
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                if (phoneHasAlreadyRing) {

                } else {
                    phoneIsReceivingOutgoingCall = true;
                }
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                // endCall();
            }

        } catch (Exception e) {
            makeLog(e.getStackTrace().toString());

        }
    }

    private void makeLog(String msg) {
        Log.i(TAG, msg);
    }


    private void startSpeakerService(@NonNull Context context, String message) {
        synchronized (new Object()) {
            Intent smsService = new Intent(context, SpeechService.class);
            smsService.putExtra(GlobalConstants.SPEAKER_SERVICE_MESSAGE, message);
            smsService.putExtra(GlobalConstants.SPEAKER_SERVICE_TARGET, true);
            context.startService(smsService);
        }
    }

    private void init() {
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = context.getSharedPreferences(
                GlobalConstants.APPLICATION_SHAREPRFERENCE, Context.MODE_PRIVATE);
        canSpeakWhenNewIncomingCallIsDetected = globalPreferences.getBoolean(
                GlobalConstants.APPLICATION_SPEAKER_CAN_SPEAK_WHEN_NEW_INCOMING_CALL_IS_DETECTED, false);
        canUseSpeakerFeature = sharedPreferences.getBoolean(
                GlobalConstants.APPLICATION_HAS_SPEAKER_FEATURE, false);
        canReplyToNewCallWhenPhoneOwnerIsBusy = globalPreferences.
                getBoolean(GlobalConstants.APPLICATION_CALL_RESPONDER_CAN_REPLY_ON_NEW_CALL, false);
        speakerIsOn=globalPreferences.getBoolean(GlobalConstants.APPLICATION_SPEAKER_IS_ENABLED,false);
        RESPONDER_REPLY_MESSAGE = globalPreferences.getString(GlobalConstants.APPLICATION_CALL_RESPONDER_DEFINED_MESSAGE, "");
        SPEAKER_DEFINED_MESSAGE = globalPreferences.getString(GlobalConstants.APPLICATION_SPEAKER_CALL_DEFINED_MODEL, "");
        PHONE_OWNER_NAME = globalPreferences.getString(GlobalConstants.APPLICATION_PHONE_OWNER_NAME, "");
    }
}

