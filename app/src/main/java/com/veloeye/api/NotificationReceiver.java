package com.veloeye.api;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;
import com.veloeye.R;
import com.veloeye.activity.BikesActivity;
import com.veloeye.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Florin on 08.01.2016.
 */
public class NotificationReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        try {
            JSONObject jsonObject = new JSONObject(extras.getString("com.parse.Data"));

            Intent resultIntent;
            String notificationMessage;
            if (jsonObject.has("email")) {
                resultIntent = new Intent(context, BikesActivity.class);
                resultIntent.putExtras(extras);
                notificationMessage = context.getString(R.string.stolen_bike_scanned);
            } else {
                resultIntent = new Intent(context, MainActivity.class);
                notificationMessage = jsonObject.optString("alert", "");
            }

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(notificationMessage)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(1, mBuilder.build());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
