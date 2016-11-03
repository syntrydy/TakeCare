package com.it.mougang.gasmyr.takecare.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.widget.ImageView;

import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.domain.SayHello;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by gamyr on 10/24/16.
 */

public class Utils {

    public static Date getNextYear(Date date) {
        LocalDate currentDate = LocalDate.fromDateFields(date);
        return currentDate.plusYears(1).toDate();
    }

    public static Date getNextDate(Date date) {
        return new Date(date.getTime() + DateUtils.YEAR_IN_MILLIS);
    }

    public static int daysBetweenUsingJoda(Date d1, Date d2) {
        return Days.daysBetween(new LocalDate(d1.getTime()), new LocalDate(d2.getTime())).getDays();
    }


    public static boolean isContactsPermissionIsGranted(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Birthday> getBirthdaysFromContact(Context context) {
        List<Birthday> birthdays = new ArrayList<>();
        Birthday birthday;
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = Long.valueOf(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
            String fullName = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            birthday = new Birthday(id, fullName, phonenumber, new Date(), new Date(), false);
            birthdays.add(birthday);
        }
        cursor.close();
        return birthdays;
    }

    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }

    public static List<SayHello> getSayHelloList(Context context) {
        List<SayHello> sayHellos = new ArrayList<>();
        SayHello sayHello;
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String fullName = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            sayHello = new SayHello(fullName,
                    phonenumber);
            sayHellos.add(sayHello);
        }
        cursor.close();
        return sayHellos;
    }

    public static void roundedProfileImage(Context context, ImageView imageView, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
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

    public static Typeface getCampagneFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/champagne.ttf");
    }

    public static Typeface getOpenBoldFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenBold.ttf");
    }

    public static Typeface getOpenItalicFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenItalic.ttf");
    }

    public static Typeface getOpenRegularFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenRegular.ttf");
    }

    public static String getCurrentUserOsVersion() {
        int buildVersion = Build.VERSION.SDK_INT;
        String result = "UNKNOWN";
        if (buildVersion == Build.VERSION_CODES.KITKAT || buildVersion == Build.VERSION_CODES.KITKAT_WATCH) {
            result = "KITKAT:" + buildVersion;
        }
        if (buildVersion == Build.VERSION_CODES.LOLLIPOP) {
            result = "LOLLIPOP:" + buildVersion;
        }
        if (buildVersion == Build.VERSION_CODES.JELLY_BEAN ||
                buildVersion == Build.VERSION_CODES.JELLY_BEAN_MR1 ||
                buildVersion == Build.VERSION_CODES.JELLY_BEAN_MR2) {
            result = "JELLY_BEAN:" + buildVersion;
        }
        if (buildVersion == Build.VERSION_CODES.M) {
            result = "MASHMALLOP:" + buildVersion;
        }
        if (buildVersion == Build.VERSION_CODES.GINGERBREAD) {
            result = "GINGERBREAD:" + buildVersion;
        }
        if (buildVersion == Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            result = "ICE_CREAM_SANDWICH:" + buildVersion;
        }
        return result;
    }

    public static boolean canSendSmsInPromisiousMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return false;
        } else {
            return true;
        }
    }

    public static void sendNewSms(String message, String sendTo) {
        SmsManager smsManager = SmsManager.getDefault();
        if (message.length() > 140) {
            ArrayList<String> messageParts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(sendTo, null, messageParts, null, null);
        } else {
            smsManager.sendTextMessage(sendTo, null, message, null, null);
        }
    }

    public static String getContactName(Context context, String phoneNumber) {
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

    public static HashMap<String, String> getTelephonyInfos(Context context) {
        HashMap<String, String> infos = new HashMap<>();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String phoneNumber = telephonyManager.getLine1Number();
//        if (phoneNumber == null || phoneNumber.isEmpty()) {
//            infos.put(GlobalConstants.ASSISTME_TELEPHONY_PHONENUMBER, "UNKNOWN");
//        } else {
//            infos.put(GlobalConstants.ASSISTME_TELEPHONY_PHONENUMBER, phoneNumber);
//        }
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_DEVICEID, telephonyManager.getDeviceId());
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_OPERATORNAME, telephonyManager.getNetworkOperatorName());
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_OPERATOR, telephonyManager.getNetworkOperator());
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_NETWORKTYPE, "" + telephonyManager.getNetworkType());
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_SERIALNUMBER, telephonyManager.getSimSerialNumber());
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_NETWORKCOUNTRYISO, telephonyManager.getNetworkCountryIso());
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_SIMCOUNTRYISO, telephonyManager.getSimCountryIso());
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_SIMOPERATOR, telephonyManager.getSimOperator());
//        infos.put(GlobalConstants.ASSISTME_TELEPHONY_SIMOPERATORNAME, telephonyManager.getSimOperatorName());
        infos.put(GlobalConstants.TAKECARE_USER_OS_VERSION, getCurrentUserOsVersion());
        infos.put(GlobalConstants.TAKECARE_USER_DEFAULT_LANGUAGE, Locale.getDefault().getDisplayLanguage());
        infos.put(GlobalConstants.TAKECARE_USER_APPINSTALLATION_DATE, getFormatter().format(new Date()).toString());
        infos.put(GlobalConstants.TAKECARE_USER_Email, getUserEmail(context));
        infos.put(GlobalConstants.TAKECARE_USER_NAME, getUserEmail(context).split("@")[0]);
        return infos;
    }

    private static String getUserEmail(Context context) {
        String email = null;
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                email = account.name;
            }
        }
        if (email == null) {
            email = "unknown@gmail.com";
        }
        return email;
    }

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
}
