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
    static boolean fakeEndPoint = false;

    public HashSet<Event> getEvents(LatLng lat) {
        if (lat == null) {
            fakeEndPoint = true;
        }
        String request = "http://mockbin.org/bin/470c5a22-c8a6-4efa-96e3-0d1240d6ee47?foo=bar&foo=baz";
        // String request = URL_ENDPOINT + "/v2/data/events?latlng=35.0136831%2c-97.3611394&count=" + EVENTCOUNT;
        if (fakeEndPoint == false) {
            String latt = lat.toString().substring(lat.toString().indexOf("(")+1,lat.toString().indexOf(","));
            String longg = lat.toString().substring(lat.toString().indexOf(",")+1,lat.toString().length()-1);
            request = URL_ENDPOINT + "/v2/data/events?latlng=" + latt + "%2c" + longg + "&count=" + EVENTCOUNT;
        }
        //String request = "http://www.mockbin.org/bin/9f202603-2e7d-4ea2-b251-be444609beed?foo=bar&foo=baz";

        HashSet<Event> events = new HashSet<Event>();
        String res = HTTPRequest.sendRequest(request);
        HashSet<String> eventlist = JsonFixerEvent(res);
        System.out.println(eventlist.size());
        for(String id : eventlist){
            Event event = getEventHeaders(id);
            events.add(event);
        }

        return events;
    }

    private Event getEventHeaders(String eventid) {
        String request = URL_ENDPOINT + "/v2/data/event/" + eventid + "?count=1";
        if (fakeEndPoint) {
            switch (eventid) {
                case "1":  request = "http://mockbin.org/bin/ef05da30-7680-4807-854a-4944b02f3eee?foo=bar&foo=baz";
                    break;
                case "2":  request = "http://mockbin.org/bin/a8f4c8c5-1bef-42f2-9905-c7202dc0d092?foo=bar&foo=baz";
                    break;
                case "3":  request = "http://mockbin.org/bin/5ae9666a-a2ab-40ae-9ad1-e1024e91eb5a?foo=bar&foo=baz";
                    break;
                case "4":  request = "http://mockbin.org/bin/f39524d3-1450-4266-907e-9a3c98911a89?foo=bar&foo=baz";
                    break;
                case "5":  request = "http://mockbin.org/bin/fde7e8fa-27a9-44b5-a467-52b82b5b22cf?foo=bar&foo=baz";
                    break;
            }
        }

        //String request = "http://www.mockbin.org/bin/1aea96b4-e063-48fb-98d6-27eebe47d715?foo=bar&foo=baz";
        String res = HTTPRequest.sendRequest(request);
        Event returnevent = null;
        res = (res.substring(0, res.lastIndexOf(",")) + "}");
        try {
            JSONObject  jsonRootObject = new JSONObject(res);
            String hashtag = jsonRootObject.getString("hashtag");
            Double lat = jsonRootObject.getDouble("lat");
            Double lng = jsonRootObject.getDouble("lng");
            LatLng ll = new LatLng(lat,lng);
            int id = Integer.valueOf(eventid);
            returnevent = new Event(id, hashtag, ll, null);
        } catch (JSONException e) {e.printStackTrace();}
        return returnevent;
        //return null;
    }

    public HashSet<Tweet> getTweets(int eventid) {
        String request = URL_ENDPOINT + "/v2/data/event/" + eventid + "?&count=" + TWEETCOUNT;
        if (fakeEndPoint) {
            switch (eventid) {
                case 1:  request = "http://mockbin.org/bin/4da4ceaa-a28e-4df6-b360-ae65c089ee02?foo=bar&foo=baz";
                    break;
                case 2:  request = "http://mockbin.org/bin/fe23951f-23e3-48f6-9e44-02efe61c2498?foo=bar&foo=baz";
                    break;
                case 3:  request = "http://mockbin.org/bin/9bf1a6bb-6ea5-4a6c-9ac8-c52fc13b6998?foo=bar&foo=baz";
                    break;
                case 4:  request = "http://mockbin.org/bin/1fe01e91-4ad4-487f-af76-1cb6e71f257b?foo=bar&foo=baz";
                    break;
                case 5:  request = "http://mockbin.org/bin/48e7d4b1-3879-40d5-b898-db582463e402?foo=bar&foo=baz";
                    break;
            }
        }
        String res = HTTPRequest.sendRequest(request);
        res = (res.substring(0, res.lastIndexOf("]")));
        res = (res.substring(0, res.lastIndexOf("}") + 1) + "]}");
        HashSet<Tweet> tweets = new HashSet<Tweet>();
        JSONObject jsonRootObject = null;
        Tweet outtweet = null;
        try {
            jsonRootObject = new JSONObject(res);
            JSONArray tweetArray = jsonRootObject.getJSONArray("tweets");
            for(int i=0; i < tweetArray.length(); i++){
                JSONObject jsono  = tweetArray.getJSONObject(i);
                String text = jsono.getString("text");
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