package teamsmartphone1.com.tvent.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Permission;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import teamsmartphone1.com.tvent.Event;
import teamsmartphone1.com.tvent.EventList;
import teamsmartphone1.com.tvent.EventServerConnector;
import teamsmartphone1.com.tvent.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String SPLASH_LOCATION = "Splash Location";
    private static final String TAG = "SplashScreenActivity";
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 4;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private boolean locationReceived = false;
    private boolean dataReceived = false;
    private LocationRequest mLocationRequest;
    private EventList events;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_splash_screen);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        Log.d(TAG, "onCreate");
        if (mGoogleApiClient == null) {
            Log.d(TAG, "ApiClient null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        new GetDataFromServerTasks().execute();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setInterval(1000);

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        if (mGoogleApiClient != null) {
            Log.d(TAG, "onStart client not null");
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        if (mGoogleApiClient != null) {
            Log.d(TAG, "onStop client not null");
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // If location availible
        Log.d(TAG, "onConnected");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "on connected permission not granted");
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_FINE_LOCATION);
        }
        Log.d(TAG, "check for location");
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            Log.d(TAG, "location not null");
            locationReceived = true;
            if (dataReceived) {
                Log.d(TAG, "data received");
                finishSplash();
            }
        } else {
            //Let's try the whole thing again
            Log.d(TAG, "location is null, querying for location");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "request permissions result");
        if (requestCode == MY_PERMISSION_REQUEST_FINE_LOCATION
                && grantResults.length !=  0
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Log.d(TAG, "permission denied");
            Toast.makeText(this, "App needs this permission to function!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "connected failed");
        Toast.makeText(this, "Unable to connect.", Toast.LENGTH_SHORT).show();
        System.exit(-1);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "connection suspended");
        if (mGoogleApiClient == null) {
            Log.d(TAG, "connection suspended client null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void finishSplash() {
        Log.d(TAG, "finish splash");
        Log.d(TAG, "location=" + mLocation);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(SPLASH_LOCATION, mLocation);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        /*if (events != null && !events.get_refresh()) return;
        Log.d(TAG, "Location Updated " + location);
        Log.d(TAG, "location lat=" + location.getLatitude() + " long=" + location.getLongitude());
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        events = new EventList(loc);
        if (events.getEvents() == null) {
            return;
        }*/
    }

    /**
     * Background task to get data from the server.
     */
    public class GetDataFromServerTasks extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            //call to server
            Log.d(TAG, "doInBackground");
            events = new EventList();
            EventServerConnector connector = new EventServerConnector();
            events.setEvents(connector.getEvents(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            Log.d(TAG, "onPostExecute");
            dataReceived = true;
            if (locationReceived) {
                Log.d(TAG, "locationReceived");
                finishSplash();
            }
        }
    }
}
