package com.example.coolweather.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.base.MyBaseActivity;
import com.example.coolweather.bean.bmob_bean.MyUser;
import com.example.coolweather.bean.bmob_bean.Post;
import com.example.coolweather.utils.DialogUtils;
import com.example.coolweather.utils.NetworkUtils;
import com.example.coolweather.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 发布帖子界面
 */
public class PublicPostActivity extends MyBaseActivity implements View.OnClickListener {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.tv_public)
    TextView tvPublic;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.public_image)
    ImageView publicImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_post);
        ButterKnife.bind(this);
        initPrs();
        initLisenter();
    }

    private void initPrs() {
        SharedPreferences prs = PreferenceManager.getDefaultSharedPreferences(this);
        String postText = prs.getString("post_text", null);
        if(postText!=null){
            etText.setText(postText);
        }
    }

    /**
     * 初始化监听器
     */
    private void initLisenter() {
        imageBack.setOnClickListener(this);
        tvPublic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_back:
                if(!TextUtils.isEmpty(etText.getText().toString())){
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("投稿尚未保存，是否保存");
                    builder.setNegativeButton("丢弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences prs = PreferenceManager.getDefaultSharedPreferences(PublicPostActivity.this);
                            String postText = prs.getString("post_text", null);
                            if(postText!=null){
                                SharedPreferences.Editor editor = prs.edit();
                                editor.putString("post_text",null);
                                editor.apply();
                            }
                            finish();
                        }
                    });
                    builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PublicPostActivity.this).edit();
                            editor.putString("post_text",etText.getText().toString());
                            editor.apply();
                            finish();
                        }
                    });
                    builder.show();
                }else{
                    finish();
                }
                break;

            case R.id.tv_public:
                if(!NetworkUtils.isAvalibale()){
                    ToastUtils.showToast("网络异常");
                    return;
                }
                if(TextUtils.isEmpty(etText.getText().toString())){
                    ToastUtils.showToast("亲，再多写一点吧");
                    return;
                }
                if(etText.getText().toString().length()>300){
                    ToastUtils.showToast("亲，文字太多了");
                    return;
                }
                DialogUtils.showDialog("正在提交",PublicPostActivity.this);
                Post post=new Post();
                post.setContent(etText.getText().toString());
                post.setAuthor(BmobUser.getCurrentUser(MyUser.class));
                post.setCommnetCount(0);
                post.setFavour(0);
                post.setStamp(0);
                post.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            DialogUtils.dissmissDialog();
                            ToastUtils.showToast("发布成功");
                            finish();
                        }else{
                            ToastUtils.showToast("发布失败");
                            DialogUtils.dissmissDialog();
                            return;
                        }
                    }
                });
                break;

        }
    }
}
