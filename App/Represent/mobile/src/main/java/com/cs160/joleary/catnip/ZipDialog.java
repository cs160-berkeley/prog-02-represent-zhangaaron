package com.cs160.joleary.catnip;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import java.util.List;

public class ZipDialog extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private TextView zipText;
    private Button mSubmit;
    private Button mLocation;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

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
                Log.d(this.getClass().toString(), v.toString());
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
                Log.d(this.getClass().toString(), v.toString());
                Log.d(this.getClass().toString(), "LAT IS: " + mLastLocation.getLatitude());
                b.putDouble("lat", mLastLocation.getLatitude());
                b.putDouble("long", mLastLocation.getLongitude());
                //b.putInt("ZIP", 94709);
                sendIntent.putExtras(b);
                Log.d(this.getClass().toString(), "SUCCESS");
                startActivity(sendIntent);
                finish();
            }


        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionHint) {
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.d(this.getClass().toString(), "LOCATION IS ------ " + mLastLocation.toString());
        } catch (SecurityException e) {
            Log.wtf("D", "Security exception " + e.getMessage());
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

}
