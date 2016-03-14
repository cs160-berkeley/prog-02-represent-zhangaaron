package com.cs160.joleary.catnip;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailedView extends Activity {

    private String sen_id;
    private TextView senator_name;
    private OkHttpClient client;
    private TextView bills;
    private TextView committees;
    private TextView end_date;
    private String term_end;
    private String legislator_name;
    ArrayList<String> bills_array = new ArrayList<>();
    ArrayList<String> committees_array = new ArrayList<>();
    public static final String BASE_URL = "congress.api.sunlightfoundation.com";
    public static final String API_KEY = "cd0c902118a84cd7965ba8e5e7f7c5fa";
    public static final String BILL_PATH = "bills";
    public static final String COMMITTEE_PATH = "committees";
    public static final String REP_PATH = "legislators";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Intent a = getIntent();
        sen_id = a.getStringExtra("rep_id");
        senator_name = (TextView)findViewById(R.id.rep_name);
        committees = (TextView)findViewById(R.id.committees_served_text);
        bills = (TextView)findViewById(R.id.sponsored_bills_text);
        end_date = (TextView)findViewById(R.id.end_date_text);


        client = new OkHttpClient();

        final Request bill_request = make_request(BILL_PATH, sen_id);
        final Request committee_request = make_request(COMMITTEE_PATH, sen_id);
        final Request leg_request = make_request(REP_PATH, sen_id);
        Thread bill_req = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response resp = client.newCall(bill_request).execute();
                    String res = resp.body().string();
                    Log.wtf("D", res);
                    JSONObject j = new JSONObject(res);
                    JSONArray bill_j = j.getJSONArray("results");
                    Log.wtf("WTF", "WTF");
                    for (int i = 0; i < bill_j.length(); i++) {
                        Log.wtf("WTF", bill_j.getJSONObject(i).toString());
                                bills_array.add(bill_j.getJSONObject(i).getString("short_title"));
                    }


                } catch (Exception e) {
                    Log.wtf(this.getClass().toString(), "Got EXception " + e.toString());
                }
            }
        });
        Thread committee_req = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response resp = client.newCall(committee_request).execute();
                    String res = resp.body().string();
                    JSONObject j = new JSONObject(res);
                    JSONArray committee_j = j.getJSONArray("results");
                    Log.wtf(this.getClass().toString(), committee_j.toString());
                    for (int i = 0; i < committee_j.length(); i++) {
                        committees_array.add(committee_j.getJSONObject(i).getString("name"));
                    }
                } catch (Exception e) {
                    Log.wtf(this.getClass().toString(), "Got EXception " + e.toString());
                }
            }
        });

        Thread leg_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response resp = client.newCall(leg_request).execute();
                    String res = resp.body().string();
                    JSONObject j = new JSONObject(res);
                    JSONArray leg_j = j.getJSONArray("results");
                    Log.wtf(this.getClass().toString(), leg_j.toString());
                    JSONObject legislator_j = leg_j.getJSONObject(0);
                    term_end = legislator_j.getString("term_end");
                    legislator_name = legislator_j.getString("first_name") + " " +
                            legislator_j.getString("last_name") + ", " + legislator_j.getString("party");
                } catch (Exception e) {
                    Log.wtf(this.getClass().toString(), "Got EXception " + e.toString());
                }
            }
        });

        bill_req.start();
        committee_req.start();
        leg_thread.start();
        try {
            bill_req.join();
            committee_req.join();
            leg_thread.join();
        } catch (Exception e) {
            Log.wtf(this.getClass().toString(), "Got Exception " + e.toString());
        }

        String committee_text = "";
        for (String s : committees_array)
        {
            committee_text += s + "\n";
        }

        committees.setText(committee_text);

        String bills_text = "";
        for (String s : bills_array)
        {
            if (!s.equals("null")) {
                bills_text += s + "\n";
            }
        }

        bills.setText(bills_text);
        senator_name.setText(legislator_name);
        end_date.setText(term_end);

    }


    protected Request make_request(String path, String bioguide) {
        HttpUrl url;
        switch (path) {
            case COMMITTEE_PATH:
                url = new HttpUrl.Builder()
                        .scheme("http")
                        .host(BASE_URL)
                        .addPathSegment(COMMITTEE_PATH)
                        .addQueryParameter("apikey", API_KEY)
                        .addQueryParameter("member_ids", bioguide)
                        .build();
                break;
            case REP_PATH:
                url = new HttpUrl.Builder()
                        .scheme("http")
                        .host(BASE_URL)
                        .addPathSegment(REP_PATH)
                        .addQueryParameter("apikey", API_KEY)
                        .addQueryParameter("bioguide_id", bioguide)
                        .build();
                break;
            default:
                url = new HttpUrl.Builder()
                        .scheme("http")
                        .host(BASE_URL)
                        .addPathSegment(BILL_PATH)
                        .addQueryParameter("apikey", API_KEY)
                        .addQueryParameter("sponsor_id", bioguide)
                        .build();
                break;
        }
        return new Request.Builder()
                .addHeader("accept", "application/json")
                .url(url.toString())
                .build();


    }

}
