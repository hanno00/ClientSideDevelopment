package com.example.meetapp.Activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.meetapp.Data.DatabaseConnection;
import com.example.meetapp.Data.DatabaseListener;
import com.example.meetapp.Data.DirectionApiListener;
import com.example.meetapp.Data.DirectionApiManager;
import com.example.meetapp.Data.LocationTracker;
import com.example.meetapp.Data.LocationTrackerListener;
import com.example.meetapp.Models.Person;
import com.example.meetapp.R;
import com.example.meetapp.Student;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationTrackerListener, DirectionApiListener, DatabaseListener {

    private Person me;

    private GoogleMap mMap;

    private Marker userLocation;
    private ArrayList<Marker> markers = new ArrayList<>();

    private DirectionApiManager directionApiManager;
    private LocationTracker locationTracker;
    private Polyline directionsLines;

    private DatabaseConnection databaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseConnection = new DatabaseConnection(this);

        locationTracker = new LocationTracker(this, this, (LocationManager) getSystemService(Context.LOCATION_SERVICE));
        directionApiManager = new DirectionApiManager(this, this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                markers.add(mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Klik")));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (me != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            me.setLatitude(location.getLatitude());
            me.setLongitude(location.getLongitude());

            databaseConnection.updatePerson(me);

//        directionApiManager.generateDirections(new LatLng(51, 4),latLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void routeLineAvailable(PolylineOptions polylineOptions) {
        if(directionsLines != null) {
            directionsLines.remove();
        }
        directionsLines = mMap.addPolyline(polylineOptions);
    }

    @Override
    public void onResponseError(Error error) {
        Toast.makeText(this, "Could not connect with Directions Api", Toast.LENGTH_SHORT);
    }


    @Override
    public void onDatabasePersonChanged() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("DATA",0);
        me = databaseConnection.getPersonByUUID(pref.getString("PERSON",""));
        drawMarkers(databaseConnection.getPersonsByLobbyUUID(me.getlobbyUUID()));
    }

    @Override
    public void onDatabaseLobbyChanged() {

    }

    private void drawMarkers(ArrayList<Person> persons){
        for (Marker m : markers) { m.remove(); }
        markers.clear();

        for (Person p : persons) {
            if (p.getUUID().equals(me.getUUID())) {
                markers.add(mMap.addMarker(new MarkerOptions()
                    .position(p.getCoordinates())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user2))
                    .title("Me")));
            } else {
                markers.add(mMap.addMarker(new MarkerOptions()
                        .position(p.getCoordinates())
                        .title(p.getName())));
            }
        }
    }
}
