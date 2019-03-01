package com.example.alanyang.bihu.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private String url = "http://bihu.jay86.com/login.php";
    private EditText un;
    private EditText pw;
    private String username;
    private String password;
    boolean isRemember;
    private CheckBox rememberPass;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initChenJin();      //设置沉浸式

        un = findViewById(R.id.login1);
        pw = findViewById(R.id.login2);
        username = un.getText().toString();
        password = pw.getText().toString();
        rememberPass = findViewById(R.id.checkbox);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isRemember = preferences.getBoolean("remember_password",false);
        if(isRemember){
            String account = preferences.getString("account","");
            String pass_word = preferences.getString("password","");
            un.setText(account);        //注意此处用的是EditText
            pw.setText(pass_word);
            rememberPass.setChecked(true);
        }

        initButton();

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

    private void initButton() {
        Button loginButton = findViewById(R.id.loginbtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                un = findViewById(R.id.login1);     //访问权限问题(昏。。
                pw = findViewById(R.id.login2);
                username = un.getText().toString();
                password = pw.getText().toString();


                editor = preferences.edit();
                if(rememberPass.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("account",un.getText().toString());
                    editor.putString("password",pw.getText().toString());
                }else{
                    editor.clear();
                }
                editor.apply();

                NetUtil.post(url, "username=" + username + "&password=" + password, new NetUtil.Callback() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int status = obj.getInt("status");

                            Log.d("tag+",password);

                            Log.d("tag1", response);

                            if (status == 200) {
                                String data = obj.getString("data");

                                JSONObject obj2 = new JSONObject(data);
                                User.token = obj2.getString("token");

                                User.avatar = obj2.getString("avatar");

                                Log.d("tag2",  User.avatar);

                                User.username = username;
                                User.password = password;

                                Log.d("tag3", response);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("tag4", response);
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(LoginActivity.this,"欢迎来到逼乎",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, CenterActivity.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("tag5", response);
                                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
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

}
