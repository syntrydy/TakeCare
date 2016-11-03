package com.it.mougang.gasmyr.takecare.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.domain.Todo;

import org.greenrobot.eventbus.EventBus;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by gamyr on 11/2/16.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> implements RealmChangeListener<RealmResults<Todo>> {

    private final EventBus eventBus = EventBus.getDefault();
    private RealmResults<Todo> realmResults;

    public TodoAdapter(RealmResults<Todo> realmResults) {
        this.realmResults = realmResults;
        this.realmResults.addChangeListener(this);
    }

    @Override
    public TodoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_list_row, parent, false);
        return new TodoAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TodoAdapter.MyViewHolder holder, final int position) {
        final Todo todo = realmResults.get(position);
        holder.titleTv.setText(todo.getTitle());
        holder.descriptionTv.setText(todo.getDescription());
        holder.startdateTv.setText(todo.getStartdate().toString());
        holder.enddateTv.setText(todo.getEndate().toString());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setEventcode(1);
                eventBus.post(todo);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                todo.setEventcode(2);
                eventBus.post(todo);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onChange(RealmResults<Todo> element) {
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTv, descriptionTv, startdateTv, enddateTv;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            titleTv = (TextView) view.findViewById(R.id.todotitle);
            descriptionTv = (TextView) view.findViewById(R.id.tododescription);
            startdateTv = (TextView) view.findViewById(R.id.todostartdate);
            enddateTv = (TextView) view.findViewById(R.id.todoendate);
        }
    }


}
