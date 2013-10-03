package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class HomeActivity extends GeoFencingActivity implements LocationListener, View.OnClickListener,
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener {

  private TextView existingDataView;
  private Button startCapture;
  private LocationRequest mLocationRequest;
  public LocationClient mLocationClient;
  private LinearLayout mapLayout;
  private MapView mapView;
  private GoogleMap googleMap;
  private LatLng currentLocation;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
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
    Location currentLocation = mLocationClient.getLastLocation();
    mLocationRequest = LocationRequest.create();
    setLocation(currentLocation);
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
    } else
      Log.d("googlemap:", "null");
    mapLayout.addView(mapView);
  }

  private void focusCurrentLocation(Address address) {
    currentLocation = new LatLng(address.getLatitude(), address.getLongitude());
    googleMap.addMarker(new MarkerOptions()
        .position(currentLocation)
        .title(address.getSubLocality())
//        .snippet("location is cool")
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.ic_launcher)));
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
  }


  @Override
  public void onClick(View view) {
    Intent wizardIntent = new Intent(this, WizardActivity.class);
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
      Address address = addresses.get(0);
      locationDetails = getLocationDetails(address);
      focusCurrentLocation(address);
    }
    existingDataView.setText(locationDetails);
  }


  public String getLocationDetails(Address address) {
    return
        "\nAddress Line       : " + address.getAddressLine(0) +
            "\nSubLocality        : " + address.getSubLocality() +
            "\nLocality Name      : " + address.getLocality() +
            "\nSubAdmin Area      : " + address.getSubAdminArea() +
//              "\nAdmin Area         : " + address.getAdminArea() +
            "\nCountryName        : " + address.getCountryName() +
//              "\nCountryCode        : " + address.getCountryCode() +
//              "\nPostal Code        : " + address.getPostalCode() +
//              "\nLocale             : " + address.getLocale() +
            "\nLatitude           : " + address.getLatitude() +
            "\nLongitude          : " + address.getLongitude();
  }


}




