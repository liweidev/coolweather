package com.example.coolweather.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.liwei.coolweather.R;
import com.example.coolweather.utils.ActivityUtils;
import com.example.coolweather.utils.NetworkUtils;
import com.example.coolweather.utils.ToastUtils;

/**
 * Created by liwei on 2017/2/21.
 */

public class MyBaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.addActivity(this);
        checkNetworkState();
    }

    /**
     * 检查网络状态
     */
    private void checkNetworkState() {
        if(!NetworkUtils.isAvalibale()){
            ToastUtils.showToast(R.string.no_network);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
    }
}
