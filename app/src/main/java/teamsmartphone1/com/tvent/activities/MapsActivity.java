package teamsmartphone1.com.tvent.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import teamsmartphone1.com.tvent.EventList;
import teamsmartphone1.com.tvent.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private static final int SPLASH_SCREEN_REQUEST_CODE = 3;
    private GoogleMap mMap;
    private EventList events;
    private Location mLocation = null;
    private LocationManager location_manager;
    private LocationListener location_listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            events = new EventList(loc);
            try { location_manager.removeUpdates(location_listener); }
            catch (SecurityException e) { e.printStackTrace(); }
            Log.d("TVENT", "Location Changed " + loc.latitude + " " + loc.longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent splashData = getIntent();
        if (splashData != null) {
            mLocation = splashData.getParcelableExtra(SplashScreenActivity.SPLASH_LOCATION);
        }
        if (mLocation == null) {
            startActivity(new Intent(this, SplashScreenActivity.class));
            finish();
            return;
        }
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean init() {
        Log.d("TVENT", "initing");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("TVENT", "We have location permissions");
        } else {
            Log.d("TVENT", "We do not have location permissiosn");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
            return false;
        }
        location_manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location_listener);
        return true;
    }

    public void onMapClick(LatLng point) {

    }

    public boolean onMarkerClick(Marker marker) {
        return true;
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
