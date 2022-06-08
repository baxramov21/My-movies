package com.example.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

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
    private static final String VIDEOS_BASE_URL = "https://api.themoviedb.org/3/movie/%s/videos";
    private static final String REVIEWS_BASE_URL = "https://api.themoviedb.org/3/review/%s";

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_VOTE_COUNT = "vote_count.gte";

    private static final String API_KEY_VALUE = "c04e9d2cd0bd48b727a09adffca7e5ef";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_AVERAGE_VOTES = "vote_average.desc";
    private static final String MIN_VOTE_COUNT_VALUE = "1000";

    public static final int POPULARITY = 0;
    public static final int AVERAGE_VOTES = 1;

    public static URL createURLForReviews(int id,String lang) {
        Uri uri = Uri.parse(String.format(REVIEWS_BASE_URL, id)).buildUpon()
                .appendQueryParameter(PARAM_LANGUAGE,lang)
                .appendQueryParameter(PARAM_API_KEY, API_KEY_VALUE).build();
        try {
            Log.i("link", uri.toString());
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONObjectForReviews(int id,String lang) {
        URL url = createURLForReviews(id,lang);
        JSONObject jsonObject = null;
        try {
            jsonObject = new GETJSONOBJECTFROMAPI().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Main", "Error while doing asyncTask: " + e);
        }
        return jsonObject;
    }

    public static URL createURLForVideos(int id,String lang) {
        Uri uri = Uri.parse(String.format(VIDEOS_BASE_URL, id)).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY_VALUE)
                .appendQueryParameter(PARAM_LANGUAGE, lang).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONObjectForVideos(int id,String lang) {
        URL url = createURLForVideos(id,lang);
        JSONObject jsonObject = null;
        try {
            jsonObject = new GETJSONOBJECTFROMAPI().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Main", "Error while doing asyncTask in getJSONObjectForVideos: " + e);
        }
        return jsonObject;
    }

    public static URL createURL(int sortBy, int page,String lang) {
        URL result = null;
        String sortByMethod;
        if (sortBy == POPULARITY) {
            sortByMethod = SORT_BY_POPULARITY;
        } else {
            sortByMethod = SORT_BY_AVERAGE_VOTES;
        }

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY_VALUE)
                .appendQueryParameter(PARAM_LANGUAGE, lang)
                .appendQueryParameter(PARAM_SORT_BY, sortByMethod)
                .appendQueryParameter(PARAM_VOTE_COUNT , MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAM_PAGE, Integer.toString(page))
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONObject(int sortBy, int page,String lang) {
        URL url = createURL(sortBy, page,lang);
        JSONObject jsonObject = null;
        try {
            jsonObject = new GETJSONOBJECTFROMAPI().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Main", "Error while doing asyncTask in getJSONObject : " + e);
        }
        return jsonObject;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {

        private Bundle bundle;
        private OnStartLoading onStartLoading;
        public interface OnStartLoading {
            void onStartLoading();
        }

        public void setOnStartLoading(OnStartLoading onStartLoading) {
            this.onStartLoading = onStartLoading;
        }

        public JSONLoader(@NonNull Context context , Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoading != null) {
                onStartLoading.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle == null) {
                return  null;
            }
            String urlAsString = bundle.getString("url");
            URL url = null;
            try {
                url  = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null) {
                return null;
            }
            JSONObject result = null;
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builder.toString());
            } catch (IOException e) {
                Log.e("Main", "IO Exception in async task in loader: " + e);
            } catch (JSONException e) {

                Log.e("Main", "JSON Exception in async task in loader: " + e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }
    }

    private static class GETJSONOBJECTFROMAPI extends AsyncTask<URL, Void, JSONObject> {
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
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builder.toString());
            } catch (IOException e) {
                Log.e("Main", "IO Exception in asynctask: " + e);
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
