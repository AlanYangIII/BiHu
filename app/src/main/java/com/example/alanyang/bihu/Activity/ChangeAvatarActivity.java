package com.example.alanyang.bihu.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;
import com.example.alanyang.bihu.Util.UriUtil;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeAvatarActivity extends AppCompatActivity {

    public Uri imageUri;
    public String image_Path;
    public final static String TOKEN_URL = "http://zzzia.net/qiniu/";  //子来的Url
    public final String accessKey = "cy0q6b-nla6l2MwDD1F259kwl7Lf_H5_gLOljadB";  //POST子来的参数
    public final String secretKey = "sg8vWV8KfbqJV3h7zQZa6mt4vASfigQ-RH1a3qx5";
    public final String bucket = "photos";
    public String uptoken;  //七牛返回的token
    public static String upKey;   //上传的图片名称
    public String url;
    public static String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avator);
        initChenJin();
        iniButtonBack();

        initButtonChoosePhoto();
        initConfirmButton();
    }


    private void initConfirmButton() {
        Button button = findViewById(R.id.confirmPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(uptoken)) {
                    Toast.makeText(ChangeAvatarActivity.this, "正在获取token。。", Toast.LENGTH_SHORT).show();
                }
                if (image_Path != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            upKey = "image" + System.currentTimeMillis();       //upKey取随机值
                            Log.d("path", image_Path);
                            Log.d("upKey", upKey);
                            Log.d("uptoken", uptoken);
                            Configuration config = new Configuration.Builder()
                                    .dns(null)
                                    .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                    .build();
                            UploadManager uploadManager = new UploadManager(config);
                            uploadManager.put(image_Path, upKey, uptoken, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if (info.isOK()) {


                                        url = "http://bihu.jay86.com/modifyAvatar.php";
                                        link = "pn7en7rar.bkt.clouddn.com";
                                        Log.d("test", "onClick: ");
                                        NetUtil.post(url, "token=" + User.token + "&avatar=http://" + link + "/" + upKey , new NetUtil.Callback() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    int status = obj.getInt("status");
                                                    Log.d("statusM", response);
                                                    if(status == 200){
                                                        User.avatar = "http://" + link + "/" + upKey;
                                                        Log.d("asd",User.token+"   "+ User.avatar);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(ChangeAvatarActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });


                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                            }
                                        });
                                        Log.d("qiniu", "Upload Fail");
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                    }
                                    Log.d("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                }
                            }, null);
                        }
                    }).start();
                } else {
                    Toast.makeText(ChangeAvatarActivity.this, "请上传头像", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void initButtonChoosePhoto() {
        getTokenFromService();
        Button choosePhoto = findViewById(R.id.choosePhoto);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后从相册选择图片
                if (ContextCompat.checkSelfPermission(ChangeAvatarActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChangeAvatarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        if (imageUri != null) {
            image_Path = UriUtil.getPathFromUri(ChangeAvatarActivity.this, imageUri);
        }
    }




    private void iniButtonBack() {
        Button back = findViewById(R.id.backToCenter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ChangeAvatarActivity.this, CenterActivity.class);
                startActivity(intent1);
            }
        });
    }


    private void initChenJin() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    public void getTokenFromService() {
        Log.d("m11", "111");
        NetUtil.post("http://zzzia.net:8080/qiniu/?accessKey=" + accessKey + "&secretKey=" + secretKey + "&bucket=" + bucket, "", new NetUtil.Callback() {
            @Override
            public void onResponse(String response) {
                Log.d("qiniu9", "22");
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    Log.d("qiniu9", response);
                    if(status == 200){
                        Log.d("qiniu1", response);
                        uptoken = obj.getString("token");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
