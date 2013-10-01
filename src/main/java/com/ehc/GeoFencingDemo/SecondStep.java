package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SecondStep extends GeoFencingActivity {

  private final int REQUEST_CODE = 1;
  Bitmap picture = null;
  ImageView frontImage;
  Button submit;
  Button cancel;
  Button takeSnap;
  CameraPreview cameraPreview;
  Camera camera;
  FrameLayout cameraView;


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.step_back_image);
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
    frontImage = (ImageView) findViewById(R.id.back_image);
    submit = (Button) findViewById(R.id.step2_submit);
    cancel = (Button) findViewById(R.id.step2_cancel);
    takeSnap = (Button) findViewById(R.id.step2_take_snap);
    cameraView = (FrameLayout) findViewById(R.id.back_camera_preview);
  }

  private void callResultStep() {
    Intent resultStep = new Intent(this, ThirdStep.class);
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
      picture = BitmapFactory.decodeByteArray(data, 0, data.length);
      picture = Bitmap.createScaledBitmap(picture, 250, 320, true);

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
