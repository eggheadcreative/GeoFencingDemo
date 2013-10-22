package com.ehc.GeoFencingDemo;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
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
    // First check for errors

    //Toast.makeText(mContext, "onHandleIntent", Toast.LENGTH_LONG).show();
    if (LocationClient.hasError(intent)) {
      // Get the error code with a static method
      int errorCode = LocationClient.getErrorCode(intent);
      // Log the error
      Log.e("GeofencingService",
          "Location Services error: " +
              Integer.toString(errorCode));
            /*
             * You can also send the error code to an Activity or
             * Fragment with a broadcast Intent
             */
        /*
         * If there's no error, get the transition type and the IDs
         * of the geofence or geofences that triggered the transition
         */
    } else {


      int transitionType = LocationClient.getGeofenceTransition(intent);
      if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ||
          transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
        List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
        String[] geofenceIds = new String[geofences.size()];
        for (int i = 0; i < geofences.size(); i++) {
          geofenceIds[i] = geofences.get(i).getRequestId();
        }
        String ids = "1";
        String transition = ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) ? "you are in" : "you are out");
//        Toast.makeText(mContext, transition, Toast.LENGTH_LONG).show();

        sendNotification(transition, ids);
        //FOR THE NOTIFICATION.

        //NotificationManager mNotificationManager =
        //      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      }


    }
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
