package com.it.mougang.gasmyr.takecare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.Realm.RealmBirthdayController;
import com.it.mougang.gasmyr.takecare.adapters.BirthdayAdapter;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.utils.Utils;
import com.it.mougang.gasmyr.takecare.utils.datepicker.DatePickerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class MainActivityFragment extends Fragment {
    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BirthdayAdapter adapter;
    private RealmResults<Birthday> realmObjects;

    private Birthday currentBirthday;


    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.birthdayRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        realmObjects = RealmBirthdayController.with(this).getAllBirthdaysAsync();
        adapter = new BirthdayAdapter(realmObjects, getActivity().getApplicationContext());
        realmObjects.addChangeListener(new RealmChangeListener<RealmResults<Birthday>>() {
            @Override
            public void onChange(RealmResults<Birthday> element) {
                adapter.notifyDataSetChanged();
            }
        });
        myRecyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSayBithdayRowChanged(@NonNull Birthday birthday) {
        currentBirthday = birthday;
        switch (birthday.getEventcode()) {
            case 1:
                Toast.makeText(getActivity().getApplicationContext(), "clicked " + birthday.getFullName(), Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getActivity().getApplicationContext(), "long clicked " + birthday.getFullName(), Toast.LENGTH_SHORT).show();
                break;
            case 3:
                showBirthdatePickerDialog();
                Toast.makeText(getActivity().getApplicationContext(), "set bithday " + birthday.getFullName() + " ->" + currentBirthday.getId(), Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getActivity().getApplicationContext(), "checked " + birthday.getFullName(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBirthdatePicked(Date date) {
        RealmBirthdayController.with(this).updateBirthday(currentBirthday,date);
        adapter.notifyDataSetChanged();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangedInDatabase(boolean hasChanged){
        if(hasChanged){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        RealmBirthdayController.with(this).close();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showBirthdatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "birthdatePicker");
    }
}
