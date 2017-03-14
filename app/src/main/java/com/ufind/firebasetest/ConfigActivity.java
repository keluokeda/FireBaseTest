package com.ufind.firebasetest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.orhanobut.logger.Logger;

public class ConfigActivity extends AppCompatActivity {

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        textView = (TextView) findViewById(R.id.tv_content);

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettings(configSettings);

        firebaseRemoteConfig.setDefaults(R.xml.config);




        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseRemoteConfig.fetch(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Logger.d("success");
                        firebaseRemoteConfig.activateFetched();
                        init();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Logger.d("fail" + e);
                    }
                });
            }
        }, 100);
    }

    private void init() {
        textView.setText(firebaseRemoteConfig.getString("welcome"));
    }
}
