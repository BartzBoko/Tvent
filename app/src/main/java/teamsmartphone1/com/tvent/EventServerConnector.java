package teamsmartphone1.com.tvent;

import android.util.Log;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;

public class EventServerConnector {
    String URL_ENDPOINT = "http://domain";
    int EVENTCOUNT = 5;
    int TWEETCOUNT = 5;
    boolean ISSERVERCONNECTED = true;

    public HashSet<Event> getEvents(LatLng lat) {
        // String request = URL_ENDPOINT + "/v2/data/events?latlng=" + latlng + "&count=" + EVENTCOUNT;
        String request = "http://www.mockbin.org/bin/f8965994-347e-4117-9845-c4d02ed44b42?foo=bar&foo=baz";

        HashSet<Event> events = new HashSet<>();
        String res = HTTPRequest.sendRequest(request);
        //Log.d("ESC", res);;
        try {
            JSONObject  jsonRootObject = new JSONObject(res);
            JSONArray jsonArray = jsonRootObject.optJSONArray("events");
            for(int i=0; i < jsonArray.length(); i++){
                int id  = jsonArray.getInt(i);
                //Log.d("ESC", Integer.toString(id));
                Event event = getEventHeaders(id);
                events.add(event);
            }
        } catch (JSONException e) {e.printStackTrace();}
        return events;
    }

    private Event getEventHeaders(int eventid) {
        // String request = URL_ENDPOINT + "/v2/data/event/" + eventid + ?&count=0";;
        String request = "http://www.mockbin.org/bin/8454d0a9-fd40-40d0-ad1d-3598c999f2b6?foo=bar&foo=baz";
        String res = HTTPRequest.sendRequest(request);
        //Log.d("ESC", res);
        Event returnevent = null;
        try {
            JSONObject  jsonRootObject = new JSONObject(res);
            String hashtag = jsonRootObject.getString("hashtag");
            Double lat = jsonRootObject.getDouble("lat");
            Double lng = jsonRootObject.getDouble("lng");
            LatLng ll = new LatLng(lat,lng);
            returnevent = new Event(eventid, hashtag, ll, null);
            //Log.d("ESC", hashtag);
        } catch (JSONException e) {e.printStackTrace();}
        return returnevent;
    }

    public HashSet<Tweet> getTweets(int eventid) {
        //String request = URL_ENDPOINT + "/v2/data/event/" + eventid + "?&count=" + TWEETCOUNT;
        String request = "http://www.mockbin.org/bin/429823d6-0af8-4ccd-91e7-795966eb81ef?foo=bar&foo=baz";
        String res = HTTPRequest.sendRequest(request);
        //Log.d("ESC", res);
        HashSet<Tweet> tweets = new HashSet<Tweet>();
        JSONObject jsonRootObject = null;
        Tweet outtweet = null;
        try {
            jsonRootObject = new JSONObject(res);
            JSONArray tweetArray = jsonRootObject.getJSONArray("tweets");
            for(int i=0; i < tweetArray.length(); i++){
                JSONObject jsono  = tweetArray.getJSONObject(i);
                String text = jsono.getString("text");
                Log.d("ESC", text);
                outtweet = new Tweet(text);
                tweets.add(outtweet);
            }
        } catch (JSONException e) {e.printStackTrace();}
        return tweets;
    }
}