package com.example.alanyang.bihu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alanyang.bihu.Bean.commentBean;
import com.example.alanyang.bihu.R;
import com.example.alanyang.bihu.User;
import com.example.alanyang.bihu.Util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.DetailViewHolder> {
    private Context mContext;
    private ArrayList<commentBean.DataBean.AnswersBean> mAnswerList;

    public AdapterDetail(Context context, ArrayList<commentBean.DataBean.AnswersBean> AnswerList) {
        mContext = context;
        mAnswerList = AnswerList;
    }


    @NonNull
    @Override
    public AdapterDetail.DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new DetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailViewHolder holder, final int position) {
        commentBean.DataBean.AnswersBean currentItem = mAnswerList.get(position);

        String authorName = currentItem.getAuthorName();
        String date = currentItem.getDate();
        String content = currentItem.getContent();

        //图片使用的第三方框架:
        holder.usernameD.setText(authorName);
        holder.timeD.setText(date);
        holder.contentDText.setText(content);

        Glide.with(mContext).load(currentItem.getAuthorAvatar()).into(holder.avatarD);
        Glide.with(mContext).load(currentItem.getImages()).into(holder.contentDImage);


        //初始化点赞等的状态
        if (mAnswerList.get(position).isIs_exciting()) {
            holder.excitingD.setImageResource(R.mipmap.exciting2);
        }
        if (mAnswerList.get(position).isIs_naive()) {
            holder.naiveD.setImageResource(R.mipmap.naive2);
        }

        holder.excitingD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerList.get(position).isIs_exciting()) {     //实现取消收藏
                    NetUtil.post("http://bihu.jay86.com/cancelExciting.php", "id=" + mAnswerList.get(position).getId() + "" + "&type=2" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.excitingD.setImageResource(R.mipmap.exciting1);
                                    mAnswerList.get(position).setIs_exciting(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {        //实现点赞
                    NetUtil.post("http://bihu.jay86.com/exciting.php", "id=" + mAnswerList.get(position).getId() + "" + "&type=2" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.excitingD.setImageResource(R.mipmap.exciting2);
                                    mAnswerList.get(position).setIs_exciting(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        holder.naiveD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerList.get(position).isIs_naive()) {     //实现取消踩
                    NetUtil.post("http://bihu.jay86.com/cancelNaive.php", "id=" + mAnswerList.get(position).getId() + "" + "&type=2" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.naiveD.setImageResource(R.mipmap.naive1);
                                    mAnswerList.get(position).setIs_naive(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {        //实现踩
                    NetUtil.post("http://bihu.jay86.com/naive.php", "id=" + mAnswerList.get(position).getId() + "" + "&type=2" + "&token=" + User.token, new NetUtil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                int status = obj.getInt("status");
                                if (status == 200) {
                                    holder.naiveD.setImageResource(R.mipmap.naive2);
                                    mAnswerList.get(position).setIs_naive(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAnswerList.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarD;
        public TextView usernameD;
        public TextView timeD;
        public TextView contentDText;
        public ImageView contentDImage;
        public ImageView excitingD;
        public ImageView naiveD;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarD = itemView.findViewById(R.id.avatarD);
            usernameD = itemView.findViewById(R.id.usernameD);
            timeD = itemView.findViewById(R.id.timeD);
            contentDText = itemView.findViewById(R.id.contentDText);
            contentDImage = itemView.findViewById(R.id.contentDImage);
            excitingD = itemView.findViewById(R.id.excitingD);
            naiveD = itemView.findViewById(R.id.naiveD);


        }
    }


}
