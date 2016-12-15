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
import com.it.mougang.gasmyr.takecare.domain.SmsForLife;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by gamyr on 10/30/16.
 */

public class RealmApplicationController {
    private static RealmApplicationController instance;
    private Realm realm;

    public RealmApplicationController(Application application) {
        realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                EventBus.getDefault().post(true);
            }
        });
        setAutoRefresh();
    }

    public static RealmApplicationController with(@NonNull Fragment fragment) {
        if (instance == null) {
            instance = new RealmApplicationController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmApplicationController with(@NonNull Activity activity) {
        if (instance == null) {
            instance = new RealmApplicationController(activity.getApplication());
        }
        return instance;
    }

    public static RealmApplicationController with(Application application) {
        if (instance == null) {
            instance = new RealmApplicationController(application);
        }
        return instance;
    }

    public static RealmApplicationController getInstance() {
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
        return result;
    }

    @NonNull
    public RealmResults<SmsForLife> getAllSmsForLifeHelloAsync() {
        if (realm.isClosed()) {
            realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        }
        RealmResults<SmsForLife> result = realm.where(SmsForLife.class).findAllAsync();
        return result.sort("text", Sort.DESCENDING);
    }


    public void updateBirthday(@NonNull Birthday birthday, Date date) {
        realm.beginTransaction();
        birthday.setIsrealm(true);
        birthday.setBirthdate(date);
        realm.commitTransaction();
    }

    public void updateSayHello(@NonNull SayHello sayHello) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(sayHello);
        realm.commitTransaction();
    }

    public Birthday updateBirthDay(final long birthdayId, final BirthdayMessageModel model) {

        final Birthday birthday1 = realm.where(Birthday.class).equalTo("id", birthdayId).findFirstAsync();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BirthdayMessageModel newModel = realm.createObject(BirthdayMessageModel.class);
                newModel.setKey(UUID.randomUUID().toString());
                newModel.setText(model.getText());
                birthday1.setMessageModel(newModel);
            }
        });
        return realm.where(Birthday.class).equalTo("id", birthdayId).findFirstAsync();
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

    public void copyLoveMessageToRealm(@Nullable List<SmsForLife> smsForLifes) {
        if (smsForLifes != null && smsForLifes.size() >= 1) {
            saveLovesMessagesWithTransaction(smsForLifes);
        }
    }

    private void saveLovesMessagesWithTransaction(final List<SmsForLife> smsForLifes) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                for (SmsForLife smsForLife : smsForLifes) {
                    realm.copyToRealmOrUpdate(smsForLife);
                }
            }
        });
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

    public Birthday getBirthdayById(long birthdayId) {
        if (realm.isClosed()) {
            realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        }
        return realm.where(Birthday.class).equalTo("id", birthdayId).findFirst();
    }
}
