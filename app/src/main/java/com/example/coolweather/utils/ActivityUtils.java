package com.example.coolweather.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwei on 2017/2/21.
 */

public class ActivityUtils {

    public static List<Activity> activityList=new ArrayList<>();

    /**
     * 添加Activity
     * @param activity
     */
    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    /**
     * 移除一个Activity
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    /**
     * 移除所有Activity
     */
    public static void removeAllActivity(){
        for(Activity activity:activityList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }

    }
}
