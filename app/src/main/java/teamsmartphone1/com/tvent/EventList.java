package teamsmartphone1.com.tvent;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;

/**
 * Created by Bruce on 9/21/2016.
 */
public class EventList {
    private static HashSet<Event> event_set;

    public EventList() {
        super();
    }

    public EventList(LatLng loc) {
        String req = String.format("http://domain/v1/data/events?latlng=%f%%2C%f", loc.latitude, loc.longitude);
        Log.d("TVENT", req);
        return;
        //HTTPRequest request = new HTTPRequest();
    }
}
