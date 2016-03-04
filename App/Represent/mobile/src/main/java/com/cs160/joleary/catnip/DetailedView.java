package com.cs160.joleary.catnip;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.cs160.joleary.catnip.R;

public class DetailedView extends Activity {

    private String name;
    private TextView senator_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Intent a = getIntent();
        name = a.getStringExtra("name");
        senator_name = (TextView)findViewById(R.id.rep_name);
        senator_name.setText(name);
    }

}
