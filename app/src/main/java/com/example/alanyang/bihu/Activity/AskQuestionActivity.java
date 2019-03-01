package com.example.alanyang.bihu.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class AskQuestionActivity extends AppCompatActivity {

    public static String titleX;
    public static String contentX;
    public Uri imageUri;
    public String image_Path;
    public final static String TOKEN_URL = "http://zzzia.net/qiniu/";  //子来的Url
    public final String accessKey = "cy0q6b-nla6l2MwDD1F259kwl7Lf_H5_gLOljadB";  //POST子来的参数
    public final String secretKey = "sg8vWV8KfbqJV3h7zQZa6mt4vASfigQ-RH1a3qx5";
    public final String bucket = "photos";
    public String uptoken;  //七牛返回的token
    public static String upKeyX;   //上传的图片名称
    public String url;
    public static String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        initData();
        initConfirmButton();

    }




    private void initConfirmButton() {
        Button button = findViewById(R.id.confirmAsk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText title = findViewById(R.id.titleAQ);
                EditText content = findViewById(R.id.contentAQ);
                titleX = title.getText().toString();      //拿到了输入的title等内容
                contentX = content.getText().toString();


                if (TextUtils.isEmpty(uptoken)) {
                    Toast.makeText(AskQuestionActivity.this, "获取数据中...", Toast.LENGTH_SHORT).show();
                }
                if (image_Path != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Configuration config = new Configuration.Builder()
                                    .dns(null)
                                    .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                    .build();
                            UploadManager uploadManager = new UploadManager(config);
                            uploadManager.put(image_Path, upKeyX, uptoken, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if (info.isOK()) {
                                        url = "http://bihu.jay86.com/question.php";
                                        link = "pn7en7rar.bkt.clouddn.com";
                                        NetUtil.post(url,  "title="+titleX + "&content="+contentX+"&images=http://"+link + "/" + upKeyX + "&token=" + User.token , new NetUtil.Callback() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    int status = obj.getInt("status");
                                                    Log.d("statusM", response);
                                                    if(status == 200){
                                                        User.avatar = "http://" + link + "/" + upKeyX;
                                                        Log.d("asd",User.token+"   "+ User.avatar);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(AskQuestionActivity.this,"提问成功",Toast.LENGTH_SHORT).show();
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
                                    }
                                }
                            }, null);
                        }
                    }).start();
                } else {
                    url = "http://bihu.jay86.com/question.php";
                    NetUtil.post(url,  "title="+titleX + "&content="+contentX+"&images=null" + "&token=" + User.token , new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                Log.d("statusM", response);
                                if(status == 200){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AskQuestionActivity.this,"提问成功",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }



                Intent intent = new Intent(AskQuestionActivity.this,CenterActivity.class);
                startActivity(intent);
            }
        });
    }



    public void initData(){

        Button button = findViewById(R.id.buttonAQ);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenFromService();
                if (ContextCompat.checkSelfPermission(AskQuestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AskQuestionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
            image_Path = UriUtil.getPathFromUri(AskQuestionActivity.this, imageUri);
            upKeyX = "image" + System.currentTimeMillis();       //upKey取随机值
            ImageView photo = findViewById(R.id.photoAQ);
            Glide.with(AskQuestionActivity.this).load(imageUri).into(photo);
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
