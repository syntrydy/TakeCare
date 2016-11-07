package com.it.mougang.gasmyr.takecare.view.todo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.Realm.RealmTodoController;
import com.it.mougang.gasmyr.takecare.adapters.TodoAdapter;
import com.it.mougang.gasmyr.takecare.domain.Todo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodosFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TodoAdapter adapter;
    private RealmResults<Todo> realmObjects;
    private Todo currentTodo;

    public TodosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.todosRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        realmObjects = RealmTodoController.with(this).getAllTodosAsync();
        adapter = new TodoAdapter(realmObjects);
        realmObjects.addChangeListener(new RealmChangeListener<RealmResults<Todo>>() {
            @Override
            public void onChange(RealmResults<Todo> element) {
                adapter.notifyDataSetChanged();
            }
        });
        myRecyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOnTodoRow(@NonNull Todo todo) {
        currentTodo = todo;
        switch (todo.getEventcode()) {
            case 1:
                Toast.makeText(getActivity().getApplicationContext(), "clicked " + currentTodo.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getActivity().getApplicationContext(), "long clicked " + currentTodo.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
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
        RealmTodoController.with(this).close();
        super.onDestroy();
    }
}
