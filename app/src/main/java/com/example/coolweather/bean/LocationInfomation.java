package com.example.coolweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liwei on 2017/2/21.
 * 定位信息的实体类
 * 封装{定位时间、国家、省、市、区}
 */

public class LocationInfomation implements Parcelable{

    /**
     * 定位时间
     */
    private String locationTime;
    /**
     * 国家
     */
    private String countryName;
    /**
     * 省
     */
    private String provinceName;
    /**
     * 市
     */
    private String cityName;
    /**
     * 区
     */
    private String districtName;

    public void setLocationTime(String locationTime) {
        this.locationTime = locationTime;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getLocationTime() {

        return locationTime;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(locationTime);
        parcel.writeString(countryName);
        parcel.writeString(provinceName);
        parcel.writeString(cityName);
        parcel.writeString(districtName);
    }

    public static final Parcelable.Creator<LocationInfomation> CREATOR=
            new Parcelable.Creator<LocationInfomation>() {
        @Override
        public LocationInfomation createFromParcel(Parcel parcel) {
            LocationInfomation locationInfomation=new LocationInfomation();
            locationInfomation.locationTime=parcel.readString();
            locationInfomation.countryName=parcel.readString();
            locationInfomation.provinceName=parcel.readString();
            locationInfomation.cityName=parcel.readString();
            locationInfomation.districtName=parcel.readString();
            return locationInfomation;
        }

        @Override
        public LocationInfomation[] newArray(int i) {
            return new LocationInfomation[i];
        }
    };

}
