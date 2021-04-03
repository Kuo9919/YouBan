package com.example.poemzj;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_cancel;
    private Button btn_post;
    private ImageButton ibtn_addimage;
    private TextView tv_getgps,tv_aite;
    private ImageView iv_getgps,iv_aite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        initView();
    }
    @SuppressLint("WrongViewCast")
    private void initView(){
        tv_cancel= (TextView) findViewById(R.id.tv_cancel_post);
        tv_cancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_post= (Button) findViewById(R.id.btn_post_post);
        tv_cancel.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        ibtn_addimage= (ImageButton) findViewById(R.id.ibtn_addimage_post);
        ibtn_addimage.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel_post:
                finish();
                break;
            case R.id.btn_post_post:
                //此处为发布添加json代码
                finish();
                break;
            case R.id.ibtn_addimage_post:
                Toast.makeText(this,"此处添加图片",Toast.LENGTH_LONG).show();
                break;

            default:
        }
    }
}