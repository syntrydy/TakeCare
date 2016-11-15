package com.it.mougang.gasmyr.takecare.Realm;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.it.mougang.gasmyr.takecare.MyApplication;
import com.it.mougang.gasmyr.takecare.domain.BirthdayMessageModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by bao on 11/13/16.
 */

public class RealmMessageModelController {
    private static RealmMessageModelController instance;
    private Realm realm;

    public RealmMessageModelController(Application application) {
        realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                EventBus.getDefault().post(true);
            }
        });
        setAutoRefresh();
    }

    public static RealmMessageModelController with(@NonNull Fragment fragment) {
        if (instance == null) {
            instance = new RealmMessageModelController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmMessageModelController with(@NonNull Activity activity) {
        if (instance == null) {
            instance = new RealmMessageModelController(activity.getApplication());
        }
        return instance;
    }

    public static RealmMessageModelController with(Application application) {
        if (instance == null) {
            instance = new RealmMessageModelController(application);
        }
        return instance;
    }

    public static RealmMessageModelController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    private void setAutoRefresh() {
        realm.setAutoRefresh(true);
    }

    @NonNull
    public RealmResults<BirthdayMessageModel> getAllBirthdayMessagesAsync() {
        if (realm.isClosed()) {
            realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        }

        RealmResults<BirthdayMessageModel> result = realm.where(BirthdayMessageModel.class)
                .findAllAsync();
        return result.sort("text", Sort.ASCENDING);
    }

    public List<String> getAllAsText() {
        List<String> models = new ArrayList<>();
        for (BirthdayMessageModel model : getAllBirthdayMessagesAsync()) {
            models.add(model.getId(),model.getText());
        }
        return models;
    }

    public void saveBirthdayMessageModelWithTransaction(@NonNull final List<BirthdayMessageModel> messageModels) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                for (BirthdayMessageModel birthdayMessageModel : messageModels) {
                    realm.copyToRealmOrUpdate(birthdayMessageModel);
                }
            }
        });
    }

    public void close() {
        realm.close();
    }

    public void copyDataToRealm(@Nullable List<BirthdayMessageModel> birthdayMessageModelList) {
        if (birthdayMessageModelList != null && birthdayMessageModelList.size() >= 1) {
            saveBirthdayMessageModelWithTransaction(birthdayMessageModelList);
        }
    }

    public BirthdayMessageModel getMessageById(int id) {
        return realm.where(BirthdayMessageModel.class).equalTo("id", id).findFirst();
    }
    public String getMessageByIdAsText(int id) {
        if(realm.isClosed()){
            realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        }
        String res = realm.where(BirthdayMessageModel.class).equalTo("id", id).findFirstAsync().getText();
        Log.d("MAIN","******** "+res);
        return res;
    }
}
