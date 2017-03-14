package com.ufind.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvContent;

    public PostViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);
    }

    public void bindModel(Post model) {
        tvTitle.setText(model.title);
        tvContent.setText(model.body);
    }
}
