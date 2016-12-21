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
        intentFilter.addAction(PredictIO.CANCELED_DEPARTURE_EVENT);
        intentFilter.addAction(PredictIO.SUSPECTED_ARRIVAL_EVENT);
        intentFilter.addAction(PredictIO.ARRIVED_EVENT);
        intentFilter.addAction(PredictIO.SEARCHING_PARKING_EVENT);
        intentFilter.addAction(PredictIO.DETECTED_TRANSPORTATION_MODE_EVENT);
        intentFilter.addAction(PredictIO.BEING_STATIONARY_AFTER_ARRIVAL_EVENT);
        intentFilter.addAction(PredictIO.TRAVELED_BY_AIRPLANE_EVENT);
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
        public void suspectedArrival(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.SUSPECTED_ARRIVAL_EVENT, tripSegment, null);
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
        public void canceledDeparture(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.CANCELED_DEPARTURE_EVENT, tripSegment, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void didUpdateLocation(Location location) {
        }

        @Override
        public void detectedTransportationMode(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.DETECTED_TRANSPORTATION_MODE_EVENT, tripSegment, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void beingStationaryAfterArrival(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.BEING_STATIONARY_AFTER_ARRIVAL_EVENT, tripSegment, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void traveledByAirplane(PIOTripSegment tripSegment) {
            try {
                onReceiveEvent(true, PredictIO.TRAVELED_BY_AIRPLANE_EVENT, tripSegment, null);
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
                    tripSegment.transportationMode,
                    tripSegment.stationaryAfterArrival,
                    tripSegment.departureZone
                ));
                break;
            case PredictIO.CANCELED_DEPARTURE_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE,
                    System.currentTimeMillis(),
                    PIOEventType.DEPARTURE_CANCELED,
                    tripSegment.transportationMode
                ));
                break;
            case PredictIO.SUSPECTED_ARRIVAL_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.arrivalLocation.getLatitude(),
                    tripSegment.arrivalLocation.getLongitude(),
                    tripSegment.arrivalLocation.getAccuracy(),
                    tripSegment.arrivalTime.getTime(),
                    PIOEventType.ARRIVAL_SUSPECTED,
                    tripSegment.transportationMode,
                    tripSegment.stationaryAfterArrival,
                    tripSegment.arrivalZone
                ));
                break;
            case PredictIO.ARRIVED_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.arrivalLocation.getLatitude(),
                    tripSegment.arrivalLocation.getLongitude(),
                    tripSegment.arrivalLocation.getAccuracy(),
                    tripSegment.arrivalTime.getTime(),
                    PIOEventType.ARRIVED,
                    tripSegment.transportationMode,
                    tripSegment.stationaryAfterArrival,
                    tripSegment.arrivalZone
                ));
                break;
            case PredictIO.SEARCHING_PARKING_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    searchingLocation.getLatitude(),
                    searchingLocation.getLongitude(),
                    searchingLocation.getAccuracy(),
                    System.currentTimeMillis(),
                    PIOEventType.SEARCHING,
                    null,
                    false
                ));
                break;
            case PredictIO.DETECTED_TRANSPORTATION_MODE_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.departureLocation.getLatitude(),
                    tripSegment.departureLocation.getLongitude(),
                    tripSegment.departureLocation.getAccuracy(),
                    System.currentTimeMillis(),
                    PIOEventType.TRANSPORT_MODE,
                    tripSegment.transportationMode,
                    tripSegment.stationaryAfterArrival,
                    tripSegment.departureZone
                ));
                break;
            case PredictIO.BEING_STATIONARY_AFTER_ARRIVAL_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.arrivalLocation.getLatitude(),
                    tripSegment.arrivalLocation.getLongitude(),
                    tripSegment.arrivalLocation.getAccuracy(),
                    System.currentTimeMillis(),
                    PIOEventType.BEING_STATIONARY_AFTER_ARRIVAL,
                    tripSegment.transportationMode,
                    tripSegment.stationaryAfterArrival,
                    tripSegment.arrivalZone
                ));
                break;
            case PredictIO.TRAVELED_BY_AIRPLANE_EVENT:
                MainUtils.persistEvent(mApplication, isListenerEvent, new PIOEvent (
                    tripSegment.arrivalLocation.getLatitude(),
                    tripSegment.arrivalLocation.getLongitude(),
                    tripSegment.arrivalLocation.getAccuracy(),
                    tripSegment.arrivalTime.getTime(),
                    PIOEventType.TRAVELED_BY_AIRPLANE,
                    tripSegment.transportationMode
                ));
                break;
        }
    }
}