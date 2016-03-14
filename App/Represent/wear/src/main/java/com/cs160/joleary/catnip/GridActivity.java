package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cs160.joleary.catnip.dummy.DummyContent;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class GridActivity extends Activity implements ItemFragment.OnListFragmentInteractionListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "IWa1hAGugIHn81U4TwXzJIoc0";
    private static final String TWITTER_SECRET = "9jdUHpRAMAUaYEPJU85Xf5iqUVPQZxDGkc6DVH6TEEZHGvWSqB";


    private TextView mTextView;
    private Button mFeedBtn;
    private int zip;
    private ArrayList<String> repNames;
    private HashMap<String, ArrayList<Double>> vote_view;
    private String county;
    private ArrayList<String> repIds;
    GridViewPager mGridPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        mGridPager = (GridViewPager) findViewById(R.id.pager);

        InputStream is = getResources().openRawResource(R.raw.election);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            Log.wtf(this.getClass().toString(), "Got exception: " + e.toString());
        }

        String jsonString = writer.toString();
        try {
            JSONArray j = new JSONArray(jsonString);
            vote_view = new HashMap<>();
            for (int i =0; i < j.length(); i++) {
                JSONObject county = j.getJSONObject(i);
                double obama_vote = county.getDouble("obama-percentage");
                double romney_vote = county.getDouble("romney-percentage");
                ArrayList<Double> votes = new ArrayList<>();
                votes.add(obama_vote);
                votes.add(romney_vote);
                vote_view.put(county.getString("county-name"), votes);
            }
        } catch (Exception e) {
            Log.wtf(this.getClass().toString(), "Got exception: " + e.toString());
        }

        try {
            Intent intent = getIntent();
            String rep_json = intent.getStringExtra("zip");
            Log.d(this.getClass().toString(), "ZIP STRING IS " + rep_json);
            if (rep_json != null) {
                JSONObject j = new JSONObject(rep_json);
                JSONArray j_a = j.getJSONArray("reps");
                JSONArray j_a_ids = j.getJSONArray("rep_ids");
                repNames = new ArrayList<>();
                repIds = new ArrayList<>();
                for (int i = 0; i < j_a.length(); i++) {
                    repNames.add(j_a.getString(i));
                    repIds.add(j_a_ids.getString(i));
                }
                Log.wtf("D", "IN GRID ACTIVITY LENGTH IS " + repIds.size());
                county = j.getString("county");
                Log.d(this.getClass().toString(), "ZIP IS ----- " + zip);
                mGridPager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager(), vote_view.get(county).get(0), vote_view.get(county).get(1), repNames, repIds));
            } else {
                mGridPager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager(), 0d, 0d, new ArrayList<String>(), new ArrayList<String>()));
            }
        } catch (JSONException e) {
            Log.d("D", e.getMessage());  //this is great I know
        }


    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem uri){
        //you can leave it empty
    }
}
