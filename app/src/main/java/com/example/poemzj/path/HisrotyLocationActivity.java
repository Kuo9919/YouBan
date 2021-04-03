package com.example.poemzj.path;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.example.poemzj.R;

public class HisrotyLocationActivity extends AppCompatActivity {
    private AMap aMap;
    private MapView mapView;
    public String USERID, PATHID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisroty_location);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent infointent = getIntent();
        this.USERID = infointent.getStringExtra("id");
        this.PATHID = infointent.getStringExtra("path");

        TextView path_view = (TextView) findViewById(R.id.his_text_tag);
        path_view.setText(this.PATHID);

        mapView = (MapView) findViewById(R.id.his_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);// 设置指南针是否显示
        uiSettings.setZoomControlsEnabled(true);// 设置缩放按钮是否显示
        uiSettings.setScaleControlsEnabled(true);// 设置比例尺是否显示
        uiSettings.setRotateGesturesEnabled(true);// 设置地图旋转是否可用
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.6507007, 117.1140042), 11));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    

    public double calculateLineDistance(LatLng start, LatLng end) {
        if ((start == null) || (end == null)) {
            throw new IllegalArgumentException("非法坐标值，不能为null");
        }
        double d1 = 0.01745329251994329D;
        double d2 = start.longitude;
        double d3 = start.latitude;
        double d4 = end.longitude;
        double d5 = end.latitude;
        d2 *= d1;
        d3 *= d1;
        d4 *= d1;
        d5 *= d1;
        double d6 = Math.sin(d2);
        double d7 = Math.sin(d3);
        double d8 = Math.cos(d2);
        double d9 = Math.cos(d3);
        double d10 = Math.sin(d4);
        double d11 = Math.sin(d5);
        double d12 = Math.cos(d4);
        double d13 = Math.cos(d5);
        double[] arrayOfDouble1 = new double[3];
        double[] arrayOfDouble2 = new double[3];
        arrayOfDouble1[0] = (d9 * d8);
        arrayOfDouble1[1] = (d9 * d6);
        arrayOfDouble1[2] = d7;
        arrayOfDouble2[0] = (d13 * d12);
        arrayOfDouble2[1] = (d13 * d10);
        arrayOfDouble2[2] = d11;
        double d14 = Math.sqrt((arrayOfDouble1[0] - arrayOfDouble2[0]) * (arrayOfDouble1[0] - arrayOfDouble2[0])
                + (arrayOfDouble1[1] - arrayOfDouble2[1]) * (arrayOfDouble1[1] - arrayOfDouble2[1])
                + (arrayOfDouble1[2] - arrayOfDouble2[2]) * (arrayOfDouble1[2] - arrayOfDouble2[2]));

        return (Math.asin(d14 / 2.0D) * 12742001.579854401D);
    }
}
