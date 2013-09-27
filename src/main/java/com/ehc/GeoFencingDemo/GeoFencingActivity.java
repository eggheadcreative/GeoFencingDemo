package com.ehc.GeoFencingDemo;

import android.app.Activity;
import android.os.Bundle;
import com.bugsnag.android.Bugsnag;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeoFencingActivity extends Activity {
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bugsnag.register(this, "7977d834798df3256fcfec6e847a6902");
    Bugsnag.setNotifyReleaseStages("production", "staging", "demo");
    Bugsnag.setReleaseStage(getResources().getString(R.string.environment_name));
  }


  @Override
  protected void onStart() {
    super.onStart();
    GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
    googleAnalytics.getTracker(getResources().getString(R.string.ga_trackingId));
    EasyTracker.getInstance().setContext(this);
    EasyTracker.getInstance().activityStart(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EasyTracker.getInstance().setContext(this);
    EasyTracker.getInstance().activityStop(this);
  }

}
