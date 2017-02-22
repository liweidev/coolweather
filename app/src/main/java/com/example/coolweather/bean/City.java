package com.example.coolweather.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by liwei on 2017/2/22.
 * 市
 */

public class City extends DataSupport{

    private int id;
    private int cityCode;
    private int provinceId;
    private String cityName;

    public void setId(int id) {
        this.id = id;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {

        return id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceId() {

        return provinceId;
    }

    public String getCityName() {
        return cityName;
    }
}
