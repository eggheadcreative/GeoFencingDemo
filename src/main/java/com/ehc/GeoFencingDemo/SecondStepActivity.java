package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SecondStepActivity extends GeoFencingActivity {

  Bitmap picture = null;
  Button submit;
  Button cancel;
  Button takeSnap;
  RelativeLayout cameraView;
  private Camera camera;
  private CameraPreview cameraPreview;


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.capture_image);
    getWidgets();
    applyProperties();
    openCamera();
  }
  //  private void takePicture() {
//    Intent frontCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
//    startActivityForResult(frontCameraIntent, REQUEST_CODE);
//  }

//  @Override
//  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//    if (resultCode == RESULT_OK) {
//      Bundle resultData = data.getExtras();
//      if (picture != null) {
//        frontImage.setImageDrawable(new BitmapDrawable(getResources(), picture));
//      }
//    }
//  }

  private void getWidgets() {
    submit = (Button) findViewById(R.id.button_continue);

    cancel = (Button) findViewById(R.id.button_cancel);
    takeSnap = (Button) findViewById(R.id.take_snap);
    takeSnap.setText("Step 3: Capture Your Premises");
    submit.setText("Continue to Final Step");
    cameraView = (RelativeLayout) findViewById(R.id.camera_preview);
  }

  private void callResultStep() {
    Intent resultStep = new Intent(this, ThirdStepActivity.class);
    Bundle bundle = getIntent().getExtras();
    bundle.putParcelable("backImage", picture);
    resultStep.putExtras(bundle);
    startActivity(resultStep);
  }

  private void applyProperties() {
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        callResultStep();
      }
    });

    takeSnap.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        camera.takePicture(null, null, pictureCallback);
        submit.setVisibility(View.VISIBLE);
        takeSnap.setVisibility(View.GONE);
      }
    });
  }

  private void openCamera() {
    camera = getCameraInstance();
    cameraPreview = new CameraPreview(this, camera);
    cameraView.addView(cameraPreview);
  }

  public Camera getCameraInstance() {
    Camera camera = null;
    try {
      camera = Camera.open();
    } catch (Exception e) {
    }
    return camera;
  }

  private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
//      File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//      if (pictureFile == null) {
//        return;
//      }
//      try {
//        FileOutputStream fos = new FileOutputStream(pictureFile);
//        fos.write(data);
//        fos.close();
//      } catch (FileNotFoundException e) {
//      } catch (IOException e) {
//      }

      Matrix matrix = new Matrix();
      matrix.postRotate(90);
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 2;
      picture = BitmapFactory.decodeByteArray(data, 0, data.length, options);
      picture = Bitmap.createScaledBitmap(picture, 250, 250, true);
      picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(), picture.getHeight(), matrix, true);
    }
  };

//  private static File getOutputMediaFile(int type) {
//    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//        Environment.DIRECTORY_PICTURES), "MyCameraApp");
//    if (!mediaStorageDir.exists()) {
//      if (!mediaStorageDir.mkdirs()) {
//        return null;
//      }
//    }
//    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//    File mediaFile;
//    if (type == MEDIA_TYPE_IMAGE) {
//      mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//          "IMG_" + timeStamp + ".jpg");
//    } else {
//      return null;
//    }
//    return mediaFile;
//  }

  @Override
  protected void onPause() {
    super.onPause();
    releaseCamera();
  }

  private void releaseCamera() {
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    releaseCamera();
  }
}
