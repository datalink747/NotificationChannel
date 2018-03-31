package com.soussidev.notificationfirebasechannel.notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.soussidev.notificationfirebasechannel.MainActivity;
import com.soussidev.notificationfirebasechannel.R;
import com.soussidev.notificationfirebasechannel.ShowNotificationContent;
import com.soussidev.notificationfirebasechannel.ShowReplyContent;
import com.soussidev.notificationfirebasechannel.notification.action.ActionReceiverNotification;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Soussi on 30/03/2018.
 */

public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent, String channel) {
        showNotificationMessage(title, message, timeStamp, intent, null, channel);
    }

    /**
     * @auteur Soussi Mohamed
     * @param channel
     * @param imageUrl
     * @param intent
     * @param message
     * @param timeStamp
     * @param title
     *
     */

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl, String channel) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        // notification icon
        final int icon = R.mipmap.icon_notification;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/alarm");


        // if imageUrl is not null
       // if (!TextUtils.isEmpty(imageUrl)) {

            if (!TextUtils.isEmpty(imageUrl) && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);
                //if Bitmap != null
                if (bitmap != null) {
                    // Start Big Notification in Android Oreo, Using Channel
                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       showBigNotification_Channel(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound, channel);

                    // playNotificationSound();
                    }
                    else {
                        showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//                       // playNotificationSound();
                    }
                }
                else {
                    //Start Small Notification in Android Oreo, Using Channel
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        showSmallNotification_channel(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound, channel);
                    //  playNotificationSound();
                    }
                    else {
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//                       // playNotificationSound();
                        }
                }
            }
  //      }
        else {
            // Start Simple Notification without Image
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                               showSmallNotification_channel(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound, channel);
            //                   playNotificationSound();
                            }
                            else {
                         showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//                     playNotificationSound();
                                }
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(NotificationID.getID(), notification);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setContentText(message)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                // .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setColor(mContext.getResources().getColor(R.color.colorAccent))
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(NotificationID.getID(), notification);
    }
    /**
     * @auteur Soussi Mohamed
     * @param channel
     * @param alarmSound
     * @param bitmap
     * @param icon
     * @param resultPendingIntent
     * @param message
     * @param timeStamp
     * @param title
     *
     */

    /*
    * Big Notification Channel in Android Oreo or + */


    private void showBigNotification_Channel(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp,
                                             PendingIntent resultPendingIntent, Uri alarmSound,String channel) {



        //This is the intent of PendingIntent
        Intent intentAction = new Intent(mContext,ActionReceiverNotification.class);
        //This is optional if you have more than one buttons and want to differentiate between two
        intentAction.putExtra("notify","getReplay");
        PendingIntent pIntentAction = PendingIntent.getBroadcast(mContext,1,intentAction,PendingIntent.FLAG_UPDATE_CURRENT);

        // Add Replay Action to Notification

        String KEY_TEXT_REPLY = "key_text_reply";

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel("Replay")
                .build();
//------------------------------------------------------------------------------
        // methode 1
        Intent intent = new Intent(mContext,ShowReplyContent.class);
        TaskStackBuilder taskStack = TaskStackBuilder.create(mContext);
        taskStack.addParentStack(ShowReplyContent.class);
        taskStack.addNextIntent(intent);

        PendingIntent pendingIntent = taskStack.getPendingIntent(100,PendingIntent.FLAG_UPDATE_CURRENT);
//------------------------------------------------------------------------------
        // methode 2
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("channel", message);
        PendingIntent pendingIntent_activity = PendingIntent.getActivity(mContext,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//------------------------------------------------------------------------------
        //Notification Action with RemoteInput instance added.
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.sym_action_chat, "REPLY", pIntentAction)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mContext, get_Channel_ID(channel))
                        .setSmallIcon(icon)
                        .setTicker(title)
                        .setWhen(0)
                        .addAction(R.mipmap.icon_send,"Show Channel",pendingIntent_activity)
                        .addAction(replyAction)
                        .setOngoing(true)
                        .setChannelId(get_Channel_ID(channel))
                        .setContentInfo(channel)
                        .setBadgeIconType(icon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setWhen(getTimeMilliSec(timeStamp))
                        .setStyle(bigPictureStyle)
                        .setColor(mContext.getResources().getColor(R.color.colorAccent))
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentIntent(resultPendingIntent);


        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channelNotification = new NotificationChannel(channelId,
//                    channel,
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channelNotification);
//        }

        // hide the notification after its selected
        notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(NotificationID.getID() /* ID of notification */, notificationBuilder.build());

    }

    /*
    * Small Notification Channel in Android Oreo or + */


    private void showSmallNotification_channel(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, String channel) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);
        String channelId  = mContext.getString(R.string.default_notification_channel_id);
        String channelName = mContext.getString(R.string.default_notification_channel_name);

        //This is the intent of PendingIntent
        Intent intentAction = new Intent(mContext,ActionReceiverNotification.class);
        //This is optional if you have more than one buttons and want to differentiate between two
        intentAction.putExtra("notify","getReplay");
        PendingIntent pIntentAction = PendingIntent.getBroadcast(mContext,NotificationID.getID(),intentAction,PendingIntent.FLAG_ONE_SHOT);

        // Add Replay Action to Notification

         String KEY_TEXT_REPLY = "key_text_reply";

          RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel("Replay")
                .build();

        //Notification Action with RemoteInput instance added.
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.sym_action_chat, "REPLY", pIntentAction)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();




        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mContext, get_Channel_ID(channel))
                        .setSmallIcon(icon)
                        .setTicker(title)
                        .addAction(replyAction)
                        .setOngoing(true)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setSound(alarmSound)
                        .setStyle(inboxStyle)
                        .setWhen(getTimeMilliSec(timeStamp));
        // .setSmallIcon(R.mipmap.ic_launcher)
        // .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon));
        // .setStyle(new NotificationCompat.BigTextStyle().bigText(message));



        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelNotification = new NotificationChannel(get_Channel_ID(channel),
                    channel,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channelNotification);
        }

        notificationManager.notify(NotificationID.getID() /* ID of notification */, notificationBuilder.build());
    }


    /* Downloading push notification image before displaying it in
    * the notification tray
    */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/alarm");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

//Func for select ID of Channel
    public String get_Channel_ID(String id) {
        switch (id) {
            case "Info": return mContext.getResources().getString(R.string.notification_channel_id_info);//"com.soussidev.notificationfirebasechannel.info"
            case "News": return mContext.getResources().getString(R.string.notification_channel_id_news);
            case "All": return mContext.getResources().getString(R.string.default_notification_channel_id);
            case "Sport": return mContext.getResources().getString(R.string.notification_channel_id_sport);
            case "Film": return mContext.getResources().getString(R.string.notification_channel_id_film);
            default: throw new IllegalArgumentException();
        }
    }


}
