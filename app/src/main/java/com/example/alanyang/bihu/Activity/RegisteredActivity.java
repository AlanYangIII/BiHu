package com.example.alanyang.bihu.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.Util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisteredActivity extends AppCompatActivity {

    private Button register;
    private EditText id;
    private EditText pwd;
    private String username;
    private String password;
    String url = "http://bihu.jay86.com/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        initChenJin();      //设置沉浸式
        register = findViewById(R.id.registeredbtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = findViewById(R.id.registered1);
                pwd = findViewById(R.id.registered2);
                username = id.getText().toString();     //获得了EditText的内容
                password = pwd.getText().toString();


                NetUtil.post(url, "username=" + username + "&password=" + password, new NetUtil.Callback() {    //网络请求
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            int status = obj.getInt("status");
                            if (status == 200) {
                                Intent intent = new Intent(RegisteredActivity.this,LoginActivity.class);
                                startActivity(intent);
                                RegisteredActivity.this.finish();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisteredActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisteredActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }


                });


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


}
