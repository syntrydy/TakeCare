package com.it.mougang.gasmyr.takecare.Realm;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.it.mougang.gasmyr.takecare.MyApplication;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.domain.BirthdayMessageModel;
import com.it.mougang.gasmyr.takecare.domain.SayHello;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by gamyr on 10/30/16.
 */

public class RealmBirthdayController {
    private static RealmBirthdayController instance;
    private Realm realm;

    public RealmBirthdayController(Application application) {
        realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                EventBus.getDefault().post(true);
            }
        });
        setAutoRefresh();
    }

    public static RealmBirthdayController with(@NonNull Fragment fragment) {
        if (instance == null) {
            instance = new RealmBirthdayController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmBirthdayController with(@NonNull Activity activity) {
        if (instance == null) {
            instance = new RealmBirthdayController(activity.getApplication());
        }
        return instance;
    }

    public static RealmBirthdayController with(Application application) {
        if (instance == null) {
            instance = new RealmBirthdayController(application);
        }
        return instance;
    }

    public static RealmBirthdayController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    private void setAutoRefresh() {
        realm.setAutoRefresh(true);
    }

    @NonNull
    public RealmResults<Birthday> getAllBirthdaysAsync() {
        if (realm.isClosed()) {
            realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        }
        RealmResults<Birthday> result = realm.where(Birthday.class).findAllAsync();
        return result.sort("isrealm", Sort.DESCENDING);
    }

    @NonNull
    public RealmResults<SayHello> getAllSayHelloAsync() {
        if (realm.isClosed()) {
            realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        }
        RealmResults<SayHello> result = realm.where(SayHello.class).findAllAsync();
        return result.sort("isSheduled", Sort.DESCENDING);
    }

    public void updateBirthday(@NonNull Birthday birthday, Date date) {
        realm.beginTransaction();
        birthday.setIsrealm(true);
        birthday.setBirthdate(date);
        realm.commitTransaction();
    }

    public void updateSayHello(@NonNull SayHello sayHello, boolean status) {
        realm.beginTransaction();
        sayHello.setSheduled(status);
        realm.commitTransaction();
    }

    public void updateBirthDay(long birthdayId, BirthdayMessageModel model) {
        Birthday birthday = realm.where(Birthday.class).equalTo("id", birthdayId).findFirstAsync();
        realm.beginTransaction();
        birthday.setMessageModel(model);
        realm.commitTransaction();
    }

    public void saveBirthdayWithTransaction(@NonNull final List<Birthday> birthdays) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                for (Birthday birthday : birthdays) {
                    realm.copyToRealmOrUpdate(birthday);
                }
            }
        });
    }

    public void close() {
        realm.close();
    }

    public void copyDataToRealm(@Nullable List<Birthday> fromContacts) {
        if (fromContacts != null && fromContacts.size() >= 1) {
            saveBirthdayWithTransaction(fromContacts);
        }
    }

    public void copyHellosToRealm(@Nullable List<SayHello> hellos) {
        if (hellos != null && hellos.size() >= 1) {
            saveSayHelloWithTransaction(hellos);
        }
    }

    private void saveSayHelloWithTransaction(final List<SayHello> hellos) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                for (SayHello hello : hellos) {
                    realm.copyToRealmOrUpdate(hello);
                }
            }
        });
    }
}
