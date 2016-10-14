package teamsmartphone1.com.tvent;

import android.util.Log;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;

public class EventServerConnector {
    String URL_ENDPOINT = "http://54.191.15.168";
    int EVENTCOUNT = 5;
    int TWEETCOUNT = 5;
    final int GETEVENTS = 1;
    final int GETHEADERS = 2;
    final int GETTWEETS = 3;

    public HashSet<Event> getEvents(LatLng lat) {

        String request = URL_ENDPOINT + "/v2/data/events?latlng=35.0136831%2c-97.3611394&count=" + EVENTCOUNT;
        if (lat != null) {
            String latt = lat.toString().substring(lat.toString().indexOf("(")+1,lat.toString().indexOf(","));
            String longg = lat.toString().substring(lat.toString().indexOf(",")+1,lat.toString().length()-1);
            request = URL_ENDPOINT + "/v2/data/events?latlng=" + latt + "%2c" + longg + "&count=" + EVENTCOUNT;
        }
        //String request = "http://www.mockbin.org/bin/9f202603-2e7d-4ea2-b251-be444609beed?foo=bar&foo=baz";

        HashSet<Event> events = new HashSet<Event>();
        String res = HTTPRequest.sendRequest(request);
        // Log.d("ESC", res);
        HashSet<String> eventlist = JsonFixerEvent(res);
        System.out.println(eventlist.size());
        for(String id : eventlist){
            System.out.println("dan => " + id);
            Event event = getEventHeaders(id);
            events.add(event);
        }

        return events;
    }

    private Event getEventHeaders(String eventid) {
        String request = URL_ENDPOINT + "/v2/data/event/" + eventid + "?count=1";
        //String request = "http://www.mockbin.org/bin/1aea96b4-e063-48fb-98d6-27eebe47d715?foo=bar&foo=baz";
        String res = HTTPRequest.sendRequest(request);
        Log.d("ESC", res);
        Event returnevent = null;
        res = (res.substring(0, res.lastIndexOf(",")) + "}");
        Log.d("ESC", res);
        try {
            JSONObject  jsonRootObject = new JSONObject(res);
            String hashtag = jsonRootObject.getString("hashtag");
            Double lat = jsonRootObject.getDouble("lat");
            Double lng = jsonRootObject.getDouble("lng");
            LatLng ll = new LatLng(lat,lng);
            int id = Integer.valueOf(eventid);
            returnevent = new Event(id, hashtag, ll, null);
            Log.d("ESC", hashtag);
        } catch (JSONException e) {e.printStackTrace();}
        return returnevent;
    }

    public HashSet<Tweet> getTweets(int eventid) {
        String request = URL_ENDPOINT + "/v2/data/event/" + eventid + "?&count=" + TWEETCOUNT;
        //String request = "http://www.mockbin.org/bin/8f6f6b76-a253-49e2-ae09-5d063452d88d?foo=bar&foo=baz";
        String res = HTTPRequest.sendRequest(request);
        // Log.d("ESCDDDDDDDDD", res);
        res = (res.substring(0, res.lastIndexOf("]")));
        res = (res.substring(0, res.lastIndexOf("}") + 1) + "]}");
        // Log.d("ESCDDDDDDDDD", res);
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

    private HashSet<String> JsonFixerEvent(String poorJSON) {
        HashSet<String> output = new HashSet<String>();
        String returnstring = poorJSON.substring((poorJSON.indexOf( '[' ) + 1) ,(poorJSON.indexOf( ']' )));
        returnstring = returnstring.replace(" ", "");
        int count = returnstring.length() - returnstring.replace(",", "").length();
        for (int x = 0; x < count; x++) {
            output.add(returnstring.substring(0,returnstring.indexOf( ',' )) );
            returnstring = returnstring.substring((returnstring.indexOf( ',') + 1),returnstring.length());
        }
        if (!returnstring.equals("")) {
            output.add(returnstring);
        }
        return output;
    }


}