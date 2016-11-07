package com.it.mougang.gasmyr.takecare.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by gamyr on 10/26/16.
 */

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.MyViewHolder> implements RealmChangeListener<RealmResults<Birthday>> {

    private final SimpleDateFormat format = Utils.getDateFormatter();
    private final EventBus eventBus = EventBus.getDefault();
    private RealmResults<Birthday> realmResults;
    private Context context;


    public BirthdayAdapter(RealmResults<Birthday> realmResults, Context context) {
        this.realmResults = realmResults;
        this.context = context;
        this.realmResults.addChangeListener(this);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.birthday_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Birthday birthday = realmResults.get(position);
        holder.fullnameTv.setText(birthday.getFullName());
        Utils.roundedProfileImage(context, holder.photoImageV, R.drawable.profile02);
        holder.phonenumberTv.setText(birthday.getPhonenumber());
        holder.birthdayTv.setText(format.format(birthday.getBirthdate()));
        if (birthday.getBirthdate().after(new Date())) {
            holder.remainingdaysTv.setText(String.valueOf(Utils.daysBetweenUsingJoda(new Date(), birthday.getBirthdate())));
        } else if (birthday.getBirthdate().getYear() == new Date().getYear()) {
            holder.remainingdaysTv.setText(String.valueOf(Utils.daysBetweenUsingJoda(birthday.getBirthdate(), new Date())));
        } else if (birthday.getBirthdate().getYear() != new Date().getYear()) {
            holder.remainingdaysTv.setText(String.valueOf(Utils.daysBetweenUsingJoda(Utils.adjustDate(birthday.getBirthdate()), new Date())));
        } else {
            holder.remainingdaysTv.setText(String.valueOf(Utils.daysBetweenUsingJoda(birthday.getBirthdate(), new Date())));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthday.setEventcode(1);
                eventBus.post(birthday);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                birthday.setEventcode(2);
                eventBus.post(birthday);
                return true;
            }
        });
        holder.setBirthdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthday.setEventcode(3);
                eventBus.post(birthday);
            }
        });
        if (birthday.isrealm()) {
            holder.setBirthdateButton.setText("edit birthday");
            holder.setBirthdateButton.setBackgroundResource(R.color.colorAccent2);
            holder.setBirthdateButton.setEnabled(false);
            holder.checkBox.setChecked(false);
        } else {
            holder.checkBox.setEnabled(false);
            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (birthday.isrealm() && isChecked) {
                    holder.setBirthdateButton.setBackgroundResource(R.color.colorAccent3);
                    holder.setBirthdateButton.setEnabled(true);
                } else if (birthday.isrealm() && !isChecked) {
                    holder.setBirthdateButton.setBackgroundResource(R.color.colorAccent2);
                    holder.setBirthdateButton.setEnabled(false);
                    holder.checkBox.setEnabled(true);
                }
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
    public void onChange(RealmResults<Birthday> element) {
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fullnameTv, phonenumberTv, birthdayTv, remainingdaysTv;
        public ImageView photoImageV;
        public Button setBirthdateButton;
        public CheckBox checkBox;
        public CardView cardView;

        public MyViewHolder(@NonNull View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            fullnameTv = (TextView) view.findViewById(R.id.fullName);
            phonenumberTv = (TextView) view.findViewById(R.id.phonenumber);
            birthdayTv = (TextView) view.findViewById(R.id.birthdate);
            remainingdaysTv = (TextView) view.findViewById(R.id.remainingdays);
            photoImageV = (ImageView) view.findViewById(R.id.photo);
            setBirthdateButton = (Button) view.findViewById(R.id.setbirthdatebutton);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }

}
