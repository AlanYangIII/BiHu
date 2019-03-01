package com.example.alanyang.bihu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alanyang.bihu.Adapter.AdapterQuestion;
import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class AnswerActivity extends AppCompatActivity {

    public String contentX;
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        initData();
    }


    public void initData(){
        Button button = findViewById(R.id.buttonA);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText content = findViewById(R.id.contentA);
                contentX = content.getText().toString();
                url = "http://bihu.jay86.com/answer.php";
                NetUtil.post(url, "qid=" + AdapterQuestion.qidA + "&content=" + contentX + "&images=null" + "&token=" + User.token, new NetUtil.Callback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int status = obj.getInt("status");
                            if(status == 200){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(AnswerActivity.this,CenterActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(AnswerActivity.this,"回答成功",Toast.LENGTH_SHORT).show();
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
