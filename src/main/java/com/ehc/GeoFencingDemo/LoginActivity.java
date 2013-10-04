package com.ehc.GeoFencingDemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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
    } else if (inputId.equals("12345")) {
      startDashboardIntent();
    } else {
      memberId.setError("Pls Enter valid Member Id");
    }
  }

  public void startDashboardIntent() {
    hideKeyboard();
    Intent homeIntent = new Intent(this, DashBoardActivity.class);
    startActivity(homeIntent);
  }

  private void displayKeyboard() {
    new Handler().postDelayed(new Runnable() {
      public void run() {
        InputMethodManager keyboard = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

      }

    }, 100);

  }

  private void hideKeyboard() {
    InputMethodManager keyboard = (InputMethodManager)
        getSystemService(Context.INPUT_METHOD_SERVICE);
    keyboard.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


  }


}
