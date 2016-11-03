package com.it.mougang.gasmyr.takecare.view.navigationdrawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.MyApplication;
import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;


public class NavigationDrawerFragment extends Fragment {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;


    public NavigationDrawerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        setupRecyclerView(view);

        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myDrawerList);
        NavigationDrawerAdapter adpater = new NavigationDrawerAdapter(getActivity(), NavigationDrawerItem.getData(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adpater);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setUpDrawer(DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        ImageView drawerProfile = (ImageView) mDrawerLayout.findViewById(R.id.userImage);
        drawerProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"change photo",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        TextView profileName = (TextView) mDrawerLayout.findViewById(R.id.profileName);
        profileName.setTypeface(Utils.getCampagneFont(getActivity().getApplicationContext()));
        TextView profileEmail = (TextView) mDrawerLayout.findViewById(R.id.profileEmail);
        profileEmail.setTypeface(Utils.getCampagneFont(getActivity().getApplicationContext()));
        Utils.roundedProfileImage(getContext(), drawerProfile, R.drawable.profile05);
        String username = MyApplication.getInstance().infos.get(GlobalConstants.TAKECARE_USER_NAME);
        profileName.setText(username);
        String email = MyApplication.getInstance().infos.get(GlobalConstants.TAKECARE_USER_Email);
        profileEmail.setText(email);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
}

