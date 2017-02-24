package com.example.coolweather.lisenter;

import android.content.Intent;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.coolweather.app.MyApplication;
import com.example.coolweather.bean.County;
import com.example.coolweather.constant.Constant;
import com.example.coolweather.utils.LogUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by liwei on 2017/2/21.
 * 获取地理位置监听器
 */

public class MyLocationListener implements BDLocationListener{
    @Override
    public void onReceiveLocation(BDLocation location) {

        /*//获取定位结果
        StringBuffer sb = new StringBuffer(256);

        sb.append("time : ");
        sb.append(location.getTime());    //获取定位时间

        sb.append("\nerror code : ");
        sb.append(location.getLocType());    //获取类型类型

        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());    //获取纬度信息

        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());    //获取经度信息

        sb.append("\nradius : ");
        sb.append(location.getRadius());    //获取定位精准度

        if (location.getLocType() == BDLocation.TypeGpsLocation){

            // GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());    // 单位：公里每小时

            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());    //获取卫星数

            sb.append("\nheight : ");
            sb.append(location.getAltitude());    //获取海拔高度信息，单位米

            sb.append("\ndirection : ");
            sb.append(location.getDirection());    //获取方向信息，单位度

            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

            // 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\noperationers : ");
            sb.append(location.getOperators());    //获取运营商信息

            sb.append("\ndescribe : ");
            sb.append("网络定位成功");

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");

        } else if (location.getLocType() == BDLocation.TypeServerError) {

            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());    //位置语义化信息

        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }*/
        //LogUtils.d("BaiduLocationApiDem", sb.toString());

        StringBuilder sb=new StringBuilder(256);
        if(location!=null){
            //定位时间
            String locationTime = location.getTime();
            //定位国家
            String countryName=location.getCountry();
            //定位省
            String provinceName = location.getProvince();
            provinceName=provinceName.substring(0,provinceName.length()-1);
            //定位市
            String cityName=location.getCity();
            cityName=cityName.substring(0,cityName.length()-1);
            //定位区
            String districtName = location.getDistrict();
            districtName=districtName.substring(0,districtName.length()-1);
            sb.append(locationTime+"\n"+countryName+"\n"+provinceName+"\n"+cityName+"\n"+districtName);
            LogUtils.d("BaiduLocationTest",sb.toString());
            //ToastUtils.showToast(sb.toString());

            /*//查询省代码
            List<Province> provinceList = DataSupport.where("provinceName=?", provinceName).find(Province.class);
            if(provinceList.size()>0){
                Province province = provinceList.get(0);
                int provinceCode = province.getProvinceCode();
                LogUtils.d("BaiduLocationTest","provinceCode="+provinceCode);
            }

            //查询市代码
            List<City> cityList = DataSupport.where("cityName=?", cityName).find(City.class);
            if(cityList.size()>0){
                int cityCode = cityList.get(0).getCityCode();
                LogUtils.d("BaiduLocationTest","cityCode="+cityCode);
            }*/
            //查询区代码
            List<County> countyList = DataSupport.where("countyName=?", districtName).find(County.class);
            if(countyList.size()>0){
                String weatherId = countyList.get(0).getWeatherId();
                LogUtils.d("BaiduLocationTest","weatherId="+weatherId);
                if(!TextUtils.isEmpty(weatherId)){
                    //TODO 发送广播
                    Intent intent=new Intent(Constant.LOCATION_SUCCEE_BROADCASTRECEIVER);
                    intent.putExtra("weatherId",weatherId);
                    intent.putExtra("countyName",districtName);
                    MyApplication.getContext().sendBroadcast(intent);
                }
            }


        }


    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
