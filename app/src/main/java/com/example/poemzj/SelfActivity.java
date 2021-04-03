package com.example.poemzj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poemzj.map.MapActivity;
import com.example.poemzj.misc.AboutActivity;
import com.example.poemzj.misc.FeedbackActivity;
import com.example.poemzj.misc.MorexchangeActivity;
import com.example.poemzj.net.IntentUtils;
import com.example.poemzj.path.PathResultActivity;
import com.example.poemzj.path.RecordActivity;
import com.example.poemzj.adapter.DBOpenHelper;
import com.example.poemzj.user.User;
import com.example.poemzj.user.UserInfoActivity;

import java.util.ArrayList;

public class SelfActivity extends AppCompatActivity implements View.OnClickListener {
    private long firstTime = 0;
    
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View vheadviewSelf;
    private TextView tv_spoor,tv_exmore;
    private DBOpenHelper dbOpenHelper;
    private ImageView imageViewHead;
    private TextView tvNavHeaderUserName,tvOneSign;
    private TextView tvUsername;
    private Button btn1,btn2,btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);
        
        initView();
        dbOpenHelper=new DBOpenHelper(this);
        ArrayList<User> data = dbOpenHelper.getAllData();
        User user=data.get(LoginActivity.IDNUM);
        
        tvUsername.setText(user.getName());
        tvNavHeaderUserName.setText(user.getName());
//        tvOneSign.setText(user.getSign());
    }
    private void initView(){
        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        navigationView= (NavigationView) findViewById(R.id.navigate_view_self);
        vheadviewSelf=navigationView.getHeaderView(0);
        tv_spoor = (TextView) findViewById(R.id.tv_spoor);
        tvUsername= (TextView) findViewById(R.id.tv_self_username);
        tv_exmore= (TextView) findViewById(R.id.tv_exchange_self);
        tv_exmore.setOnClickListener(this);

        btn1= (Button) findViewById(R.id.btn1_bottom_self);
        btn2= (Button) findViewById(R.id.btn2_bottom_self);
        btn3= (Button) findViewById(R.id.btn3_bottom_self);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        
        tvNavHeaderUserName= (TextView) vheadviewSelf.findViewById(R.id.tv_navheader_username);
        tvOneSign= (TextView) vheadviewSelf.findViewById(R.id.tv_navheader_onesign);
    
        toolbar.setTitle("");
        toolbar.setTitleTextAppearance(this,R.style.Toolbar_TitleText);
        toolbar.setSubtitleTextAppearance(this,R.style.Toolbar_SubTitleText);
        setSupportActionBar(toolbar);
        setNavigationItem();
    }
    public void setNavigationItem(){
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_about:
                        IntentUtils.SetIntent(SelfActivity.this, AboutActivity.class);
                        break;
                    case R.id.nav_feed:
                        IntentUtils.SetIntent(SelfActivity.this, FeedbackActivity.class);
                        break;
                    case R.id.nav_share:
                        Intent share_intent=new Intent(Intent.ACTION_SEND);
                        share_intent.setType("text/plain");
                        share_intent.putExtra("分享内容",Intent.EXTRA_TEXT);
                        startActivity(share_intent);
                        break;
                    
                    case R.id.nav_map:      //位置共享
                        IntentUtils.SetIntent(SelfActivity.this, MapActivity.class);
                        break;
                    case R.id.nav_record:   //记录足迹
                        IntentUtils.SetIntent(SelfActivity.this, RecordActivity.class);
                        break;

                    
                    case R.id.nav_loginout:
                        IntentUtils.SetIntent(SelfActivity.this, LoginActivity.class);
                        finish();
                        break;
                        
                    default:

                }
                return true;
            
            }
        });
    }
    public void startUserInfo(View view) {
        Intent intent = new Intent(SelfActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }
    public void onExpandMenu(View view){
        drawer.openDrawer(GravityCompat.START);
    }
    public void onSetting(View view){
        Intent intent=new Intent(SelfActivity.this,SetActivity.class);
        startActivity(intent);
    }
    
    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.btn1_bottom_self:
                intent = new Intent(SelfActivity.this, MessageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn2_bottom_self:
                intent = new Intent(SelfActivity.this, DiscoverActivity.class);
                startActivity(intent);
                
                break;
            case R.id.btn3_bottom_self:         //到当前页面,不必跳转
//                Toast.makeText(this,"Local",Toast.LENGTH_LONG).show();
//                intent = new Intent(SelfActivity.this, SelfActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_exchange_self:
                intent = new Intent(SelfActivity.this, MorexchangeActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(SelfActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        }
        else {
            finish();
        }
    }
}