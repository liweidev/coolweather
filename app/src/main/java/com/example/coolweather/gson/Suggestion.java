package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liwei on 2017/2/22.
 */

public class Suggestion {

    /*"suggestion": {
        "air": {
                    "brf": "中",
                    "txt": "气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"
        },
        "comf": {
                    "brf": "较舒适",
                    "txt": "白天有雨，风力较强，这种天气条件下，人们会感到有些凉意，但大部分人完全可以接受。"
        },
        "cw": {
                    "brf": "不宜",
                    "txt": "不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"
        },
        "drsg": {
                    "brf": "较冷",
                    "txt": "建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"
        },
        "flu": {
                    "brf": "易发",
                    "txt": "天冷风大且空气湿度大，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。"
        },
        "sport": {
                    "brf": "较不宜",
                    "txt": "有降水，且风力较强，推荐您在室内进行低强度运动；若坚持户外运动，请注意保暖并携带雨具。"
        },
        "trav": {
                    "brf": "适宜",
                    "txt": "有降水，虽然风稍大，但温度适宜，适宜旅游，可不要错过机会呦！"
        },
        "uv": {
                    "brf": "最弱",
                    "txt": "属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"
       }
    }*/

    /**
     * 空气质量
     */
    public AIR air;
    public class AIR{
        /**
         * 程度
         */
        @SerializedName("brf")
        public String level;

        /**
         * 具体信息
         */
        @SerializedName("txt")
        public String info;
    }

    /**
     * 舒适度
     */
    @SerializedName("comf")
    public Comfort comfort;
    public class Comfort{
        /**
         * 程度
         */
        @SerializedName("brf")
        public String level;

        /**
         * 具体信息
         */
        @SerializedName("txt")
        public String info;
    }

    /**
     * 洗车指数
     */
    @SerializedName("cw")
    public CarWash carWash;
    public class CarWash{
        /**
         * 程度
         */
        @SerializedName("brf")
        public String level;

        /**
         * 具体信息
         */
        @SerializedName("txt")
        public String info;
    }

    /**
     * 体感温度
     */
    @SerializedName("drsg")
    public Sendible sendible;
    public class Sendible{
        /**
         * 程度
         */
        @SerializedName("brf")
        public String level;

        /**
         * 具体信息
         */
        @SerializedName("txt")
        public String info;
    }

    /**
     * 健康度
     */
    @SerializedName("flu")
    public Health health;
    public class Health{
        /**
         * 程度
         */
        @SerializedName("brf")
        public String level;

        /**
         * 具体信息
         */
        @SerializedName("txt")
        public String info;
    }

    /**
     * 运动建议
     */
    public Sport sport;
    public class Sport{
        /**
         * 程度
         */
        @SerializedName("brf")
        public String level;

        /**
         * 具体信息
         */
        @SerializedName("txt")
        public String info;

    }

    /**
     * 旅游建议
     */
    @SerializedName("trav")
    public Travel travel;
    public class Travel{
        /**
         * 程度
         */
        @SerializedName("brf")
        public String level;

        /**
         * 具体信息
         */
        @SerializedName("txt")
        public String info;
    }

    /**
     * 紫外线强度
     */
    @SerializedName("uv")
    public Ultraviolet ultraviolet;
    public class Ultraviolet{
        /**
         * 程度
         */
        @SerializedName("brf")
        public String level;

        /**
         * 具体信息
         */
        @SerializedName("txt")
        public String info;
    }

}
