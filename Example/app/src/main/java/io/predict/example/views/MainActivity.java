package io.predict.example.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import io.predict.PredictIO;
import io.predict.PredictIOStatus;
import io.predict.example.BuildConfig;
import io.predict.example.R;
import io.predict.example.common.MainUtils;
import io.predict.example.common.PermissionUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements
        EasyPermissions.PermissionCallbacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Menu mMenu;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ((TextView) findViewById(R.id.tv_version_name) ).setText("SDK v" + PredictIO.SDK_VERSION);
        ((TextView) findViewById(R.id.tv_version_code) ).setText("Build v" + BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        boolean isTrackerRunning = PredictIO.getInstance(this).getStatus() == PredictIOStatus.ACTIVE;
        updateActionBar(!isTrackerRunning, isTrackerRunning);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start:
                activatePredictIOTracker();
                break;
            case R.id.action_stop:
                PredictIO.getInstance(getApplication()).stop();
                updateActionBar(true, false);
                break;
            case R.id.action_status:
                checkPredictIOStatus();
                break;
            case R.id.action_visiting_zone:
                Intent intent = new Intent(MainActivity.this, VisitingZonesActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateActionBar(boolean showStart, boolean showStop) {
        if (mMenu != null) {
            mMenu.findItem(R.id.action_start).setVisible(showStart);
            mMenu.findItem(R.id.action_stop).setVisible(showStop);
        }
    }

    public void checkPredictIOStatus() {
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
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                default:
                case 0:
                    return PIOEventsFragment.newInstance(true);
                case 1:
                    return PIOEventsFragment.newInstance(false);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Listener events";
                case 1:
                    return "Broadcast events";
            }
            return null;
        }
    }

    @AfterPermissionGranted(PermissionUtils.RC_PIO_PERM)
    public void activatePredictIOTracker() {
        //Get PredictIO instance
        final PredictIO predictIO = PredictIO.getInstance(getApplication());
        //Set modes
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
                    MainActivity.this.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                updateActionBar(false, true);
                                Toast.makeText(MainActivity.this, "predict.io activated!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    );
                }

                @Override
                public void onActivationFailed(int error) {
                    MainActivity.this.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                updateActionBar(false, true);
                            }
                        }
                    );
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
        //Persist FCM request token
        MainUtils.persistFCMToken(this, FirebaseInstanceId.getInstance().getToken());
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
