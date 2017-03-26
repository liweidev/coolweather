package com.example.coolweather.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.liwei.coolweather.R;
import com.example.coolweather.adapter.ChooseAreaAdapter;
import com.example.coolweather.bean.City;
import com.example.coolweather.bean.County;
import com.example.coolweather.bean.Province;
import com.example.coolweather.ui.MainActivity;
import com.example.coolweather.ui.WeatherActivity;
import com.example.coolweather.utils.HttpUtils;
import com.example.coolweather.utils.JsonUtils;
import com.example.coolweather.utils.LogUtils;
import com.example.coolweather.utils.NetworkUtils;
import com.example.coolweather.utils.ToastUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by liwei on 2017/2/22.
 * 遍历省、市、县数据的碎片
 */

public class ChooseAreaFragment extends Fragment {

    /**
     * 标题
     */
    @BindView(R.id.title_text)
    TextView titleText;
    /**
     * 返回键
     */
    @BindView(R.id.back_button)
    Button backButton;
    /**
     * 展示数据列表
     */
    @BindView(R.id.list_view)
    ListView listView;

    private List<String>dataList=new ArrayList<>();
    private ChooseAreaAdapter adapter;
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;
    private ProgressDialog progressDialog;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County>countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        ButterKnife.bind(this, view);
        //LogUtils.d("listView",listView+"");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter=new ChooseAreaAdapter(dataList, getActivity());
        if(adapter!=null){
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LogUtils.d("currentLevel",currentLevel+"");
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }
                else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    if(getActivity() instanceof MainActivity){
                        Intent intent=new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity weatherActivity= (WeatherActivity) getActivity();
                        weatherActivity.drawerLayout.closeDrawers();
                        weatherActivity.swipeRefresh.setRefreshing(true);
                        weatherActivity.requestWeather(weatherId);
                    }
                }
            }
        });

        backButton.setOnClickListener((v)->{
            if(currentLevel==LEVEL_COUNTY){
                queryCities();
            }
            else if(currentLevel==LEVEL_CITY){
                queryProvinces();
            }
        });
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询
     * 查询不到再去服务器查询
     */
    private void queryProvinces() {
        titleText.setText(R.string.china);
        backButton.setVisibility(View.INVISIBLE);
        provinceList=DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }



    /**
     * 查询全国所有的市，优先从数据库查询
     * 查询不到再去服务器查询
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        LogUtils.d("ProvinceName",selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
                LogUtils.d("CityName",city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    /**
     * 查询全国所有的县，优先从数据库查询
     * 查询不到再去服务器查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getCountyName());
                LogUtils.d("CityName",county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    /**
     * 根据传递的省市县类型从服务器上查询
     * @param address
     * @param type
     */
    private void queryFromServer(String address,final String type) {
        if(!NetworkUtils.isAvalibale()){
            ToastUtils.showToast(R.string.no_network);
            return;
        }
        showProgressDialog();
        HttpUtils.sendHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(()->{
                    closeProgressDialog();
                    ToastUtils.showToast(R.string.load_error);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result=false;
                if ("province".equals(type)){
                    result= JsonUtils.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result=JsonUtils.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result=JsonUtils.handleCountyResponse(responseText,selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(()->{
                        closeProgressDialog();
                        if ("province".equals(type)){
                            queryProvinces();
                        }else if("city".equals(type)){
                            queryCities();
                        }else if("county".equals(type)){
                            queryCounties();
                        }
                    });


                }


            }
        });



    }

    private void closeProgressDialog() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

}
