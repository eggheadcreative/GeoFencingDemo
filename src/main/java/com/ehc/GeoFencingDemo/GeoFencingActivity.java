package com.ehc.GeoFencingDemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.bugsnag.android.Bugsnag;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeoFencingActivity extends Activity {

  public int REQUEST_CODE = 1;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bugsnag.register(this, "7977d834798df3256fcfec6e847a6902");
    Bugsnag.setNotifyReleaseStages("production", "staging", "demo");
    Bugsnag.setReleaseStage(getResources().getString(R.string.environment_name));
  }


  @Override
  protected void onStart() {
    super.onStart();
    GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
    googleAnalytics.getTracker(getResources().getString(R.string.ga_trackingId));
    EasyTracker.getInstance().setContext(this);
    EasyTracker.getInstance().activityStart(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EasyTracker.getInstance().setContext(this);
    EasyTracker.getInstance().activityStop(this);
  }

  public String getLocationDetails(Address address) {

    String locationDetails = "Address Line:    \t\t " + address.getAddressLine(0) +
        "\nSubLocality:     \t\t " + address.getSubLocality() +
        "\nLocality Name: \t\t " + address.getLocality() +
        "\nCountry Name: \t\t " + address.getCountryName() +
        "\nLatitude:             \t\t " + address.getLatitude() +
        "\nLongitude:         \t\t " + address.getLongitude();
    return locationDetails;
  }


  public String getLocationDetails(GeoFencingDTO dto) {

    String locationDetails = "Address Line:    \t\t " + dto.getLocationAddress() +
        "\nSubLocality:     \t\t " + dto.getSubLocality() +
        "\nLocality Name: \t\t " + dto.getLocalityName() +
        "\nCountry Name: \t\t " + dto.getCountryName() +
        "\nLatitude:             \t\t " + dto.getLatitude() +
        "\nLongitude:         \t\t " + dto.getLongitude();
    return locationDetails;
  }


  private ArrayList<Uri> getImages(Bitmap frontPicture, Bitmap backPicture) {

    String frontImagePath = MediaStore.Images.Media.insertImage(getContentResolver(), frontPicture, "frontImage", null);
    Uri frontImageUri = Uri.parse(frontImagePath);

    String backImagePath = MediaStore.Images.Media.insertImage(getContentResolver(), backPicture, "backImage", null);
    Uri backImageUri = Uri.parse(backImagePath);

    ArrayList<Uri> uris = new ArrayList<Uri>();
    uris.add(frontImageUri);
    uris.add(backImageUri);
    return uris;

  }


  public void sendInformation(GeoFencingDTO dto) {
    Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    ArrayList<Uri> images = getImages(dto.getFrontImage(), dto.getBackImage());
    emailIntent.setType("message/image");
    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"prem@eggheadcreative.com"});
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Location Info");
    emailIntent.putExtra(Intent.EXTRA_TEXT, getLocationDetails(dto));
    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
    startActivityForResult(emailIntent, REQUEST_CODE);
  }


}
