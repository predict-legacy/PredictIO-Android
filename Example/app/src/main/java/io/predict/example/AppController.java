package io.predict.example;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class AppController extends Application {

    private PredictIOManager mPredictIOManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        //PredictIO SDK code
        mPredictIOManager = new PredictIOManager(this);
        mPredictIOManager.onApplicationCreate();
    }
}