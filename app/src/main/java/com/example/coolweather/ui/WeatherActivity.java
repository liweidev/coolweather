package com.example.coolweather.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coolweather.R;
import com.example.coolweather.app.MyApplication;
import com.example.coolweather.base.MyBaseActivity;
import com.example.coolweather.constant.Constant;
import com.example.coolweather.fragment.ChooseAreaFragment;
import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.lisenter.ImagePathLisenter;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.utils.ActivityUtils;
import com.example.coolweather.utils.HttpUtils;
import com.example.coolweather.utils.JsonUtils;
import com.example.coolweather.utils.LogUtils;
import com.example.coolweather.utils.NotificationUtils;
import com.example.coolweather.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherActivity extends MyBaseActivity {

    @BindView(R.id.title_city)
    TextView titleCity;
    @BindView(R.id.title_update_time)
    TextView titleUpdateTime;
    /**
     * 当天温度
     */
    @BindView(R.id.degree_text)
    TextView degreeText;
    /**
     * 天气状况
     */
    @BindView(R.id.weather_info_text)
    TextView weatherInfoText;
    @BindView(R.id.wind_level_text)
    TextView windLevelText;
    @BindView(R.id.forcast_layout)
    LinearLayout forcastLayout;
    @BindView(R.id.aqi_text)
    TextView aqiText;
    @BindView(R.id.pm10_text)
    TextView pm10Text;
    @BindView(R.id.pm25_text)
    TextView pm25Text;
    /**
     * 舒适度
     */
    @BindView(R.id.comfort_text)
    TextView comfortText;
    @BindView(R.id.air_text)
    TextView airText;
    @BindView(R.id.car_wash_text)
    TextView carWashText;
    /**
     * 体感温度
     */
    @BindView(R.id.sendible_text)
    TextView sendibleText;
    @BindView(R.id.health_text)
    TextView healthText;
    @BindView(R.id.sport_text)
    TextView sportText;
    @BindView(R.id.travel_text)
    TextView travelText;
    @BindView(R.id.ultraviolet_text)
    TextView ultravioletText;
    @BindView(R.id.weather_layout)
    ScrollView weatherLayout;
    @BindView(R.id.bing_pic_img)
    ImageView bingPicImg;
    @BindView(R.id.image_code)
    ImageView imageCode;
    @BindView(R.id.swipe_refresh)
    public SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nav_button)
    Button navButton;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    private String weatherId;
    LocationSucceeBroadcastReceiver locationSucceeBroadcastReceiver;
    /**
     * 头像
     */
    private CircleImageView avarta;
    /**
     * 登录状态
     */
    private Button loginState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        //初始化sharesdk
        ShareSDK.initSDK(this);
        //初始化短信验证码
        SMSSDK.initSDK(this, "1bb63ed9d8400", "71e61ef9186c11261c0898663667bd58");
        registeReceiver();
        initStateBar();
        initViews();
        SharedPreferences prs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherContent = prs.getString("weather", null);
        if (weatherContent != null) {
            Weather weather = JsonUtils.handlerWeatherResponse(weatherContent);
            weatherId = weather.basic.weatherId;
            if (weather != null) {
                showWeatherInfo(weather);
            }
        } else {
            //无缓存时,去服务器查询
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        String bingPic = prs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }

    /**
     * 注册广播监听器
     */
    private void registeReceiver() {
        locationSucceeBroadcastReceiver = new LocationSucceeBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.LOCATION_SUCCEE_BROADCASTRECEIVER);
        registerReceiver(locationSucceeBroadcastReceiver, filter);
    }

    /**
     * 初始化Views
     */
    private void initViews() {

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(() -> {
            requestWeather(weatherId);
        });

        navButton.setOnClickListener((v) -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        //navigationView.setCheckedItem(R.id.hand_update);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.hand_update:
                        //ToastUtils.showToast("手动更新");
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction bt = manager.beginTransaction();
                        bt.replace(R.id.navigation_view,new ChooseAreaFragment());
                        bt.addToBackStack(null);
                        bt.commit();
                        break;

                    case R.id.shard_app:
                        showShare();
                        break;

                    case R.id.city_friends:
                        ToastUtils.showToast("同城约吧");
                        break;

                    case R.id.with_us:
                        ToastUtils.showToast("关于我们");
                        break;


                }
                return true;
            }
        });

        View headView = navigationView.getHeaderView(0);
        avarta= (CircleImageView) headView.findViewById(R.id.civ_head);
        avarta.setOnClickListener((v)->{
            ToastUtils.showToast("点击了头像");
        });
        loginState=(Button)headView.findViewById(R.id.login_state);
        loginState.setOnClickListener((v)->{
            showRegist();
        });
    }

    /**
     * 打开短信验证注册界面
     */
    private void showRegist() {
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");

                    // 提交用户信息（此方法可以不调用）
                    //registerUser(country, phone);
                    LogUtils.d("phone",phone);
                }
            }
        });
        registerPage.show(MyApplication.getContext());
    }

    /**
     * 初始化系统状态栏
     * 活动布局与状态栏融合
     */
    private void initStateBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    ToastUtils.showToast(R.string.load_bing_error);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(() -> {
                    Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                });
            }
        });


    }

    /**
     * 从服务器请求天气数据
     *
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        this.weatherId = weatherId;
        String weatherUrl = "http://guolin.tech/api/weather?cityid="
                + weatherId + "&key=666db77849ad443fb81f94ac03b7ad42";
        HttpUtils.sendHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    ToastUtils.showToast(R.string.load_weather_error);
                    swipeRefresh.setRefreshing(false);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Weather weather = JsonUtils.handlerWeatherResponse(responseText);
                runOnUiThread(() -> {
                    if (weather != null && weather.status.equals("ok")) {
                        SharedPreferences prs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                        SharedPreferences.Editor editor = prs.edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                        showWeatherInfo(weather);
                    } else {
                        ToastUtils.showToast(R.string.load_weather_error);
                    }
                    swipeRefresh.setRefreshing(false);
                });
            }
        });
    }

    /**
     * 处理并展示weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityNmae;
        String updateTime = weather.basic.update.updateTime;
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        String windLevel = weather.now.wind.windDirection + "   " + weather.now.wind.windLevel + "级";
        windLevelText.setText(windLevel);
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forcastLayout.removeAllViews();
        String imageCodeUrl = "http://files.heweather.com/cond_icon/" + weather.now.more.code + ".png";
        Glide.with(this).load(imageCodeUrl).into(imageCode);
        LogUtils.d("imageCodeUrl", imageCodeUrl);
        for (Forecast forecast : weather.froecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forcastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forcastLayout.addView(view);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm10Text.setText(weather.aqi.city.pm10);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        airText.setText("空气质量:" + weather.suggestion.air.level + "\n" + weather.suggestion.air.info);
        sendibleText.setText("体感温度:" + weather.suggestion.sendible.level + "\n" + weather.suggestion.sendible.info);
        healthText.setText("健康指数:" + weather.suggestion.health.level + "\n" + weather.suggestion.health.info);
        comfortText.setText("舒适度:" + weather.suggestion.comfort.level + "\n" + weather.suggestion.comfort.info);
        sportText.setText("运动建议:" + weather.suggestion.sport.level + "\n" + weather.suggestion.sport.info);
        travelText.setText("旅游建议:" + weather.suggestion.travel.level + "\n" + weather.suggestion.travel.info);
        carWashText.setText("洗车指数:" + weather.suggestion.carWash.level + "\n" + weather.suggestion.carWash.info);
        ultravioletText.setText("紫外线强度:" + weather.suggestion.ultraviolet.level + "\n" + weather.suggestion.ultraviolet.info);
        weatherLayout.setVisibility(View.VISIBLE);
        if (weather != null && weather.status.equals("ok")) {
            final Intent intent = new Intent(this, AutoUpdateService.class);
            intent.putExtra("cityName", cityName);//大兴
            intent.putExtra("weatherInfo", weatherInfo);//晴
            intent.putExtra("degree", degree);//8℃
            intent.putExtra("windLevel", windLevel);//西南风  4-5级


            NotificationUtils.getImagePathFromCatch(imageCodeUrl, new ImagePathLisenter() {
                @Override
                public void sendImagePath(String imagepath) {
                    runOnUiThread(() -> {
                        intent.putExtra("imageCodeUrl", imagepath);//图片地址
                        startService(intent);
                    });
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeAllActivity();
        if (locationSucceeBroadcastReceiver != null) {
            unregisterReceiver(locationSucceeBroadcastReceiver);
        }
    }

    /**
     * 定位成功自动获取位置更新天气信息
     */
    class LocationSucceeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String locationWeatherId = intent.getStringExtra("weatherId");
            String countyName = intent.getStringExtra("countyName");
            if (!TextUtils.isEmpty(locationWeatherId)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WeatherActivity.this);
                dialog.setTitle("定位成功");
                dialog.setMessage("是否获取当前位置天气-" + countyName);
                dialog.setCancelable(false);
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        swipeRefresh.setRefreshing(true);
                        requestWeather(locationWeatherId);
                    }
                });
                dialog.show();

            }
        }
    }

    /**
     * 显示分享界面
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("酷欧天气");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("https://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("当你真正使用酷欧天气软件时，你发现它不止是一款天气软件！");
        try{
            File file=new File("/sdcard/test.jpg");
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileOutputStream outputStream=new FileOutputStream(file);
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

}
