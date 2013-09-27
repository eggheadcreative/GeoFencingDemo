package com.ehc.GeoFencingDemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 27/9/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecondStep extends GeoFencingActivity {

  private final int REQUEST_CODE = 1;
  Bitmap picture = null;
  ImageView frontImage;
  Button submit;
  Button cancel;


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.step_back_image);
    getWidgets();
    applyProperties();
    takePicture();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private void takePicture() {
    Intent frontCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
    startActivityForResult(frontCameraIntent, REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      Bundle resultData = data.getExtras();
      picture = (Bitmap) resultData.get("data");
      if (picture != null) {
        frontImage.setImageDrawable(new BitmapDrawable(getResources(), picture));
      }
    }
  }

  private void getWidgets() {
    frontImage = (ImageView) findViewById(R.id.back_image);
    submit = (Button) findViewById(R.id.step2_submit);
    cancel = (Button) findViewById(R.id.step2_cancel);
  }

  private void callResultStep() {
    Intent resultStep = new Intent(this, ThirdStep.class);
//    Bundle bundle = getIntent().getExtras();
//    bundle.putParcelable("backImage", picture);
//    resultStep.putExtra("bundle", bundle);
    startActivity(resultStep);
  }

  private void applyProperties() {
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        callResultStep();
      }
    });
  }
}
