package com.ehc.GeoFencingDemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 10/10/13
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationDetailsActivity extends GeoFencingActivity {
  TextView locationDetails;
  ImageView frontImage;
  ImageView backImage;
  Button saveButton;
  Button shareButton;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.step_result);
    getWidgets();
    showDetails();
  }

  private void getWidgets() {
    locationDetails = (TextView) findViewById(R.id.location_result);
    frontImage = (ImageView) findViewById(R.id.front_image_result);
    frontImage.setMinimumWidth(250);
    frontImage.setMinimumHeight(250);
    backImage = (ImageView) findViewById(R.id.back_image_result);
    backImage.setMinimumWidth(250);
    backImage.setMinimumHeight(250);
    saveButton = (Button) findViewById(R.id.save);
    saveButton.setVisibility(View.GONE);
    shareButton = (Button) findViewById(R.id.send);
  }

  private void showDetails() {
    int index = getIntent().getIntExtra("index", 0);
    GeoFencingDTO dto = getRecord(index);
    locationDetails.setText(getLocationDetails(dto));
    frontImage.setImageBitmap(dto.getFront_image());
    backImage.setImageBitmap(dto.getBack_image());

  }

  private GeoFencingDTO getRecord(int index) {
    GeoFencingDTO dto = new GeoFencingDTO();
    SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(getBaseContext());
    dto = sqlLiteDbHelper.getRecord(index);
    return dto;
  }


}
