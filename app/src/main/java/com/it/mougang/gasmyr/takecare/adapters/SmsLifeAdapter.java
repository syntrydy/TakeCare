package com.it.mougang.gasmyr.takecare.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.domain.EventBusMessage;
import com.it.mougang.gasmyr.takecare.domain.SmsForLife;

import org.greenrobot.eventbus.EventBus;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by gasmyr.mougang on 12/12/16.
 */

public class SmsLifeAdapter extends RecyclerView.Adapter<SmsLifeAdapter.MyViewHolder> implements RealmChangeListener<RealmResults<SmsForLife>> {
    private RealmResults<SmsForLife> realmResults;
    private Context context;
    private EventBus eventBus;

    public SmsLifeAdapter(RealmResults<SmsForLife> smsForLifes, Context context, EventBus eventBus) {
        this.realmResults = smsForLifes;
        this.context = context;
        this.eventBus = eventBus;
        this.realmResults.addChangeListener(this);
    }

    @Override
    public void onChange(RealmResults<SmsForLife> element) {
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public CardView cardView;
        private ImageButton actionSend;
        private CheckBox actionCheck;

        public MyViewHolder(@NonNull View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            message = (TextView) view.findViewById(R.id.loveMessage);
            actionSend = (ImageButton) view.findViewById(R.id.actionSend);
            actionCheck = (CheckBox) view.findViewById(R.id.actionCkeck);
        }

    }

    @NonNull
    @Override
    public SmsLifeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.smslife_row, parent, false);
        return new SmsLifeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SmsLifeAdapter.MyViewHolder holder, final int position) {
        final SmsForLife smsForLife = realmResults.get(position);
        holder.message.setText(smsForLife.getText());
        holder.actionSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBusMessage eventBusMessage=new EventBusMessage("Ok",smsForLife.getText());
                eventBus.post(eventBusMessage);
            }
        });
        holder.actionCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    holder.actionSend.setVisibility(View.VISIBLE);
                } else {
                    holder.actionSend.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }
}
