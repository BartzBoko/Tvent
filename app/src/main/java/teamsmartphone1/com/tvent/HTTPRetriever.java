package teamsmartphone1.com.tvent;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Object to retrieve and organize movie information
 */
class HTTPRetriever implements Runnable {
    /**
     * Request String
     */
    private final String req;
    /**
     * Response String
     */
    private String res;

    /**
     * Constructor to set request string
     * @param r Request string
     */
    public HTTPRetriever(String r) { req = r; }

    @Override
    public void run() {
        try {
            Log.d("HTTPRetriever", "Requesting HTTP");
            final URL url = new URL(req);
            final InputStream iss = url.openStream();
            final StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = iss.read()) != -1) {
                sb.append((char) ch);
            }
            res = sb.toString();
        } catch (IOException e) {
            Log.d("HTTPRetriever", e.toString());
        }
    }

    /**
     * Get JSON response string
     * @return JSON response string
     */
    public String getResponse() { return res; }
}
