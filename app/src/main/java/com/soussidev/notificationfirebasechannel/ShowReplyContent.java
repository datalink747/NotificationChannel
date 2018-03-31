package com.soussidev.notificationfirebasechannel;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.soussidev.notificationfirebasechannel.notification.NotificationID;

import java.net.URISyntaxException;

public class ShowReplyContent extends AppCompatActivity {
    private TextView getReply;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reply_content);

        getReply =(TextView)findViewById(R.id.notify_replay);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) {


            Intent intent=getIntent();
            getReply.setText(intent.getStringExtra("channel"));

            //This is used to close the notification tray
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            ShowReplyContent.this.sendBroadcast(it);
        }
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if(remoteInput != null){
            return remoteInput.getCharSequence("key_text_reply");
        }
        return null;
    }
}
