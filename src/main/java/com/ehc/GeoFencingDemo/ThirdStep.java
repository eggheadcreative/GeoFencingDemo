package com.ehc.GeoFencingDemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
public class ThirdStep extends GeoFencingActivity {
  TextView locationDetails;
  ImageView frontImage;
  ImageView backImage;
  Button cancel;
  Button submit;
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
    submit = (Button) findViewById(R.id.submit);
    cancel = (Button) findViewById(R.id.cancel);
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
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        sendInformation();
      }
    });

  }


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
    startActivityForResult(emailIntent, 1);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1 && resultCode == RESULT_OK) {
      Intent finalIntent = new Intent(this, FinalActivity.class);
      startActivity(finalIntent);
    }
  }
}
