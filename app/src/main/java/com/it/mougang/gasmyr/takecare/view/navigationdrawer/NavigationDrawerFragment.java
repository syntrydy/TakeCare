package com.it.mougang.gasmyr.takecare.view.navigationdrawer;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.MainActivity;
import com.it.mougang.gasmyr.takecare.MyApplication;
import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.imageutils.PickerBuilder;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class NavigationDrawerFragment extends Fragment {
    ImageView drawerProfile;
    LinearLayout carocell;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String currentUri;


    public NavigationDrawerFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        return view;
    }

    public void setUpDrawer(DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        drawerProfile = (ImageView) mDrawerLayout.findViewById(R.id.userImage);
        carocell=(LinearLayout)mDrawerLayout.findViewById(R.id.carocell);
        carocell.setBackgroundResource(Utils.getResourceID("a"+Utils.getRandom(20),"drawable",getActivity().getApplicationContext()));
        drawerProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                loadImagePicker();
                return true;
            }
        });
        init();
        if (currentUri == null) {
            Utils.roundedProfileImage(getContext(), drawerProfile, R.drawable.profile05);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(currentUri).getEncodedPath());
            Utils.roundedBitmap(getContext(), drawerProfile, bitmap);
        }

        TextView profileName = (TextView) mDrawerLayout.findViewById(R.id.profileName);
        profileName.setTypeface(Utils.getOpenItalicFont(getActivity().getApplicationContext()));
        TextView profileEmail = (TextView) mDrawerLayout.findViewById(R.id.profileEmail);
        profileEmail.setTypeface(Utils.getOpenItalicFont(getActivity().getApplicationContext()));
        profileName.setText(MyApplication.getInstance().infos.get(GlobalConstants.APPLICATION_PHONE_OWNER_NAME));
        profileEmail.setText(MyApplication.getInstance().infos.get(GlobalConstants.APPLICATION_PHONE_OWNER_EMAIL));
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

    private void init() {
        sharedPreferences = getActivity().getSharedPreferences(GlobalConstants.APPLICATION_SHAREPRFERENCE, MODE_PRIVATE);
        currentUri = sharedPreferences.getString(GlobalConstants.APPLICATION_PHOTO_URI, null);
    }

    private void loadImagePicker() {
        new PickerBuilder(getActivity(), PickerBuilder.SELECT_FROM_GALLERY)
                .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                    @Override
                    public void onImageReceived(Uri imageUri) {
                        editor = sharedPreferences.edit();
                        editor.putString(GlobalConstants.APPLICATION_PHOTO_URI, imageUri.toString());
                        editor.apply();
                        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getEncodedPath());
                        Utils.roundedBitmap(getContext(), drawerProfile, bitmap);
                    }
                })
                .setImageName("Photo")
                .setImageFolderName(getActivity().getResources().getString(R.string.app_name))
                .withTimeStamp(true)
                .setCropScreenColor(Color.CYAN)
                .start();
    }

}

