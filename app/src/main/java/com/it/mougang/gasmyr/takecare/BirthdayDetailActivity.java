package com.it.mougang.gasmyr.takecare;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.mougang.gasmyr.takecare.Realm.RealmApplicationController;
import com.it.mougang.gasmyr.takecare.domain.Birthday;
import com.it.mougang.gasmyr.takecare.domain.BirthdayMessageModel;
import com.it.mougang.gasmyr.takecare.service.SpeechService;
import com.it.mougang.gasmyr.takecare.utils.BirthdayMessageModelLoader;
import com.it.mougang.gasmyr.takecare.utils.GlobalConstants;
import com.it.mougang.gasmyr.takecare.utils.Utils;

public class BirthdayDetailActivity extends AppCompatActivity {
    public static final String PATTERN = "{name}";
    private String birthday_date, birthday_next_date, birthday_fullname,
            birthday_remaingsDays, birthday_number;
    private long birthdayId = 0;
    private TextView birthday_next_dateTv,
            birthday_fullnameTv, birthday_remaingsDaysTv, birthday_remainingTV, birthday_numberTv;
    private ImageView imageView, annivImageView;
    private TextView birthday_text_messageTv;
    private FloatingActionButton sendButton, shareButton, emailButton;
    private BirthdayMessageModelLoader loader;
    private String currentBirthdayMessage;
    private Typeface typeface;
    private Button messageswitcherButton, useMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this,2);
        setContentView(R.layout.activity_birthday_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        typeface = Utils.getCampagneFont(this);
        initViews();
        initButtons();
        setupMessageModel();
        setupValues();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupMessageModel() {
        loader = new BirthdayMessageModelLoader();
    }

    private void setupValues() {
        Intent intent = getIntent();
        birthdayId = Long.parseLong(intent.getStringExtra(GlobalConstants.BIRTHDAY_ID));
        birthday_date = intent.getStringExtra(GlobalConstants.BIRTHDAY_DATE);
        birthday_next_date = intent.getStringExtra(GlobalConstants.BIRTHDAY_NEXT_DATE);
        birthday_fullname = intent.getStringExtra(GlobalConstants.BIRTHDAY_FULLNAME);
        birthday_fullnameTv.setText(birthday_fullname);
        birthday_fullnameTv.setTypeface(typeface);
        birthday_number = intent.getStringExtra(GlobalConstants.BIRTHDAY_NUMBER);
        birthday_remaingsDays = intent.getStringExtra(GlobalConstants.BIRTHDAY_REMAINING_DAYS);
        birthday_remaingsDaysTv.setText(birthday_remaingsDays + " " + getResources().getString(R.string.days_text));
        birthday_next_dateTv.setText(birthday_next_date);
        birthday_numberTv.setText(birthday_number);
        birthday_remainingTV.setText(birthday_remaingsDays + " " + getResources().getString(R.string.days_text));
        currentBirthdayMessage = loader.getData().get(Utils.getRandom(20)).getText();
        currentBirthdayMessage = currentBirthdayMessage.replace(PATTERN, birthday_fullname);
        birthday_text_messageTv.setText(currentBirthdayMessage);

    }

    private void initViews() {
        sendButton = (FloatingActionButton) findViewById(R.id.send);
        shareButton = (FloatingActionButton) findViewById(R.id.share);
        emailButton = (FloatingActionButton) findViewById(R.id.email);
        birthday_fullnameTv = (TextView) findViewById(R.id.fullName);
        birthday_fullnameTv.setTypeface(Utils.getOpenItalicFont(getApplicationContext()));
        birthday_remaingsDaysTv = (TextView) findViewById(R.id.remainingdays);
        imageView = (ImageView) findViewById(R.id.photo);
        Utils.roundedProfileImage(getApplicationContext(), imageView, Utils.getResourceID("pro"+Utils.getRandom(30),"drawable",getApplicationContext()));
        annivImageView = (ImageView) findViewById(R.id.annivImage);
        Utils.roundedProfileImage(getApplicationContext(), annivImageView, Utils.getResourceID("a"+Utils.getRandom(20),"drawable",getApplicationContext()));
        birthday_next_dateTv = (TextView) findViewById(R.id.nextbirthdate);
        birthday_numberTv = (TextView) findViewById(R.id.phonenumber);
        birthday_remainingTV = (TextView) findViewById(R.id.remainingday);
        birthday_text_messageTv = (TextView) findViewById(R.id.birthday_text_message);
        messageswitcherButton = (Button) findViewById(R.id.changeMessageButton);
        useMessageButton = (Button) findViewById(R.id.useMessageButton);
    }

    private void initButtons() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBirthdaySms();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBirthdayDate();
            }
        });

        messageswitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentBirthdayMessage = loader.getData().get(Utils.getRandom(20)).getText();
                currentBirthdayMessage = currentBirthdayMessage.replace(PATTERN, birthday_fullname);
                birthday_text_messageTv.setText(currentBirthdayMessage);
            }
        });

        useMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Birthday birthday = RealmApplicationController.
                        with(BirthdayDetailActivity.this).updateBirthDay(birthdayId,
                        new BirthdayMessageModel(currentBirthdayMessage));
                if(birthday!=null)
                    Toast.makeText(BirthdayDetailActivity.this, ""+birthday.getMessageModel().getText(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendBirthdaySms() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", birthday_number);
        smsIntent.putExtra("sms_body", currentBirthdayMessage);
        startActivity(smsIntent);
    }

    private void shareBirthdayDate() {
        String message = "#TC/".concat(birthday_fullname).concat("/").concat(birthday_date);
        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setData(Uri.parse("smsto:"));
        shareIntent.setType("vnd.android-dir/mms-sms");
        shareIntent.putExtra("sms_body", message);
        startActivity(shareIntent);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_tab_title_birtdays));
        emailIntent.putExtra(Intent.EXTRA_TEXT, currentBirthdayMessage);
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            startSpeakerService(this, "You don't have an email client in your phone");
        }
    }

    private void startSpeakerService(@NonNull Context context, String message) {
        Intent speakerServiceIntent = new Intent(context, SpeechService.class);
        speakerServiceIntent.putExtra(GlobalConstants.SPEAKER_SERVICE_MESSAGE, message);
        speakerServiceIntent.putExtra(GlobalConstants.SPEAKER_SERVICE_TARGET, false);
        context.startService(speakerServiceIntent);
    }


}
