package com.example.poemzj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.example.poemzj.utils.FriendsZone;
import com.example.poemzj.utils.JsSupport;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DiscoverActivity extends AppCompatActivity implements View.OnClickListener {
    private long firstTime = 0;
    private static final String TAG = "DiscoverActivity";
    private WebView mWebView;
    private ImageButton ibtn_back,ibtn_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        initView();

        //解决点击链接跳转浏览器问题
        mWebView.setWebViewClient(new WebViewClient());
        //js支持
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //允许访问assets目录
        settings.setAllowFileAccess(true);
        //设置WebView排版算法, 实现单列显示, 不允许横向移动
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //assets文件路径
        String path = "file:///android_asset/index.html";
        //添加Json数据
        addJson();
        //加载Html页面
        mWebView.loadUrl(path);
        

    }
    
    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
        ibtn_back= (ImageButton) findViewById(R.id.ibtn_back_moment);
        ibtn_more= (ImageButton) findViewById(R.id.ibtn_release_moment);
        ibtn_back.setOnClickListener(this);
        ibtn_more.setOnClickListener(this);
    }
    private void addJson() {
        JsSupport jsSupport = new JsSupport(this);
        List<FriendsZone> zones = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            zones.add(new FriendsZone("Admin" + i, "images/icon.png", "这里是Html测试数据, 这里是Html测试数据, 这里是Html测试数据" + i));
        }
        Gson gson = new Gson();
        String json = gson.toJson(zones);
        Log.d(TAG, "addJson: json => " + json);
        jsSupport.setJson(json);
        //添加js交互接口, 并指明js中对象的调用名称
        mWebView.addJavascriptInterface(jsSupport, "weichat");
    }

    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back_moment:
                finish();
                break;
            case R.id.ibtn_release_moment:
                Intent intent=new Intent(DiscoverActivity.this,PostActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}