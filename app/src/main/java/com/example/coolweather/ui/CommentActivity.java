package com.example.coolweather.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liwei.coolweather.R;
import com.example.coolweather.adapter.CommentAdapter;
import com.example.coolweather.base.MyBaseActivity;
import com.example.coolweather.bean.bmob_bean.Comment;
import com.example.coolweather.bean.bmob_bean.MyUser;
import com.example.coolweather.bean.bmob_bean.Post;
import com.example.coolweather.utils.DialogUtils;
import com.example.coolweather.utils.NetworkUtils;
import com.example.coolweather.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 评论界面
 */
public class CommentActivity extends MyBaseActivity implements View.OnClickListener {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.report)
    TextView report;
    //@BindView(R.id.avarta)
    //CircleImageView avarta;
    //@BindView(R.id.nickname)
    //TextView nickname;
    //@BindView(R.id.production)
    //TextView production;
    //@BindView(R.id.post_content)
    //TextView postContent;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.send)
    Button send;
    private Post post;
    private CommentAdapter adapter;
    private List<Comment>commentList=new ArrayList<>();
    private String type=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initLisenter();
        getPost();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //manager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(manager);
        //recyclerView.setNestedScrollingEnabled(false);
        adapter=new CommentAdapter(this,commentList,post);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryCommentWithPost();
    }

    /**
     * 查询当前帖子的所有评论
     */
    private void queryCommentWithPost() {
        if(!NetworkUtils.isAvalibale()){
            ToastUtils.showToast("当前网络不可用");
            return;
        }
        DialogUtils.showDialog("正在查询",this);
        BmobQuery<Comment> query=new BmobQuery<>();
        query.addWhereEqualTo("post",post);
        query.include("user");
        query.setLimit(999);
        query.order("-updatedAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e==null){
                    DialogUtils.dissmissDialog();
                    //ToastUtils.showToast("查询评论成功");
                    if(list.size()==0){
                        ToastUtils.showToast("当前暂无评论");
                        return;
                    }
                    commentList.clear();
                    commentList.addAll(list);
                    adapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast("查询评论失败");
                    DialogUtils.dissmissDialog();
                    return;
                }
            }
        });


    }

    /**
     * 初始化监听器
     */
    private void initLisenter() {
        imageBack.setOnClickListener(this);
        send.setOnClickListener(this);
        report.setOnClickListener(this);
    }

    /**
     * 获取帖子
     */
    private void getPost() {
        post = (Post) getIntent().getSerializableExtra("post");
        type=getIntent().getStringExtra("type");
        /*if(post!=null){
            MyUser user = post.getAuthor();
            String content = post.getContent();
            if(user!=null){
                BmobFile bmobFile = user.getAlbum();
                String nickName = user.getNickName();
                //nickname.setText(nickName);
                if(bmobFile!=null){
                    String fileUrl = bmobFile.getFileUrl();
                    if(fileUrl!=null){
                        //Glide.with(this).load(fileUrl).into(avarta);
                    }
                }
            }
            //postContent.setText(content);
        }*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.send:
                if(!NetworkUtils.isAvalibale()){
                    ToastUtils.showToast("当前网络不可用");
                    return;
                }
                if(TextUtils.isEmpty(etComment.getText().toString())){
                    ToastUtils.showToast("亲,再多写一点吧");
                    return;
                }
                Comment comment=new Comment();
                MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                if(myUser!=null){
                    comment.setUser(myUser);
                }
                comment.setContent(etComment.getText().toString());
                comment.setPost(post);
                DialogUtils.showDialog("正在发送",this);
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            DialogUtils.dissmissDialog();
                            //ToastUtils.showToast("评论成功");
                            etComment.setText("");
                            queryCommentWithPost();
                            recyclerView.scrollToPosition(1);
                        }else{
                            DialogUtils.dissmissDialog();
                            ToastUtils.showToast("评论失败");
                            etComment.setText("");
                        }
                    }
                });
                break;

        }
    }
}
