package com.example.GeoFencingDemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends GeoFencingActivity implements View.OnClickListener {

  private EditText memberId;
  private Button signIn;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    getWidgets();
    displayKeyboard();
  }

  private void getWidgets() {
    memberId = (EditText) findViewById(R.id.input_memberid);
    signIn = (Button) findViewById(R.id.login);
    signIn.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    String inputId = memberId.getText().toString();
    if (inputId.equals("")) {
      memberId.setError("Pls Enter Member Id");
    } else {
      startHomeIntent();
    }
  }

  public void startHomeIntent() {
    Intent homeIntent = new Intent(this, HomeActivity.class);
    startActivity(homeIntent);
  }

  private void displayKeyboard() {
    InputMethodManager keyboard = (InputMethodManager)
        getSystemService(Context.INPUT_METHOD_SERVICE);
    keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
  }

}
