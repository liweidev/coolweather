package com.example.coolweather.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.coolweather.R;
import com.example.coolweather.base.MyBaseActivity;
import com.example.coolweather.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 段子主界面
 */
public class AboutActivity extends MyBaseActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String imagePath = sp.getString("imagePath", null);
        if (imagePath != null) {
            LogUtils.d("imagePath", imagePath);
            Glide.with(this).load(imagePath).into(avatar);
        }

        initLisenter();
    }

    /**
     * 初始化监听器
     */
    private void initLisenter() {
        imageEdit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_edit:
                startActivity(new Intent(this,PublicPostActivity.class));
                break;
        }
    }
}
