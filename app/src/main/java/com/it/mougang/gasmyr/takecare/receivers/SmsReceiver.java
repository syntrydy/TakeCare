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
    public static final String AUTOCOMPUTE_AT = " . >>>>>> @generated at ";
    public static final String RNAME = "{name}";
    public static final String RSENDER_NAME = "{sname}";
    public static final String RMESSAGE = "{message}";
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
    private int simToUse = 0;

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
                    SMS_SENDER = messages[i].getOriginatingAddress();
                    SMS_SENDER_NAME = Utils.getContactName(context, SMS_SENDER);
                    Utils.makeLog(SMS_SENDER_NAME);
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
            Utils.makeLog(e.getMessage());
        }
    }

    private void handleSmsResponder() {
        if (canReplyToNewSmsWhenPhoneOwnerIsBusy && canSend(SMS_SENDER)) {
            SMS_RESPONDER_MESSAGE = Utils.getGrretingPreffix() + SMS_SENDER_NAME + " JE SUIS LE TELEPHONE DE " + OWNER_NAME + " IL NE PEUT VOUS REPONDRE. IL A LAISSER CE MESSAGE COMME MOTIF:" + SMS_RESPONDER_MESSAGE + AUTOCOMPUTE_AT + Utils.getTimeFormatter().format(new Date());
            SMS_RESPONDER_MESSAGE = SMS_RESPONDER_MESSAGE.replace(RNAME, OWNER_NAME);
            Utils.sendNewSms(SMS_RESPONDER_MESSAGE, SMS_SENDER);
        }
    }

    private boolean canSend(String sms_sender) {
        if (simToUse == 1 && Utils.isMtn(sms_sender)) {
            return true;
        } else if (simToUse == 2 && Utils.isOrange(sms_sender)) {
            return true;
        } else if (simToUse == 3 && Utils.isNexttel(sms_sender)) {
            return true;
        } else {
            return false;
        }
    }

    private void triggeSmsSpeaker(@NonNull Context context) {
        if (hasSpeakerFeature && speakerIsOn && canSpeakWhenNewIncomingSmsIsDetected) {
            if (speakJustTheSmsSummary) {
                FULL_MESSAGE = SMS_SPEAKER_SUMMARY_MESSAGE.replace(RNAME, OWNER_NAME);
                FULL_MESSAGE = FULL_MESSAGE.replace(RSENDER_NAME, SMS_SENDER_NAME);
            } else {
                FULL_MESSAGE = SMS_SPEAKER_CONTENT_MESSAGE.replace(RNAME, OWNER_NAME);
                FULL_MESSAGE = FULL_MESSAGE.replace(RSENDER_NAME, SMS_SENDER_NAME);
                FULL_MESSAGE = FULL_MESSAGE.replace(RMESSAGE, SMS_BODY);
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
                GlobalConstants.APPLICATION_SPEAKER_CAN_SPEAK_WHEN_NEW_INCOMING_SMS_IS_DETECTED, true);
        speakJustTheSmsSummary = globalPreferences.getBoolean(
                GlobalConstants.APPLICATION_SPEAKER_SMS_CAN_READ_JUST_THE_SUMMARY, true);
        hasSpeakerFeature = sharedPreferences.getBoolean(
                GlobalConstants.APPLICATION_HAS_SPEAKER_FEATURE, true);
        canReplyToNewSmsWhenPhoneOwnerIsBusy = globalPreferences.
                getBoolean(GlobalConstants.APPLICATION_SMS_RESPONDER_CAN_REPLY_ON_NEW_SMS, true);
        speakerIsOn = globalPreferences.getBoolean(GlobalConstants.APPLICATION_SPEAKER_IS_ENABLED, true);
        SMS_RESPONDER_MESSAGE = globalPreferences.getString(GlobalConstants.APPLICATION_SMS_RESPONDER_DEFINED_MESSAGE, "");
        SMS_SPEAKER_CONTENT_MESSAGE = globalPreferences.getString(GlobalConstants.APPLICATION_SPEAKER_SMS_CONTENT_DEFINED_MODEL, "");
        SMS_SPEAKER_SUMMARY_MESSAGE =
                globalPreferences.getString(GlobalConstants.APPLICATION_SPEAKER_SMS_SUMMARY_DEFINED_MODEL, "");
        OWNER_NAME = globalPreferences.getString(GlobalConstants.APPLICATION_PHONE_OWNER_NAME, "");
        simToUse = Integer.valueOf(sharedPreferences.getString(GlobalConstants.APPLICATION_SMS_REPLY_SIM, "1"));
    }


}
