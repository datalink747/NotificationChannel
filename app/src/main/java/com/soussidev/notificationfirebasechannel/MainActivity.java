package com.soussidev.notificationfirebasechannel;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.soussidev.notificationfirebasechannel.notification.Config;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver broadcastReceiver;
    private TextView getReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction("Broadcast.to.activity.transfer");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                if (intent != null)
                {
                  getReply.setText(intent.getStringExtra("data"));
                    //This is used to close the notification tray
                    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    context.sendBroadcast(it);
                }

                   // Toast.makeText(MainActivity.this, intent.getStringExtra("data").toString(), Toast.LENGTH_LONG).show();
            }
        };
        registerReceiver(broadcastReceiver, filter);

        getReply =(TextView)findViewById(R.id.notify_replay);



        // configure Notification Firebase

        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.d(TAG, String.valueOf(Build.VERSION.SDK_INT));

            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);

            String channelId_info  = getString(R.string.notification_channel_id_info);
            String channelName_info = getString(R.string.notification_channel_name_info);

            String channelId_news  = getString(R.string.notification_channel_id_news);
            String channelName_news = getString(R.string.notification_channel_name_news);

            String channelId_sport  = getString(R.string.notification_channel_id_sport);
            String channelName_sport = getString(R.string.notification_channel_name_sport);

            String channelId_film  = getString(R.string.notification_channel_id_film);
            String channelName_film = getString(R.string.notification_channel_name_film);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            // create android channel default
            NotificationChannel all = new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT);
            all.enableLights(true);
            all.setLightColor(Color.GREEN);
            all.enableVibration(true);

            // create android channel news
            NotificationChannel news = new NotificationChannel(channelId_news,
                    channelName_news, NotificationManager.IMPORTANCE_HIGH);
            news.enableLights(true);
            news.setLightColor(Color.BLUE);
            news.enableVibration(true);

            // create android channel sport
            NotificationChannel sport = new NotificationChannel(channelId_sport,
                    channelName_sport, NotificationManager.IMPORTANCE_HIGH);
            news.enableLights(true);
            news.setLightColor(Color.MAGENTA);
            news.enableVibration(true);

            // set channel to notificationManager
            notificationManager.createNotificationChannel(all);
            notificationManager.createNotificationChannel(news);
            notificationManager.createNotificationChannel(sport);

            notificationManager.createNotificationChannel(new NotificationChannel(channelId_info,
                    channelName_info, NotificationManager.IMPORTANCE_LOW));

          //  notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(channelId_info,channelName_info));
          //  notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(channelId,channelName));
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
        {
            final ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

            ForegroundColorSpan colorSpan_1 = new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_dark, getTheme()));
            String label_linkedin ="Soussidev";
            SpannableStringBuilder colouredLabel_linkedin = new SpannableStringBuilder(label_linkedin);
            colouredLabel_linkedin.setSpan(colorSpan_1, 0, label_linkedin.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            ShortcutInfo linkedin = new ShortcutInfo.Builder(this, "LinkedIn")
                    .setShortLabel(colouredLabel_linkedin)
                    //.setLongLabel("Soussi Mohamed")
                    .setIcon(Icon.createWithResource(this, R.mipmap.linkedin))
                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.web_linkedin))))
                    .setRank(0)
                    .build();

            ForegroundColorSpan colorSpan_2 = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent, getTheme()));
            String label_twitter ="Soussidev";
            SpannableStringBuilder colouredLabel_twitter = new SpannableStringBuilder(label_twitter);
            colouredLabel_twitter.setSpan(colorSpan_2, 0, label_twitter.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            ShortcutInfo twitter = new ShortcutInfo.Builder(this, "Twitter")
                    .setShortLabel(colouredLabel_twitter)
                   // .setLongLabel("Soussi Mohamed")
                    .setIcon(Icon.createWithResource(this, R.mipmap.twitter))
                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.web_twitter))))
                    .setRank(1)
                    .build();


            if (shortcutManager != null) {
                shortcutManager.setDynamicShortcuts(Arrays.asList(linkedin,twitter));
            }
        }




    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);




    }

}
