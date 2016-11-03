package com.it.mougang.gasmyr.takecare.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.domain.SayHello;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by gamyr on 10/28/16.
 */

public class SayHelloAdapter extends RecyclerView.Adapter<SayHelloAdapter.MyViewHolder> {

    private List<SayHello> sayHellos;
    private Context context;
    private final EventBus eventBus=EventBus.getDefault();

    public SayHelloAdapter(List<SayHello> hellos, Context context) {
        this.sayHellos = hellos;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fullnameTv, phonenumberTv;
        public ImageView photoImageV;
        public Button sayhelloButton, makebipmeButton;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            fullnameTv = (TextView) view.findViewById(R.id.fullName);
            phonenumberTv = (TextView) view.findViewById(R.id.phonenumber);
            photoImageV = (ImageView) view.findViewById(R.id.photo);
            sayhelloButton = (Button) view.findViewById(R.id.sayhellobutton);
            makebipmeButton = (Button) view.findViewById(R.id.bipmebutton);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sayhello_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SayHello sayHello = sayHellos.get(position);
        holder.fullnameTv.setText(sayHello.getFullName());
        Utils.roundedProfileImage(context, holder.photoImageV, R.drawable.profile00);
        holder.phonenumberTv.setText(sayHello.getPhonenumber());
        holder.sayhelloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sayHello.setEventcode(3);
                eventBus.post(sayHello);
            }
        });
        holder.makebipmeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sayHello.setEventcode(4);
                eventBus.post(sayHello);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sayHello.setEventcode(1);
                eventBus.post(sayHello);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sayHello.setEventcode(2);
                eventBus.post(sayHello);
                return true;
            }
        });
    }


    public void clear() {
        sayHellos.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<SayHello> hellos) {
        sayHellos.addAll(hellos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sayHellos.size();
    }
}