package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liwei on 2017/2/22.
 */

public class Basic {
   /* "basic": {
                "city": "苏州",
                "cnty": "中国",
                "id": "CN101190401",
                "lat": "31.309000",
                "lon": "120.612000",
                "update": {
                            "loc": "2017-02-21 14:52",
                            "utc": "2017-02-21 06:52"
                            }
    }*/

    @SerializedName("city")
    public String cityNmae;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }

}
