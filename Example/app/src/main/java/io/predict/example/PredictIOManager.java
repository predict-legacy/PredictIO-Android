package io.predict.example;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

import io.predict.PIOTripSegment;
import io.predict.PredictIO;
import io.predict.PredictIOListener;
import io.predict.example.common.MainUtils;
import io.predict.example.models.PIOEvent;
import io.predict.example.models.PIOEventType;

public class PredictIOManager {

    private Application mApplication;

    public PredictIOManager(Application application) {
        mApplication = application;
    }

    public void onApplicationCreate() {
        PredictIO predictIO = PredictIO.getInstance(mApplication);
        // this notifies sdk that app is initialised, this is important for the performance of the detection.
        predictIO.setAppOnCreate(mApplication);
        // set this to get event callbacks in listener
        predictIO.setListener(mPredictIOListener);
        // set this to get event callbacks in broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PredictIO.DEPARTED_EVENT);
        intentFilter.addAction(PredictIO.DEPARTURE_CANCELED_EVENT);
        intentFilter.addAction(PredictIO.ARRIVAL_SUSPECTED_EVENT);
        intentFilter.addAction(PredictIO.ARRIVED_EVENT);
        intentFilter.addAction(PredictIO.SEARCHING_PARKING_EVENT);
        intentFilter.addAction(PredictIO.TRANSPORTATION_MODE_EVENT);
        LocalBroadcastManager.getInstance(mApplication).registerReceiver(mPredictIOEventsReceiver, intentFilter);
    }

    public PredictIOListener getPredictIOListener() {
        return mPredictIOListener;
    }

    /**
     ****************************************
     ********* Events Via Listener **********
     ****************************************
     */
    private final PredictIOListener mPredictIOListener = new PredictIOListener() {
        @Override
        public void departed(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.DEPARTED_EVENT, tripSegment, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void arrived(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.ARRIVED_EVENT, tripSegment, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void arrivalSuspected(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.ARRIVAL_SUSPECTED_EVENT, tripSegment, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void searchingInPerimeter(Location location) {
            try {
                onReceiveEvent(true, PredictIO.SEARCHING_PARKING_EVENT, null, location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void departureCanceled(PIOTripSegment pioTripSegment) {
            try {
                onReceiveEvent(true, PredictIO.DEPARTURE_CANCELED_EVENT, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void didUpdateLocation(Location location) {
        }

        @Override
        public void transportationMode(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.TRANSPORTATION_MODE_EVENT, tripSegment, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     ****************************************
     ***** Events Via Broadcast Receiver ****
     ****************************************
     */
    private final BroadcastReceiver mPredictIOEventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            PIOTripSegment tripSegment = intent.getParcelableExtra("PIOTripSegment");
            Location searchingLocation = intent.getParcelableExtra("Location");
            try {
                onReceiveEvent(false, intent.getAction(), tripSegment, searchingLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     ****************************************
     ****** Receive predict.io Events *******
     ****************************************
     */

    public void onReceiveEvent(boolean isListenerEvent, String event,
                               PIOTripSegment tripSegment, Location searchingLocation) {
        switch (event) {
            case PredictIO.DEPARTED_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.departureLocation.getLatitude(),
                    tripSegment.departureLocation.getLongitude(),
                    tripSegment.departureLocation.getAccuracy(),
                    tripSegment.departureTime.getTime(),
                    PIOEventType.DEPARTED,
                    null
                ));
                break;
            case PredictIO.DEPARTURE_CANCELED_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE,
                    System.currentTimeMillis(),
                    PIOEventType.DEPARTURE_CANCELED,
                    null
                ));
                break;
            case PredictIO.ARRIVAL_SUSPECTED_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.arrivalLocation.getLatitude(),
                    tripSegment.arrivalLocation.getLongitude(),
                    tripSegment.arrivalLocation.getAccuracy(),
                    tripSegment.arrivalTime.getTime(),
                    PIOEventType.ARRIVAL_SUSPECTED,
                    null
                ));
                break;
            case PredictIO.ARRIVED_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.arrivalLocation.getLatitude(),
                    tripSegment.arrivalLocation.getLongitude(),
                    tripSegment.arrivalLocation.getAccuracy(),
                    tripSegment.arrivalTime.getTime(),
                    PIOEventType.ARRIVED,
                    null
                ));
                break;
            case PredictIO.SEARCHING_PARKING_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    searchingLocation.getLatitude(),
                    searchingLocation.getLongitude(),
                    searchingLocation.getAccuracy(),
                    System.currentTimeMillis(),
                    PIOEventType.SEARCHING,
                    null
                ));
                break;
            case PredictIO.TRANSPORTATION_MODE_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.departureLocation.getLatitude(),
                    tripSegment.departureLocation.getLongitude(),
                    tripSegment.departureLocation.getAccuracy(),
                    System.currentTimeMillis(),
                    PIOEventType.TRANSPORT_MODE,
                    tripSegment.transportationMode
                ));
                break;
        }
    }
}