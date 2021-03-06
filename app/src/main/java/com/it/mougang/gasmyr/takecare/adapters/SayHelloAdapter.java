package com.it.mougang.gasmyr.takecare.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.Realm.RealmApplicationController;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.domain.SayHello;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by gamyr on 10/28/16.
 */

public class SayHelloAdapter extends RecyclerView.Adapter<SayHelloAdapter.MyViewHolder> implements RealmChangeListener<RealmResults<SayHello>> {

    private RealmResults<SayHello> realmResults;
    private Context context;
    private Realm realm;

    public SayHelloAdapter(RealmResults<SayHello> hellos, Context context, Realm realm) {
        this.realmResults = hellos;
        this.context = context;
        this.realmResults.addChangeListener(this);
        this.realm=realm;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fullnameTv, phonenumberTv;
        public ImageView photoImageV;
        public CheckBox checkBox;
        public CardView cardView;

        public MyViewHolder(@NonNull View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            fullnameTv = (TextView) view.findViewById(R.id.fullName);
            phonenumberTv = (TextView) view.findViewById(R.id.phonenumber);
            photoImageV = (ImageView) view.findViewById(R.id.photo);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sayhello_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final SayHello sayHello = realmResults.get(position);
        holder.fullnameTv.setText(sayHello.getFullName());
        Utils.roundedProfileImage(context, holder.photoImageV, R.drawable.profile00);
        holder.phonenumberTv.setText(sayHello.getPhonenumber());
        holder.checkBox.setChecked(sayHello.isRunning());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    realm.beginTransaction();
                    sayHello.setRunning(true);
                    sayHello.setSorthelper("A");
                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    sayHello.setRunning(false);
                    sayHello.setSorthelper("B");
                    realm.commitTransaction();
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
    @Override
    public void onChange(RealmResults<SayHello> element) {
        notifyDataSetChanged();
    }
}