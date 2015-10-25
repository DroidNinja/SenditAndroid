/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.binarywalllabs.sendit.managers.gcm;

import com.binarywalllabs.sendit.utils.CopyToClipboard;
import com.binarywalllabs.sendit.utils.IntentUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final String YES_ACTION = "SHARE";
    public GcmIntentService() {
        super("GcmIntentService");


    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
               // sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
               // sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                //sendNotification("Received: " + extras.getString("name") +  "\n" + extras.getString("body") + "\n" + extras.getString("type"));
                String name = extras.getString("name");
                String body = extras.getString("body");
                String type = extras.getString("type");
//                if(type.equals("image")) {
//                    generateImageNotification(name, body);
//                }
//                else if(type.equals("text"))
//                    generateMessageNotification(name,body);
//                else if(type.equals("link"))
//                    generateLinkNotification(name, body);

                Log.i(TAG, "Received: " + extras.getString("type"));
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

//    private void generateLinkNotification(String name, String body) {
//
//        int icon = R.drawable.ic_stat_gcm;
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//
//        Intent notificationIntent = new Intent(this, FetchDataActivity.class);
//        // set intent so it does not start a new activity
//        // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//        //       Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent =
//                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Notification notification = new NotificationCompat.Builder(this)
//                .setContentTitle(name)
//                .setContentText(body)
//                .setContentIntent(intent)
//                .setSmallIcon(icon)
//                .setWhen(when)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(body))
//                .addAction(R.drawable.link, "OPEN", createOpenIntent(body))
//                .addAction(android.R.drawable.ic_menu_share, "SHARE", createShareIntent(body))
//                .build();
//
//        Log.i("normla","added");
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
//
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        Date date = new Date();
//        int notification_id = (int) date.getTime();
//        notificationManager.notify(notification_id, notification);
//    }
//
//    /**
//     * Issues a notification to inform the user that server has sent a message.
//     */
//    public void generateImageNotification(String message, String image_url) {
//
//
//        // The bitmap to download
//        Bitmap message_bitmap = null;
//        // Should we download the image?
//        if ((image_url != null) && (!image_url.equals(""))) {
//            message_bitmap =imageLoader.loadImageSync(image_url);
//            // Log.i("imagenot", message_bitmap.toString());
//        }
//
//        // If we didn't get the image, we're out of here
//        if (message_bitmap == null) {
//            Log.i("bitmap","null");
//            generateMessageNotification(message, image_url);
//            return;
//        }
//
//
//        int icon = R.drawable.ic_stat_gcm;
//        long when = System.currentTimeMillis();
//
//        NotificationManager notificationManager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//        String title = this.getString(R.string.app_name);
//
//        Intent notificationIntent = new Intent(this, FetchDataActivity.class);
//        // set intent so it does not start a new activity
//        // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//        //       Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent =
//                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//
//
//
//        PendingIntent pendingIntentYes = PendingIntent.getActivity(this, 0, IntentUtils.getShareImageIntent(image_url), PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//
//        Notification notification = new NotificationCompat.Builder(this)
//                .setContentTitle(message)
//                .setContentIntent(intent)
//                .setSmallIcon(icon)
//                .setWhen(when)
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(message_bitmap))
//                .addAction(android.R.drawable.ic_menu_share, "SHARE", pendingIntentYes)
//                .build();
//
//
//
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
//
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        Date date = new Date();
//        int notification_id = (int) date.getTime();
//        notificationManager.notify(notification_id, notification);
//
//    }
//
//
//
//    public void generateMessageNotification(String message, String body) {
//        int icon = R.drawable.ic_stat_gcm;
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//
//        Intent notificationIntent = new Intent(this, FetchDataActivity.class);
//        // set intent so it does not start a new activity
//        // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//        //       Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent =
//                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Notification notification = new NotificationCompat.Builder(this)
//                .setContentTitle(message)
//                .setContentText(body)
//                .setContentIntent(intent)
//                .setSmallIcon(icon)
//                .setWhen(when)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(body))
//                .addAction(R.drawable.copy, "COPY", createCopyToClipboardIntent(body))
//                .addAction(android.R.drawable.ic_menu_share, "SHARE", createShareIntent(body))
//                .build();
//
//        Log.i("normla","added");
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
//
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        Date date = new Date();
//        int notification_id = (int) date.getTime();
//        notificationManager.notify(notification_id, notification);
//
//    }

    PendingIntent createShareIntent(String url) {
        Intent shareIntent = IntentUtils.shareText("", url);
        return PendingIntent.getActivity(this, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    PendingIntent createOpenIntent(String url) {
        Intent shareIntent = IntentUtils.openLink(url);
        return PendingIntent.getActivity(this, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    PendingIntent createCopyToClipboardIntent(String message) {
        Intent clipboardIntent = CopyToClipboard.createIntent(
                this, message, message);

        return PendingIntent.getService(this, 0, clipboardIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
    }


}
