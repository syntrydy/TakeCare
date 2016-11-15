package com.it.mougang.gasmyr.takecare.view.navigationdrawer;

/**
 * Created by gamyr on 10/24/16.
 */


import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.utils.AlarmManagerUtils;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;

import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    private Context context;
    private List<NavigationDrawerItem> navigationDrawerItems;
    private LayoutInflater inflater;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public NavigationDrawerAdapter(@NonNull Context context, List<NavigationDrawerItem> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.navigationDrawerItems = items;
        sharedPreferences = context.getSharedPreferences(GlobalConstants.TAKECARE_SHARE_PRFERENCE, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final NavigationDrawerItem current = navigationDrawerItems.get(position);
        holder.title.setText(current.getNavItemTitle());
        holder.aSwitch.setChecked(sharedPreferences.getBoolean(current.getNavItemTitle(), true));
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putBoolean(current.getNavItemTitle(), true);
                    editor.commit();


                    if (current.getId() == 1) {
                        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        AlarmManagerUtils.scheduleBirthdayAlarm(context, alarm);
                    } else if (current.getId() == 2) {

                    } else if (current.getId() == 3) {

                    } else if (current.getId() == 4) {

                    } else if (current.getId() == 5) {

                    } else if (current.getId() == 6) {

                    }

                } else {
                    editor.putBoolean(current.getNavItemTitle(), false);
                    editor.commit();

                    if (current.getId() == 1) {
                        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        AlarmManagerUtils.cancelBirthDayAlarm(context, alarm);
                    } else if (current.getId() == 2) {

                    } else if (current.getId() == 3) {

                    } else if (current.getId() == 4) {

                    } else if (current.getId() == 5) {

                    } else if (current.getId() == 6) {

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return navigationDrawerItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        //        ImageView imgIcon;
        Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.nav_item_title);
//            imgIcon = (ImageView) itemView.findViewById(R.id.nav_item_image);
            aSwitch = (Switch) itemView.findViewById(R.id.mySwitch);
            aSwitch.setChecked(true);
        }
    }
}

