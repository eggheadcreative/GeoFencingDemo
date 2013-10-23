package com.ehc.GeoFencingDemo;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 21/10/13
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeofencingService extends IntentService {

  public GeofencingService() {
    super("GeofencingService");
  }

  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  protected void onHandleIntent(Intent intent) {

    Log.d("login", "in onHandleIntent");
    String transition = "you are in out side of";
    if (LocationClient.hasError(intent)) {
      int errorCode = LocationClient.getErrorCode(intent);
      Log.d("login",
          "Location Services error: " +
              Integer.toString(errorCode));
    } else {
      Log.d("login", "Location Services success: ");
      int transitionType = LocationClient.getGeofenceTransition(intent);
//      if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
//        List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
//        String[] geofenceIds = new String[geofences.size()];
//        for (int i = 0; i < geofences.size(); i++) {
//          geofenceIds[i] = geofences.get(i).getRequestId();
//        }
      transition = "you are in";
    }

    Log.d("login", transition);
    ResultReceiver rec = intent.getParcelableExtra("receiverTag");
    Bundle bundle = new Bundle();
    bundle.putString("ServiceResult", transition);
    rec.send(0, bundle);


    //NotificationManager mNotificationManager =
    //      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


// else {
//        Log.d("login", "you are out");
//        ResultReceiver rec = intent.getParcelableExtra("receiverTag");
//        Bundle b = new Bundle();
//        b.putString("ServiceResult", "you are out");
//        rec.send(0, b);
//      }


  }


  private void sendNotification(String transitionType, String ids) {

    // Create an explicit content Intent that starts the main Activity
    Intent notificationIntent =
        new Intent(getApplicationContext(), LocationActivity.class);
//
    // Construct a task stack
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

    // Adds the main Activity to the task stack as the parent
    stackBuilder.addParentStack(LocationActivity.class);

    // Push the content Intent onto the stack
    stackBuilder.addNextIntent(notificationIntent);

    // Get a PendingIntent containing the entire back stack
    PendingIntent notificationPendingIntent =
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    // Get a notification builder that's compatible with platform versions >= 4
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

    // Set the notification contents
    builder.setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(
            transitionType + " fence " + ids)
        .setContentIntent(notificationPendingIntent);

    // Get an instance of the Notification manager
    NotificationManager mNotificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    // Issue the notification
    mNotificationManager.notify(0, builder.build());
  }


}
