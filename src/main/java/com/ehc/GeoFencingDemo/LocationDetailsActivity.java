package com.ehc.GeoFencingDemo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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
    frontImage.setPadding(8, 0, 0, 0);
    backImage.setPadding(8, 0, 0, 0);
    backImage.setMinimumWidth(250);
    backImage.setMinimumHeight(250);
    saveButton = (Button) findViewById(R.id.save);
    saveButton.setVisibility(View.GONE);
    shareButton = (Button) findViewById(R.id.send);
    RelativeLayout imageLayout = (RelativeLayout) findViewById(R.id.image_layout);
    imageLayout.setPadding(0, 0, 8, 8);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    params.gravity = Gravity.CENTER_VERTICAL;
    params.setMargins(8, 0, 8, 0);
    shareButton.setLayoutParams(params);
  }

  private void showDetails() {
    String timeStamp = getIntent().getStringExtra("timeStamp");
    final LocationDetailsDTO dto = getRecord(timeStamp);
    locationDetails.setText(getLocationDetails(dto));
    frontImage.setImageBitmap(dto.getFrontImage());
    backImage.setImageBitmap(dto.getBackImage());
    shareButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        sendInformation(dto);
      }
    });
  }

  private LocationDetailsDTO getRecord(String timeStamp) {
    LocationDetailsDTO dto = new LocationDetailsDTO();
    SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(getBaseContext());
    dto = sqlLiteDbHelper.getRecord(timeStamp);
    return dto;
  }


}
