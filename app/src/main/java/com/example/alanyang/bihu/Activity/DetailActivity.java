package com.example.alanyang.bihu.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alanyang.bihu.Adapter.AdapterDetail;
import com.example.alanyang.bihu.Adapter.AdapterQuestion;
import com.example.alanyang.bihu.Bean.commentBean;
import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {
    private RecyclerView nRecyclerView;
    private AdapterDetail nAdapterDetail;
    private ArrayList<commentBean.DataBean.AnswersBean> commentsBeanList;
    private SwipeRefreshLayout swipeRefresh;
    private int page = 0;
    private int count = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initData();
        initReFresh();

    }

    public void initData() {
        nRecyclerView = findViewById(R.id.recycler_viewD);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsBeanList = new ArrayList<>();
        Log.d("qid", AdapterQuestion.qidQL + "");
        NetUtil.post("http://bihu.jay86.com/getAnswerList.php", "page=" + page + "&count=" + count + "&qid=" + AdapterQuestion.qidQL + "&token=" + User.token, new NetUtil.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj1 = new JSONObject(response);
                    String data = obj1.getString("data");
                    JSONObject obj2 = new JSONObject(data);
                    String answers = obj2.getString("answers");
                    JSONArray jsonArray = new JSONArray(answers);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String authorName = obj.getString("authorName");
                        String authorAvatar = obj.getString("authorAvatar");
                        String date = obj.getString("date");
                        String content = obj.getString("content");
                        String images = obj.getString("images");
                        int id = obj.getInt("id");
                        int exciting = obj.getInt("exciting");
                        int naive = obj.getInt("naive");
                        boolean is_exciting = obj.getBoolean("is_exciting");
                        boolean is_naive = obj.getBoolean("is_naive");
                        commentBean.DataBean.AnswersBean cDAB = new commentBean.DataBean.AnswersBean();
                        cDAB.setAuthorName(authorName);
                        cDAB.setAuthorAvatar(authorAvatar);
                        cDAB.setDate(date);
                        cDAB.setContent(content);
                        cDAB.setImages(images);
                        cDAB.setId(id);
                        cDAB.setExciting(exciting);
                        cDAB.setNaive(naive);
                        cDAB.setIs_exciting(is_exciting);
                        cDAB.setIs_naive(is_naive);
                        commentsBeanList.add(cDAB);
                    }
                    nAdapterDetail = new AdapterDetail(DetailActivity.this, commentsBeanList);
                    nRecyclerView.setAdapter(nAdapterDetail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent mIntent = getIntent();
        String mAvatar = mIntent.getStringExtra("avatar");
        String mUsername = mIntent.getStringExtra("username");
        String mTitle = mIntent.getStringExtra("title");
        String mTime = mIntent.getStringExtra("time");
        String mText = mIntent.getStringExtra("text");
        String mImage = mIntent.getStringExtra("image");
        TextView username = findViewById(R.id.usernameC);
        TextView title = findViewById(R.id.titleC);
        TextView time = findViewById(R.id.timeC);
        TextView text = findViewById(R.id.contentCText);
        text.setText(mText);
        time.setText(mTime);
        title.setText(mTitle);
        username.setText(mUsername);
        ImageView avatar = findViewById(R.id.avatarC);
        ImageView image = findViewById(R.id.contentCImage);
        Glide.with(DetailActivity.this).load(mAvatar).into(avatar);
        Glide.with(DetailActivity.this).load(mImage).into(image);


    }

    @SuppressLint("ResourceAsColor")
    private void initReFresh() {
        swipeRefresh = findViewById(R.id.swipe_refreshD);
        swipeRefresh.setColorSchemeColors(R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        nAdapterDetail.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
