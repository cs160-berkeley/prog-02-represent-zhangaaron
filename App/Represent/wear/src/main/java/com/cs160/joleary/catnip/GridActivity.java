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

import java.util.ArrayList;

public class GridActivity extends Activity implements ItemFragment.OnListFragmentInteractionListener {

    private TextView mTextView;
    private Button mFeedBtn;
    private int zip;
    private ArrayList<String> repNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

        try {
            Intent intent = getIntent();
            String zip_string = intent.getStringExtra("zip");
            Log.d(this.getClass().toString(), "ZIP STRING IS " + zip_string);
            if (zip_string != null) {
                zip = Integer.parseInt(zip_string);
                Log.d(this.getClass().toString(), "ZIP INT IS " + zip);
            }
        } catch (NullPointerException e) {
            Log.d("D", "Probably caught a null pointer");  //this is great I know
        }
        GridViewPager mGridPager = (GridViewPager) findViewById(R.id.pager);
        Log.d(this.getClass().toString(), "ZIP INT IS ----- " + zip);
        mGridPager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager(), zip));

//        mFeedBtn = (Button) findViewById(R.id.feed_btn);




//        if (extras != null) {
//            String catName = extras.getString("CAT_NAME");
//            mFeedBtn.setText("Feed " + catName);
//        }

//        mFeedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
//                Log.d("DEBUG", "fEED BUTTON PRESSED");
//                sendIntent.putExtra("REP_ID", "11");
//                startService(sendIntent);
//            }
//        });
//
//        mFeedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
//                sendIntent.putExtra("REP_ID", "11");
//                startService(sendIntent);
//            }
//        });
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem uri){
        //you can leave it empty
    }
}