package com.example.meetapp.Data;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class LocationTracker extends Service {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private LocationTrackerListener listener;
    private final Context context;

    public LocationTracker(Context context, LocationTrackerListener listener, LocationManager locationManager) {
        this.context = context;
        this.listener = listener;
        this.locationManager = locationManager;
        setLocationListener();
        trackLocation();
    }

    private void setLocationListener() {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                listener.onLocationChanged(location);
            }
            //region override methods
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("Debug", "Provider enabled");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("Debug", "Provider disabled");
            }
            //endregion
        };
    }

    private void trackLocation() {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, locationListener);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
