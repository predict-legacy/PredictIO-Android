package predictio.testapp;

import predictio.sdk.PredictIOError;
import predictio.sdk.PredictIo;
import predictio.sdk.protocols.PredictIoCallback;

/**
 * Created by shahrozali on 12/14/17.
 */

public class SampleJavaExample {

    public void start() {
        PredictIo.Companion.start(new PredictIoCallback() {
            @Override
            public void error(PredictIOError predictIOError) {
                switch (predictIOError) {
                    case invalidKey:
                        // Your API key is invalid (incorrect or deactivated)
                    case killSwitch:
                        // Kill switch has been enabled to stop the SDK
                    case wifiDisabled:
                        // Wifi is disabled
                    case locationPermission:
                        // Location permission is not granted
                    default:
                        // SDK started without any error
                }
            }
        });
    }

}
