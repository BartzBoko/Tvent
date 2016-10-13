package teamsmartphone1.com.tvent;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Bruce on 9/21/2016.
 *
 * @author Bruce
 * @version 1.0
 */
public class EventList {
    private static final String TAG = "EventList";
    private static HashSet<Event> event_set;
    private static boolean refresh;

    public EventList() {
        super();
    }

    /**
     * EventList constructor for one LatLng
     * @param loc Location to add to eventlist
     */
    public EventList(LatLng loc) {
        String req = String.format(Locale.US, "http://domain/v1/data/events?latlng=%f%%2C%f",
                loc.latitude, loc.longitude);
        Log.d(TAG, "req=" + req);
        String res = HTTPRequest.sendRequest(req);
        Log.d(TAG, "res=" + res);
        if (res == null) {
            return;
        }
        JSONObject response = null;
        try {
            response = new JSONObject(res);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to get JSON object");
        }
        assert response != null;
        //now get the array of "movies"data
        JSONArray events = response.optJSONArray("events");
        for (int i = 0; i < events.length(); i++) {
            try {
                JSONObject event = events.getJSONObject(i);
                assert event != null;
                int id= event.optInt("id");
                HashSet<Tweet> tweet_set = new HashSet<Tweet>();
                JSONArray tweets = event.optJSONArray("tweets");
                for (int j = 0; j < tweets.length(); j++) {
                    tweet_set.add((Tweet)tweets.opt(j));
                }
                event_set.add(new Event(id, "", loc, tweet_set));
            } catch (JSONException e) {
                Log.e(TAG, "Failed to get JSON object");
            }
        }
        refresh = false;
    }

    public void need_refresh() { refresh = true; }

    public boolean get_refresh() { return refresh; }

    public void setEvents(HashSet<Event> events) { event_set = events; }

    public HashSet<Event> getEvents() {
        return event_set;
    }
}
