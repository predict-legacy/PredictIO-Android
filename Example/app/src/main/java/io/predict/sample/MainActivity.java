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

import io.predict.PredictIO;
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
                if (PredictIO.getInstance(this).isTrackerRunning()) {
                    Toast.makeText(this, "PredictIO tracker is in running state."
                            , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "PredictIO tracker is not in running state."
                            , Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @AfterPermissionGranted(PermissionUtils.RC_PIO_PERM)
    public void activatePredictIOTracker() {
        //Get PredictIO instance
        final PredictIO predictIO = PredictIO.getInstance(getApplication());
        //Set modes
        predictIO.enableFullPrecision(true);
        predictIO.enableSearchingInPerimeter(true);

        //Validate tracker not already running
        if (predictIO.isTrackerRunning()) {
            return;
        }

        //Validate google play services
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
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
            predictIO.start();
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