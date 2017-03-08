package com.example.coolweather.bean.bmob_bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liwei on 2017/3/8.
 */

public class MyUser extends BmobUser{

    public void setAlbum(BmobFile album) {
        this.album = album;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BmobFile getAlbum() {

        return album;
    }

    public String getNickName() {
        return nickName;
    }

    /**
     * 头像
     */
    private BmobFile album;

    /**
     * 昵称
     */
    private String nickName;


}
