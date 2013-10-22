package com.ehc.GeoFencingDemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

  Context mContext;

  /*
   * Define the required method for broadcast receivers
   * This method is invoked when a broadcast Intent triggers the receiver
   */
  @Override
  public void onReceive(Context context, Intent intent) {

    // Check the action code and determine what to do
    mContext = context;
    String action = intent.getAction();

    // Intent contains information about errors in adding or removing geofences
    if (TextUtils.equals(action, "com.example.geofence.ACTION_GEOFENCES_ADDED")) {

      handleGeofenceStatus(context, intent);

      // Intent contains information about a geofence transition
    } else if (TextUtils.equals(action, "com.example.geofence.ACTION_GEOFENCE_TRANSITION")) {

      handleGeofenceTransition(context, intent);

      // The Intent contained an invalid action
    } else {

      Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
    }
  }

  /**
   * If you want to display a UI message about adding or removing geofences, put it here.
   *
   * @param context A Context for this component
   * @param intent  The received broadcast Intent
   */
  private void handleGeofenceStatus(Context context, Intent intent) {

  }

  /**
   * Report geofence transitions to the UI
   *
   * @param context A Context for this component
   * @param intent  The Intent containing the transition
   */
  private void handleGeofenceTransition(Context context, Intent intent) {
    createNotification();
            /*
             * If you want to change the UI when a transition occurs, put the code
             * here. The current design of the app uses a notification to inform the
             * user that a transition has occurred.
             */
  }

  /**
   * Report addition or removal errors to the UI, using a Toast
   *
   * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
   */
  private void handleGeofenceError(Context context, Intent intent) {

  }

  public void createNotification() {
    // Prepare intent which is triggered if the
    // notification is selected
//    Intent intent = new Intent(this, NotificationReceiver.class);
//    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

    // Build notification
    // Actions are just fake
    Notification noti = new Notification.Builder(mContext)
        .setContentTitle("New mail from " + "test@gmail.com")
//        .setContentText("Subject").setSmallIcon(R.drawable.icon)
//        .setContentIntent(pIntent)
//        .addAction(R.drawable.icon, "Call", pIntent)
//        .addAction(R.drawable.icon, "More", pIntent)
//        .addAction(R.drawable.icon, "And more", pIntent)
        .build();
    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Activity.NOTIFICATION_SERVICE);
    // hide the notification after its selected
    noti.flags |= Notification.FLAG_AUTO_CANCEL;

    notificationManager.notify(0, noti);

  }


}
