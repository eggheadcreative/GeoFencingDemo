package com.ehc.GeoFencingDemo;

import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

import java.util.List;
import java.util.Locale;

public class HomeActivity extends GeoFencingActivity implements LocationListener, View.OnClickListener,
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener {
  private TextView existingDataView;
  private ImageView mapImage;
  private Button startCapture;
  private LocationRequest mLocationRequest;
  public LocationClient mLocationClient;
  //  private MapView mapView;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    getWidgets();
    mLocationClient = new LocationClient(this, this, this);
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
    (new HomeActivity.GetAddressTask(this)).execute(currentLocation);
  }

  private void getWidgets() {
    existingDataView = (TextView) findViewById(R.id.existing_data);
    mapImage = (ImageView) findViewById(R.id.map_image);
    startCapture = (Button) findViewById(R.id.start_capture);
    startCapture.setOnClickListener(this);
//    mapView = (MapView) findViewById(R.id.map_view);
//    mapView.setEnabled(true);
//    mapView.setBuiltInZoomControls(true);
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


  protected class GetAddressTask extends AsyncTask<Location, Void, String> {

    Context localContext;

    public GetAddressTask(Context context) {
      super();
      localContext = context;
    }

    @Override
    protected String doInBackground(Location... params) {
      Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());
      String locationDetails = "";
      Location location = params[0];
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
      }

      return locationDetails;
    }


    public String getLocationDetails(Address address) {

      return
          "\nAddress Line       : " + address.getAddressLine(0) +
              "\nSubLocality        : " + address.getSubLocality() +
              "\nLocality Name      : " + address.getLocality() +
              "\nSubAdmin Area      : " + address.getSubAdminArea() +
              "\nAdmin Area         : " + address.getAdminArea() +
              "\nCountryName        : " + address.getCountryName() +
              "\nCountryCode        : " + address.getCountryCode() +
              "\nPhone              : " + address.getPhone() +
              "\nPostal Code        : " + address.getPostalCode() +
              "\nFeature Name       : " + address.getFeatureName() +
              "\nLocale             : " + address.getLocale() +
              "\nExtras             : " + address.getExtras() +
              "\nLatitude           : " + address.getLatitude() +
              "\nLongitude          : " + address.getLongitude() +
              "\nPremises           : " + address.getPremises() +
              "\nMaxAddressLineIndex: " + address.getMaxAddressLineIndex() +
              "\nSubThoroughfare    : " + address.getSubThoroughfare() +
              "\nThoroughfare       : " + address.getThoroughfare() +
              "\nUrl                : " + address.getUrl();
    }


    @Override
    protected void onPostExecute(String address) {
      existingDataView.setText(address);
    }
  }
}




