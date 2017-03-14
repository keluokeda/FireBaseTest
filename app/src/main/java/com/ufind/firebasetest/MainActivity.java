package com.ufind.firebasetest;

import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";


    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference database;

    BaseQuickAdapter<Post> postBaseQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        database = FirebaseDatabase.getInstance().getReference().child("posts");
        database.keepSynced(true);

        postBaseQuickAdapter = new BaseQuickAdapter<Post>(R.layout.item_post, new ArrayList<Post>(0)) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, Post post) {
                baseViewHolder.setText(R.id.tv_title, post.title);
                long birthday = Long.parseLong(post.body) ;
                Date date = new Date(birthday);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                baseViewHolder.setText(R.id.tv_content, simpleDateFormat.format(date));
            }
        };

        recyclerView.setAdapter(postBaseQuickAdapter);

//        database.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Post post = dataSnapshot.getValue(Post.class);
//                String key = dataSnapshot.getKey();
//                Logger.d("onChildAdded key= " + key + ", value =" + post.toString());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Post post = dataSnapshot.getValue(Post.class);
//                String key = dataSnapshot.getKey();
//                Logger.d("key= " + key + ", value =" + post.toString());
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Post post = dataSnapshot.getValue(Post.class);
//                String key = dataSnapshot.getKey();
//                Logger.d("key= " + key + ", value =" + post.toString());
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                Post post = dataSnapshot.getValue(Post.class);
//                String key = dataSnapshot.getKey();
//                Logger.d("key= " + key + ", value =" + post.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        writeNewPost("1000","hankuke0","title0","content0",database);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }




    public void add(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "100");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "add");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "click");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        startActivity(new Intent(this, PostActivity.class));
    }

    public void addImage(View view) {
        startActivity(new Intent(this, ImageActivity.class));
    }

    public void config(View view) {
        startActivity(new Intent(this, ConfigActivity.class));
    }

    public void auth(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }


    public void delete(View view) {
        database.child("-KXnTm4kR8UcpHh-Ebvy").removeValue();
    }

    ChildEventListener mChildEventListener;
    int index = 1;

    public void query(View view) {
        if (mChildEventListener != null) {
            database.removeEventListener(mChildEventListener);
        }
        mChildEventListener = null;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                Logger.d("onChildAdded key= " + key + ", value =" + post.toString()+", s="+s);
                if (postBaseQuickAdapter.getData().contains(post)) {
                    return;
                }
                List<Post> posts = new ArrayList<>(1);
                posts.add(post);
                postBaseQuickAdapter.addData(posts);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Post post = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                Logger.d("onChildChanged key= " + key + ", value =" + post.toString()+", s ="+s);
                if (postBaseQuickAdapter.getData().contains(post)) {
                    int index = postBaseQuickAdapter.getData().indexOf(post);
                    postBaseQuickAdapter.getData().set(index,post);
                    postBaseQuickAdapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Post post = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                Logger.d("onChildRemoved key= " + key + ", value =" + post.toString());
                if (postBaseQuickAdapter.getData().contains(post)) {
                    int index = postBaseQuickAdapter.getData().indexOf(post);
                    postBaseQuickAdapter.remove(index);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                Logger.d("onChildMoved key= " + key + ", value =" + post.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logger.d(databaseError.getMessage());
            }
        };


        database.limitToLast(index).addChildEventListener(mChildEventListener);
        index++;

//        database.limitToLast(count).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Post post = dataSnapshot.getValue(Post.class);
//                String key = dataSnapshot.getKey();
//                Logger.d("onChildAdded key= " + key + ", value =" + post.toString());
//                List<Post> posts = new ArrayList<>(1);
//                posts.add(post);
//                postBaseQuickAdapter.addData(posts);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        count++;
    }
}
