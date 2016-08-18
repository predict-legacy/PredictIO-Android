package io.predict.sample;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import io.predict.PIOTripSegment;
import io.predict.PredictIO;
import io.predict.PredictIOListener;
import io.predict.sample.common.MainUtils;

public class PredictIOManager {

    private Application mApplication;

    public PredictIOManager(Application application) {
        mApplication = application;
    }

    public void onApplicationCreate() {
        PredictIO predictIO = PredictIO.getInstance(mApplication);
        // This notifies sdk that app is initialised, this is important for the performance of the detection.
        predictIO.setAppOnCreate(mApplication);
        // set this to get event callbacks
        predictIO.setListener(mPredictIOListener);
    }

    public PredictIOListener getPredictIOListener() {
        return mPredictIOListener;
    }

    private final PredictIOListener mPredictIOListener = new PredictIOListener() {

        @Override
        public void departed(PIOTripSegment tripSegment) {
            String mess = "User has departed from his location.";
            MainUtils.notify(mApplication, 2, "Departed", mess);
        }

        @Override
        public void arrived(PIOTripSegment tripSegment) {
            try {
                String message = "User has arrived at destination.";
                MainUtils.notify(mApplication, 1, "Arrived", message);
            } catch (Exception e) {
                Log.e("Error", "Error = " + e.getMessage());
            }
        }

        @Override
        public void arrivalSuspected(PIOTripSegment tripSegment) {
            try {
                String message = "predict.io suspected, user has arrived at destination";
                MainUtils.notify(mApplication, 6, "Arrival suspected", message);
            } catch (Exception e) {
                Log.e("Error", "Error = " + e.getMessage());
            }
        }

        @Override
        public void searchingInPerimeter(Location location) {
            MainUtils.notify(mApplication, 3, "Looking for parking space"
                    , "predict.io has detected that you are looking for parking space");
        }

        @Override
        public void departureCanceled() {
            MainUtils.notify(mApplication, 5, "Departure canceled"
                    , "predict.io has cancelled a last departed location");
        }

        @Override
        public void didUpdateLocation(Location location) {
        }

        @Override
        public void transportationMode(PIOTripSegment tripSegment) {
            try {
                String message = "Transportation mode: " + tripSegment.transportationMode.toString();
                MainUtils.notify(mApplication, 7, "Transportation mode predicted", message);
            } catch (Exception e) {
                Log.e("Error", "Error = " + e.getMessage());
            }
        }
    };

}
