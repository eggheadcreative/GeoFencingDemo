package com.ehc.GeoFencingDemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.location.Address;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

  private Context context;
  private SQLiteDatabase database;
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "GeoFencingDemo.db";


  public SqlLiteDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
    database = getWritableDatabase();
  }

  public void onCreate(SQLiteDatabase db) {
    String CREATE_TABLE = "create table geo_fencing(location_address varchar2(20),location_sublocality varchar2(20)," +
        "location_locality varchar2(20),location_country varchar2(20),location_latitude varchar2(20),location_longitude varchar2(20)," +
        "location_timestamp date,front_image BLOB,back_image BLOB)";
    db.execSQL(CREATE_TABLE);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }


//  public void checkDatabase() {
//    File database = context.getDatabasePath("GeoFencingDemo.db");
//    if (!database.exists()) {
//      Log.d("database:", "not Exist");
//      getDataBase();
//    }
//  }
//
//  public SQLiteDatabase getDataBase() {
//    database = this.getWritableDatabase();
//    return database;
//  }

  public void insertRecord(Address address, Bitmap frontImage, Bitmap backImage) {
    try {
      ContentValues values = new ContentValues();
      values.put("location_address", address.getAddressLine(0));
      values.put("location_sublocality", address.getSubLocality());
      values.put("location_locality", address.getLocality());
      values.put("location_country", address.getCountryName());
      values.put("location_latitude", address.getLatitude());
      values.put("location_longitude", address.getLongitude());
      values.put("location_timestamp", new java.sql.Date(new Date().getTime()) + "");
      values.put("front_image", getBitmapAsByteArray(frontImage));
      values.put("back_image", getBitmapAsByteArray(backImage));
      database.insert("geo_fencing", null, values);
      Log.d("record Inserted:", "success");
    } catch (Exception e) {
      e.printStackTrace();
      Log.d("record Inserted:", "failed");
    }
  }

  public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);

    Log.d("image size:", "" + outputStream.size());
    return outputStream.toByteArray();
  }


  public LinkedList<GeoFencingDTO> getRecords() {
    try {
      LinkedList<GeoFencingDTO> locations = new LinkedList<>();
      Cursor dbCursor = database.rawQuery("select * from geo_fencing", null);
      Log.d("retrive:", "query success");

      while (dbCursor.moveToNext() && !dbCursor.isAfterLast()) {
        GeoFencingDTO dto = new GeoFencingDTO();
        dto.populateFields(dbCursor);
        Log.d("retrive:", "populate success");
        locations.add(dto);
        Log.d("retrive:", "added success");
      }
      dbCursor.close();
      Log.d("retrive:", "success");
      return locations;
    } catch (Exception e) {
      e.printStackTrace();
      Log.d("retrive:", "fail");
      return null;
    }
  }

  public void deleteRecord() {

  }


}



