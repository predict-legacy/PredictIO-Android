package predictio.testapp

import android.app.Application
import predictio.sdk.PredictIo

/**
 * Created by shahrozali on 12/29/17.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Init SDK
        PredictIo.init(this)
    }
}