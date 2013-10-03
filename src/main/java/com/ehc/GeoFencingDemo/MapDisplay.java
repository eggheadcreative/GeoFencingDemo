package com.ehc.GeoFencingDemo;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapActivity;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 3/10/13
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class MapDisplay extends FragmentActivity {

  static final LatLng HAMBURG = new LatLng(53.558, 9.927);
  static final LatLng KIEL = new LatLng(53.551, 9.993);

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
    try {
      MapsInitializer.initialize(getBaseContext());
    } catch (GooglePlayServicesNotAvailableException e) {
      e.printStackTrace();
    }

//    map.onCreate(savedInstanceState);

//    map.displayZoomControls(true);
//    map.setBuiltInZoomControls(true);
//    map.setSatellite(true);
//    map.setTraffic(true);
//    GoogleMap googleMap = map.getMap();

//    if (googleMap != null)
//      Log.d("googlemap:", googleMap.toString());
//    else
//
//      Log.d("googlemap:", null);


    GoogleMapOptions options = new GoogleMapOptions();
    options.mapType(GoogleMap.MAP_TYPE_SATELLITE);
    options.zoomControlsEnabled(true);
    MapView map = new MapView(this, options);
    map.onCreate(savedInstanceState);
    map.onResume();
    map.setEnabled(true);
    GoogleMap googleMap = map.getMap();
    if (googleMap != null) {
      Log.d("googlemap:", googleMap.toString());
      googleMap.setMyLocationEnabled(true);

      Marker hamburg = googleMap.addMarker(new MarkerOptions().position(HAMBURG)
          .title("Hamburg"));
      Marker kiel = googleMap.addMarker(new MarkerOptions()
          .position(KIEL)
          .title("Kiel")
          .snippet("Kiel is cool")
          .icon(BitmapDescriptorFactory
              .fromResource(R.drawable.ic_launcher)));

      googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
      googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    } else
      Log.d("googlemap:", null);
    mainLayout.addView(map);


//    GoogleMapOptions options = new GoogleMapOptions();
//    options.mapType(GoogleMap.MAP_TYPE_SATELLITE);
//    options.zoomControlsEnabled(true);
//
//    MapFragment map = MapFragment.newInstance(options);
////
//    map.onCreate(savedInstanceState);
//    FragmentTransaction fragmentTransaction =
//        getFragmentManager().beginTransaction();
//    fragmentTransaction.add(R.id.main_layout, map);
//    fragmentTransaction.commit();
//
//    GoogleMap googleMap = map.getMap();
//    if (googleMap != null) {
//      Log.d("googlemap:", googleMap.toString());
//      googleMap.setMyLocationEnabled(true);
//
//      Marker hamburg = googleMap.addMarker(new MarkerOptions().position(HAMBURG)
//          .title("Hamburg"));
//      Marker kiel = googleMap.addMarker(new MarkerOptions()
//          .position(KIEL)
//          .title("Kiel")
//          .snippet("Kiel is cool")
//          .icon(BitmapDescriptorFactory
//              .fromResource(R.drawable.ic_launcher)));
//
//      googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
//      googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//    } else
//      Log.d("googlemap:", "null");
////    mainLayout.addView(map);


//    MapFragment mapFragment = MapFragment.newInstance(options);
//
//    mapFragment.onCreate(savedInstanceState);
//    FragmentTransaction fragmentTransaction =
//        getFragmentManager().beginTransaction();
//    fragmentTransaction.add(R.id.main_layout, mapFragment);
//    fragmentTransaction.commit();


  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }


}
