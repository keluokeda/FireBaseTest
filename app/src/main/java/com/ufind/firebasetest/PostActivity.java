package com.ufind.firebasetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
    }


    public void commit(View view) {
        EditText etTitle = (EditText) findViewById(R.id.et_title);


        String title = etTitle.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        writeNewPost("100", "hankuke", title, databaseReference);
    }

    private void writeNewPost(String userId, String username, String title, DatabaseReference mDatabase) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(key, userId, username, title, String.valueOf(System.currentTimeMillis()));
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
//        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }
}
