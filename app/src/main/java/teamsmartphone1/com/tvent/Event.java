package teamsmartphone1.com.tvent;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;

/**
 * Created by Bruce on 9/14/2016.
 */
public class Event {
    private int id;
    private LatLng geotag;
    private HashSet<Tweet> tweets;

    public Event(int i, LatLng g, HashSet<Tweet> t) {
        id = i;
        geotag = g;
        tweets = t;
    }

    public int getId() { return id; }

    public LatLng getGeotag() { return geotag; }


}
