package com.example.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.coolweather.gson.Weather;
import com.example.coolweather.utils.HttpUtils;
import com.example.coolweather.utils.JsonUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 后台自动更新天气
 */
public class AutoUpdateService extends Service {

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int eightHour=8*60*60*1000;
        long targetAtTime= SystemClock.elapsedRealtime()+eightHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,targetAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器更新必应图片
     */
    private void updateBingPic() {

    }

    /**
     * 从服务器更新天气
     */
    private void updateWeather() {
        SharedPreferences prs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prs.getString("weather", null);
        if(weatherString!=null){
            Weather weather = JsonUtils.handlerWeatherResponse(weatherString);
            if(weather!=null){
                String weatherId = weather.basic.weatherId;
                String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=666db77849ad443fb81f94ac03b7ad42";
                HttpUtils.sendHttpRequest(weatherUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putBoolean("status",false);
                        editor.apply();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        Weather weather=JsonUtils.handlerWeatherResponse(responseText);
                        if(weather!=null&&weather.status.equals("ok")){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                        }

                    }
                });



            }
        }
    }


}
