package com.it.mougang.gasmyr.takecare;

import android.content.Intent;
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

import com.it.mougang.gasmyr.takecare.Realm.RealmApplicationController;
import com.it.mougang.gasmyr.takecare.adapters.BirthdayAdapter;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;
import com.it.mougang.gasmyr.takecare.utils.datepicker.DatePickerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class MainActivityFragment extends Fragment {
    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BirthdayAdapter adapter;
    private RealmResults<Birthday> realmObjects;
    private SimpleDateFormat formatter = Utils.getFormatter();

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
        realmObjects = RealmApplicationController.with(this).getAllBirthdaysAsync();
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
                break;
            case 2:
                launchBirthdayDetail(birthday);
                break;
            case 3:
                showBirthdatePickerDialog();
                break;
            case 4:
                break;
        }
    }

    private void launchBirthdayDetail(Birthday currentBirthday) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BirthdayDetailActivity.class);
        intent.putExtra(GlobalConstants.BIRTHDAY_DATE, formatter.format(currentBirthday.getBirthdate()));
        intent.putExtra(GlobalConstants.BIRTHDAY_NEXT_DATE, formatter.format(currentBirthday.getNextBirthDate()));
        intent.putExtra(GlobalConstants.BIRTHDAY_FULLNAME, currentBirthday.getFullName());
        intent.putExtra(GlobalConstants.BIRTHDAY_NUMBER, currentBirthday.getPhonenumber());
        intent.putExtra(GlobalConstants.BIRTHDAY_REMAINING_DAYS,currentBirthday.getRemainingsDays());
        intent.putExtra(GlobalConstants.BIRTHDAY_ID, String.valueOf(currentBirthday.getId()));
        getActivity().startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBirthdatePicked(Date date) {
        RealmApplicationController.with(this).updateBirthday(currentBirthday, date);
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangedInDatabase(boolean hasChanged) {
        if (hasChanged) {
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
        RealmApplicationController.with(this).close();
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
