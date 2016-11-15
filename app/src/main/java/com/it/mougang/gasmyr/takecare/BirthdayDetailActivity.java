package com.it.mougang.gasmyr.takecare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.utils.BirthdayMessageModelLoader;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

public class BirthdayDetailActivity extends AppCompatActivity {
    private String birthday_date, birthday_next_date, birthday_fullname,
            birthday_remaingsDays, birthday_number;
    private long birthdayId = 0;
    private TextView birthday_dateTv, birthday_next_dateTv,
            birthday_fullnameTv, birthday_remaingsDaysTv, birthday_remainingTV, birthday_numberTv;
    private ImageView imageView, annivImageView;
    private TextView birthday_text_messageTv;
    private FloatingActionButton sendButton, shareButton;
    private BirthdayMessageModelLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        initButtons();
        setupMessageModel();
        setupValues();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupMessageModel() {
        loader=new BirthdayMessageModelLoader();
    }

    private void setupValues() {
        Intent intent = getIntent();
        birthdayId = Long.parseLong(intent.getStringExtra(GlobalConstants.BIRTHDAY_ID));
        birthday_date = intent.getStringExtra(GlobalConstants.BIRTHDAY_DATE);
        birthday_next_date = intent.getStringExtra(GlobalConstants.BIRTHDAY_NEXT_DATE);
        birthday_fullname = intent.getStringExtra(GlobalConstants.BIRTHDAY_FULLNAME);
        birthday_fullnameTv.setText(birthday_fullname);
        birthday_number = intent.getStringExtra(GlobalConstants.BIRTHDAY_NUMBER);
        birthday_remaingsDays = intent.getStringExtra(GlobalConstants.BIRTHDAY_REMAINING_DAYS);
        birthday_remaingsDaysTv.setText(birthday_remaingsDays + " " + getResources().getString(R.string.days_text));
        birthday_dateTv.setText(birthday_date);
        birthday_next_dateTv.setText(birthday_next_date);
        birthday_numberTv.setText(birthday_number);
        birthday_remainingTV.setText(birthday_remaingsDays);
        birthday_text_messageTv.setText(loader.getData().get(Utils.getRandom(20)).getText());

    }

    private void initViews() {
        sendButton = (FloatingActionButton) findViewById(R.id.send);
        shareButton = (FloatingActionButton) findViewById(R.id.share);
        birthday_fullnameTv = (TextView) findViewById(R.id.fullName);
        birthday_fullnameTv.setTypeface(Utils.getOpenItalicFont(getApplicationContext()));
        birthday_remaingsDaysTv = (TextView) findViewById(R.id.remainingdays);
        imageView = (ImageView) findViewById(R.id.photo);
        Utils.roundedProfileImage(getApplicationContext(), imageView, R.drawable.profile01);
        annivImageView = (ImageView) findViewById(R.id.annivImage);
        Utils.roundedProfileImage(getApplicationContext(), annivImageView, R.drawable.a13);
        birthday_dateTv = (TextView) findViewById(R.id.birthdate);
        birthday_next_dateTv = (TextView) findViewById(R.id.nextbirthdate);
        birthday_numberTv = (TextView) findViewById(R.id.phonenumber);
        birthday_remainingTV = (TextView) findViewById(R.id.remainingday);
        birthday_text_messageTv = (TextView) findViewById(R.id.birthday_text_message);
    }

    private void initButtons() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BirthdayDetailActivity.this, " send button clicked ", Toast.LENGTH_SHORT).show();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BirthdayDetailActivity.this, " share button clicked ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
