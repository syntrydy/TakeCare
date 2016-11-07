package com.it.mougang.gasmyr.takecare;

import android.app.Application;
import android.content.res.Configuration;

import com.it.mougang.gasmyr.takecare.utils.Utils;

import net.danlew.android.joda.JodaTimeAndroid;

import java.security.SecureRandom;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by gamyr on 10/27/16.
 */

public class MyApplication extends Application {

    public static HashMap<String, String> infos = new HashMap<>();
    public static MyApplication singleton;
    public static RealmConfiguration realmConfiguration;

    public static MyApplication getInstance() {
        if (singleton == null) {
            singleton = new MyApplication();
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);
        realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        JodaTimeAndroid.init(this);
        if (!Utils.hasM()) {
            infos = Utils.getTelephonyInfos(this);
        } else {
            if (Utils.isContactsPermissionIsGranted(getApplicationContext())) {
                infos = Utils.getTelephonyInfos(this);
            }
        }

    }


    public void setInfos(HashMap<String, String> newInfos) {
        infos = newInfos;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
