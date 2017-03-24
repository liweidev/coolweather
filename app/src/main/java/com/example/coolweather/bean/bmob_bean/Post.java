package com.example.coolweather.bean.bmob_bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liwei on 2017/3/13.
 * 帖子表
 */

public class Post extends BmobObject implements Serializable{

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 作者
     */
    private MyUser author;
    /**
     * 图片
     */
    private BmobFile image;

    /**
     * 赞
     */
    private Integer favour;

    /**
     * 踩
     */
    private Integer stamp;

    /**
     * 评论数量
     */
    private Integer commnetCount;

    /**
     * 喜欢帖子的用户
     */
    private List<MyUser> userList;

    /**
     * 帖子的类型
     */
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {

        return type;
    }

    public void setUserList(List<MyUser> userList) {
        this.userList = userList;
    }

    public List<MyUser> getUserList() {

        return userList;
    }

    public void setCommnetCount(Integer commnetCount) {
        this.commnetCount = commnetCount;
    }

    public Integer getCommnetCount() {

        return commnetCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public void setFavour(Integer favour) {
        this.favour = favour;
    }

    public void setStamp(Integer stamp) {
        this.stamp = stamp;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public MyUser getAuthor() {
        return author;
    }

    public BmobFile getImage() {
        return image;
    }

    public Integer getFavour() {
        return favour;
    }

    public Integer getStamp() {
        return stamp;
    }
}
