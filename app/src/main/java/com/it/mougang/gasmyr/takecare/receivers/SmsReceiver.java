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
    private static boolean incomingSmsSpeakerIsEnable = false;
    private static boolean incomingSmsSummarySpeakerIsEnable = false;
    private static boolean incomingSmsBodySpeakerIsEnable = false;
    private static boolean canUseSpeakerFeature = false;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences globalPreferences;
    String SMS_SENDER = "";
    String SMS_BODY = "";
    String SMS_EmailBODY = "";
    String SMS_EmailFROM = "";
    String SMS_DISPLAYBODY = "";
    String SMS_PSEUDO_SUBJECT = "";
    String SMS_ServiceCenterAddress = "";
    @Nullable
    private String messageBody = "";
    @Nullable
    private String messageSummary = "";
    @Nullable
    private String userName = "";
    private String fullMessage = "";
    private boolean responderIsEnable;
    private String reponderMessage="";

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
                    SMS_SENDER = Utils.getContactName(context, SMS_SENDER);
                    SMS_DISPLAYBODY += messages[i].getDisplayMessageBody();
                    SMS_PSEUDO_SUBJECT += messages[i].getPseudoSubject();
                    SMS_ServiceCenterAddress = messages[i].getServiceCenterAddress();
                    if (messages[i].isEmail()) {
                        SMS_EmailBODY = messages[i].getEmailBody();
                        SMS_EmailFROM = messages[i].getEmailFrom();
                    }
                }
                if (intent.getAction().equals(ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED)) {
                    if (SMS_BODY.length() >= 2) {
                        manage_incoming_sms_speaker(context);
                        if(responderIsEnable){
                            reponderMessage="Salut"+SMS_SENDER+" "+reponderMessage+" @autocompute "+new Date().getTime();
                            Utils.sendNewSms(reponderMessage,SMS_SENDER);
                        }
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

    private void manage_incoming_sms_speaker(@NonNull Context context) {
        if (canUseSpeakerFeature && incomingSmsSpeakerIsEnable) {
            if (incomingSmsSummarySpeakerIsEnable &&
                    !incomingSmsBodySpeakerIsEnable) {
                fullMessage = messageSummary.replace("{name}", userName);
                fullMessage = fullMessage.replace("{sname}", SMS_SENDER);
            }
            if ((incomingSmsSpeakerIsEnable && incomingSmsBodySpeakerIsEnable) ||
                    (!incomingSmsSpeakerIsEnable && incomingSmsBodySpeakerIsEnable)) {
                fullMessage = messageBody.replace("{name}", userName);
                fullMessage = fullMessage.replace("{sname}", SMS_SENDER);
                fullMessage = fullMessage.replace("{message}", SMS_BODY);
            }
            startSpeakerService(context, fullMessage);
        }
    }

    private void startSpeakerService(@NonNull Context context, String message) {
        Intent speakerServiceIntent = new Intent(context, SpeechService.class);
        speakerServiceIntent.putExtra(GlobalConstants.TAKECARE_TEXTTOSPEECH_Message, message);
        speakerServiceIntent.putExtra(GlobalConstants.TAKECARE_TEXTTOSPEECH_TARGET, false);
        context.startService(speakerServiceIntent);
    }

    private void init(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(
                GlobalConstants.TAKECARE_SHARE_PRFERENCE, Context.MODE_PRIVATE);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        incomingSmsSpeakerIsEnable = globalPreferences.getBoolean(
                GlobalConstants.TAKECARE_ENABLE_INCOMING_SMS_SPEAKER, false);
        incomingSmsSummarySpeakerIsEnable = globalPreferences.getBoolean(
                GlobalConstants.TAKECARE_ENABLE_INCOMING_SMS_SUMMARY_SPEAKER, false);
        incomingSmsBodySpeakerIsEnable = globalPreferences.getBoolean(
                GlobalConstants.TAKECARE_ENABLE_INCOMING_SMS_BODY_SPEAKER, false);
        canUseSpeakerFeature = sharedPreferences.getBoolean(
                GlobalConstants.TAKECARE_CAN_USE_SPEAKER_FEATURE, false);
        reponderMessage=globalPreferences.getString(GlobalConstants.TAKECARE_SMS_RESPONDER_MESSAGE,"@compute");
        responderIsEnable=globalPreferences.getBoolean(GlobalConstants.TAKECARE_SMS_RESPONDER,false);
        messageBody = globalPreferences.getString(GlobalConstants.TAKECARE_SMS_BODY_MODEL_SPEAKER, "");
        messageSummary = globalPreferences.getString(GlobalConstants.TAKECARE_SMS_SUMMARY_MODEL_SPEAKER, "");
        userName = globalPreferences.getString(GlobalConstants.TAKECARE_USER_DEFINE_NAME, "");
    }


}
