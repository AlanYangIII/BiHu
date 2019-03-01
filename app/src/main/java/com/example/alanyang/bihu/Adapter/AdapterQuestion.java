package com.example.alanyang.bihu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.alanyang.bihu.Activity.AnswerActivity;
import com.example.alanyang.bihu.Activity.DetailActivity;
import com.example.alanyang.bihu.Bean.JsonBean;
import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.QuestionListViewHolder> {
    private Context mContext;
    private ArrayList<JsonBean.DataBean.QuestionsBean> mQuestionList;
    public static int qidQL;
    public static int qidA;


    public AdapterQuestion(Context context, ArrayList<JsonBean.DataBean.QuestionsBean> questionList) {
        mContext = context;
        mQuestionList = questionList;
    }

    @NonNull
    @Override
    public QuestionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.question_item, parent, false);
        return new QuestionListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionListViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final JsonBean.DataBean.QuestionsBean currentItem = mQuestionList.get(position);


        String authorName = currentItem.getAuthorName();
        String date = currentItem.getDate();
        String title = currentItem.getTitle();
        String content = currentItem.getContent();

        Log.d("AQL1", authorName);

        //初始化点赞等的状态
        if (mQuestionList.get(position).isIs_exciting()) {
            holder.excitingQL.setImageResource(R.mipmap.exciting2);
        }
        if (mQuestionList.get(position).isIs_naive()) {
            holder.naiveQL.setImageResource(R.mipmap.naive2);
        }
        if (mQuestionList.get(position).isIs_favorite()) {
            holder.favoriteQL.setImageResource(R.mipmap.favorite2);
        }


        //图片使用的第三方框架:
        holder.usernameQL.setText(authorName);
        holder.timeQL.setText(date);
        holder.titleQL.setText(title);
        holder.contentQLText.setText(content);
        Glide.with(mContext).load(currentItem.getAuthorAvatar()).into(holder.avatarQL);
        Glide.with(mContext).load(currentItem.getImages()).into(holder.contentQLImage);


        holder.commentQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qidA = mQuestionList.get(position).getId();
                Intent intentM = new Intent(mContext,AnswerActivity.class);
                mContext.startActivity(intentM);
            }
        });


        holder.excitingQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionList.get(position).isIs_exciting()) {     //实现取消收藏
                    NetUtil.post("http://bihu.jay86.com/cancelExciting.php", "id=" + mQuestionList.get(position).getId() + "" + "&type=1" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.excitingQL.setImageResource(R.mipmap.exciting1);
                                    mQuestionList.get(position).setIs_exciting(false);
//                                    Toast.makeText(mContext, "取消点赞成功!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {        //实现点赞
                    NetUtil.post("http://bihu.jay86.com/exciting.php", "id=" + mQuestionList.get(position).getId() + "" + "&type=1" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.excitingQL.setImageResource(R.mipmap.exciting2);
                                    mQuestionList.get(position).setIs_exciting(true);
//                                    Toast.makeText(mContext, "点赞成功!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        holder.naiveQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionList.get(position).isIs_naive()) {     //实现取消踩
                    NetUtil.post("http://bihu.jay86.com/cancelNaive.php" , "id=" + mQuestionList.get(position).getId() + "" +"&type=1" + "&token=" + User.token , new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.naiveQL.setImageResource(R.mipmap.naive1);
                                    mQuestionList.get(position).setIs_naive(false);
//                                    Toast.makeText(mContext, "取消踩成功!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {        //实现踩
                    NetUtil.post("http://bihu.jay86.com/naive.php", "id=" + mQuestionList.get(position).getId() + "" +"&type=1" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.naiveQL.setImageResource(R.mipmap.naive2);
                                    mQuestionList.get(position).setIs_naive(true);
//                                    Toast.makeText(mContext, "踩成功!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        holder.favoriteQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionList.get(position).isIs_favorite()) {     //实现取消收藏
                    NetUtil.post("http://bihu.jay86.com/cancelFavorite.php", "qid=" + mQuestionList.get(position).getId() + "" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.favoriteQL.setImageResource(R.mipmap.favorite1);
                                    mQuestionList.get(position).setIs_favorite(false);
//                                    Toast.makeText(mContext, "取消收藏成功!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {        //实现收藏
                    NetUtil.post("http://bihu.jay86.com/favorite.php", "qid=" + mQuestionList.get(position).getId() + "" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.favoriteQL.setImageResource(R.mipmap.favorite2);
                                    mQuestionList.get(position).setIs_favorite(true);
//                                    Toast.makeText(mContext, "收藏成功!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qidQL = mQuestionList.get(position).getId();
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("avatar",currentItem.getAuthorAvatar());
                intent.putExtra("username",currentItem.getAuthorName());
                intent.putExtra("title",currentItem.getTitle());
                intent.putExtra("time",currentItem.getDate());
                intent.putExtra("text",currentItem.getContent());
                intent.putExtra("image",currentItem.getImages());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }


    public class QuestionListViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarQL;
        public TextView usernameQL;
        public TextView timeQL;
        public TextView contentQLText;
        public ImageView contentQLImage;
        public ImageView excitingQL;
        public TextView excitingNumberQL;
        public ImageView naiveQL;
        public TextView naiveNumberQL;
        public ImageView commentQL;
        public ImageView favoriteQL;
        public TextView titleQL;



        public QuestionListViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarQL = itemView.findViewById(R.id.avatarQL);
            usernameQL = itemView.findViewById(R.id.usernameQL);
            timeQL = itemView.findViewById(R.id.timeQL);
            contentQLText = itemView.findViewById(R.id.contentQLText);
            contentQLImage = itemView.findViewById(R.id.contentQLImage);
            excitingQL = itemView.findViewById(R.id.excitingQL);
            naiveQL = itemView.findViewById(R.id.naiveQL);
            excitingNumberQL = itemView.findViewById(R.id.excitingNumberQL);
            naiveNumberQL = itemView.findViewById(R.id.naiveNumberQL);
            commentQL = itemView.findViewById(R.id.commentQL);
            favoriteQL = itemView.findViewById(R.id.favoriteQL);
            titleQL = itemView.findViewById(R.id.titleQL);

        }
    }


}
