package com.example.poemzj.misc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.poemzj.R;
import com.example.poemzj.LoginActivity;
import com.example.poemzj.SelfActivity;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_DISPLAY_LENGHT= 3000;    //延迟6秒
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);//去掉标题
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();   //关闭splashActivity，将其回收，否则按返回键会返回此界面
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
