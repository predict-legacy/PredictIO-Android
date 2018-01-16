package predictio.testapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import predictio.sdk.PredictIOError
import predictio.sdk.PredictIo
import predictio.sdk.protocols.PredictIoCallback
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set custom Parameters
        PredictIo.setCustomParameter(mapOf(Pair("Test", "SDK")))

        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            startPredictIoService()

        } else {
            EasyPermissions.requestPermissions(this, "App needs access to these permissions",
                    421, Manifest.permission.ACCESS_FINE_LOCATION)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            421 -> {
                if (resultCode == RESULT_OK) {
                    startPredictIoService()
                } else {
                    Toast.makeText(this, "Please turn on your location to continue", Toast.LENGTH_LONG).show();
                    finish()
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun startPredictIoService() {
        //Start SDK
        PredictIo.Companion.start(object : PredictIoCallback {
            override fun error(error: PredictIOError?) {
                when (error) {
                    PredictIOError.invalidKey -> {
                        // Your API key is invalid (incorrect or deactivated)
                    }
                    PredictIOError.killSwitch -> {
                        // Kill switch has been enabled to stop the SDK
                    }
                    PredictIOError.wifiDisabled -> {
                        // Wifi is disabled
                    }
                    PredictIOError.locationPermission -> {
                        // Location permission is not granted
                    }
                    else -> {
                        // SDK started without any error
                    }
                }
            }
        })

    }
}