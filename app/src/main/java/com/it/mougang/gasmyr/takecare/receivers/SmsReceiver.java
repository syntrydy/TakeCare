package com.it.mougang.gasmyr.takecare.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.util.Log;

import com.it.mougang.gasmyr.takecare.service.SpeechService;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    public static final String ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String ANDROID_PROVIDER_TELEPHONY_SMS_SENT = "android.provider.Telephony.SMS_SENT";
    public static final String FORMAT = "3gpp";
    public static final String PDUS = "pdus";
    private static boolean canSpeakWhenNewIncomingSmsIsDetected = false;
    private static boolean speakJustTheSmsSummary = false;
    private static boolean canReplyToNewSmsWhenPhoneOwnerIsBusy = false;
    private static boolean speakerIsOn = false;


    private static boolean hasSpeakerFeature = false;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences globalPreferences;
    String SMS_SENDER;
    String SMS_SENDER_NAME;
    String SMS_BODY;
    @Nullable
    private String SMS_SPEAKER_CONTENT_MESSAGE;
    @Nullable
    private String SMS_SPEAKER_SUMMARY_MESSAGE;
    @Nullable
    private String OWNER_NAME;
    private String FULL_MESSAGE;
    private String SMS_RESPONDER_MESSAGE;

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = null;
            init(context);
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get(PDUS);
                messages = new SmsMessage[pdus.length];
                for (int i = 0; i < messages.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], FORMAT);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    SMS_BODY += messages[i].getMessageBody();
                    SMS_SENDER += messages[i].getOriginatingAddress();
                    SMS_SENDER_NAME = Utils.getContactName(context, SMS_SENDER);
                }
                if (intent.getAction().equals(ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED)) {
                    if (SMS_BODY.length() >= 2) {
                        triggeSmsSpeaker(context);
                        handleSmsResponder();
                    }
                }
                if (intent.getAction().equals(ANDROID_PROVIDER_TELEPHONY_SMS_SENT)) {
                    throw new Exception("Not yet implemented");
                }

            }
        } catch (Exception e) {
            Log.e("GASMYR", e.getMessage());
        }
    }

    private void handleSmsResponder() {
        if (canReplyToNewSmsWhenPhoneOwnerIsBusy) {
            SMS_RESPONDER_MESSAGE = "Salut" + SMS_SENDER + " " + SMS_RESPONDER_MESSAGE + " @autocompute " + new Date().getTime();
            Utils.sendNewSms(SMS_RESPONDER_MESSAGE, SMS_SENDER);
        }
    }

    private void triggeSmsSpeaker(@NonNull Context context) {
        if (hasSpeakerFeature && speakerIsOn && canSpeakWhenNewIncomingSmsIsDetected) {
            if (speakJustTheSmsSummary) {
                FULL_MESSAGE = SMS_SPEAKER_SUMMARY_MESSAGE.replace("{name}", OWNER_NAME);
                FULL_MESSAGE = FULL_MESSAGE.replace("{sname}", SMS_SENDER);
            } else {
                FULL_MESSAGE = SMS_SPEAKER_CONTENT_MESSAGE.replace("{name}", OWNER_NAME);
                FULL_MESSAGE = FULL_MESSAGE.replace("{sname}", SMS_SENDER);
                FULL_MESSAGE = FULL_MESSAGE.replace("{message}", SMS_BODY);
            }
            startSpeakerService(context, FULL_MESSAGE);
        }
    }

    private void startSpeakerService(@NonNull Context context, String message) {
        Intent speakerServiceIntent = new Intent(context, SpeechService.class);
        speakerServiceIntent.putExtra(GlobalConstants.SPEAKER_SERVICE_MESSAGE, message);
        speakerServiceIntent.putExtra(GlobalConstants.SPEAKER_SERVICE_TARGET, false);
        context.startService(speakerServiceIntent);
    }

    private void init(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(
                GlobalConstants.APPLICATION_SHAREPRFERENCE, Context.MODE_PRIVATE);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        canSpeakWhenNewIncomingSmsIsDetected = globalPreferences.getBoolean(
                GlobalConstants.APPLICATION_SPEAKER_CAN_SPEAK_WHEN_NEW_INCOMING_SMS_IS_DETECTED, false);
        speakJustTheSmsSummary = globalPreferences.getBoolean(
                GlobalConstants.APPLICATION_SPEAKER_SMS_CAN_READ_JUST_THE_SUMMARY, false);
        hasSpeakerFeature = sharedPreferences.getBoolean(
                GlobalConstants.APPLICATION_HAS_SPEAKER_FEATURE, false);
        canReplyToNewSmsWhenPhoneOwnerIsBusy = globalPreferences.
                getBoolean(GlobalConstants.APPLICATION_SMS_RESPONDER_CAN_REPLY_ON_NEW_SMS, false);
        speakerIsOn=globalPreferences.getBoolean(GlobalConstants.APPLICATION_SPEAKER_IS_ENABLED,false);
        SMS_RESPONDER_MESSAGE = globalPreferences.getString(GlobalConstants.APPLICATION_SMS_RESPONDER_DEFINED_MESSAGE, "");
        SMS_SPEAKER_CONTENT_MESSAGE = globalPreferences.getString(GlobalConstants.APPLICATION_SPEAKER_SMS_CONTENT_DEFINED_MODEL, "");
        SMS_SPEAKER_SUMMARY_MESSAGE =
                globalPreferences.getString(GlobalConstants.APPLICATION_SPEAKER_SMS_SUMMARY_DEFINED_MODEL, "");
        OWNER_NAME = globalPreferences.getString(GlobalConstants.APPLICATION_PHONE_OWNER_NAME, "");
    }


}
