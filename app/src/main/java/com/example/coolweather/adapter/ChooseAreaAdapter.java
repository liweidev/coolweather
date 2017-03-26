package com.example.coolweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liwei.coolweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwei on 2017/2/22.
 * 显示省、市、县适配器
 */

public class ChooseAreaAdapter extends BaseAdapter{

    private List<String>dataList=new ArrayList<>();
    private Context mContext;

    public ChooseAreaAdapter(List<String> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh=null;
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.choose_area_item,viewGroup,false);
            vh.tvText= (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.tvText.setText(dataList.get(position));
        return convertView;
    }

    class ViewHolder{
        private TextView tvText;
    }


}
