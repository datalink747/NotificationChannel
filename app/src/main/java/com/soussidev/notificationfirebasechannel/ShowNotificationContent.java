package com.soussidev.notificationfirebasechannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ShowNotificationContent extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView timeStampTextView;
    private TextView articleTextView;
    private String title,message,image,time, channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification_content);

        //Function InitUI
        InitView();

        //receive data from MyFirebaseMessagingService class

        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");
        image = intent.getStringExtra("image");
        time = intent.getStringExtra("time");
        channel = intent.getStringExtra("channel");

        //Set data on UI
        titleTextView.setText(title+" Category: "+channel);
        articleTextView.setText(message);
        timeStampTextView.setText(time);
        if(!image.isEmpty()) {
            Glide.with(ShowNotificationContent.this)
                    .load(image)
                    .into(imageView);
        }
    }

    private void InitView()
    {
        imageView = (ImageView) findViewById(R.id.featureGraphics);
        titleTextView = (TextView) findViewById(R.id.header);
        timeStampTextView = (TextView) findViewById(R.id.timeStamp);
        articleTextView = (TextView) findViewById(R.id.article);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");
        image = intent.getStringExtra("image");
        time = intent.getStringExtra("time");

        titleTextView.setText("Refreshed Notification: \n"+title);
        articleTextView.setText(message);
        timeStampTextView.setText(time);
        if(!image.isEmpty()) {
            Glide.with(ShowNotificationContent.this)
                    .load(image)
                    .into(imageView);
        }

    }
}
