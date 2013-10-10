package com.ehc.GeoFencingDemo;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 9/10/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoFencingDTO {

  private int serialNo;
  private String locationAddress;
  private String subLocality;
  private String localityName;
  private String countryName;
  private String latitude;
  private String longitude;
  private String timeStamp;
  private Bitmap front_image;
  private Bitmap back_image;

  public void populateFields(Cursor cursor) throws ParseException {
    if (cursor != null) {
      setLocationAddress(cursor.getString(0));
      setSubLocality(cursor.getString(1));
      setLocalityName(cursor.getString(2));
      setCountryName(cursor.getString(3));
      setLatitude(cursor.getString(4));
      setLongitude(cursor.getString(5));
      String dateString = cursor.getString(6);
//      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//      Date date = dateFormat.parse(dateString);
      Log.d("retrive:", "date retrived success");
      setTimeStamp(dateString);

      setFront_image(getBitmap(cursor, 7));
      Log.d("retrive:", "get first bitmap success");

      setBack_image(getBitmap(cursor, 8));
      Log.d("retrive:", "get second bitmap success");

    }
  }


  private Bitmap getBitmap(Cursor cursor, int index) {
    byte[] blob = cursor.getBlob(index);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 2;
    Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
    return bitmap;
  }

  public int getSerialNo() {
    return serialNo;
  }

  public void setSerialNo(int serialNo) {
    this.serialNo = serialNo;
  }

  public String getLocationAddress() {
    return locationAddress;
  }

  public void setLocationAddress(String locationAddress) {
    this.locationAddress = locationAddress;
  }

  public String getSubLocality() {
    return subLocality;
  }

  public void setSubLocality(String subLocality) {
    this.subLocality = subLocality;
  }

  public String getLocalityName() {
    return localityName;
  }

  public void setLocalityName(String localityName) {
    this.localityName = localityName;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  public Bitmap getFront_image() {
    return front_image;
  }

  public void setFront_image(Bitmap front_image) {
    this.front_image = front_image;
  }

  public Bitmap getBack_image() {
    return back_image;
  }

  public void setBack_image(Bitmap back_image) {
    this.back_image = back_image;
  }
}
