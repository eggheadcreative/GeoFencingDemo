package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 27/9/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThirdStepActivity extends GeoFencingActivity {
  final int REQUEST_CODE = 1;
  TextView locationDetails;
  ImageView frontImage;
  ImageView backImage;
  Button saveButton;
  Button sendButton;
  Bitmap frontPicture;
  Bitmap backPicture;
  String locationInfo;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.step_result);
    getWidgets();
    applyProperties();
    populateWidgets();
  }


  private void getWidgets() {
    locationDetails = (TextView) findViewById(R.id.location_result);
    frontImage = (ImageView) findViewById(R.id.front_image_result);
    backImage = (ImageView) findViewById(R.id.back_image_result);
    sendButton = (Button) findViewById(R.id.send);
    saveButton = (Button) findViewById(R.id.save);
    locationDetails.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoSlab-Regular.ttf"));
  }


  private void populateWidgets() {
    Bundle bundle = getIntent().getExtras();
    frontPicture = (Bitmap) bundle.get("frontImage");
    backPicture = (Bitmap) bundle.get("backImage");
    frontImage.setImageDrawable(new BitmapDrawable(getResources(), frontPicture));
    backImage.setImageDrawable(new BitmapDrawable(getResources(), backPicture));
    locationInfo = bundle.getString("locationInfo");
    locationDetails.setText(locationInfo);
  }

  private void applyProperties() {
    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        sendInformation();
      }
    });
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        saveCurrentLocation();
        Toast.makeText(getBaseContext(), "Location Details Saved Successfully", Toast.LENGTH_SHORT).show();
        callHomeActivity();
      }
    });
  }

  private void callHomeActivity() {
    finish();
    Intent homeIntent = new Intent(getBaseContext(), HomeActivity.class);
    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(homeIntent);
  }

//  public void saveCurrentLocation() {
//    DataBaseHelper dbHelper = new DataBaseHelper(this);
//    if ( != null)
//      dbHelper.saveLocation(address.getSubLocality());
//  }


  private ArrayList<Uri> getImages() {

    String frontImagePath = MediaStore.Images.Media.insertImage(getContentResolver(), frontPicture, "frontImage", null);
    Uri frontImageUri = Uri.parse(frontImagePath);

    String backImagePath = MediaStore.Images.Media.insertImage(getContentResolver(), backPicture, "backImage", null);
    Uri backImageUri = Uri.parse(backImagePath);

    ArrayList<Uri> uris = new ArrayList<Uri>();
    uris.add(frontImageUri);
    uris.add(backImageUri);
    return uris;

  }


  public void sendInformation() {
    Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    ArrayList<Uri> images = getImages();
    emailIntent.setType("message/image");
    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"prem@eggheadcreative.com"});
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Location Info");
    emailIntent.putExtra(Intent.EXTRA_TEXT, locationInfo);
    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
    startActivityForResult(emailIntent, REQUEST_CODE);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      callHomeActivity();
    }
  }
}
