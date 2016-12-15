package com.it.mougang.gasmyr.takecare.view.loves;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.Realm.RealmApplicationController;
import com.it.mougang.gasmyr.takecare.adapters.SayHelloAdapter;
import com.it.mougang.gasmyr.takecare.adapters.SmsLifeAdapter;
import com.it.mougang.gasmyr.takecare.domain.EventBusMessage;
import com.it.mougang.gasmyr.takecare.domain.SayHello;
import com.it.mougang.gasmyr.takecare.domain.SmsForLife;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class LovesFragment extends Fragment {
    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<SmsForLife> smsForLifes;
    private SmsLifeAdapter adapter;


    public LovesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loves, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.smsforlifeRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        smsForLifes = RealmApplicationController.with(this).getAllSmsForLifeHelloAsync();
        adapter = new SmsLifeAdapter(smsForLifes, getActivity().getApplicationContext(), EventBus.getDefault());
        smsForLifes.addChangeListener(new RealmChangeListener<RealmResults<SmsForLife>>() {
            @Override
            public void onChange(RealmResults<SmsForLife> element) {
                adapter.notifyDataSetChanged();
            }
        });
        myRecyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSayHelloRowChanged(@NonNull EventBusMessage eventBusMessage) {
        if (eventBusMessage.getMessageCode().equalsIgnoreCase("OK")) {
            sendLoveMessageSms(eventBusMessage.getMessageText());
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
    private void sendLoveMessageSms(String message) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", "");
        smsIntent.putExtra("sms_body", message);
        startActivity(smsIntent);
    }

}
