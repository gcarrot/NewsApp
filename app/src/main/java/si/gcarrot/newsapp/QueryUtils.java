package si.gcarrot.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Urban on 7/5/17.
 */

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            Thread.sleep(2000);
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<News> news = extractFeatureFromJson(jsonResponse);

        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the API JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String JSONData) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JSONData)) {
            return null;
        }

        List<News> news = new ArrayList<>();
        try {

            JSONObject baseJsonResponse = new JSONObject(JSONData);

            //Log.i(LOG_TAG, "JSON Rsponse: " + baseJsonResponse.toString());

            if (baseJsonResponse.has("response")) {

                JSONObject response = baseJsonResponse.getJSONObject("response");
                JSONArray newsArray = response.getJSONArray("results");

                for (int i = 0; i < newsArray.length(); i++) {

                    JSONObject current = newsArray.getJSONObject(i);
                    String webTitle = "";

                    if (current.has("webTitle")) {
                        webTitle = current.getString("webTitle");
                    }

                    String webUrl = current.getString("webUrl");
                    String sectionName = "";
                    String trailText = "";
                    String thumbnailUrl = "";

                    if (current.has("sectionName")) {
                        sectionName = current.getString("sectionName");
                    }

                    if (current.has("fields")) {
                        JSONObject fields = current.getJSONObject("fields");
                        if (fields.has("trailText")) {
                            trailText = fields.getString("trailText");
                        }
                        if (fields.has("thumbnail")) {
                            thumbnailUrl = fields.getString("thumbnail");
                        }
                    }

                    News currentNews = new News(webTitle, sectionName, trailText, webUrl, thumbnailUrl);
                    news.add(currentNews);
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the API JSON results", e);
        }

        // Return the list
        return news;
    }
}
