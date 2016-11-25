package com.it.mougang.gasmyr.takecare.view.navigationdrawer;


import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.MyApplication;
import com.it.mougang.gasmyr.takecare.R;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

import static android.app.Activity.RESULT_OK;


public class NavigationDrawerFragment extends Fragment {
    private final static int PICK_IMAGE_REQUEST_CODE = 245;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ImageView drawerProfile;


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
        drawerProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                loadImagePicker();
                return true;
            }
        });
        Utils.roundedProfileImage(getContext(), drawerProfile, R.drawable.profile05);
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

    private void loadImagePicker() {
        Log.d("BIRTHDAY","start");
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType("image/*");
        imagePickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (Utils.hasM()) {
            Intent intent = Intent.createChooser(imagePickerIntent, "Select a picture");
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
        } else {
            startActivityForResult(imagePickerIntent, PICK_IMAGE_REQUEST_CODE);
        }
        Log.d("BIRTHDAY","end");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("BIRTHDAY","hdhdhdhd");
        if(resultCode== PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Uri uri=data.getData();

            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                Log.d("BIRTHDAY",String.valueOf(bitmap));
                Utils.roundedBitmap(getContext(),drawerProfile,bitmap);
                drawerProfile.setImageBitmap(bitmap);

            }catch (Exception e){

            }

        }
    }
}

