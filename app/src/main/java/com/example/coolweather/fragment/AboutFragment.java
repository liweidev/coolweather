package com.example.coolweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coolweather.R;
import com.example.coolweather.adapter.AboutAdapter;
import com.example.coolweather.bean.bmob_bean.Post;
import com.example.coolweather.utils.DialogUtils;
import com.example.coolweather.utils.NetworkUtils;
import com.example.coolweather.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by liwei on 2017/3/13.
 */

public class AboutFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AboutAdapter adapter;
    private List<Post> postList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter=new AboutAdapter(postList,getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryPost();
    }

    /**
     * 查询帖子
     */
    private void queryPost() {
        if(!NetworkUtils.isAvalibale()){
            ToastUtils.showToast("当前网络不可用");
            return;
        }
        DialogUtils.showDialog("正在加载",getActivity());
        BmobQuery<Post> query=new BmobQuery<>();
        query.setLimit(50);
        query.order("-updatedAt");
        // 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.include("author");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e==null){
                    ToastUtils.showToast("加载成功");
                    DialogUtils.dissmissDialog();
                    postList.clear();
                    postList.addAll(list);
                    adapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast("加载失败");
                    DialogUtils.dissmissDialog();
                    return;
                }
            }
        });
    }
}
