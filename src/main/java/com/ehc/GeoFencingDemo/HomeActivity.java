package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 4/10/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomeActivity extends GeoFencingActivity implements View.OnClickListener {
  private TextView savedData;
  private Button startButton;
  private ListView listView;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_layout);

    getWidgets();
    displayExistingData();
  }

  private void displayExistingData() {
    DataBaseHelper dbHelper = new DataBaseHelper(getBaseContext());
    LinkedList<String> savedLocations = dbHelper.getLocations();
    if (savedLocations != null && savedLocations.size() != 0) {
      ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, savedLocations);
      listView.setAdapter(adapter);
    } else {
      savedData.setVisibility(View.VISIBLE);
      savedData.append("No Previous Captures");
    }
  }


  private void getWidgets() {
    savedData = (TextView) findViewById(R.id.saved_data);
    savedData.setLineSpacing(1.5f, 1.5f);
    listView = (ListView) findViewById(R.id.list_container);
    startButton = (Button) findViewById(R.id.start_tracking);
    startButton.setOnClickListener(this);
  }

  private void startLocationIntent() {
    Intent locationIntent = new Intent(this, LocationActivity.class);
    startActivity(locationIntent);
  }


  @Override
  public void onClick(View view) {
    startLocationIntent();
  }


  @Override
  public void onBackPressed() {
    super.onBackPressed();
    this.finish();
  }
}
