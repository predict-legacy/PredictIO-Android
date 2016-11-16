package io.predict.example.views;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import io.predict.example.R;
import io.predict.example.models.PIOEvent;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String EXTRA_EVENT = "EXTRA_EVENT";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setZoomGesturesEnabled(true);
        String eventString = getIntent().getStringExtra(EXTRA_EVENT);
        if(eventString != null) {
            PIOEvent event = new Gson().fromJson(eventString, PIOEvent.class);
            if (event != null) {
                LatLng location = new LatLng(event.latitude, event.longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(event.type.getName(event))).showInfoWindow();
                plotAccuracyCircleOnMap(event.latitude, event.longitude, (float) event.accuracy, 0x004285F4);
                setCamera(location);
            }
            if (event.zone != null) {
                TextView tvDetails = (TextView) findViewById(R.id.tv_details);
                switch (event.zone.zoneType) {
                    case HOME:
                        tvDetails.setVisibility(View.VISIBLE);
                        tvDetails.setText(R.string.event_in_home_zone);
                        break;
                    case WORK:
                        tvDetails.setVisibility(View.VISIBLE);
                        tvDetails.setText(R.string.event_in_work_zone);
                        break;
                }
            }
        }
    }

    public void setCamera(LatLng location) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(location);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
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