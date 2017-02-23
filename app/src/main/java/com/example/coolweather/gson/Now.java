package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liwei on 2017/2/22.
 */

public class Now {
    /*"now": {
        "cond": {
                    "code": "104",
                    "txt": "阴"
                },
                "fl": "5",
                "hum": "61",
                "pcpn": "0",
                "pres": "1028",
                "tmp": "8",
                "vis": "8",
                "wind": {
                    "deg": "99",
                    "dir": "东风",
                    "sc": "5-6",
                    "spd": "33"
                        }
    }*/
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        /**
         * 阴、晴等...
         */
        @SerializedName("txt")
        public String info;

        public String code;

    }

    public Wind wind;

    public class Wind{
        /**
         * 风向
         */
        @SerializedName("dir")
        public String  windDirection;
        /**
         * 风等级
         */
        @SerializedName("sc")
        public String windLevel;

    }






}
