package io.predict.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import io.predict.PrecisionMode;
import io.predict.PredictIO;
import io.predict.PredictIOStatus;
import io.predict.sample.common.MainUtils;
import io.predict.sample.common.PermissionUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    private Button mActivate;
    private Button mDeactivate;
    private Button mTrackerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivate = (Button) findViewById(R.id.activate);
        mDeactivate = (Button) findViewById(R.id.deactivate);
        mActivate.setOnClickListener(this);
        mDeactivate.setOnClickListener(this);
        mTrackerStatus = (Button) findViewById(R.id.tracker_status);
        mTrackerStatus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activate:
                activatePredictIOTracker();
                break;
            case R.id.deactivate:
                PredictIO.getInstance(getApplication()).stop();
                break;
            case R.id.tracker_status:
                String message;
                switch (PredictIO.getInstance(getApplication()).getStatus()) {
                    case ACTIVE:
                        message = "'predict.io' tracker is in working state.";
                        break;
                    case LOCATION_DISABLED:
                        message = "'predict.io' tracker is not in running state. GPS is disabled.";
                        break;
                    case AIRPLANE_MODE_ENABLED:
                        message = "'predict.io' tracker is not in running state. Airplane mode is enabled.";
                        break;
                    case INSUFFICIENT_PERMISSION:
                        message = "'predict.io' tracker is not in running state. Location permission is not granted.";
                        break;
                    default:
                    case IN_ACTIVE:
                        message = "'predict.io' tracker is in in-active state.";
                        break;
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @AfterPermissionGranted(PermissionUtils.RC_PIO_PERM)
    public void activatePredictIOTracker() {
        //Get PredictIO instance
        final PredictIO predictIO = PredictIO.getInstance(getApplication());
        //Set modes
        predictIO.setPrecision(PrecisionMode.HIGH);
        predictIO.enableSearchingInPerimeter(true);

        //Validate tracker not already running
        if (predictIO.getStatus() == PredictIOStatus.ACTIVE) {
            return;
        }

        //Validate google play services
        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 1000).show();
            }
            return;
        }

        // This check is only required if your app target level is 23 or above
        // Validate required permissions granted, EasyPermissions Api Ref: https://github.com/googlesamples/easypermissions
        if (!EasyPermissions.hasPermissions(this, PredictIO.getAllRequiredDangerousPermissions())) {
            // Ask for required permissions
            // Note: current function (having AfterPermissionGranted annotation) will be called recursively when required permission will be granted
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_pio_permissions),
                    PermissionUtils.RC_PIO_PERM,
                    PredictIO.getAllRequiredDangerousPermissions()
            );
            return;
        }

        //All validations cleared, start tracker
        try {
            //noinspection ResourceType
            predictIO.start(new PredictIO.PIOActivationListener() {
                @Override
                public void onActivated() {
                    MainUtils.notify(MainActivity.this, 4, "Activated!", "predict.io activated.");
                }

                @Override
                public void onActivationFailed(int error) {
                    switch (error) {
                        default:
                            MainUtils.notify(MainActivity.this, 4, "Activation failed!"
                                    , "Something went wrong!");
                            break;
                        case 401:
                            MainUtils.notify(MainActivity.this, 4, "Activation failed!"
                                    , "Please verify your API key!");
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= 23) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }

}