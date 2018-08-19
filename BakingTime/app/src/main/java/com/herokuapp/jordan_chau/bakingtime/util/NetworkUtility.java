package com.herokuapp.jordan_chau.bakingtime.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * <h1>Network Utility</h1>
 * This class handles all necessary HTTP Connections in order to retrieve movie information from the API
 * It also provides helper methods to build the URL
 *
 * @author Jordan Chau
 * @since 2018-06-15
 */
public class NetworkUtility {

    //private static final String TAG = NetworkUtility.class.getSimpleName();
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * This method builds a URL with the specified api parameter
     * and returns the URL
     * @return URL - Returns a URL with the specified api parameter
     */
    public static URL buildURL() {

        Uri uri = Uri.parse(BASE_URL).buildUpon().build();

        URL builtURL = null;
        try {
            builtURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("NetworkUtility: ", "Built URI = " + builtURL);

        return builtURL;
    }

    /**
     * This method creates a GET request with the specified url, sends it through a HTTP URL Connection
     * and returns a String that represents a response containing information about all movies
     * @param url - Url parameter to create the request with
     * @return String - Returns a String that represents all movie info
     */
    public static String getHttpUrlResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Boolean checkInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showErrorMessage(View v) {
        //Toast.makeText(getActivity(),"Please check your internet connection and try again.", Toast.LENGTH_LONG).show();
        Snackbar snackbar = Snackbar.make(v, "Please check your internet connection and try again.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
