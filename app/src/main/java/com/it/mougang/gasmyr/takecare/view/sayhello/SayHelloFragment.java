package com.it.mougang.gasmyr.takecare.view.sayhello;


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
import com.it.mougang.gasmyr.takecare.domain.SayHello;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class SayHelloFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<SayHello> sayHellos;
    private SayHelloAdapter adapter;


    public SayHelloFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_say_hello, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.sayhelloRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sayHellos = RealmApplicationController.with(this).getAllSayHelloAsync();
        adapter = new SayHelloAdapter(sayHellos, getActivity().getApplicationContext());
        sayHellos.addChangeListener(new RealmChangeListener<RealmResults<SayHello>>() {
            @Override
            public void onChange(RealmResults<SayHello> element) {
                adapter.notifyDataSetChanged();
            }
        });
        myRecyclerView.setAdapter(adapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSayHelloRowChanged(@NonNull SayHello sayHello) {
        switch (sayHello.getEventcode()) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                handleRowChecked(sayHello, sayHello.getStatus());
                break;
        }
    }

    private void handleRowChecked(SayHello sayHello,boolean status) {
        RealmApplicationController.with(getActivity()).updateSayHello(sayHello, status);
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
}
