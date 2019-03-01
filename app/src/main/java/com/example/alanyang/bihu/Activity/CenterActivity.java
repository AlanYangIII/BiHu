package com.example.alanyang.bihu.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alanyang.bihu.Adapter.AdapterQuestion;
import com.example.alanyang.bihu.Bean.JsonBean;
import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class CenterActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AdapterQuestion mAdaterQusetion;
    private ArrayList<JsonBean.DataBean.QuestionsBean> questionsBeanArrayList;
    private String Url = "http://bihu.jay86.com/getQuestionList.php";
    private int page = 0;
    private int count = 20;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefresh;
    private ImageView avatar;
    private TextView username;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        header = navView.inflateHeaderView(R.layout.nav_header);
        avatar = header.findViewById(R.id.Avatar);
        username = header.findViewById(R.id.username);
        username.setText(User.username);

        initFab();
        initData();
        initReFresh();



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //点击事件

                switch (item.getItemId()) {

                    case R.id.Key:
                        Intent intent1 = new Intent(CenterActivity.this, ChangepwdActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.Logout:
                        Intent intent2 = new Intent(CenterActivity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.AvatarItem:
                        Intent intent3 = new Intent(CenterActivity.this, ChangeAvatarActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.Question:
                        Intent intent4 = new Intent(CenterActivity.this , AskQuestionActivity.class);
                        startActivity(intent4);

                    case R.id.Favorate:
                        Intent intent5 = new Intent(CenterActivity.this,FavoriteActivity.class);
                        startActivity(intent5);

                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(User.avatar != null){
            Glide.with(this).load(User.avatar).into(avatar);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void initReFresh() {
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary);
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
                        mAdaterQusetion.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


    private void initData() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsBeanArrayList = new ArrayList<>();
        NetUtil.post(Url, "page=" + page + "&count=" + count + "&token=" + User.token, new NetUtil.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("CA1", response);
                    JSONObject obj1 = new JSONObject(response);
                    String data = obj1.getString("data");
                    JSONObject obj2 = new JSONObject(data);
                    String questions = obj2.getString("questions");
                    JSONArray jsonArray = new JSONArray(questions);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String authorName = obj.getString("authorName");
                        String authorAvatar = obj.getString("authorAvatar");
                        String date = obj.getString("date");
                        String title = obj.getString("title");
                        String content = obj.getString("content");
                        String images = obj.getString("images");
                        int id = obj.getInt("id");
                        int exciting = obj.getInt("exciting");
                        int naive = obj.getInt("naive");
                        boolean is_exciting = obj.getBoolean("is_exciting");
                        boolean is_naive = obj.getBoolean("is_naive");
                        boolean is_favorite = obj.getBoolean("is_favorite");
                        final JsonBean.DataBean.QuestionsBean jDQB = new JsonBean.DataBean.QuestionsBean();
                        jDQB.setAuthorName(authorName);
                        jDQB.setAuthorAvatar(authorAvatar);
                        jDQB.setDate(date);
                        jDQB.setTitle(title);
                        jDQB.setContent(content);
                        jDQB.setImages(images);
                        jDQB.setId(id);
                        jDQB.setExciting(exciting);
                        jDQB.setNaive(naive);
                        jDQB.setIs_exciting(is_exciting);
                        jDQB.setIs_naive(is_naive);
                        jDQB.setIs_favorite(is_favorite);
                        questionsBeanArrayList.add(jDQB);
                        Log.d("CA2", jDQB.toString());
                    }

                    Log.d("CA3", questionsBeanArrayList.toString());

                    mAdaterQusetion = new AdapterQuestion(CenterActivity.this, questionsBeanArrayList);
                    mRecyclerView.setAdapter(mAdaterQusetion);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void initFab() {     //悬浮按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CenterActivity.this, AskQuestionActivity.class);
                startActivity(intent);
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }


}
