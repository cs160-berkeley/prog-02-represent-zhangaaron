package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private RecyclerView mRecyclerMain;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int zip_code;
    private TextView zip_code_display;
    final String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";

    @Override
    protected void onResume() {
        super.onResume();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            zip_code = b.getInt("ZIP");
        }
        if (zip_code == 0) {
            Intent intent = new Intent(this, ZipDialog.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle b = getIntent().getExtras();
        if (b != null) {

            zip_code = b.getInt("ZIP");
        }
        if (zip_code == 0) {
            Intent intent = new Intent(this, ZipDialog.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        zip_code_display = (TextView)findViewById(R.id.zip_code_display);
        zip_code_display.setText("Showing results for: " + zip_code);


         mRecyclerMain = (RecyclerView)findViewById(R.id.recycler_main);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerMain.setHasFixedSize(true);

        //set grid layout manager
        mLayoutManager = new GridLayoutManager(this, 1); // span count 2
        mRecyclerMain.setLayoutManager(mLayoutManager);

        List<Representative> _list = new ArrayList<>();
        //populate with dummy rep
        Representative a1 = new Representative("Bernie Sanders, I", "bern@something.gov", "website.com", R.drawable.fred_160);
        Representative b2 = new Representative("Hillary Clinton, D", "hil@privateserver.gov", "website.com", R.drawable.fred_160);
        Representative c3 = new Representative("Dolan Trump, R", "dolan@something.gov", "websitearony.com",  R.drawable.fred_160);
        _list.add(a1);
        _list.add(b2);
        _list.add(c3);

//        for (int i= 0; i < 3; i++) {
//            Random rand = new Random();
//            int start1 = rand.nextInt(40);
//            int start2 = rand.nextInt(40);
//            Representative dummy_rep = new Representative(lorem.substring(start1, start1 + 10) + " , I", lorem.substring(start2, start2 + 10) + "@something.gov", "website.com", R.drawable.fred_160);
//            _list.add(dummy_rep);
//        }

        //set adapter
        mAdapter = new RepDataAdapter(_list, getApplicationContext());
        mRecyclerMain.setAdapter(mAdapter);
        Intent startWatch = new Intent(getApplicationContext(), PhoneToWatchService.class);
        startWatch.putExtra("zip", new Integer(zip_code).toString());
        startService(startWatch);

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
}
