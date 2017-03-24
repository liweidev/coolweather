package com.example.coolweather.ui;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coolweather.R;
import com.example.coolweather.base.MyBaseActivity;
import com.example.coolweather.bean.bmob_bean.MyUser;
import com.example.coolweather.bean.bmob_bean.Post;
import com.example.coolweather.utils.DialogUtils;
import com.example.coolweather.utils.NetworkUtils;
import com.example.coolweather.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

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
    @BindView(R.id.imageView)
    ImageView imageView;
    private Uri imageUri;

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
        if (postText != null) {
            etText.setText(postText);
        }
    }

    /**
     * 初始化监听器
     */
    private void initLisenter() {
        imageBack.setOnClickListener(this);
        tvPublic.setOnClickListener(this);
        publicImage.setOnClickListener(this);
    }
    BmobFile bmobFile=null;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                if (!TextUtils.isEmpty(etText.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("投稿尚未保存，是否保存");
                    builder.setNegativeButton("丢弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences prs = PreferenceManager.getDefaultSharedPreferences(PublicPostActivity.this);
                            String postText = prs.getString("post_text", null);
                            if (postText != null) {
                                SharedPreferences.Editor editor = prs.edit();
                                editor.putString("post_text", null);
                                editor.apply();
                            }
                            finish();
                        }
                    });
                    builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PublicPostActivity.this).edit();
                            editor.putString("post_text", etText.getText().toString());
                            editor.apply();
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    finish();
                }
                break;

            case R.id.tv_public:
                if (!NetworkUtils.isAvalibale()) {
                    ToastUtils.showToast("网络异常");
                    return;
                }
                if (TextUtils.isEmpty(etText.getText().toString())) {
                    ToastUtils.showToast("亲，再多写一点吧");
                    return;
                }
                if (etText.getText().toString().length() > 300) {
                    ToastUtils.showToast("亲，文字太多了");
                    return;
                }
                DialogUtils.showDialog("正在提交", PublicPostActivity.this);
                if(imageView.getDrawable()==null){
                    //发送的是文本
                    Post post = new Post();
                    post.setContent(etText.getText().toString());
                    post.setAuthor(BmobUser.getCurrentUser(MyUser.class));
                    post.setCommnetCount(0);
                    post.setFavour(0);
                    post.setStamp(0);
                    post.setType("text");
                    post.setUserList(new ArrayList<MyUser>());
                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                DialogUtils.dissmissDialog();
                                ToastUtils.showToast("发布成功");
                                finish();
                            } else {
                                ToastUtils.showToast("发布失败");
                                DialogUtils.dissmissDialog();
                                return;
                            }
                        }
                    });

                }else{
                    //发送的是图片
                    Post post = new Post();
                    post.setTitle(etText.getText().toString());
                    post.setAuthor(BmobUser.getCurrentUser(MyUser.class));
                    post.setCommnetCount(0);
                    post.setFavour(0);
                    post.setStamp(0);
                    post.setType("picture");
                    post.setUserList(new ArrayList<MyUser>());
                    switch (requestCode){
                        case TAKE_PHOTO:
                            if(imageUri!=null){
                                bmobFile=new BmobFile(new File(imageUri.getPath()));
                            }
                            break;

                        case OPEN_ALBUM:
                            if(imagePath!=null){
                                bmobFile=new BmobFile(new File(imagePath));
                            }
                            break;
                    }
                    if(bmobFile!=null){
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    post.setImage(bmobFile);
                                    post.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                DialogUtils.dissmissDialog();
                                                ToastUtils.showToast("发布成功");
                                                finish();
                                            } else {
                                                ToastUtils.showToast("发布失败");
                                                DialogUtils.dissmissDialog();
                                                return;
                                            }
                                        }
                                    });
                                }else{
                                    ToastUtils.showToast("出现异常");
                                    DialogUtils.dissmissDialog();
                                }
                            }
                        });
                    }

                }
                break;
            case R.id.public_image:
                selectAlbum();
                //ToastUtils.showToast("点击了图片");
                break;

        }
    }
    private String[] items=new String[]{"相机拍照","系统图库","取消"};
    /**
     * 照相
     */
    private static final int TAKE_PHOTO = 0;
    /**
     * 打开系统图库
     */
    private static final int OPEN_ALBUM = 1;
    /**
     * 选择头像
     */
    private void selectAlbum() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0://相机拍照
                        //ToastUtils.showToast("相机拍照");
                        //判断是否有SD卡
                        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                            ToastUtils.showToast("请检查SD卡状态");
                            return;
                        }
                        try{
                            File imageFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+"album.jpg");
                            imageUri= Uri.fromFile(imageFile);
                            if(imageFile.exists()){
                                imageFile.delete();
                            }
                            imageFile.createNewFile();
                            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                            startActivityForResult(intent,TAKE_PHOTO);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 1://系统图库
                        //ToastUtils.showToast("系统图库");
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,OPEN_ALBUM);
                        break;
                    case 2:
                        break;
                }
            }
        });
        builder.show();
    }

    private int requestCode;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.requestCode=requestCode;
        switch (requestCode){
            case TAKE_PHOTO://拍照
                if(resultCode==RESULT_OK){
                    Glide.with(this).load(imageUri).into(imageView);
                    /*DialogUtils.showDialog("正在上传头像...",this);
                    if(!NetworkUtils.isAvalibale()){
                        ToastUtils.showToast("网络不可用,上传头像失败");
                        DialogUtils.dissmissDialog();
                        return;
                    }
                    File file=new File(imageUri.getPath());
                    new Thread(()->{
                        boolean isBreak=true;
                        while (isBreak){
                            if(file.length()!=0){
                                isBreak=false;
                                //LogUtils.d("fileLength",file.length()+"");
                                final BmobFile bmobFile=new BmobFile(new File(imageUri.getPath()));
                                bmobFile.uploadblock(new UploadFileListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            //Log.d("tag","成功:"+fileUrl);
                                            DialogUtils.dissmissDialog();
                                            ToastUtils.showToast("上传成功");
                                            //将头像保存到本地
                                            Glide.with(PublicPostActivity.this).load(imageUri).into(imageView);
                                            *//*SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PublicPostActivity.this).edit();
                                            editor.putString("imagePath",imageUri.getPath());
                                            editor.apply();*//*
                                            //更新MyUser头像
                                            //String fileUrl = bmobFile.getFileUrl();
                                            //MyUser myUser= BmobUser.getCurrentUser(MyUser.class);
                                            *//*if(myUser!=null){
                                                MyUser user=new MyUser();
                                                user.setAlbum(bmobFile);
                                                user.update(myUser.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if(e==null){
                                                            LogUtils.d("myUser","数据保存成功");
                                                        }else{
                                                            LogUtils.d("myUser","数据保存失败");
                                                            ToastUtils.showToast("保存失败");
                                                        }
                                                    }
                                                });
                                            }*//*

                                        }else{
                                            Log.d("tag","失败");
                                            e.printStackTrace();
                                            DialogUtils.dissmissDialog();
                                            ToastUtils.showToast("上传失败");
                                        }
                                    }
                                });
                            }
                        }
                    }).start();*/
                }
                break;
            case OPEN_ALBUM://打开系统图库
                if(RESULT_OK==resultCode){
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    private String imagePath = null;
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {

        Uri uri = data.getData();
        //Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    /**
     * 显示图片
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        Glide.with(this).load(imagePath).into(imageView);
        /*if (imagePath != null) {
            //TODO 此处应该把本地路径上传到服务器
            LogUtils.d("imagePath",imagePath);
            if(!NetworkUtils.isAvalibale()){
                ToastUtils.showToast("当前网络不可用");
                return;
            }
            DialogUtils.showDialog("正在上传",this);
            BmobFile bmobFile=new BmobFile(new File(imagePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                        if(myUser!=null){
                            MyUser user=new MyUser();
                            user.setAlbum(bmobFile);
                            user.update(myUser.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Glide.with(PublicPostActivity.this).load(imagePath).into(imageView);
                                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PublicPostActivity.this).edit();
                                        editor.putString("imagePath",imagePath);
                                        editor.apply();
                                        DialogUtils.dissmissDialog();
                                        ToastUtils.showToast("图片上传成功");
                                    }else{
                                        ToastUtils.showToast("图片上传失败");
                                        DialogUtils.dissmissDialog();
                                    }
                                }
                            });
                        }else{
                            DialogUtils.dissmissDialog();
                        }
                    }else{
                        DialogUtils.dissmissDialog();
                    }
                }
            });

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }*/
    }
}
