package io.predict.sample;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import io.predict.PredictIO;
import io.predict.PredictIOInterface;
import io.predict.TransportMode;

public class AppController extends Application implements PredictIOInterface {

    @Override
    public void onCreate() {
        super.onCreate();
        //PredictIO SDK code
        PredictIO predictIO = PredictIO.getInstance(getApplicationContext());
        // This notifies sdk that app is initialised, this is important for the performance of the detection.
        predictIO.setAppOnCreate(this);
        // set this to get event callbacks
        predictIO.setListener(this);
    }

    @Override
    public void departedLocation(Location location, long departureTime, TransportMode transportMode) {
        String mess = "User has departed from his location";
        notify(getApplicationContext(), 1, "Departed", mess);
    }

    @Override
    public void searchingInPerimeter(Location location) {
        notify(getApplicationContext(), 2, "Looking for parking"
                , "PredictIO has detected that you are looking for parking space");
    }

    @Override
    public void arrivalSuspectedFromLocation(Location location, long departureTime, long arrivalTime
            , TransportMode transportMode) {
        String message = "Suspected User has arrived at destination";
        notify(getApplicationContext(), 3, "Arrived suspected ", message);
    }

    @Override
    public void arrivedAtLocation(Location location, long departureTime, long arrivalTime
            , TransportMode transportMode) {
        try {
            Context context = getApplicationContext();
            String message = "User has arrived at destination";
            notify(context, 4, "Arrived", message);
        } catch (Exception e) {
            Log.e("Error", "Error = " + e.getMessage());
        }
    }

    @Override
    public void departureCanceled() {
        notify(getApplicationContext(), 5, "Departure cancelled"
                , "Sorry for inconvenience, your last departure detection has been cancelled");
    }

    @Override
    public void didUpdateLocation(Location location) {

    }

    @Override
    public void activationFailed(int errorCode) {
        if (errorCode == 401) {
            notify(getApplicationContext(), 5, "Activation Failed", "API authentication failed");
        } else {
            notify(getApplicationContext(), 5, "Activation Failed", "Activation failed");
        }
    }

    private void notify(Context context, int id, String title, String detail) {
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