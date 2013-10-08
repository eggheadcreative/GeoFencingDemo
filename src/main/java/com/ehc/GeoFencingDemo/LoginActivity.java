package com.ehc.GeoFencingDemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends GeoFencingActivity {

  private EditText memberId;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    getWidgets();
    displayKeyboard();
  }

  private void getWidgets() {
    memberId = (EditText) findViewById(R.id.input_memberid);

    memberId.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        //To change body of implemented methods use File | Settings | File Templates.


      }

      @Override
      public void afterTextChanged(Editable editable) {
        String inputId = memberId.getText().toString();
        if (inputId.length() >= 5 && !inputId.equals("12345")) {
          memberId.setError("Please Enter A Valid PIN Number");
        } else if (inputId.equals("12345")) {
          startDashboardIntent();
        }
      }
    });
  }


  public void startDashboardIntent() {
    hideKeyboard();
    Intent homeIntent = new Intent(this, HomeActivity.class);
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
