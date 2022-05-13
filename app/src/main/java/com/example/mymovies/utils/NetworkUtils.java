package com.example.mymovies.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String VIDEOS_BASE_URL = "https://api.themoviedb.org/3/discover/movie/%s/videos";
    private static final String REVIEWS_BASE_URL = "https://api.themoviedb.org/3/discover/movie/%s/reviews";


    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAM_PAGE = "page";

    private static final String API_KEY_VALUE = "c04e9d2cd0bd48b727a09adffca7e5ef";
    private static final String LANGUAGE_VALUE = "en-US";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_AVERAGE_VOTES = "vote_average.desc";

    public static final int POPULARITY = 0;
    public static final int AVERAGE_VOTES = 1;


    private static URL createURLForVideos(int movieID) {
        URL result = null;
        Uri uri = Uri.parse(String.format(VIDEOS_BASE_URL,movieID)).buildUpon()
                .appendQueryParameter(PARAM_API_KEY , API_KEY_VALUE)
                .appendQueryParameter(PARAM_LANGUAGE , LANGUAGE_VALUE).build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static JSONObject getJSONObjectForVideos(int movieID) {
        URL url = createURLForVideos(movieID);
        JSONObject jsonObject = null;
        try {
            jsonObject = new GETJSONOBJECTFROMAPI().execute(url).get();
        } catch (ExecutionException e) {
            Log.e("Main" , "Error while doing asyncTask: " + e);
        } catch (InterruptedException e) {
            Log.e("Main" , "Error while doing asyncTask: " + e);
        }
        return jsonObject;
    }

    private static URL createURLForReviews(int movieID) {
        URL result = null;
        Uri uri = Uri.parse(String.format(REVIEWS_BASE_URL,movieID)).buildUpon()
                .appendQueryParameter(PARAM_API_KEY , API_KEY_VALUE)
                .appendQueryParameter(PARAM_LANGUAGE , LANGUAGE_VALUE).build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONObjectForReviews(int movieID) {
        URL url = createURLForReviews(movieID);
        JSONObject jsonObject = null;
        try {
            jsonObject = new GETJSONOBJECTFROMAPI().execute(url).get();
        } catch (ExecutionException e) {
            Log.e("Main" , "Error while doing asyncTask: " + e);
        } catch (InterruptedException e) {
            Log.e("Main" , "Error while doing asyncTask: " + e);
        }
        return jsonObject;
    }


    private static URL createURL(int sortBy, int page) {
        URL result = null;
        String sortByMethod;
        if (sortBy == POPULARITY) {
            sortByMethod = SORT_BY_POPULARITY;
        } else {
            sortByMethod = SORT_BY_AVERAGE_VOTES;
        }

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY_VALUE)
                .appendQueryParameter(PARAM_LANGUAGE , LANGUAGE_VALUE)
                .appendQueryParameter(PARAM_SORT_BY , sortByMethod)
                .appendQueryParameter(PARAM_PAGE, Integer.toString(page))
                .build();  // <---

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONObject(int sortBy , int page) {
        URL url = createURL(sortBy , page);
        JSONObject jsonObject = null;
        try {
            jsonObject = new GETJSONOBJECTFROMAPI().execute(url).get();
        } catch (ExecutionException e) {
            Log.e("Main" , "Error while doing asyncTask: " + e);
        } catch (InterruptedException e) {
         Log.e("Main" , "Error while doing asyncTask: " + e);
        }
        return jsonObject;
    }

    private static class GETJSONOBJECTFROMAPI extends AsyncTask<URL,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            if (urls == null || urls.length == 0) {
                return null;
            }
            JSONObject result = null;
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {                 /// <---
                    builder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builder.toString());
                return result;
            } catch (IOException e) {
                Log.e("Main" , "IO Exception in asynctask: " + e);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }
    }
}
