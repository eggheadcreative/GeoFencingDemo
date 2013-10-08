package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class FinalActivity extends GeoFencingActivity {

  private Button exit;
  private Button home;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.final_layout);
    getWidgets();
  }


  private void getWidgets() {
    exit = (Button) findViewById(R.id.exit);
    exit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
      }
    });

    home = (Button) findViewById(R.id.home);

    home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Intent homeIntent = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(homeIntent);
      }
    });
  }
}
