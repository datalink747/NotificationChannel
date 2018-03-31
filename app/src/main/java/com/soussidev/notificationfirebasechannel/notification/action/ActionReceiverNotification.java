package com.soussidev.notificationfirebasechannel.notification.action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Soussi on 31/03/2018.
 */

public class ActionReceiverNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {



        String action=intent.getStringExtra("notify");


        if(action.equals("Info")){
            sendEmail(context, action);
            sendAction(context,action);
        }
        else if(action.equals("News")){
            sendEmail(context, action);
            sendAction(context,action);

        }
        else if(action.equals("All")){
            sendEmail(context, action);
            sendAction(context,action);

        }
        else if(action.equals("Sport")){
            sendEmail(context, action);
            sendAction(context,action);

        }
        else if(action.equals("Film")){
            sendEmail(context, action);
            sendAction(context,action);

        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void sendEmail(Context context, String channel){
        Toast.makeText(context,channel,Toast.LENGTH_SHORT).show();
    }


    public void sendAction(Context context,String channel){
        Log.d("ActionReceiver",channel);
    }

}