package com.example.coolweather.bean.bmob_bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by liwei on 2017/3/13.
 * 评论表
 */

public class Comment extends BmobObject implements Serializable{

    /**
     * 内容
     */
    private String content;
    /**
     * 用户
     */
    private MyUser user;
    /**
     * 帖子
     */
    private Post post;

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setFavour(Integer favour) {
        this.favour = favour;
    }

    public String getContent() {

        return content;
    }

    public MyUser getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    public Integer getFavour() {
        return favour;
    }

    /**
     * 赞
     */
    private Integer favour;

}
