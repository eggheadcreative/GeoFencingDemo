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
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

  private Context context;
  private SQLiteDatabase database;
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "GeoFencingDemo.db";
  public static final String TABLE_NAME = "geo_fencing";


  public SqlLiteDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
    database = getWritableDatabase();
  }

  public void onCreate(SQLiteDatabase db) {
    String CREATE_TABLE = "create table " + TABLE_NAME + "(location_address varchar2(20),location_sublocality varchar2(20)," +
        "location_locality varchar2(20),location_country varchar2(20),location_latitude varchar2(20),location_longitude varchar2(20)," +
        "location_timestamp date,front_image BLOB,back_image BLOB)";
    db.execSQL(CREATE_TABLE);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }

  public void insertRecord(Address address, Bitmap frontImage, Bitmap backImage) {
    try {
      deleteIfMoreRecords();
      ContentValues values = new ContentValues();
      values.put("location_address", address.getAddressLine(0));
      values.put("location_sublocality", address.getSubLocality());
      values.put("location_locality", address.getLocality());
      values.put("location_country", address.getCountryName());
      values.put("location_latitude", address.getLatitude());
      values.put("location_longitude", address.getLongitude());
      values.put("location_timestamp", String.valueOf(new Date()).replace("GMT+05:30", ""));
      values.put("front_image", getBitmapAsByteArray(frontImage));
      values.put("back_image", getBitmapAsByteArray(backImage));
      database.insert("geo_fencing", null, values);
      Log.d("record Inserted:", "success");
    } catch (Exception e) {
      e.printStackTrace();
      Log.d("record Inserted:", "failed");
    }
  }

  private void deleteIfMoreRecords() {
    Cursor dbCursor = database.rawQuery("select count(*) from " + TABLE_NAME, null);
    if (dbCursor.moveToNext() && !dbCursor.isAfterLast()) {
      int count = dbCursor.getInt(0);
      if (count >= 5) {
        deleteFirstRecord();
      }
    }
  }

  public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);

    Log.d("image size:", "" + outputStream.size());
    return outputStream.toByteArray();
  }


  public LinkedList<LocationDetailsDTO> getRecords() {
    try {
      LinkedList<LocationDetailsDTO> locations = new LinkedList<>();
      Cursor dbCursor = database.rawQuery("select * from " + TABLE_NAME, null);
      while (dbCursor.moveToNext() && !dbCursor.isAfterLast()) {
        LocationDetailsDTO dto = new LocationDetailsDTO();
        dto.populateFields(dbCursor);
        locations.add(dto);
      }
      dbCursor.close();
      return locations;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  public LocationDetailsDTO getRecord(String timeStamp) {
    LocationDetailsDTO dto = new LocationDetailsDTO();
    Cursor dbCursor = database.rawQuery("select * from " + TABLE_NAME + " where location_timestamp='" + timeStamp + "'", null);
    if (dbCursor.moveToNext() && !dbCursor.isAfterLast()) {
      try {
        dto.populateFields(dbCursor);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return dto;

  }

  public void deleteFirstRecord() {
    database.execSQL("delete from " + TABLE_NAME + " where rowid=(select min(rowid) from " + TABLE_NAME + ")");
  }

}



