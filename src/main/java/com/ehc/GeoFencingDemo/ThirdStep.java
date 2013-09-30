package com.ehc.GeoFencingDemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
  }


  private void populateWidgets() {
    Bundle bundle = getIntent().getExtras();
    Bitmap frontPicture = (Bitmap) bundle.get("frontImage");
    Bitmap backPicture = (Bitmap) bundle.get("backImage");
    frontImage.setImageDrawable(new BitmapDrawable(getResources(), frontPicture));
    backImage.setImageDrawable(new BitmapDrawable(getResources(), backPicture));
    locationDetails.setText("final");
  }

  private void applyProperties() {

  }
}
