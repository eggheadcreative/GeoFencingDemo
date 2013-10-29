package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

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
  Address address;
  SqlLiteDbHelper sqlLiteDbHelper;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.step_result);
    getWidgets();
    applyProperties();
    populateWidgets();
    sqlLiteDbHelper = new SqlLiteDbHelper(getBaseContext());
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
//    locationInfo = bundle.getString("locationInfo");
    parseAddressObject(bundle);
    locationDetails.setText(getLocationDetails(address));
  }


  private void parseAddressObject(Bundle bundle) {
    String json = bundle.getString("address");
    address = new Gson().fromJson(json, Address.class);
  }

  private void applyProperties() {
    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LocationDetailsDTO dto = new LocationDetailsDTO();
        dto.populateFields(address, frontPicture, backPicture);
        sendInformation(dto);
      }
    });
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        sqlLiteDbHelper.insertRecord(address, frontPicture, backPicture);
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


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      callHomeActivity();
    }
  }
}
