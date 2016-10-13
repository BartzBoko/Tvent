package teamsmartphone1.com.tvent;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bruce on 9/14/2016.
 */
public class Tweet {
    private LatLng geotag;
    private String hashtag;
    private String message;

    public Tweet(String inmessage) {
        message = inmessage;
    }
}
