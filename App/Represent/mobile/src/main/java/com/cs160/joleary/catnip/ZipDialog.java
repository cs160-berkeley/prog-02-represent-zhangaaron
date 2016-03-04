package com.cs160.joleary.catnip;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ZipDialog extends Activity {
    private TextView zipText;
    private Button mSubmit;
    private Button mLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_dialog);
        zipText = (TextView) findViewById(R.id.zip_code);
        mSubmit = (Button)findViewById(R.id.submit_zip);
        mLocation = (Button)findViewById(R.id.submit_location);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), MainActivity.class);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putInt("ZIP", Integer.parseInt(zipText.getText().toString()));
                sendIntent.putExtras(b);
                Log.d(this.getClass().toString(), "SUCCESS");
                startActivity(sendIntent);
                finish();
            }


        });
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), MainActivity.class);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putInt("ZIP", 94709);
                sendIntent.putExtras(b);
                Log.d(this.getClass().toString(), "SUCCESS");
                startActivity(sendIntent);
                finish();
            }


        });


    }

}
