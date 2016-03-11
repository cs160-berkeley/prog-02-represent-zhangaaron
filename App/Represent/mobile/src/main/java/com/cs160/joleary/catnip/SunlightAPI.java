package com.cs160.joleary.catnip;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.wearable.Asset;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaron on 3/7/16.
 */
public class SunlightAPI implements Runnable {
    public static final String BASE_URL = "congress.api.sunlightfoundation.com";
    public static final String API_KEY = "cd0c902118a84cd7965ba8e5e7f7c5fa";
    public static final String LOCATE_PATH = "/locate";
    private Location loc;
    private int zip;
    private OkHttpClient client;

    public SunlightAPI(Location loc) {
         this.loc = loc;
         client = new OkHttpClient();
    }

    public SunlightAPI(int zip) {
        this.zip = zip;
        client = new OKHttpClient();
    }


    @Override
    public void run() {
        if (loc == null) {
            reps_for_area(loc);
        } else {
            reps_for_area(zip);
        }
    }
    public ArrayList<Representative> reps_for_area(Location loc) {
        HttpUrl url = new HttpUrl.Builder()
                .host(BASE_URL)
                .addPathSegment(LOCATE_PATH)
                .addQueryParameter("apikey", API_KEY)
                .addQueryParameter("latitutde", new Double(loc.getLatitude()).toString())
                .addQueryParameter("longitude", new Double(loc.getLongitude()).toString())
                .build();
        Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(url.toString())
                        .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject j = new JSONObject(response.body().string());
            return parse_reps(j.getJSONArray("results"));
        } catch (IOException|JSONException e) {
            Log.wtf(this.getClass().toString(), "Got Exception in reps_for_area " + e.getMessage());
        }
        return null;

    }

    public ArrayList<Representative> reps_for_area(int zip) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_URL)
                .addPathSegment(LOCATE_PATH)
                .addQueryParameter("apikey", API_KEY)
                .addQueryParameter("zip", new Integer(zip).toString())
                .build();
        Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(url.toString())
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject j = new JSONObject(response.body().string());
            return parse_reps(j.getJSONArray("results"));
        } catch (IOException|JSONException e) {
            Log.wtf(this.getClass().toString(), "Got Exception in reps_for_area " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Representative> parse_reps(JSONArray json_a) {
        ArrayList<Representative> ret = new ArrayList<>();
        for (int i = 0; i < json_a.length(); i ++ ) {
            try {
                JSONObject j = json_a.getJSONObject(i);
                String name = j.getString("first_name") + " " + j.getString("last_name");
                Representative r = new Representative(name, j.getString("oc_email"),
                        j.getString("website"), j.getString("twitter_id"));
            } catch (JSONException e) {
                Log.d(this.getClass().toString(), "Caught exception: " + e.getMessage());
            }

        }
        return null;
    }
}
