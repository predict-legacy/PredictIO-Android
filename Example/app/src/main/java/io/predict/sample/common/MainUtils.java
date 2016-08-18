package io.predict.sample.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

public class MainUtils {

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

}
