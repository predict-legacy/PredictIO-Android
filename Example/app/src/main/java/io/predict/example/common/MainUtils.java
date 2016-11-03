package io.predict.example.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;

import io.predict.PredictIO;
import io.predict.example.models.PIOEvent;

public class MainUtils {
    public static final String TAG = "PredictIO";
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM hh:mm a");

    public static void notify(Context context, int id, String title, String detail) {
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setTicker(detail)
                .setContentText(detail)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources()
                        , android.R.drawable.ic_dialog_alert))
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public static void persistEvent(Context context, boolean isListenerEvent, PIOEvent event) {
        if(isListenerEvent) {
            PIOPreferences.getInstance(context).persistListenerEvent(event);
        } else {
            PIOPreferences.getInstance(context).persistBroadcastEvent(event);
        }
        //Send local broadcast to aware app about event update
        Intent intent = new Intent(PIOBroadcastUtils.ACTION_EVENT_UPDATE);
        intent.putExtra(PIOBroadcastUtils.EXTRA_EVENT_UPDATE_IS_LISTENER, isListenerEvent);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void persistFCMToken(Context context, String requestToken) {
        PredictIO.getInstance(context).setCustomParameter("device_token", requestToken);
        PredictIO.getInstance(context).setWebhookURL("https://api.parktag.mobi/demo/notifications/send_notification");
    }
}