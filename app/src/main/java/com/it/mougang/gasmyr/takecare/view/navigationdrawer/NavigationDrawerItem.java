package com.it.mougang.gasmyr.takecare.view.navigationdrawer;

/**
 * Created by gamyr on 10/24/16.
 */


import android.content.Context;
import android.support.annotation.NonNull;

import com.it.mougang.gasmyr.takecare.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerItem {
    private String NavItemTitle;
    private int NavItemImageId;
    private int id;

    public NavigationDrawerItem() {
    }

    @NonNull
    public static List<NavigationDrawerItem> getData(@NonNull Context context) {
        List<NavigationDrawerItem> items = new ArrayList<>();
        String[] titles = getTitles(context);
        int[] imagesIds = getImagesIds();
        int[] ids = getIds();
        for (int i = 0; i < titles.length; i++) {
            NavigationDrawerItem item = new NavigationDrawerItem();
            item.setNavItemTitle(titles[i]);
            item.setNavItemImageId(imagesIds[i]);
            item.setId(ids[i]);
            items.add(item);
        }
        return items;
    }

    @NonNull
    private static int[] getImagesIds() {
        return new int[]{
                R.drawable.ic_trending_up_white_24dp,
                R.drawable.ic_flight_takeoff_white_24dp,
                R.drawable.ic_timeline_white_24dp,
                R.drawable.ic_traffic_white_24dp,
                R.drawable.ic_tune_white_24dp,
                R.drawable.ic_swap_horiz_white_24dp
        };
    }

    @NonNull
    private static int[] getIds() {
        return new int[]{
                1,
                2,
                3,
                4,
                5,
                6
        };
    }

    @NonNull
    private static String[] getTitles(@NonNull Context context) {

        return new String[]{
                context.getResources().getString(R.string.app_drawer_enable_birthday_notification),
                context.getResources().getString(R.string.app_drawer_enable_sms_responder_notification),
                context.getResources().getString(R.string.app_drawer_enable_call_responder_notification),
                context.getResources().getString(R.string.app_drawer_enable_toto_reminder),
                context.getResources().getString(R.string.app_drawer_enable_sayhello_sms_sender),
                context.getResources().getString(R.string.app_drawer_enable_sayhello_sms_sender)
        };
    }

    public String getNavItemTitle() {
        return NavItemTitle;
    }

    public void setNavItemTitle(String navItemTitle) {
        NavItemTitle = navItemTitle;
    }

    public int getNavItemImageId() {
        return NavItemImageId;
    }

    public void setNavItemImageId(int navItemImageId) {
        NavItemImageId = navItemImageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

