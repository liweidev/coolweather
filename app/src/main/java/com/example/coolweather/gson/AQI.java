package com.example.coolweather.gson;

/**
 * Created by liwei on 2017/2/22.
 */

public class AQI {
    /*"aqi": {
            "city": {
                    "aqi": "55",
                    "co": "1",
                    "no2": "41",
                    "o3": "65",
                    "pm10": "57",
                    "pm25": "30",
                    "qlty": "良",
                    "so2": "12"
                    }
    }*/

    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm10;
        public String pm25;
    }


}
