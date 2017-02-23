package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liwei on 2017/2/22.
 */

public class Forecast {

    /*"daily_forecast": [
    {
        "astro": {
                "mr": "02:14",
                "ms": "13:00",
                "sr": "06:32",
                "ss": "17:49"
    },
        "cond": {
                "code_d": "305",
                "code_n": "305",
                "txt_d": "小雨",
                "txt_n": "小雨"
    },
            "date": "2017-02-21",
            "hum": "76",
            "pcpn": "2.5",
            "pop": "93",
            "pres": "1025",
            "tmp": {
                "max": "11",
                "min": "5"
    },
            "uv": "5",
            "vis": "10",
            "wind": {
                "deg": "112",
                "dir": "东风",
                "sc": "4-5",
                "spd": "19"
    }
    },
    {
        "astro": {
                "mr": "03:04",
                "ms": "13:49",
                "sr": "06:31",
                "ss": "17:50"
    },
        "cond": {
                "code_d": "305",
                "code_n": "104",
                "txt_d": "小雨",
                "txt_n": "阴"
    },
            "date": "2017-02-22",
            "hum": "91",
            "pcpn": "15.2",
            "pop": "100",
            "pres": "1019",
            "tmp": {
                "max": "10",
                "min": "4"
    },
            "uv": "3",
            "vis": "7",
            "wind": {
                "deg": "308",
                "dir": "西北风",
                "sc": "4-5",
                "spd": "20"
    }
    },
    {
        "astro": {
                "mr": "03:53",
                "ms": "14:41",
                "sr": "06:30",
                "ss": "17:51"
    },
        "cond": {
                "code_d": "101",
                "code_n": "101",
                "txt_d": "多云",
                "txt_n": "多云"
    },
            "date": "2017-02-23",
            "hum": "74",
            "pcpn": "0.0",
            "pop": "0",
            "pres": "1027",
            "tmp": {
                "max": "8",
                "min": "3"
    },
            "uv": "5",
            "vis": "10",
            "wind": {
                "deg": "358",
                "dir": "北风",
                "sc": "3-4",
                "spd": "16"
    }
    }
    ]*/

    /**
     * 日期
     */
    public String date;

    /**
     * 最高和最低温度
     */
    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{
        public String max;
        public String min;
    }

    @SerializedName("cond")
    public More more;
    public class More{
        /**
         * 天气状况
         */
        @SerializedName("txt_d")
        public String info;
    }

    /**
     * 风向和风级别
     */
    public Wind wind;
    public class Wind{
        /**
         * 风向
         */
        @SerializedName("dir")
        public String  windDirection;

        /**
         * 风的级别
         */
        @SerializedName("sc")
        public String windLevel;

    }


}
