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
import com.example.alanyang.bihu.Bean.favoriteBean;
import com.example.alanyang.bihu.R;

import java.util.ArrayList;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.FavoriteViewHolder> {
    private Context mContext;
    private ArrayList<favoriteBean.DataBean.QuestionsBean> mFavoriteList;

    public AdapterFavorite(Context context, ArrayList<favoriteBean.DataBean.QuestionsBean> favoriteList) {
        mContext = context;
        mFavoriteList = favoriteList;
    }


    @Override
    public AdapterFavorite.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavorite.FavoriteViewHolder holder, int position) {
        favoriteBean.DataBean.QuestionsBean currentItem = mFavoriteList.get(position);

        String authorName = currentItem.getAuthorName();
        String date = currentItem.getDate();
        String title = currentItem.getTitle();
        String content = currentItem.getContent();

        holder.usernameF.setText(authorName);
        holder.timeF.setText(date);
        holder.titleF.setText(title);
        holder.contentFText.setText(content);
        Glide.with(mContext).load(currentItem.getAuthorAvatar()).into(holder.avatarF);
        Glide.with(mContext).load(currentItem.getImages()).into(holder.contentFImage);

    }

    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {



        public ImageView avatarF;
        public TextView usernameF;
        public TextView timeF;
        public TextView contentFText;
        public ImageView contentFImage;
        public ImageView excitingF;
        public TextView excitingNumberF;
        public ImageView naiveF;
        public TextView naiveNumberF;
        public ImageView commentF;
        public ImageView favoriteF;
        public TextView titleF;


        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarF = itemView.findViewById(R.id.avatarF);
            usernameF = itemView.findViewById(R.id.usernameF);
            timeF = itemView.findViewById(R.id.timeF);
            contentFText = itemView.findViewById(R.id.contentFText);
            contentFImage = itemView.findViewById(R.id.contentFImage);
            excitingF = itemView.findViewById(R.id.excitingF);
            naiveF = itemView.findViewById(R.id.naiveF);
            excitingNumberF = itemView.findViewById(R.id.excitingNumberF);
            naiveNumberF = itemView.findViewById(R.id.naiveNumberF);
            commentF = itemView.findViewById(R.id.commentF);
            favoriteF = itemView.findViewById(R.id.favoriteF);
            titleF = itemView.findViewById(R.id.titleF);
        }
    }
}
