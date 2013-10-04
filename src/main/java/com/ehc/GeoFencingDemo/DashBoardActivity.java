package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 4/10/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashBoardActivity extends GeoFencingActivity implements View.OnClickListener {
  private TextView savedData;
  private Button startButton;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dashboard);
    getWidgets();
    displayExistingData();
  }

  private void displayExistingData() {

    DataBaseHelper dbHelper = new DataBaseHelper(getBaseContext());
    Set savedLocations = dbHelper.getLocations();

    if (savedLocations != null && savedLocations.size() != 0) {
      StringBuilder data = new StringBuilder();
      Iterator iterator = savedLocations.iterator();
      int i = 1;
      while (iterator.hasNext()) {
        data = data.append("\n" + i + ". " + iterator.next().toString());
      }

      savedData.setText(data);
    } else {
      savedData.setText("No existing information");
    }


  }


  private void getWidgets() {
    savedData = (TextView) findViewById(R.id.saved_data);
    startButton = (Button) findViewById(R.id.start_tracking);
    startButton.setOnClickListener(this);

  }

  private void startHomeIntent() {
    Intent homeIntent = new Intent(this, HomeActivity.class);
    startActivity(homeIntent);
  }


  @Override
  public void onClick(View view) {
    startHomeIntent();
  }
}
