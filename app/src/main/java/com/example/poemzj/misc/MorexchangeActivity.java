package com.example.poemzj.misc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.poemzj.R;

public class MorexchangeActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morexchange);
    }
    public void onBack(View view){
        finish();
    }
}