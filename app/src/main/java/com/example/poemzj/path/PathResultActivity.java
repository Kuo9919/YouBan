package com.example.poemzj.path;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.poemzj.misc.MorexchangeActivity;
import com.example.poemzj.R;
import com.example.poemzj.adapter.DBOpenHelper;
import com.example.poemzj.LoginActivity;
import com.example.poemzj.user.User;

import java.util.ArrayList;

public class PathResultActivity extends AppCompatActivity implements View.OnClickListener {
    DBOpenHelper dbOpenHelper;
    private TextView tvUsername;
    private TextView tvTotalNum,tv_more;
    private ImageButton btn_share;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_result);
        initView();
        dbOpenHelper=new DBOpenHelper(this);
        ArrayList<User> data = dbOpenHelper.getAllData();
        User user=data.get(LoginActivity.IDNUM);
        tvUsername.setText(user.getName());
    }
    private void initView(){
        tvUsername= (TextView) findViewById(R.id.tv_username_recordresult);
        tvTotalNum= (TextView) findViewById(R.id.tv_totalnum_result);
        btn_share= (ImageButton) findViewById(R.id.ibtn_share_result);
        tv_more= (TextView) findViewById(R.id.tv_morexchange_result);
        
        btn_share.setOnClickListener(this);
        tv_more.setOnClickListener(this);
    }
    public void onBack(View view){
        finish();
    }
    
    @Override
    public void onClick(View view) {
        Intent ex_intent=null;
        
        switch (view.getId()){
            case R.id.ibtn_share_result:
                Intent share_intent=new Intent(Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra("分享内容",Intent.EXTRA_TEXT);
                startActivity(share_intent);
                break;
            case R.id.tv_morexchange_result :
                ex_intent=new Intent(PathResultActivity.this, MorexchangeActivity.class);
                startActivity(ex_intent);
                break;
            case R.id.ll_ex1_result :
                ex_intent=new Intent(PathResultActivity.this, MorexchangeActivity.class);
                startActivity(ex_intent);
                break;
            case R.id.ll_ex2_result :
                ex_intent=new Intent(PathResultActivity.this, MorexchangeActivity.class);
                startActivity(ex_intent);
                break;
            case R.id.ll_ex3_result :
                ex_intent=new Intent(PathResultActivity.this, MorexchangeActivity.class);
                startActivity(ex_intent);
                break;
            default:
        }
    }
}
