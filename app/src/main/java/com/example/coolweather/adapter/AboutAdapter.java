package com.example.coolweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coolweather.R;
import com.example.coolweather.bean.bmob_bean.Post;
import com.example.coolweather.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liwei on 2017/3/13.
 */

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.MyViewHolder> implements View.OnClickListener {


    private List<Post> postList = new ArrayList<>();
    private Context mContext;

    public AboutAdapter(List<Post> postList, Context mContext) {
        this.postList = postList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.about_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = postList.get(position);
        BmobFile bmobFile = post.getAuthor().getAlbum();
        if(bmobFile!=null){
            Glide.with(mContext).load(bmobFile.getFileUrl()).into(holder.avatar);
        }
        holder.nickname.setText(post.getAuthor().getNickName());
        holder.content.setText(post.getContent());
        holder.tvFavour.setText(post.getFavour()+"");
        holder.tvStamp.setText(post.getStamp()+"");
        holder.tvComment.setText(post.getCommnetCount()+"");

        holder.llFavour.setOnClickListener(this);
        holder.llStamp.setOnClickListener(this);
        holder.llComment.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_favour://赞
                ToastUtils.showToast("赞");
                break;
            case R.id.ll_stamp://踩
                ToastUtils.showToast("踩");
                break;
            case R.id.ll_comment://评论
                ToastUtils.showToast("评论");
                break;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar)
        CircleImageView avatar;
        @BindView(R.id.nickname)
        TextView nickname;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.tv_favour)
        TextView tvFavour;
        @BindView(R.id.ll_favour)
        LinearLayout llFavour;
        @BindView(R.id.tv_stamp)
        TextView tvStamp;
        @BindView(R.id.ll_stamp)
        LinearLayout llStamp;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.ll_comment)
        LinearLayout llComment;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
