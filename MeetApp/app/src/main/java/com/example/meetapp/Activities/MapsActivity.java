package com.example.meetapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.meetapp.Data.DatabaseConnection;
import com.example.meetapp.Data.DatabaseListener;
import com.example.meetapp.Data.DirectionApiListener;
import com.example.meetapp.Data.DirectionApiManager;
import com.example.meetapp.Data.LocationTracker;
import com.example.meetapp.Data.LocationTrackerListener;
import com.example.meetapp.Models.Person;
import com.example.meetapp.Models.Waypoint;
import com.example.meetapp.R;
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

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationTrackerListener, DirectionApiListener, DatabaseListener, GoogleMap.OnMarkerClickListener {

    private Person me;
    private GoogleMap mMap;

    private ArrayList<Marker> markers = new ArrayList<>();
    private Marker waypointMarker;
    private Waypoint waypoint;

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
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (directionsLines != null)
                    directionsLines.remove();
                addWaypoint(latLng);
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

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void routeLineAvailable(PolylineOptions polylineOptions) {
        if (directionsLines != null) {
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
        SharedPreferences pref = getApplicationContext().getSharedPreferences("DATA", 0);
        me = databaseConnection.getPersonByUUID(pref.getString("PERSON", ""));
        drawMarkers(databaseConnection.getPersonsByLobbyUUID(me.getlobbyUUID()));
    }

    @Override
    public void onDatabaseLobbyChanged() {

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.getTag() == null)
            return false;

        if (marker.getTag().equals("Waypoint")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to navigate to this waypoint?")
                    .setTitle("Title")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            directionApiManager.generateDirections(me.getCoordinates(),marker.getPosition());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

            builder.create().show();
        }
        return false;
    }

    public void addWaypoint(LatLng latLng) {
        waypoint = new Waypoint("Waypoint", latLng.latitude, latLng.longitude, me.getlobbyUUID());
        databaseConnection.insertWaypoint(waypoint);
    }

    private void drawMarkers(ArrayList<Person> persons) {
        for (Marker m : markers) {
            m.remove();
        }
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

    @Override
    public void onDatabaseWaypointChanged() {
        if (me == null)
            return;
        waypoint = databaseConnection.getWaypointByUUID(me.getlobbyUUID());
        if (waypoint != null) {
            if (waypointMarker != null)
                waypointMarker.remove();
            waypointMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(waypoint.getLatitude(), waypoint.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(getString(R.string.Waypoint)));
            waypointMarker.setTag("Waypoint");
        }
    }
}
