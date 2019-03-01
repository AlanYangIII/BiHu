package com.example.alanyang.bihu.Activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangepwdActivity extends AppCompatActivity {

    String url = "http://bihu.jay86.com/changePassword.php";
    EditText pwd;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        initChenJin();
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

    private void initButton(){
        Button button = findViewById(R.id.confirmbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd = findViewById(R.id.confirmChange);
                password = pwd.getText().toString();
                //POST接口修改密码
                NetUtil.post(url, "password=" + password + "&token=" + User.token, new NetUtil.Callback() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int status = obj.getInt("status");
                            if (status == 200) {
                                Log.d("tagd",password);
                                Log.d("tagc", response);
                                User.password = password;
                                ChangepwdActivity.this.finish();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangepwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.d("tagc", response);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangepwdActivity.this, "修改失败", Toast.LENGTH_LONG).show();
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
