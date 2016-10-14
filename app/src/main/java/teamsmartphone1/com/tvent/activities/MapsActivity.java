package teamsmartphone1.com.tvent.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.HashSet;

import teamsmartphone1.com.tvent.Event;
import teamsmartphone1.com.tvent.EventList;
import teamsmartphone1.com.tvent.R;
import teamsmartphone1.com.tvent.Tweet;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener
        /*LocationListener*/ {
    private static final String TAG = "MapsActivity";
    private static final int SPLASH_SCREEN_REQUEST_CODE = 3;
    private Location mLocation = null;
    private boolean visitedSplashScreen = false;
    private GoogleMap mMap;
    private EventList events;
    private LocationManager location_manager;
    private LocationRequest mLocationRequest;
    private LatLng mCurrentLocation;
    /*private LocationListener location_listener = new LocationListener() {
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

    };*/

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
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private boolean init() {
        events = new EventList();
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("TVENT", "We have location permissions");
        } else {
            Log.d("TVENT", "We do not have location permissiosn");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
            return false;
        }
        location_manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location_listener);
        */
        return true;
    }

    @Override
    public void onMapClick(LatLng point) {
        //There's nothing to do here, right?
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Take this fancy marker, and then get the tag.
        //Hopefully this works:
        Object event = marker.getTag();
        if (event == null || event.getClass() != Event.class) {
            return false;
        }
        
        Intent intent = new Intent(this, TweetsActivity.class);
        Event realEvent = (Event) event;
        HashSet<Tweet> set = realEvent.getTweets();
        intent.putExtra("Tweets", set);
        startActivity(intent);
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

        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Add a marker in Sydney and move the camera

        for (Event e : events.getEvents()) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(e.getGeotag()).title(e.getHashtag()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            marker.setTag(e);
            marker.setTitle("PirateParty");
            marker.showInfoWindow();
        }

        LatLng userPos = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(userPos).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userPos));
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    /*@Override
    public void onLocationChanged(Location location) {
        if (events != null && !events.get_refresh()) return;
        Log.d(TAG, "Location Updated " + location);
        Log.d(TAG, "location lat=" + location.getLatitude() + " long=" + location.getLongitude());
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        events = new EventList(loc);
        if (events.getEvents() == null) {
            return;
        }
        for (Event e : events.getEvents()) {
            mMap.addMarker(new MarkerOptions().position(e.getGeotag()));
        }
    }*/
}
