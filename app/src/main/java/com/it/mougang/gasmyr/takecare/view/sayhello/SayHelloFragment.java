package com.it.mougang.gasmyr.takecare.view.sayhello;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.adapters.SayHelloAdapter;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.domain.SayHello;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SayHelloFragment extends Fragment{

    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<SayHello> sayHellos;
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
        sayHellos = new ArrayList<>();
        adapter = new SayHelloAdapter(sayHellos, getActivity().getApplicationContext());
        adapter.clear();
        adapter.addAll(Utils.getSayHelloList(getContext()));
        myRecyclerView.setAdapter(adapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSayHelloRowChanged(@NonNull SayHello sayHello) {
        switch (sayHello.getEventcode()){
            case 1:
                Toast.makeText(getActivity().getApplicationContext(),"clicked "+sayHello.getFullName(),Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getActivity().getApplicationContext(),"long clicked "+sayHello.getFullName(),Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getActivity().getApplicationContext(),"said hello "+sayHello.getFullName(),Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getActivity().getApplicationContext(),"bipme" +sayHello.getFullName(),Toast.LENGTH_SHORT).show();
                break;
        }
        Log.d("XAVIERE",sayHello.getFullName());
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
