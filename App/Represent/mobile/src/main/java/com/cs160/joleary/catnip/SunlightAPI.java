package com.cs160.joleary.catnip;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

import com.google.android.gms.wearable.Asset;
//import com.mashape.unirest.*;
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaron on 3/7/16.
 */
public class SunlightAPI {
    public static final String BASE_URL = "congress.api.sunlightfoundation.com";
    public static final String API_KEY = "cd0c902118a84cd7965ba8e5e7f7c5fa";
    public static final String LOCATE_PATH = "/locate";


    public SunlightAPI() {}

    public ArrayList<Representative> reps_for_area(Location loc) {
//        try {
//            HttpResponse<JsonNode> jsonResponse = Unirest.post(BASE_URL + LOCATE_PATH)
//                    .header("accept", "application/json")
//                    .queryString("apikey", API_KEY)
//                    .field("latitude", new Double(loc.getLatitude()).toString())
//                    .field("longitude", loc.getLongitude())
//                    .asJson();
//            JSONArray results = jsonResponse.getBody().getObject().getJSONArray("results");
//            return parse_reps(results);
//        } catch (UnirestException e) {
//            Log.d(this.getClass().toString(), "Caught exception: " + e.getMessage());
//        } catch (JSONException e) {
//            Log.d(this.getClass().toString(), "Caught exception: " + e.getMessage());
//        }
        return null; //TODO: complete me
    }

    public ArrayList<Representative> reps_for_area(int zip) {
//        try {
//            HttpResponse<JsonNode> jsonResponse = Unirest.post(BASE_URL + LOCATE_PATH)
//                    .header("accept", "application/json")
//                    .queryString("apikey", API_KEY)
//                    .field("zip", new Integer(zip).toString())
//                    .asJson();
//            JSONArray results = jsonResponse.getBody().getObject().getJSONArray("results");
//            return parse_reps(results);
//        } catch (UnirestException e) {
//            Log.d(this.getClass().toString(), "Caught exception: " + e.getMessage());
//        } catch (JSONException e) {
//            Log.d(this.getClass().toString(), "Caught exception: " + e.getMessage());
//        }
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
