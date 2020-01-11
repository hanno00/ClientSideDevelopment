package com.example.meetapp.Data;

import com.google.android.gms.maps.model.PolylineOptions;

public interface DirectionApiListener {
    void routeLineAvailable(PolylineOptions polylineOptions);

    void onResponseError(Error error);
}
