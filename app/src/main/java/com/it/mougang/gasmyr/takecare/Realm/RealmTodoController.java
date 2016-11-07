package com.it.mougang.gasmyr.takecare.Realm;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.it.mougang.gasmyr.takecare.MyApplication;
import com.it.mougang.gasmyr.takecare.domain.Todo;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by gamyr on 11/2/16.
 */

public class RealmTodoController {

    private static RealmTodoController instance;
    private Realm realm;

    public RealmTodoController(Application application) {
        realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                EventBus.getDefault().post(true);
            }
        });
        setAutoRefresh();
    }

    public static RealmTodoController with(@NonNull Fragment fragment) {
        if (instance == null) {
            instance = new RealmTodoController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmTodoController with(@NonNull Activity activity) {
        if (instance == null) {
            instance = new RealmTodoController(activity.getApplication());
        }
        return instance;
    }

    public static RealmTodoController with(Application application) {
        if (instance == null) {
            instance = new RealmTodoController(application);
        }
        return instance;
    }

    public static RealmTodoController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    private void setAutoRefresh() {
        realm.setAutoRefresh(true);
    }

    @NonNull
    public RealmResults<Todo> getAllTodosAsync() {
        if(realm.isClosed()){
            realm = Realm.getInstance(MyApplication.getInstance().realmConfiguration);
        }
        RealmResults<Todo> result = realm.where(Todo.class).findAllAsync();
        return result.sort("startdate", Sort.DESCENDING);
    }

    public void updateTodo(Todo todo) {

    }



    public void close() {
        realm.close();
    }

}
