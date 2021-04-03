package com.example.poemzj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
    }
    public void onBack(View view){
        finish();
    }
}