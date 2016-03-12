package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "IWa1hAGugIHn81U4TwXzJIoc0";
    private static final String TWITTER_SECRET = "9jdUHpRAMAUaYEPJU85Xf5iqUVPQZxDGkc6DVH6TEEZHGvWSqB";
    private TwitterLoginButton loginButton;


    private RecyclerView mRecyclerMain;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int zip_code;
    private double lat;
    private double _long;
    private TextView zip_code_display;
    private SunlightAPI congressAPI;
    private String county;
    private OkHttpClient client;


    @Override
    protected void onResume() {
        super.onResume();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            zip_code = b.getInt("ZIP");
            lat = b.getDouble("lat");
            _long = b.getDouble("long");
        }

        Log.wtf("D", "LITERALLY FUCK EVERYTHING");
        zip_code_display = (TextView)findViewById(R.id.zip_code_display);
        if (zip_code != 0) {
            zip_code_display.setText("Showing results for: " + zip_code);
            congressAPI = new SunlightAPI(this.getApplicationContext(), mAdapter, zip_code );
        } else {
            zip_code_display.setText("Showing results for location: lat " + lat + " long " + _long); // TODO: replace with location name.
            congressAPI = new SunlightAPI(this.getApplicationContext(), mAdapter, lat, _long);
            reverse_geocode(lat, _long);
            Log.wtf("D", "COUNTY IS " + county);
        }

        try {
            Thread fuck_this_thread = new Thread(congressAPI);
            fuck_this_thread.start(); // get sunlight info update the recyclerview data adapter
            fuck_this_thread.join();
        } catch (Exception e) {
            Log.wtf("d", "fuck everything");
        }
        mAdapter.notifyDataSetChanged();

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
        } catch (Exception e) {
            Log.wtf(this.getClass().toString(), "Got exception: " + e.toString());
        }

        Intent startWatch = new Intent(getApplicationContext(), PhoneToWatchService.class);
        startWatch.putExtra("zip", repData());
        startService(startWatch);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        setContentView(R.layout.activity_main);
        Bundle b = getIntent().getExtras();
        client = new OkHttpClient();
        if (b != null) {
            lat = b.getDouble("lat");
            _long = b.getDouble("long");
            zip_code = b.getInt("ZIP");
        }
        if (zip_code == 0 && lat == 0.0) { // not instantiated yet, so do that first.
            Intent intent = new Intent(this, ZipDialog.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        mRecyclerMain = (RecyclerView)findViewById(R.id.recycler_main);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerMain.setHasFixedSize(true);

        //set grid layout manager
        mLayoutManager = new GridLayoutManager(this, 1); // span count 1
        mRecyclerMain.setLayoutManager(mLayoutManager);

        List<Representative> _list = new ArrayList<>();
//        //populate with dummy rep
        Representative a1 = new Representative("Bernie Sanders, I", "bern@something.gov", "website.com", R.drawable.fred_160);
        Representative b2 = new Representative("Hillary Clinton, D", "hil@privateserver.gov", "website.com", R.drawable.fred_160);
        Representative c3 = new Representative("Dolan Trump, R", "dolan@something.gov", "websitearony.com",  R.drawable.fred_160);
        _list.add(a1);
        _list.add(b2);
        _list.add(c3);
        //set adapter
        mAdapter = new RepDataAdapter(_list, getApplicationContext());
        mRecyclerMain.setAdapter(mAdapter);





//        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
//        loginButton.setCallback(new Callback<TwitterSession>() {
//            @Override
//            public void success(Result<TwitterSession> result) {
//                // The TwitterSession is also available through:
//                // Twitter.getInstance().core.getSessionManager().getActiveSession()
//                TwitterSession session = result.data;
//                // TODO: Remove toast and use the TwitterSession's userID
//                // with your app's user model
//                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.d("TwitterKit", "Login with Twitter failure", exception);
//            }
//        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    void reverse_geocode(double lat, double _long) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat+ ","+ _long +"&key=AIzaSyCLL_I-jIoipC8gr7z3kU97YZFn98GYlDU";
        final Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(url.toString())
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    String res = response.body().string();
                    JSONObject j = new JSONObject(res);
                    Log.wtf("RES", res);
                    JSONArray addr_comps = j.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                    for (int i = 0; i < addr_comps.length(); i++) {
                        JSONObject addr = addr_comps.getJSONObject(i);
                        JSONArray types = addr.getJSONArray("types");
                        if (types.getString(0).equals("administrative_area_level_2")) {
                            county = addr.getString("short_name").replaceAll("County", "").trim();
                        }
                    }



                } catch (IOException|JSONException e) {
                    Log.wtf(this.getClass().toString(), "got exception " + e.toString());
                }
            }
        });
        t.start();
        try {

            t.join();
        } catch (Exception e) {
            Log.wtf("D", "TIMEOUT OR SOMETHING: " + e.getMessage());
        }
    }


    protected String repData() {
        JSONObject j = new JSONObject();
        try {

            j.accumulate("county", county);
            List<Representative> r = ((RepDataAdapter) mAdapter).repList;
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> _rep_ids = new ArrayList<>();
            for (int i = 0; i < r.size(); i++) {
                names.add(r.get(i).name);
                _rep_ids.add(r.get(i).bioguide);
            }
            JSONArray reps = new JSONArray(names);
            JSONArray rep_ids = new JSONArray(_rep_ids);
            j.accumulate("reps", reps);
            j.accumulate("rep_ids", rep_ids);
        } catch (JSONException e) {
            Log.wtf("D", "Caught JSON exception when marshalling data 248");

        }
        return j.toString();
    }
}
