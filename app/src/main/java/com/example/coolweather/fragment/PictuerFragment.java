package com.example.coolweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.coolweather.R;
import com.example.coolweather.adapter.PicterAdapter;
import com.example.coolweather.bean.bmob_bean.Comment;
import com.example.coolweather.bean.bmob_bean.Post;
import com.example.coolweather.lisenter.FloatingButtonLisenter;
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
 * Created by liwei on 2017/3/24.
 * 图片Fragment
 */

public class PictuerFragment extends Fragment {

    @BindView(R.id.iv_no_data_hint)
    ImageView ivNoDataHint;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private PicterAdapter adapter;
    private List<Post>postList=new ArrayList<>();
    private int skip = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new PicterAdapter(getActivity(),postList);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPost(null);
            }
        });
        queryPost(null);
    }
    /**
     * 查询帖子
     */
    public void queryPost(FloatingButtonLisenter lisenter) {
        swipeRefresh.setRefreshing(true);
        if (!NetworkUtils.isAvalibale()) {
            ToastUtils.showToast("当前网络不可用");
            return;
        }
        //DialogUtils.showDialog("正在加载", getActivity());
        BmobQuery<Post> query = new BmobQuery<>();
        query.setSkip(skip);
        query.setLimit(20);
        query.addWhereEqualTo("type","picture");
        query.order("-createdAt");
        // 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.include("author,userList");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    postList.clear();
                    for (Post post : list) {
                        BmobQuery<Comment> query = new BmobQuery<>();
                        query.addWhereEqualTo("post", post);
                        query.findObjects(new FindListener<Comment>() {
                            @Override
                            public void done(List<Comment> list, BmobException e) {
                                if (e == null) {
                                    if (list != null) {
                                        post.setCommnetCount(list.size());
                                        postList.add(post);
                                    }
                                }
                            }
                        });
                    }

                    ToastUtils.showToast("加载成功");
                    //DialogUtils.dissmissDialog();
                    swipeRefresh.setRefreshing(false);
                    /*postList.clear();
                    postList.addAll(list);*/
                    adapter.notifyDataSetChanged();
                    if (lisenter != null) {
                        lisenter.onSuccess();
                    }
                    skip += 20;
                    if (list.size() == 0) {
                        ToastUtils.showToast("暂无段子可浏览...");
                        ivNoDataHint.setVisibility(View.VISIBLE);
                    }else{
                        ivNoDataHint.setVisibility(View.INVISIBLE);
                    }
                } else {
                    ToastUtils.showToast("加载失败");
                    //DialogUtils.dissmissDialog();
                    swipeRefresh.setRefreshing(false);
                    if (lisenter != null) {
                        lisenter.onError();
                    }
                    return;
                }
            }
        });
    }

}
