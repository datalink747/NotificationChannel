package com.soussidev.notificationfirebasechannel.notification.action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Soussi on 31/03/2018.
 */

public class ActionReceiverNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getStringExtra("notify");

        try {
            if(action.equals("getReplay")) {
                Log.d("getReplay", String.valueOf(getMessageText(intent)));
                Intent local = new Intent();
                local.setAction("Broadcast.to.activity.transfer");
                local.putExtra("data", getMessageText(intent));
                context.sendBroadcast(local);

            }

        } catch (Exception e) {
        e.printStackTrace();
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