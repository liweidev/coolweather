package com.example.coolweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.liwei.coolweather.R;
import com.example.coolweather.bean.bmob_bean.MyUser;
import com.example.coolweather.bean.bmob_bean.Post;
import com.example.coolweather.ui.CommentActivity;
import com.example.coolweather.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liwei on 2017/3/24.
 */

public class PicterAdapter extends RecyclerView.Adapter<PicterAdapter.MyViewHolder> {


    private Context mContext;
    private List<Post> postList = new ArrayList<>();

    public PicterAdapter(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.picture_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = postList.get(position);
        MyUser author = post.getAuthor();
        if(author!=null){
            String nickName = author.getNickName();
            BmobFile bmobFile = author.getAlbum();
            if(bmobFile!=null){
                String fileUrl = bmobFile.getFileUrl();
                if(fileUrl!=null){
                    Glide.with(mContext).load(fileUrl).into(holder.avatar);
                }
            }
            holder.nickname.setText(nickName);
        }
        String title = post.getTitle();
        holder.title.setText(title);
        BmobFile image = post.getImage();
        if(image!=null){
            String fileUrl = image.getFileUrl();
            if(fileUrl!=null){
                Glide.with(mContext).load(fileUrl).into(holder.imageView);
            }
        }
        Integer commentCount = post.getCommnetCount();
        Integer favour = post.getFavour();
        Integer stamp = post.getStamp();
        holder.tvComment.setText(commentCount+"");
        holder.tvFavour.setText(favour+"");
        holder.tvStamp.setText(stamp+"");
        holder.llFavour.setOnClickListener((v)->{
            //判断当前用户是否已经点过赞
            List<MyUser> userList = post.getUserList();
            if(userList!=null&&userList.size()>0){
                for(MyUser myUser:userList){
                    if(myUser.getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
                        ToastUtils.showToast("您已经顶过了...");
                        return;
                    }
                }
            }
            post.increment("favour");
            post.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        ToastUtils.showToast("点赞成功");
                        post.setFavour(post.getFavour()+1);
                        notifyDataSetChanged();
                        //将当前用户设置为已经点过赞
                        userList.clear();
                        userList.add(BmobUser.getCurrentUser(MyUser.class));
                        post.setUserList(userList);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    ToastUtils.showToast("更新成功");
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            });
        });
        holder.llStamp.setOnClickListener((v)->{
            //判断当前用户是否已经点过赞
            List<MyUser> userList = post.getUserList();
            if(userList!=null&&userList.size()>0){
                for(MyUser myUser:userList){
                    if(myUser.getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
                        ToastUtils.showToast("您已经顶过了...");
                        return;
                    }
                }
            }
            post.increment("stamp");
            post.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        ToastUtils.showToast("踩踏成功");
                        post.setStamp(post.getStamp()+1);
                        notifyDataSetChanged();
                        //将当前用户设置为已经点过赞
                        userList.clear();
                        userList.add(BmobUser.getCurrentUser(MyUser.class));
                        post.setUserList(userList);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    ToastUtils.showToast("更新成功");
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            });
        });
        holder.llComment.setOnClickListener((v)->{
            Intent intent=new Intent(mContext, CommentActivity.class);
            intent.putExtra("post",post);
            intent.putExtra("type","image");
            mContext.startActivity(intent);
        });
        //内容监听
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("post",post);
                intent.putExtra("type","image");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar)
        CircleImageView avatar;
        @BindView(R.id.nickname)
        TextView nickname;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.imageView)
        ImageView imageView;
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
