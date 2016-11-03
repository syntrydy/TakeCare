package com.it.mougang.gasmyr.takecare;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.Realm.RealmBirthdayController;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;
import com.it.mougang.gasmyr.takecare.view.navigationdrawer.NavigationDrawerFragment;
import com.it.mougang.gasmyr.takecare.view.sayhello.SayHelloFragment;
import com.it.mougang.gasmyr.takecare.view.todo.TodosFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FragmentPagerItemAdapter pagerItemAdapter;
    private ViewPager mPager;
    private SmartTabLayout viewPagerTab;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 12;
    public static final int MY_PERMISSIONS_REQUEST_SMS = 21;
    private ProgressBar spinner;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_48dp);
        setupSharepref();
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        enableRuntimePermission();
    }

    private void initComponents() {
        setupNavigationDrawer();
        setupApplicationViewPager();
        setupPageChangeLintener();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "works well", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupApplicationViewPager() {
        pagerItemAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.app_tab_title_birtdays, MainActivityFragment.class)
                .add(R.string.app_tab_title_todos, TodosFragment.class)
                .add(R.string.app_tab_title_sayhello, SayHelloFragment.class)
                .add(R.string.app_tab_title_alarms, MainActivityFragment.class)
                .add(R.string.app_tab_title_recordings, MainActivityFragment.class)
                .create());
        mPager = (ViewPager) findViewById(R.id.myViewPager);
        mPager.setAdapter(pagerItemAdapter);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(mPager);
    }

    private void setupNavigationDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_drwr_fragment);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUpDrawer(drawerLayout, toolbar);
    }


    private void setupPageChangeLintener() {
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String title = pagerItemAdapter.getPageTitle(position).toString();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void enableRuntimePermission() {
        if (Utils.hasM() && !Utils.isContactsPermissionIsGranted(getApplicationContext())) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(getApplicationContext(), "We need your permissions to access contacts", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            spinner.setVisibility(View.VISIBLE);
            if(sharedPreferences.getBoolean(GlobalConstants.ASSISTME_IS_FISRT_LAUNCH,true)){
                copyDataToRealm(Utils.getBirthdaysFromContact(getApplicationContext()));
                editor = sharedPreferences.edit();
                editor.putBoolean(GlobalConstants.ASSISTME_IS_FISRT_LAUNCH, false);
                editor.commit();
            }
            initComponents();
            spinner.setVisibility(View.GONE);
        }

    }

    private void setupSharepref() {
        sharedPreferences = getSharedPreferences(GlobalConstants.TAKECARE_SHARE_PRFERENCE, MODE_PRIVATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    spinner.setVisibility(View.VISIBLE);
                    MyApplication.getInstance().setInfos(Utils.getTelephonyInfos(getApplicationContext()));
                    if (sharedPreferences.getBoolean(GlobalConstants.ASSISTME_IS_FISRT_LAUNCH, true)) {
                        copyDataToRealm(Utils.getBirthdaysFromContact(getApplicationContext()));
                        editor = sharedPreferences.edit();
                        editor.putBoolean(GlobalConstants.ASSISTME_IS_FISRT_LAUNCH, false);
                        editor.apply();
                    }
                    initComponents();
                    spinner.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "We cannot show birthdays.", Toast.LENGTH_LONG).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_SMS:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Canceled.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void copyDataToRealm(List<Birthday> birthdays) {
        RealmBirthdayController.with(this).copyDataToRealm(birthdays);
    }

}
