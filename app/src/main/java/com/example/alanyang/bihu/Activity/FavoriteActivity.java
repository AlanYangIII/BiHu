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
import com.example.alanyang.bihu.Adapter.AdapterFavorite;
import com.example.alanyang.bihu.Adapter.AdapterQuestion;
import com.example.alanyang.bihu.Bean.commentBean;
import com.example.alanyang.bihu.Bean.favoriteBean;
import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView nRecyclerView;
    private AdapterFavorite mAdapterFavorite;
    private ArrayList<favoriteBean.DataBean.QuestionsBean> favoriteList;
    private SwipeRefreshLayout swipeRefresh;
    private int page = 0;
    private int count = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initData();
        initReFresh();

    }


    public void initData() {
        nRecyclerView = findViewById(R.id.recycler_viewF);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteList = new ArrayList<>();
        Log.d("qid", AdapterQuestion.qidQL + "");
        NetUtil.post("http://bihu.jay86.com/getFavoriteList.php", "page=" + page + "&count=" + count + "&token=" + User.token, new NetUtil.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj1 = new JSONObject(response);
                    String data = obj1.getString("data");
                    JSONObject obj2 = new JSONObject(data);
                    String answers = obj2.getString("questions");
                    JSONArray jsonArray = new JSONArray(answers);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String authorName = obj.getString("authorName");
                        String authorAvatar = obj.getString("authorAvatar");
                        String date = obj.getString("date");
                        String title = obj.getString("title");
                        String content = obj.getString("content");
                        String images = obj.getString("images");
                        int id = obj.getInt("id");
                        favoriteBean.DataBean.QuestionsBean fDQB = new favoriteBean.DataBean.QuestionsBean();
                        fDQB.setAuthorName(authorName);
                        fDQB.setAuthorAvatar(authorAvatar);
                        fDQB.setDate(date);
                        fDQB.setContent(content);
                        fDQB.setImages(images);
                        fDQB.setId(id);
                        fDQB.setTitle(title);
                        favoriteList.add(fDQB);
                    }
                    mAdapterFavorite = new AdapterFavorite(FavoriteActivity.this , favoriteList);
                    nRecyclerView.setAdapter(mAdapterFavorite);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @SuppressLint("ResourceAsColor")
    private void initReFresh() {
        swipeRefresh = findViewById(R.id.swipe_refreshF);
        swipeRefresh.setColorSchemeColors(R.color.colorAccent);
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
                        mAdapterFavorite.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
