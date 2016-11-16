package io.predict.example.views;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import io.predict.PIOZone;
import io.predict.PredictIO;
import io.predict.example.R;

public class VisitingZonesActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private ArrayList<Marker> mMarketsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                try {
                    showHomeWorkZones();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showHomeWorkZones() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        PIOZone homeZone = PredictIO.getInstance(this).getHomeZone();
        PIOZone workZone = PredictIO.getInstance(this).getWorkZone();
        boolean isSomeZoneDetected = false;
        mMarketsList.clear();
        if(homeZone != null && homeZone.center!= null) {
            mMarketsList.add(mMap.addMarker(
                    new MarkerOptions()
                            .position(homeZone.center)
                            .icon(BitmapDescriptorFactory.defaultMarker(27))
                            .title(getString(R.string.home_zone))
            ));
            builder.include(homeZone.center);
            plotAccuracyCircleOnMap(homeZone.center.latitude, homeZone.center.longitude, homeZone.radius, 0x004285F4);
            isSomeZoneDetected = true;
        }
        if(workZone != null && workZone.center!= null) {
            mMarketsList.add(mMap.addMarker(
                    new MarkerOptions()
                            .position(workZone.center)
                            .icon(BitmapDescriptorFactory.defaultMarker(265))
                            .title(getString(R.string.work_zone))
            ));
            builder.include(workZone.center);
            plotAccuracyCircleOnMap(workZone.center.latitude, workZone.center.longitude, workZone.radius, 0x004285F4);
            isSomeZoneDetected = true;
        }
        if(!isSomeZoneDetected) {
            Toast.makeText(this, R.string.no_zone_detected_message, Toast.LENGTH_LONG).show();
            return;
        }

        //Animate camera
        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32,
                this.getResources().getDisplayMetrics());
        CameraUpdate camera = CameraUpdateFactory.newLatLngBounds(builder.build(), pad);
        mMap.animateCamera(camera, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                if (mMarketsList.size() > 0) {
                    mMarketsList.get(0).showInfoWindow();
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void plotAccuracyCircleOnMap(double latitude, double longitude, float radius, int color) {
        LatLng latLng = new LatLng(latitude, longitude);
        // adding circle to map
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(0xff000000 | color);
        circleOptions.strokeWidth(2.0f);
        circleOptions.fillColor(0x55000000 | color);
        mMap.addCircle(circleOptions);
    }
}