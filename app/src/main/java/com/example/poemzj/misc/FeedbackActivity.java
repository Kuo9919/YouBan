package com.example.poemzj.misc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.poemzj.R;

public class FeedbackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
    }
    public void onFeedback(View view){
        Toast.makeText(this,"已提交",Toast.LENGTH_LONG).show();
        finish();
    }
    public void onBack(View view){
        finish();
    }
}
