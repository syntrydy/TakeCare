package com.it.mougang.gasmyr.takecare.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.widget.ImageView;

import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.domain.SayHello;
import com.it.mougang.gasmyr.takecare.service.SpeechService;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by gamyr on 10/24/16.
 */

public class Utils {

    public static boolean isContactsPermissionIsGranted(@NonNull Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isTelephonyPermissionIsGranted(@NonNull Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @NonNull
    public static List<Birthday> getBirthdaysFromContact(@NonNull Context context) {
        List<Birthday> birthdays = new ArrayList<>();
        Birthday birthday;
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = Long.valueOf(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
            String fullName = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            birthday = new Birthday(id, fullName, phonenumber, Utils.getDefaultDate(), false);
            birthdays.add(birthday);
        }
        cursor.close();
        return birthdays;
    }

    @NonNull
    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }

    public static Date getDefaultDate() {
        Date date = new Date(1970, 0, 1);
        String res = getDateFormatter().format(date);
        try {
            return getDateFormatter().parse(res);
        } catch (ParseException e) {
            return date;
        }
    }

    @NonNull
    public static List<SayHello> getSayHelloList(@NonNull Context context) {
        List<SayHello> sayHellos = new ArrayList<>();
        SayHello sayHello;
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = Long.valueOf(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
            String fullName = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            sayHello = new SayHello(id, fullName, phonenumber, false);
            sayHellos.add(sayHello);
        }
        cursor.close();
        return sayHellos;
    }

    public static void roundedProfileImage(@NonNull Context context, @NonNull ImageView imageView, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        rounded.setCornerRadius(bitmap.getWidth());
        imageView.setImageDrawable(rounded);
    }

    public static void roundedBitmap(@NonNull Context context, @NonNull ImageView imageView, Bitmap bitmap) {
        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        rounded.setCornerRadius(bitmap.getWidth());
        imageView.setImageDrawable(rounded);
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Typeface getCampagneFont(@NonNull Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/champagne.ttf");
    }

    public static Typeface getOpenBoldFont(@NonNull Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenBold.ttf");
    }

    public static Typeface getOpenItalicFont(@NonNull Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenItalic.ttf");
    }

    public static Typeface getOpenRegularFont(@NonNull Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenRegular.ttf");
    }

    public static boolean canSendSmsInPromisiousMode() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }

    public static void sendNewSms(@NonNull String message, String sendTo) {
        SmsManager smsManager = SmsManager.getDefault();
        if (message.length() > 140) {
            ArrayList<String> messageParts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(sendTo, null, messageParts, null, null);
        } else {
            smsManager.sendTextMessage(sendTo, null, message, null, null);
        }
    }

    public static String getContactName(@NonNull Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return phoneNumber;
        }
        String contactName = phoneNumber;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    @NonNull
    public static HashMap<String, String> getTelephonyInfos(@NonNull Context context) {
        HashMap<String, String> infos = new HashMap<>();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (isTelephonyPermissionIsGranted(context)) {
            String phoneNumber = telephonyManager.getLine1Number();
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                infos.put(GlobalConstants.APPLICATION_USER_PHONENUMBER, "UNKNOWN");
            } else {
                infos.put(GlobalConstants.APPLICATION_USER_PHONENUMBER, phoneNumber);
            }
            infos.put(GlobalConstants.APPLICATION_USER_DEVICE_ID, telephonyManager.getDeviceId());
            infos.put(GlobalConstants.APPLICATION_USER_OPERATOR_NAME, telephonyManager.getNetworkOperatorName());
            infos.put(GlobalConstants.APPLICATION_USER_NETWORK_TYPE, "" + telephonyManager.getNetworkType());
            infos.put(GlobalConstants.APPLICATION_USER_SERIAL_NUMBER, telephonyManager.getSimSerialNumber());
            infos.put(GlobalConstants.APPLICATION_USER_SIM_NAME, telephonyManager.getSimOperatorName());
        }
        infos.put(GlobalConstants.APPLICATION_PHONE_OWNER_EMAIL, getUserEmail(context));
        infos.put(GlobalConstants.APPLICATION_PHONE_OWNER_NAME, getUserEmail(context).split("@")[0]);
        return infos;
    }

    @Nullable
    private static String getUserEmail(@NonNull Context context) {
        String email = null;
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        if (hasM() && ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            email = getEmail(context, email, gmailPattern);
        } else {
            email = getEmail(context, email, gmailPattern);
        }
        if (email == null) {
            email = "unknown@gmail.com";
        }
        return email;
    }

    private static String getEmail(@NonNull Context context, String email, Pattern gmailPattern) {
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                email = account.name;
            }
        }
        return email;
    }

    @NonNull
    public static SimpleDateFormat getFormatter() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }

    public static void playDefaultNotificationSound(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri getDefaultNotificationSoundUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    public static Uri getDefaultAlarmSoundUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

    public static void vibrate(@NonNull Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);
    }


    public static int getRandom(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    public static void startSpeakerService(@NonNull Context context, String message) {
        Intent speakerServiceIntent = new Intent(context, SpeechService.class);
        speakerServiceIntent.putExtra(GlobalConstants.SPEAKER_SERVICE_MESSAGE, message);
        speakerServiceIntent.putExtra(GlobalConstants.SPEAKER_SERVICE_TARGET, false);
        context.startService(speakerServiceIntent);
    }

}
