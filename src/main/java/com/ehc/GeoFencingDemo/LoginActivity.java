package com.ehc.GeoFencingDemo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationStatusCodes;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends GeoFencingActivity implements LocationListener, LocationClient.OnAddGeofencesResultListener,
    GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, MyResultReceiver.Receiver {

  private static final int REQUEST_ID = 1;
  private final float ampt_latitude = 17.435978600000000000f;
  private final float ampt_longitude = 78.448195599999960000f;

  private final float office_latitude = 17.4140419f;
  private final float office_longitude = 78.449124f;


  private final float pantry_latitude = 17.414538f;
  private final float pantry_longitude = 78.44846f;


  private final float kishore_latitude = 17.4406386f;
  private final float kishore_longitude = 78.4282972f;
  private EditText memberId;
  private TextView status;
  private LocationClient mLocationClient;
  private MyResultReceiver mReceiver;
//  private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//
//    @Override
//
//    public void onReceive(Context context, Intent intent) {
//
//      String response = intent.getCharSequenceExtra("data").toString();
//      Log.d("login", response);
//
//      if (!response.equals("you are in")) {
//        Toast message = Toast.makeText(getBaseContext(), "you are not in fence", Toast.LENGTH_LONG);
//        message.setGravity(Gravity.TOP, 0, 0);
//        message.show();
//      }
//
//    }
//
//  };


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    mReceiver = new MyResultReceiver(new Handler());
    mReceiver.setReceiver(this);
    getWidgets();
//    displayKeyboard();
  }


  @Override
  protected void onResume() {
    super.onStart();
    mLocationClient.connect();
  }

  @Override
  protected void onPause() {
    super.onPause();

    if (mLocationClient != null)
      mLocationClient.disconnect();
  }

  private void getWidgets() {
    memberId = (EditText) findViewById(R.id.input_memberid);
    status = (TextView) findViewById(R.id.status);

    memberId.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        //To change body of implemented methods use File | Settings | File Templates.


      }

      @Override
      public void afterTextChanged(Editable editable) {
        String inputId = memberId.getText().toString();
        if (inputId.length() >= 5 && !inputId.equals("12345")) {
          memberId.setError("Please Enter A Valid PIN Number");
        } else if (inputId.equals("12345")) {
          startDashboardIntent();
        }
      }
    });

    mLocationClient = new LocationClient(this, this, this);
  }


  public void startDashboardIntent() {
    hideKeyboard();
    Intent homeIntent = new Intent(this, HomeActivity.class);
    startActivity(homeIntent);
  }

  private void displayKeyboard() {
    new Handler().postDelayed(new Runnable() {
      public void run() {
        InputMethodManager keyboard = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
      }

    }, 100);
  }

  private void hideKeyboard() {
    InputMethodManager keyboard = (InputMethodManager)
        getSystemService(Context.INPUT_METHOD_SERVICE);
    keyboard.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
  }


  public void createGeofence() {
    Log.d("login", "trying to create fence1");
    Geofence.Builder builder = new Geofence.Builder();
    Log.d("login", "trying to create fence2");
//    builder.setCircularRegion(ampt_latitude, ampt_longitude, 50);
    builder.setCircularRegion(pantry_latitude, pantry_longitude, 80);
    Log.d("login", "trying to create fence3");

    builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER);
    Log.d("login", "trying to create fence4");

    builder.setRequestId("" + REQUEST_ID);
    builder.setExpirationDuration(Geofence.NEVER_EXPIRE);
    Geofence geofence = builder.build();
    ArrayList<Geofence> list = new ArrayList<Geofence>();
    list.add(geofence);
    Log.d("login", "trying to create fence5");

    mLocationClient.addGeofences(list, getTransitionPendingIntent(), this);
    Log.d("login", "trying to create fence6");

  }


  private PendingIntent getTransitionPendingIntent() {
    Intent localIntent = new Intent(this, GeofencingService.class);
    localIntent.putExtra("receiverTag", mReceiver);
    return PendingIntent.getService(
        this, 0, localIntent,
        PendingIntent.FLAG_UPDATE_CURRENT);
  }


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
//    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(broadcastIntent);
  }

  @Override
  public void onConnected(Bundle bundle) {
    createGeofence();
  }

  @Override
  public void onDisconnected() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    //To change body of implemented methods use File | Settings | File Templates.
  }


  @Override
  public void onLocationChanged(Location location) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onStatusChanged(String s, int i, Bundle bundle) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onProviderEnabled(String s) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onProviderDisabled(String s) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onReceiveResult(int resultCode, Bundle resultData) {
    String response = resultData.getString("ServiceResult");
    allowAccess(response);
  }

  private void allowAccess(String response) {
    Log.d("login", response);
    Toast message = Toast.makeText(getBaseContext(), response + " fence", Toast.LENGTH_LONG);
    message.setGravity(Gravity.TOP, 0, 0);
    message.show();
    status.setVisibility(View.GONE);
    memberId.setVisibility(View.VISIBLE);
    memberId.setFocusable(true);
  }
}
