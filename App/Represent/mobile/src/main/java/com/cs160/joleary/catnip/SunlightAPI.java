package com.cs160.joleary.catnip;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
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
    public static final String LEGISLATOR_PATH = "legislators";
    public static final String LOCATE_PATH = "locate";
    private double  lat;
    private double _long;
    private int zip;
    private Context context;
    RecyclerView.Adapter mAdapter;
    private OkHttpClient client;

    public SunlightAPI(Context context, RecyclerView.Adapter mAdapter, double lat, double _long) {
        this.lat = lat;
        this._long = _long;
        this.context = context;
        this.mAdapter = mAdapter;
        client = new OkHttpClient();
    }

    public SunlightAPI(Context context, RecyclerView.Adapter mAdapter, int zip) {
        this.zip = zip;
        this.context = context;
        this.mAdapter = mAdapter;
        client = new OkHttpClient();
    }


    @Override
    public void run() {
        List<Representative> reps = reps_for_area();
        if (reps != null) {
            RepDataAdapter x = (RepDataAdapter)mAdapter;
            x.repList = reps; // don't question this.
//        x.notifyDataSetChanged();
        } else {
            Log.wtf(this.getClass().toString(), "FUCK WHY DIDN'T WE GET ANYTHING OH FUCKING A");
        }
    }
    public ArrayList<Representative> reps_for_area() {
        HttpUrl url;
        if (zip == 0) {
             url = new HttpUrl.Builder()
                     .scheme("http")
                    .host(BASE_URL)
                     .addPathSegment(LEGISLATOR_PATH)
                    .addPathSegment(LOCATE_PATH)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("latitude", new Double(lat).toString())
                    .addQueryParameter("longitude", new Double(_long).toString())
                    .build();
        } else {
             url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_URL)
                     .addPathSegment(LEGISLATOR_PATH)
                     .addPathSegment(LOCATE_PATH)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("zip", new Integer(zip).toString())
                    .build();
        }
        Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(url.toString())
                .build();
        Log.wtf("D", "URL IS" +  url.toString());
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.wtf("D",res);
            JSONObject j = new JSONObject(res);
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
                String rep_id = j.getString("bioguide_id");
                String party = j.getString("party");
                Representative r = new Representative(name, j.getString("oc_email"),
                        j.getString("website"), j.getString("twitter_id"), rep_id, party);
                ret.add(r);
            } catch (JSONException e) {
                Log.wtf(this.getClass().toString(), "Caught exception: " + e.getMessage());
            }

        }
        return ret;
    }
}
