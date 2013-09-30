package com.ehc.GeoFencingDemo;


import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.*;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
  private SurfaceHolder mHolder;
  private Camera mCamera;
  private Context context;

  public CameraPreview(Context context, Camera camera) {
    super(context);
    this.context = context;
    mCamera = camera;
    // Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destroyed.
    mHolder = getHolder();
    mHolder.addCallback(this);
    // deprecated setting, but required on Android versions prior to 3.0
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
  }

  public void surfaceCreated(SurfaceHolder holder) {
    // The Surface has been created, now tell the camera where to draw the preview.
    try {
      mCamera.setPreviewDisplay(holder);
      mCamera.startPreview();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    // empty. Take care of releasing the Camera preview in your activity.
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    // If your preview can change or rotate, take care of those events here.
    // Make sure to stop the preview before resizing or reformatting it.


    Camera.Parameters parameters = mCamera.getParameters();
    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

    if (display.getRotation() == Surface.ROTATION_0) {
      parameters.setPreviewSize(height, width);
      mCamera.setDisplayOrientation(90);
    }

    if (display.getRotation() == Surface.ROTATION_90) {
      parameters.setPreviewSize(width, height);
    }

    if (display.getRotation() == Surface.ROTATION_180) {
      parameters.setPreviewSize(height, width);
    }

    if (display.getRotation() == Surface.ROTATION_270) {
      parameters.setPreviewSize(width, height);
      mCamera.setDisplayOrientation(180);
    }
// Todo will come back soon
//    mCamera.setParameters(parameters);
    if (mHolder.getSurface() == null) {
      return;
    }
    try {
      mCamera.stopPreview();
    } catch (Exception e) {
    }
    try {
      mCamera.setPreviewDisplay(mHolder);
      mCamera.startPreview();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
