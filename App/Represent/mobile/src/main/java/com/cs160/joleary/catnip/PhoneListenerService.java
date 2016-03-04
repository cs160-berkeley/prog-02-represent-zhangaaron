//package com.cs160.joleary.catnip;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.wearable.MessageEvent;
//import com.google.android.gms.wearable.WearableListenerService;
//
//import java.nio.charset.StandardCharsets;
//
///**
// * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
// */
//public class PhoneListenerService extends WearableListenerService {
//
////   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
//private static final String REP = "/get_rep";
//
//    @Override
//    public void onMessageReceived(MessageEvent messageEvent) {
//        Log.d(this.getClass().toString(), "in PhoneListenerService, got: " + messageEvent.getPath());
//        if( messageEvent.getPath().equalsIgnoreCase(REP) ) {
//
//            // Value contains the String we sent over in WatchToPhoneService, "good job"
//            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//
//            // Make a toast with the String
////            Context context = getApplicationContext();
////            int duration = Toast.LENGTH_SHORT;
////
////            Toast toast = Toast.makeText(context, value, duration);
////            toast.show();
//
//            Intent detailed_view = new Intent(this, DetailedView.class);
//            detailed_view.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(detailed_view);
//            // so you may notice this crashes the phone because it's
//            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
//            // replace sending a toast with, like, starting a new activity or something.
//            // who said skeleton code is untouchable? #breakCSconceptions
//
//        } else {
//            super.onMessageReceived( messageEvent );
//        }
//
//    }
//}
package com.cs160.joleary.catnip;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String REP_PATH = "/get_rep";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if (messageEvent.getPath().equalsIgnoreCase(REP_PATH)) {
            String repName = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent detailed_view = new Intent(this, DetailedView.class);
            detailed_view.putExtra("name", repName);  // put the rep name from the intent
            detailed_view.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(detailed_view);

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}

