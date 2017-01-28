package com.it.mougang.gasmyr.takecare;

import android.Manifest;
import android.app.AlarmManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.Realm.RealmApplicationController;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.domain.SayHello;
import com.it.mougang.gasmyr.takecare.domain.SmsForLife;
import com.it.mougang.gasmyr.takecare.utils.AlarmManagerUtils;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;
import com.it.mougang.gasmyr.takecare.utils.datepicker.LoveSmsLoader;
import com.it.mougang.gasmyr.takecare.view.loves.LovesFragment;
import com.it.mougang.gasmyr.takecare.view.navigationdrawer.NavigationDrawerFragment;
import com.it.mougang.gasmyr.takecare.view.sayhello.SayHelloFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_CODE = 7951;
    private Toolbar toolbar;
    private FragmentPagerItemAdapter pagerItemAdapter;
    private ViewPager mPager;
    private SmartTabLayout viewPagerTab;
    private ProgressBar spinner;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FloatingActionButton floatingActionButton;
    private AlarmManager alarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this,2);
        setContentView(R.layout.activity_main);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_48dp);
        setupSharepref();
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.INVISIBLE);
        requestAllPermissions();
    }


    private void requestAllPermissions() {
        if (!Utils.hasM()) {
            spinner.setVisibility(View.VISIBLE);
            if (sharedPreferences.getBoolean(GlobalConstants.APPLICATION_IS_FISRT_LAUNCH, true)) {
                setupUseffulConfig();
            }
            initComponents();
            spinner.setVisibility(View.GONE);
        } else {
            spinner.setVisibility(View.VISIBLE);
            if (Utils.checkAndRequestPermissions(getApplicationContext(), MY_PERMISSIONS_REQUEST_CODE, this)) {
                setupUseffulConfig();
            }
            initComponents();
            spinner.setVisibility(View.GONE);
        }
    }


    private void initComponents() {
        setupNavigationDrawer();
        setupApplicationViewPager();
        setupPageChangeLintener();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(MainActivity.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUseffulConfig() {
        copyBirthdayDataToRealm(Utils.getBirthdaysFromContact(getApplicationContext()));
        copySayHelloDataToRealm(Utils.getSayHelloList(getApplicationContext()));
        copySayLovesDataToRealm(new LoveSmsLoader().getData());
        startBirthdayNotificationAlarm();
        startGreetingNotificationAlarm();
        editor = sharedPreferences.edit();
        editor.putBoolean(GlobalConstants.APPLICATION_IS_FISRT_LAUNCH, false);
        editor.putBoolean(GlobalConstants.APPLICATION_HAS_SPEAKER_FEATURE, true);
        editor.putString(GlobalConstants.APPLICATION_INSTALLATION_DATE, Utils.getFormatter().format(new Date()));
        editor.commit();
    }


    private void setupSharepref() {
        sharedPreferences = getSharedPreferences(GlobalConstants.APPLICATION_SHAREPRFERENCE, MODE_PRIVATE);
    }

    private void setupApplicationViewPager() {
        pagerItemAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.app_tab_title_birtdays, MainActivityFragment.class)
                //.add(R.string.app_tab_title_sayhello, SayHelloFragment.class)
                .add(R.string.app_tab_title_loves, LovesFragment.class)
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
                Fragment currentFragment = pagerItemAdapter.getItem(position);
                if (currentFragment instanceof MainActivityFragment) {
                    floatingActionButton.setVisibility(View.INVISIBLE);
                } else if (currentFragment instanceof SayHelloFragment) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageResource(R.drawable.ic_settings_applications_white_24dp);
                } else if (currentFragment instanceof LovesFragment) {
                    floatingActionButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    spinner.setVisibility(View.VISIBLE);
                    MyApplication.getInstance().setInfos(Utils.getTelephonyInfos(getApplicationContext()));
                    if (sharedPreferences.getBoolean(GlobalConstants.APPLICATION_IS_FISRT_LAUNCH, true)) {
                        setupUseffulConfig();
                    }
                    initComponents();
                    spinner.setVisibility(View.GONE);
                } else {
                    if (!Utils.isContactsPermissionIsGranted(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "We cannot show birthdays.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_notificator) {
            Intent intent = new Intent(getApplicationContext(), TakeCareStuffActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void copyBirthdayDataToRealm(List<Birthday> birthdays) {
        RealmApplicationController.with(this).copyDataToRealm(birthdays);
    }

    private void copySayHelloDataToRealm(List<SayHello> hellos) {
        RealmApplicationController.with(this).copyHellosToRealm(hellos);
    }

    private void copySayLovesDataToRealm(List<SmsForLife> data) {
        try {
            RealmApplicationController.with(this).copyLoveMessageToRealm(data);
        } catch (Exception e) {
            Utils.makeLog("error loading loves messages inside database");
        }
    }

    private void startBirthdayNotificationAlarm() {
        AlarmManagerUtils.scheduleBirthdayAlarm(getApplicationContext(), alarm);
    }

    private void startGreetingNotificationAlarm() {
        AlarmManagerUtils.GreetingAlarm(getApplicationContext(), alarm);
    }

}
