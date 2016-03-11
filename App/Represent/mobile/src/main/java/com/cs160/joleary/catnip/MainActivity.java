package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.*;
import io.fabric.sdk.android.Fabric;

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

    @Override
    protected void onResume() {
        super.onResume();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            zip_code = b.getInt("ZIP");
            lat = b.getDouble("lat");
            _long = b.getDouble("long");
        }
        if (zip_code == 0 && lat == 0.0) {
            Intent intent = new Intent(this, ZipDialog.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        setContentView(R.layout.activity_main);
        Bundle b = getIntent().getExtras();
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

        zip_code_display = (TextView)findViewById(R.id.zip_code_display);
        if (zip_code != 0) {
            zip_code_display.setText("Showing results for: " + zip_code);
        } else {
            zip_code_display.setText("Showing results for location: lat " + lat + " long " + _long); // TODO: replace with location name.
        }

         mRecyclerMain = (RecyclerView)findViewById(R.id.recycler_main);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerMain.setHasFixedSize(true);

        //set grid layout manager
        mLayoutManager = new GridLayoutManager(this, 1); // span count 2
        mRecyclerMain.setLayoutManager(mLayoutManager);
        congressAPI = new SunlightAPI();
        //List<Representative> _list = congressAPI.reps_for_area(94709);

//        List<Representative> _list = new ArrayList<>();
//        //populate with dummy rep
//        Representative a1 = new Representative("Bernie Sanders, I", "bern@something.gov", "website.com", R.drawable.fred_160);
//        Representative b2 = new Representative("Hillary Clinton, D", "hil@privateserver.gov", "website.com", R.drawable.fred_160);
//        Representative c3 = new Representative("Dolan Trump, R", "dolan@something.gov", "websitearony.com",  R.drawable.fred_160);
//        _list.add(a1);
//        _list.add(b2);
//        _list.add(c3);
        congressAPI = new SunlightAPI(94709);
        Log.d(this.getClass().toString(), congressAPI.reps_for_area(94709).toString());

        //set adapter
        mAdapter = new RepDataAdapter(_list, getApplicationContext());
        mRecyclerMain.setAdapter(mAdapter);
        Intent startWatch = new Intent(getApplicationContext(), PhoneToWatchService.class);
        startWatch.putExtra("zip", new Integer(zip_code).toString());
        startService(startWatch);


        // TODO: Use a more specific parent
        final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();
        // TODO: Base this Tweet ID on some data from elsewhere in your app
        long tweetId = 631879971628183552L;
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tweetView = new TweetView(MainActivity.this, result.data);
                parentView.addView(tweetView);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });


        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


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


}
