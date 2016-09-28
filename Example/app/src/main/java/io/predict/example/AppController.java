package io.predict.example;

import android.app.Application;

public class AppController extends Application {

    private PredictIOManager mPredictIOManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //PredictIO SDK code
        mPredictIOManager = new PredictIOManager(this);
        mPredictIOManager.onApplicationCreate();
    }
}