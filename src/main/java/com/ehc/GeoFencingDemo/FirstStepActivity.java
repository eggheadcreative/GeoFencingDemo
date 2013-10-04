package com.ehc.GeoFencingDemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class FirstStepActivity extends GeoFencingActivity {
  ImageView frontImage;
  Button submit;
  Button cancel;
  Button takeSnap;
  FrameLayout cameraView;
  private Camera camera;
  private CameraPreview cameraPreview;
  Bitmap bitmapPicture;


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.step_front_image);
    getWidgets();
    applyProperties();
  }

  @Override
  protected void onResume() {
    super.onResume();
    openCamera();
  }

  private void getWidgets() {
    frontImage = (ImageView) findViewById(R.id.front_image);
    submit = (Button) findViewById(R.id.step1_submit_continue);
    cancel = (Button) findViewById(R.id.step1_cancel);
    takeSnap = (Button) findViewById(R.id.step1_take_snap);
    cameraView = (FrameLayout) findViewById(R.id.front_camera_preview);
  }

  private void callSecondStep() {
    releaseCamera();
    Intent secondStep = new Intent(this, SecondStepActivity.class);
    Bundle bundle = getIntent().getExtras();
    bundle.putParcelable("frontImage", bitmapPicture);
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
      int frontCam = getFrontCamId();
      if (frontCam > 0)
        camera = Camera.open(getFrontCamId());
      else
        camera = Camera.open();
    } catch (Exception e) {
    }
    return camera;
  }

  private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

      //TODO: if we want to store image in gallary un-comment below code
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
      matrix.postRotate(-90);
      bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
      bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture, 250, 250, true);
      bitmapPicture = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), matrix, true);

    }
  };


  //TODO: if we stored image un-comment below code for retrive image

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

  private int getFrontCamId() {
    int cameraCount = 0;
    int frontCam = -1;
    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    cameraCount = Camera.getNumberOfCameras();
    for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
      Camera.getCameraInfo(camIdx, cameraInfo);
      if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        frontCam = camIdx;
      }
    }
    return frontCam;
  }

}

