package io.predict.example.views;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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
            if(event != null) {
                LatLng location = new LatLng(event.latitude, event.longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(event.type.getName()));
                setCamera(location);
            }
        }
    }

    public void setCamera(LatLng location) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(location);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }
}