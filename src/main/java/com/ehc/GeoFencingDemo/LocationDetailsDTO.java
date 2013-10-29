package com.ehc.GeoFencingDemo;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;

import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 9/10/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationDetailsDTO {

  private int serialNo;
  private String locationAddress;
  private String subLocality;
  private String localityName;
  private String countryName;
  private String latitude;
  private String longitude;
  private String timeStamp;
  private Bitmap frontImage;
  private Bitmap backImage;

  public void populateFields(Cursor cursor) throws ParseException {
    if (cursor != null) {
      setLocationAddress(cursor.getString(0));
      setSubLocality(cursor.getString(1));
      setLocalityName(cursor.getString(2));
      setCountryName(cursor.getString(3));
      setLatitude(cursor.getString(4));
      setLongitude(cursor.getString(5));
      String dateString = cursor.getString(6);
//      if (dateString.contains("GMT+05:30")) ;
//      dateString = dateString.replace("GMT+05:30", "");
      setTimeStamp(dateString);
      setFrontImage(getBitmap(cursor, 7));
      setBackImage(getBitmap(cursor, 8));
    }
  }


  public void populateFields(Address address,Bitmap frontPicture,Bitmap backPicture)
  {
    setLocationAddress(address.getAddressLine(0));
    setSubLocality(address.getSubLocality());
    setLocalityName(address.getLocality());
    setCountryName(address.getCountryName());
    setLatitude(String.valueOf(address.getLatitude()));
    setLongitude(String.valueOf(address.getLongitude()));
    setFrontImage(frontPicture);
    setBackImage(backPicture);

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

  public Bitmap getFrontImage() {
    return frontImage;
  }

  public void setFrontImage(Bitmap frontImage) {
    this.frontImage = frontImage;
  }

  public Bitmap getBackImage() {
    return backImage;
  }

  public void setBackImage(Bitmap backImage) {
    this.backImage = backImage;
  }
}
