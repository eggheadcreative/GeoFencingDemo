package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HomeActivity extends GeoFencingActivity implements View.OnClickListener {
  private TextView savedData;
  private Button startButton;
  private ListView listView;
  SqlLiteDbHelper sqlLiteDbHelper;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_layout);
    sqlLiteDbHelper = new SqlLiteDbHelper(getBaseContext());
    getWidgets();
    displayExistingData();
  }

  private void displayExistingData() {
    LinkedList<String> savedLocations = new LinkedList<>();
    List<GeoFencingDTO> geoFencingDTOList = new LinkedList<GeoFencingDTO>();
    geoFencingDTOList = sqlLiteDbHelper.getRecords();
    if (geoFencingDTOList != null && geoFencingDTOList.size() > 0) {
      Iterator iterator = geoFencingDTOList.iterator();
      while (iterator.hasNext()) {
        GeoFencingDTO geoFencingDTO = (GeoFencingDTO) iterator.next();
        savedLocations.add(geoFencingDTO.getSubLocality() + ", " + geoFencingDTO.getTimeStamp());

        if (savedLocations != null && savedLocations.size() != 0) {
          ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, savedLocations);
          listView.setAdapter(adapter);
          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              String text = (String) adapterView.getItemAtPosition(i);
              callLocationDetailsActivity(text);
            }
          });
        }
      }
    } else {
      savedData.setVisibility(View.VISIBLE);
      savedData.append("No Previous Captures");
    }

  }

  private void callLocationDetailsActivity(String timeStamp) {
    String[] content = timeStamp.split(",");
    Intent intent = new Intent(getBaseContext(), LocationDetailsActivity.class);
    intent.putExtra("timeStamp", content[1].trim());
    startActivity(intent);
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
