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
import com.google.android.gms.location.LocationServices;

import teamsmartphone1.com.tvent.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String SPLASH_LOCATION = "Splash Location";
    private static final String TAG = "SplashScreenActivity";
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 4;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private boolean locationReceived = false;
    private boolean dataReceived = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_splash_screen);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        Log.e(TAG, "onCreate");
        if (mGoogleApiClient == null) {
            Log.e(TAG, "ApiClient null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        new GetDataFromServerTasks().execute();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        if (mGoogleApiClient != null) {
            Log.e(TAG, "onStart client not null");
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        if (mGoogleApiClient != null) {
            Log.e(TAG, "onStop client not null");
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // If location availible
        Log.e(TAG, "onConnected");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "on connected permission not granted");
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_FINE_LOCATION);
        }
        Log.e(TAG, "check for location");
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            Log.e(TAG, "location not null");
            locationReceived = true;
            if (dataReceived) {
                Log.e(TAG, "data received");
                finishSplash();
            }
        } else {
            //Let's try the whole thing again
            Log.e(TAG, "location null");
            onConnected(bundle);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "request permissions result");
        if (requestCode == MY_PERMISSION_REQUEST_FINE_LOCATION
                && grantResults.length !=  0
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "permission denied");
            Toast.makeText(this, "App needs this permission to function!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "connected failed");
        Toast.makeText(this, "Unable to connect.", Toast.LENGTH_SHORT).show();
        System.exit(-1);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "connection suspended");
        if (mGoogleApiClient == null) {
            Log.e(TAG, "connection suspended client null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void finishSplash() {
        Log.e(TAG, "finish splash");
        Log.e(TAG, "location=" + mLocation);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(SPLASH_LOCATION, mLocation);
        startActivity(intent);
        finish();
    }

    /**
     * Background task to get data from the server.
     */
    public class GetDataFromServerTasks extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            //call to server
            Log.e(TAG, "doInBackground");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            Log.e(TAG, "onPostExecute");
            dataReceived = true;
            if (locationReceived) {
                Log.e(TAG, "locationReceived");
                finishSplash();
            }
        }
    }
}
