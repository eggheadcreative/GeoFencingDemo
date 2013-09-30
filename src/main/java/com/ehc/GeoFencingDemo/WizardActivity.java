package com.ehc.GeoFencingDemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class WizardActivity extends GeoFencingActivity {
  private final int REQUEST_CODE = 1;
  Bitmap picture = null;
  ImageView frontImage;
  Button submit;
  Button cancel;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.step_front_image);
    getWidgets();
    applyProperties();
    takePicture();
  }


  private void takePicture() {
    Intent frontCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    frontCameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
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
    frontImage = (ImageView) findViewById(R.id.front_image);
    submit = (Button) findViewById(R.id.step1_submit);
    cancel = (Button) findViewById(R.id.step1_cancel);
  }

  private void callSecondStep() {
    Intent secondStep = new Intent(this, SecondStep.class);
    Bundle bundle = new Bundle();
    bundle.putParcelable("frontImage", picture);
    secondStep.putExtras(bundle);
    startActivity(secondStep);
  }

  private void applyProperties() {
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        callSecondStep();
      }
    });
  }

}
