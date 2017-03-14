package com.example.coolweather.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.coolweather.R;
import com.example.coolweather.base.MyBaseActivity;
import com.example.coolweather.bean.bmob_bean.MyUser;
import com.example.coolweather.fragment.AboutFragment;
import com.example.coolweather.lisenter.FloatingButtonLisenter;
import com.example.coolweather.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 段子主界面
 */
public class AboutActivity extends MyBaseActivity implements View.OnClickListener,FloatingButtonLisenter {

    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.rb_text)
    RadioButton rbText;
    @BindView(R.id.rb_picter)
    RadioButton rbPicter;
    @BindView(R.id.rg_select)
    RadioGroup rgSelect;
    @BindView(R.id.image_edit)
    ImageView imageEdit;
    @BindView(R.id.fab_refresh)
    FloatingActionButton fabRefresh;
    private ObjectAnimator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String imagePath = sp.getString("imagePath", null);
        if (imagePath != null && avatar != null) {
            LogUtils.d("imagePath", imagePath);
            Glide.with(this).load(imagePath).into(avatar);
        } else {
            MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
            BmobFile bmobFile = myUser.getAlbum();
            if (bmobFile != null) {
                Glide.with(this).load(bmobFile.getFileUrl()).into(avatar);
            }
        }
        initLisenter();
    }

    /**
     * 初始化监听器
     */
    private void initLisenter() {
        imageEdit.setOnClickListener(this);
        fabRefresh.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_edit:
                startActivity(new Intent(this, PublicPostActivity.class));
                break;
            case R.id.fab_refresh:
                animator = ObjectAnimator.ofFloat(fabRefresh, "rotation", 0f, -360f);
                animator.setRepeatCount(-1);
                animator.setInterpolator(new LinearInterpolator());
                animator.setAutoCancel(true);
                animator.setDuration(500).start();
                int rbId = rgSelect.getCheckedRadioButtonId();
                switch (rbId) {
                    case R.id.rb_text://文字
                        AboutFragment aboutFragment= (AboutFragment) getSupportFragmentManager().findFragmentById(R.id.about_fragment);
                        if(aboutFragment!=null){
                            aboutFragment.queryPost(this);
                        }
                        break;

                    case R.id.rb_picter://图片
                        //TODO
                        break;
                }
                break;
        }
    }

    @Override
    public void onSuccess() {
        animator.cancel();
    }

    @Override
    public void onError() {
        animator.cancel();
    }
}
