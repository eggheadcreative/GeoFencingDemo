package com.ehc.GeoFencingDemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.*;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.*;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends GeoFencingActivity implements LocationListener, View.OnClickListener,
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener, LocationClient.OnAddGeofencesResultListener {

  private TextView existingDataView;
  private Button startCapture;
  private LocationRequest mLocationRequest;
  public LocationClient mLocationClient;
  private LinearLayout mapLayout;
  private MapView mapView;
  private GoogleMap googleMap;
  private LatLng currentLocation;
  private Address address;

  private CameraPosition INIT = null;
  private final int REQUEST_ID = 1;
  private final float ampt_latitude = 17.435978600000000000f;
  private final float ampt_longitude = 78.448195599999960000f;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.location);
    checkGooglePlayService();
    getWidgets();
    showMap(savedInstanceState);
  }

  private void checkGooglePlayService() {
    int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
    if (result == ConnectionResult.SUCCESS) {
      Log.d("result:", "success");
    } else {
      GooglePlayServicesUtil.getErrorDialog(result, this, 1).show();
      Log.d("result:", "fail");
    }
  }


  @Override
  protected void onStart() {
    super.onStart();
    mLocationClient.connect();
  }

  @Override
  public void onStop() {
    if (mLocationClient.isConnected()) {
      mLocationClient.disconnect();
    }
    super.onStop();
  }

  private void findLocation() {

    String locationProviders = Settings.Secure.getString(getContentResolver(),
        Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    if (locationProviders == null || locationProviders.equals("")) {


    } else {
      Location currentLocation = mLocationClient.getLastLocation();
      mLocationRequest = LocationRequest.create();
      setLocation(currentLocation);
    }


  }

  private void getWidgets() {
    existingDataView = (TextView) findViewById(R.id.existing_data);
    existingDataView.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoSlab-Regular.ttf"));
    startCapture = (Button) findViewById(R.id.start_capture);
    startCapture.setOnClickListener(this);
    mapLayout = (LinearLayout) findViewById(R.id.map_layout);
    mLocationClient = new LocationClient(this, this, this);
  }

  private void showMap(Bundle savedInstanceState) {
    try {
      MapsInitializer.initialize(getBaseContext());
    } catch (GooglePlayServicesNotAvailableException e) {
      e.printStackTrace();
    }
    GoogleMapOptions options = new GoogleMapOptions();
    options.mapType(GoogleMap.MAP_TYPE_NORMAL);
    options.zoomControlsEnabled(true);
    mapView = new MapView(this, options);
    mapView.onCreate(savedInstanceState);
    mapView.onResume();
    mapView.setEnabled(true);
    googleMap = mapView.getMap();
    if (googleMap != null) {
      googleMap.setMyLocationEnabled(true);
    }
    mapLayout.addView(mapView);
  }

  private void focusCurrentLocation(Address address) {
    currentLocation = new LatLng(address.getLatitude(), address.getLongitude());
    googleMap.addMarker(new MarkerOptions()
        .position(currentLocation)
        .title(address.getSubLocality())
//        .snippet("location is cool")
//        .icon(BitmapDescriptorFactory
//            .fromResource(R.drawable.ic_launcher))
    );
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    INIT = new CameraPosition.Builder()
        .target(currentLocation)
        .zoom(17.5F)
        .bearing(300F) // orientation
//        .tilt(50F) // viewing angle
        .build();
//    googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
  }


  @Override
  public void onClick(View view) {
    Intent wizardIntent = new Intent(this, FirstStepActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("address", new Gson().toJson(address));
    wizardIntent.putExtras(bundle);
    startActivity(wizardIntent);
  }

  @Override
  public void onLocationChanged(Location location) {
    mLocationClient.requestLocationUpdates(mLocationRequest, this);
    findLocation();
  }

  @Override
  public void onConnected(Bundle bundle) {
    findLocation();
  }

  @Override
  public void onDisconnected() {

  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

  }

  protected void setLocation(Location params) {
    Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
    String locationDetails = "";
    Location location = params;
    List<Address> addresses = null;
    try {
      addresses = geocoder.getFromLocation(location.getLatitude(),
          location.getLongitude(), 1);
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    if (addresses != null && addresses.size() > 0) {
      address = addresses.get(0);
      locationDetails = getLocationDetails(address);
      focusCurrentLocation(address);
//      createGeofence(address);
    }
    existingDataView.setText(locationDetails);
  }

//  public void createGeofence(Address address) {
//
//    Geofence.Builder builder = new Geofence.Builder();
//    builder.setCircularRegion(address.getLatitude(), address.getLongitude(), 100);
////    builder.setCircularRegion(ampt_latitude, ampt_longitude, 100);
//    builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
//    builder.setRequestId("" + REQUEST_ID);
//    builder.setExpirationDuration(Geofence.NEVER_EXPIRE);
//    Geofence geofence = builder.build();
//    ArrayList<Geofence> list = new ArrayList<Geofence>();
//    list.add(geofence);
//    mLocationClient.addGeofences(list, getTransitionPendingIntent(), this);
//
//
////    googleMap.addCircle(new CircleOptions()
////        .center(new LatLng(address.getLatitude(), address.getLongitude())).radius(100)
////        .fillColor(Color.parseColor("#B2A9F6")));
//
//
//  }


//  private PendingIntent getTransitionPendingIntent() {
//    // Create an explicit Intent
//    Intent localIntent = new Intent(this, GeofencingService.class);
//    return PendingIntent.getService(
//        this, 0, localIntent,
//        PendingIntent.FLAG_UPDATE_CURRENT);
//  }


  @Override
  public void onAddGeofencesResult(int statusCode, String[] strings) {
    Intent broadcastIntent = new Intent();

    if (LocationStatusCodes.SUCCESS == statusCode) {
      Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

      broadcastIntent.setAction("com.example.android.geofence.ACTION_GEOFENCES_ADDED")
          .addCategory("com.example.android.geofence.CATEGORY_LOCATION_SERVICES")
          .putExtra("com.example.android.geofence.EXTRA_GEOFENCE_STATUS", "test");
    } else {
      Toast.makeText(this, "AddGeoError", Toast.LENGTH_SHORT).show();
    }

    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(broadcastIntent);

  }
}


///// MainActivity
//
//package com.example.geofence;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.util.Log;
//
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
//import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.location.Geofence;
//import com.google.android.gms.location.LocationClient;
//import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
//
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationStatusCodes;
//
//public class MainActivity extends FragmentActivity implements
//    ConnectionCallbacks,
//    OnConnectionFailedListener,
//    OnAddGeofencesResultListener {
//  private IntentFilter mIntentFilter;
//  private LocationClient locationClient;
//  private LocationRequest locatRequest;
//
//  private PendingIntent intent;
//  private List<Geofence> mGeoList;
//  private Context mContext;
//  private Geofence companyLocation;
//  private GeofenceBroadcastReceiver mBroadcastReceiver;
//
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
//    mContext = this;
//
//    locatRequest = null;
//    mGeoList = new ArrayList<Geofence>();
//
//    intent = null;
//    locationClient = new LocationClient(this, this, this);
//
//    mIntentFilter = new IntentFilter();
//    mIntentFilter.addAction("com.example.geofence.ACTION_GEOFENCES_ADDED");
//    mIntentFilter.addCategory("com.example.geofence.CATEGORY_LOCATION_SERVICES");
//    mBroadcastReceiver = new GeofenceBroadcastReceiver();
//  }
//
//
//  @Override
//  protected void onStart() {
//
//    companyLocation = new Geofence.Builder()
//        .setRequestId("1")
//        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
//        .setCircularRegion(
//            49.220531, -122.986772, (float) 50)
//        .setExpirationDuration(Geofence.NEVER_EXPIRE)
//        .build();
//
//    mGeoList.add(companyLocation);
//    locationClient.connect();
//    super.onStart();
//
//  }
//
//
//  @Override
//  public void onConnected(Bundle arg0) {
//    // TODO Auto-generated method stub
//    intent = getTransitionPendingIntent();
//
//    locatRequest = LocationRequest.create();
//    locatRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//    locatRequest.setInterval(5000);
//
//    try {
//      addGeofence();
//    } catch (UnsupportedOperationException e) {
//      Toast.makeText(this, "add_geofences_already_requested_error",
//          Toast.LENGTH_LONG).show();
//    }
//    //    locationClient.requestLocationUpdates(locatRequest, intent);
//
//
//  }
//
//  public void addGeofence() {
//    locationClient.addGeofences(mGeoList, intent, this);
//
//  }
//
//  private PendingIntent getTransitionPendingIntent() {
//    // Create an explicit Intent
//    Intent localIntent = new Intent(this,
//        ReceiveTransitionsIntentService.class);
//            /*
//             * Return the PendingIntent
//             */
//    return PendingIntent.getService(
//        this,
//        0,
//        localIntent,
//        PendingIntent.FLAG_UPDATE_CURRENT);
//  }
//
//
//  @Override
//  protected void onStop() {
//
//    locationClient.disconnect();
//    super.onStop();
//
//  }
//
//  @Override
//  public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
//    // TODO Auto-generated method stub
//    Intent broadcastIntent = new Intent();
//
//    if (LocationStatusCodes.SUCCESS == statusCode) {
//      Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//
//      broadcastIntent.setAction("com.example.android.geofence.ACTION_GEOFENCES_ADDED")
//          .addCategory("com.example.android.geofence.CATEGORY_LOCATION_SERVICES")
//          .putExtra("com.example.android.geofence.EXTRA_GEOFENCE_STATUS", "test");
//    } else {
//      Toast.makeText(this, "AddGeoError", Toast.LENGTH_SHORT).show();
//    }
//
//    LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcastIntent);
//
//  }
//
//
//  @Override
//  public void onConnectionFailed(ConnectionResult arg0) {        // TODO Auto-generated method stub
//    int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//    switch (code) {
//
//      case ConnectionResult.SERVICE_MISSING: {
//        Toast.makeText(this, "SERVICE_MISSING " + code + " ConnectionResult.SERVICE_MISSING " + ConnectionResult.SERVICE_MISSING, Toast.LENGTH_SHORT).show();
//        break;
//
//      }
//      case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED: {
//
//        Toast.makeText(this, "SERVICE_VERSION_UPDATE_REQUIRED " + code + " ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED " + ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, Toast.LENGTH_SHORT).show();
//
//        break;
//      }
//      default: {
//        Toast.makeText(this, "start " + code, Toast.LENGTH_SHORT).show();
//      }
//
//    }
//  }
//
//
//  @Override
//  protected void onDestroy() {
//    // TODO Auto-generated method stub
//    super.onDestroy();
//  }
//
//
//  @Override
//  protected void onPause() {
//    // TODO Auto-generated method stub
//    super.onPause();
//  }
//
//
//  @Override
//  protected void onResume() {
//    // TODO Auto-generated method stub
//    LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, mIntentFilter);
//    //locationClient.connect();
//    super.onResume();
//  }
//
//
//  @Override
//  public void onDisconnected() {
//    // TODO Auto-generated method stub
//    locationClient = null;
//  }
//
//    /*
//     * Handle results returned to this Activity by other Activities started with
//     * startActivityForResult(). In particular, the method onConnectionFailed() in
//     * GeofenceRemover and GeofenceRequester may call startResolutionForResult() to
//     * start an Activity that handles Google Play services problems. The result of this
//     * call returns here, to onActivityResult.
//     * calls
//     */
//
//  @Override
//  protected void onActivityResult(
//      int requestCode, int resultCode, Intent data) {
//  }
//
//
//  /**
//   * Define a Broadcast receiver that receives updates from connection listeners and
//   * the geofence transition service.
//   */
//  public class GeofenceBroadcastReceiver extends BroadcastReceiver {
//    /*
//     * Define the required method for broadcast receivers
//     * This method is invoked when a broadcast Intent triggers the receiver
//     */
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//      // Check the action code and determine what to do
//      String action = intent.getAction();
//
//      // Intent contains information about errors in adding or removing geofences
//      if (TextUtils.equals(action, "com.example.geofence.ACTION_GEOFENCES_ADDED")) {
//
//        handleGeofenceStatus(context, intent);
//
//        // Intent contains information about a geofence transition
//      } else if (TextUtils.equals(action, "com.example.geofence.ACTION_GEOFENCE_TRANSITION")) {
//
//        handleGeofenceTransition(context, intent);
//
//        // The Intent contained an invalid action
//      } else {
//
//        Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
//      }
//    }
//
//    /**
//     * If you want to display a UI message about adding or removing geofences, put it here.
//     *
//     * @param context A Context for this component
//     * @param intent  The received broadcast Intent
//     */
//    private void handleGeofenceStatus(Context context, Intent intent) {
//
//    }
//
//    /**
//     * Report geofence transitions to the UI
//     *
//     * @param context A Context for this component
//     * @param intent  The Intent containing the transition
//     */
//    private void handleGeofenceTransition(Context context, Intent intent) {
//            /*
//             * If you want to change the UI when a transition occurs, put the code
//             * here. The current design of the app uses a notification to inform the
//             * user that a transition has occurred.
//             */
//    }
//
//    /**
//     * Report addition or removal errors to the UI, using a Toast
//     *
//     * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
//     */
//    private void handleGeofenceError(Context context, Intent intent) {
//
//    }
//  }
//
//
//}
//
//
/////// ReceiveTransitionsIntentService
//
//package com.example.geofence;
//
//import java.util.List;
//
//import android.app.IntentService;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.app.TaskStackBuilder;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.location.Geofence;
//import com.google.android.gms.location.LocationClient;
//
//public class ReceiveTransitionsIntentService extends IntentService {
//
//  /**
//   * Sets an identifier for the service
//   */
//  private Context mContext;
//
//  public ReceiveTransitionsIntentService(Context c) {
//
//    super("ReceiveTransitionsIntentService");
//    mContext = c;
//  }
//
//  /**
//   * Handles incoming intents
//   *
//   * @param intent The Intent sent by Location Services. This
//   *               Intent is provided
//   *               to Location Services (inside a PendingIntent) when you call
//   *               addGeofences()
//   */
//  @Override
//  protected void onHandleIntent(Intent intent) {
//    // First check for errors
//
//    //Toast.makeText(mContext, "onHandleIntent", Toast.LENGTH_LONG).show();
//    if (LocationClient.hasError(intent)) {
//      // Get the error code with a static method
//      int errorCode = LocationClient.getErrorCode(intent);
//      // Log the error
//      Log.e("ReceiveTransitionsIntentService",
//          "Location Services error: " +
//              Integer.toString(errorCode));
//            /*
//             * You can also send the error code to an Activity or
//             * Fragment with a broadcast Intent
//             */
//        /*
//         * If there's no error, get the transition type and the IDs
//         * of the geofence or geofences that triggered the transition
//         */
//    } else {
//
//
//      int transitionType = LocationClient.getGeofenceTransition(intent);
//      if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ||
//          transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
//        List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
//        String[] geofenceIds = new String[geofences.size()];
//        for (int i = 0; i < geofences.size(); i++) {
//          geofenceIds[i] = geofences.get(i).getRequestId();
//        }
//        String ids = "1";
//        String transition = ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) ? "you are in" : "you are out");
//        Toast.makeText(mContext, transition, Toast.LENGTH_LONG).show();
//
//        sendNotification(transition, ids);
//        //FOR THE NOTIFICATION.
//
//        //NotificationManager mNotificationManager =
//        //      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//      }
//
//
//    }
//  }
//
//
//  private void sendNotification(String transitionType, String ids) {
//
//    // Create an explicit content Intent that starts the main Activity
//    Intent notificationIntent =
//        new Intent(getApplicationContext(), MainActivity.class);
//
//    // Construct a task stack
//    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//
//    // Adds the main Activity to the task stack as the parent
//    stackBuilder.addParentStack(MainActivity.class);
//
//    // Push the content Intent onto the stack
//    stackBuilder.addNextIntent(notificationIntent);
//
//    // Get a PendingIntent containing the entire back stack
//    PendingIntent notificationPendingIntent =
//        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//    // Get a notification builder that's compatible with platform versions >= 4
//    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//
//    // Set the notification contents
//    builder.setSmallIcon(R.drawable.ic_notification)
//        .setContentTitle(
//            transitionType + " geofence_transition_notification_title " + ids)
//        .setContentIntent(notificationPendingIntent);
//
//    // Get an instance of the Notification manager
//    NotificationManager mNotificationManager =
//        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//    // Issue the notification
//    mNotificationManager.notify(0, builder.build());
//  }
//
//
//}




