package teamsmartphone1.com.tvent;

import android.util.Log;

/**
 * Object to retrieve movie information
 */
class HTTPRequest {

    /**
     * Request String
     */
    //private final String req;
    /**
     * Response String
     */
    //private String res;

    /**
     * Constructor to set request
     * @param reqs Request string
     */
    /*public HTTPRequest(String reqs) {
        this.req = reqs;

    }*/

    /**
     * Send a Event info request
     */
    public static String sendRequest(String req) {
        final HTTPRetriever retriever = new HTTPRetriever(req);
        final Thread thread = new Thread(retriever);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.d("HTTPRequest", "Thread failed to join");
        }
        return retriever.getResponse();
    }

    /**
     * Get JSON response
     * @return String form of JSON response
     */
//    public String getResponse() { return res; }

}
