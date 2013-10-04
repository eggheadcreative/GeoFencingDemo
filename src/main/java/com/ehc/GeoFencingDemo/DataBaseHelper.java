package com.ehc.GeoFencingDemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 26/9/13
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataBaseHelper {

  private static Context context;
  public static SharedPreferences sharedPreferences;

  DataBaseHelper(Context context) {
    this.context = context;
    sharedPreferences = context.getSharedPreferences("locations", Context.MODE_PRIVATE);
  }

  public void saveLocation(String location) {
    Set<String> locationSet = sharedPreferences.getStringSet("savedLocations", new LinkedHashSet<String>());
    Log.d("db:", locationSet.toString());
    if (locationSet.size() > 5) {
      Iterator iterator = locationSet.iterator();
      iterator.next();
      iterator.remove();
    }
    locationSet.add(location);
    Log.d("db:", locationSet.toString());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putStringSet("savedLocations", locationSet);
    editor.commit();
  }

  public Set<String> getLocations() {

    Set locationSet = sharedPreferences.getStringSet("savedLocations", new LinkedHashSet<String>());
    return locationSet;

  }
}
