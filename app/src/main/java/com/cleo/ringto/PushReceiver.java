package com.cleo.ringto;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class PushReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //-----------------------------
        // Create a test notification
        //
        // (Use deprecated notification
        // API for demonstration purposes,
        // to avoid having to import
        // the Android Support Library)
        //-----------------------------

        String notificationTitle = "Pushy";
        String notificationDesc = "Test notification";
        String message = intent.getStringExtra("message");
        message = message.replaceAll("'", "\\\\'");
        message = message.replaceAll("\"", "\\\\\"");

        try
        {
            JSONObject json_message = new JSONObject(message);
            Log.d("Output", json_message.toString());
        }

        catch(JSONException e)
        {
            Log.e("Message", e.toString());
        }
        //-----------------------------
        // Create a test notification
        //-----------------------------

        Notification notification = new Notification(android.R.drawable.ic_dialog_info, notificationDesc, System.currentTimeMillis());

        //-----------------------------
        // Sound + vibrate + light
        //-----------------------------

        notification.defaults = Notification.DEFAULT_ALL;

        //-----------------------------
        // Dismisses when pressed
        //-----------------------------

        notification.flags = Notification.FLAG_AUTO_CANCEL;

        //-----------------------------
        // Create pending intent
        // without a real intent
        //-----------------------------

        notification.setLatestEventInfo(context, notificationTitle, notificationDesc, null );

        //-----------------------------
        // Get notification manager
        //-----------------------------

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //-----------------------------
        // Show the notification
        //-----------------------------

        mNotificationManager.notify(0, notification);
    }
}