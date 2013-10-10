package com.ehc.GeoFencingDemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.gson.Gson;

import java.io.File;
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

  public LinkedList<String> getLocations() {
    LinkedList<String> locationsList = null;
    String locationsString = sharedPreferences.getString("savedLocations", "");
    if (!locationsString.equals("")) {
      locationsList = parseJson(locationsString);
    }
    return locationsList;
  }

  public void saveLocation(String location) {
    String locationString = sharedPreferences.getString("savedLocations", "");
    LinkedList<String> locationList = new LinkedList<String>();
    if (!locationString.equals("")) {
      locationList = parseJson(locationString);
      if (locationList.size() >= 5) {
        Iterator iterator = locationList.iterator();
        iterator.next();
        iterator.remove();
      }
    }
    locationList.add(location);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("savedLocations", new Gson().toJson(locationList));
    editor.commit();
  }


  private LinkedList parseJson(String json) {
    Gson gson = new Gson();
    String[] jsonArray = gson.fromJson(json, String[].class);
    LinkedList<String> jsonCollection = new LinkedList<String>();
    jsonCollection.addAll(Arrays.asList(jsonArray));
    return jsonCollection;
  }


}
