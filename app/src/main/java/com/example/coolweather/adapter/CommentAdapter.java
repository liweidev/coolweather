package com.example.coolweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coolweather.R;
import com.example.coolweather.bean.bmob_bean.Comment;
import com.example.coolweather.bean.bmob_bean.MyUser;
import com.example.coolweather.bean.bmob_bean.Post;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liwei on 2017/3/15.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {



    private Context mContext;
    private List<Comment> commentList = new ArrayList<>();
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private int current_type = 2;
    private Post post;
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            current_type = TYPE_HEADER;
            return current_type;
        } else {
            current_type = TYPE_NORMAL;
            return current_type;
        }
    }

    public CommentAdapter(Context mContext, List<Comment> commentList,Post post) {
        this.mContext = mContext;
        this.commentList = commentList;
        this.post=post;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (current_type == TYPE_HEADER) {
            //加载头部布局
            view = LayoutInflater.from(mContext).inflate(R.layout.recycler_head_view, parent, false);
        }
        if (current_type == TYPE_NORMAL) {
            //加载普通布局
            view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(current_type==TYPE_NORMAL){
            position--;
            Comment comment = commentList.get(position);
            MyUser user = comment.getUser();
            if (user != null) {
                BmobFile bmobFile = user.getAlbum();
                if (bmobFile != null) {
                    String fileUrl = bmobFile.getFileUrl();
                    if (fileUrl != null) {
                        Glide.with(mContext).load(fileUrl).into(holder.avarta);
                    }
                }
                String nickName = user.getNickName();
                holder.nickname.setText(nickName);
            }
            holder.updatetime.setText(comment.getUpdatedAt());
            holder.content.setText(comment.getContent());
        }
        if(current_type==TYPE_HEADER){
            if(post!=null){
                MyUser user = post.getAuthor();
                String content = post.getContent();
                if(user!=null){
                    BmobFile bmobFile = user.getAlbum();
                    String nickName = user.getNickName();
                    //nickname.setText(nickName);
                    holder.head_nickname.setText(nickName);
                    if(bmobFile!=null){
                        String fileUrl = bmobFile.getFileUrl();
                        if(fileUrl!=null){
                            Glide.with(mContext).load(fileUrl).into(holder.head_avarta);
                        }
                    }
                }
                //postContent.setText(content);
                holder.head_postContent.setText(content);
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * 普通布局
         */
        //@BindView(R.id.comment_avarta)
        private CircleImageView avarta;
        //@BindView(R.id.nickname)
        private TextView nickname;
        //@BindView(R.id.updatetime)
        private TextView updatetime;
        //@BindView(R.id.content)
        private TextView content;

        /**
         * 头部布局
         */
        //@BindView(R.id.head_avarta)
        private CircleImageView head_avarta;
        //@BindView(R.id.head_nickname)
        private TextView head_nickname;
        //@BindView(R.id.head_production)
        private TextView head_production;
        //@BindView(R.id.head_post_content)
        private TextView head_postContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (current_type == TYPE_NORMAL) {
                //绑定普通布局
                //ButterKnife.bind(this, itemView);
                avarta= (CircleImageView) itemView.findViewById(R.id.comment_avarta);
                nickname= (TextView) itemView.findViewById(R.id.nickname);
                updatetime= (TextView) itemView.findViewById(R.id.updatetime);
                content= (TextView) itemView.findViewById(R.id.content);
            }
            if (current_type == TYPE_HEADER) {
                //绑定头部布局
                //ButterKnife.bind(this, itemView);
                head_avarta= (CircleImageView) itemView.findViewById(R.id.head_avarta);
                head_nickname= (TextView) itemView.findViewById(R.id.head_nickname);
                head_production= (TextView) itemView.findViewById(R.id.head_production);
                head_postContent= (TextView) itemView.findViewById(R.id.head_post_content);
            }
        }
    }
}
