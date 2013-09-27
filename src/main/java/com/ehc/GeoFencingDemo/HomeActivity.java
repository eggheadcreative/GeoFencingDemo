package com.ehc.GeoFencingDemo;

import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends GeoFencingActivity {
  private TextView existingDataView;
  private ImageView mapImage;
  private Button startCapture;
//  private MapView mapView;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    getWidgets();
    FindingLocation();

  }

  private void FindingLocation() {
    LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
    boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    if (enabled) {
      Criteria criteria = new Criteria();
      String provider = service.getBestProvider(criteria, false);
      Log.d("***", "service:" + provider);
      Location location = service.getLastKnownLocation(provider);
      double latitude = location.getLatitude();
      double longitude = location.getLatitude();
      Log.d("***", "Longitude:" + location.getLongitude() + "\n" + "Latitude:" + location.getLatitude());

      Geocoder geocoder = new Geocoder(getApplicationContext(),
          Locale.getDefault());
      try {
        List<Address> listAddresses = geocoder.getFromLocation(
            latitude, longitude, 1);
        if (null != listAddresses && listAddresses.size() > 0) {
          Address address = listAddresses.get(0);
          String data = "country:" + address.getCountryName() + "\n Admin Area:" + address.getAdminArea() + "\n Locality" + address.getLocality() +
              "\n featureName:" + address.getFeatureName() + "\n getPostalCode:" + address.getPostalCode() + "\n phone" + address.getPhone();
          existingDataView.setText(data);
        }
      } catch (IOException e) {

        e.printStackTrace();

      }


    }
  }

  private void getWidgets() {
    existingDataView = (TextView) findViewById(R.id.existing_data);
    mapImage = (ImageView) findViewById(R.id.map_image);
    startCapture = (Button) findViewById(R.id.start_capture);
//    mapView = (MapView) findViewById(R.id.map_view);
  }


}
