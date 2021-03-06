package com.example.poemzj.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.poemzj.R;
import com.example.poemzj.utils.Constants;
import com.example.poemzj.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements AMap.OnPOIClickListener,
        AMap.OnMyLocationChangeListener, View.OnClickListener,
        GeocodeSearch.OnGeocodeSearchListener, NavigationView.OnNavigationItemSelectedListener,
        PoiSearch.OnPoiSearchListener {
    
    private Marker marker = null;
    private String marker_title = null;
    private String city = Constants.DEFAULT_CITY;
    private LatLng curLocation = null;
    
    private boolean isFirstLocate = true;
    private boolean isFirstLocateFailed = false;
    private boolean isBackClickOnce = false;
    private boolean isOnResultBack = false;
    
    private MapView mapView = null;
    private AMap aMap = null;
    
    private NestedScrollView bottomSheet = null;
    private TextView textName = null;
    private TextView textDistance = null;
    private DrawerLayout drawerLayout = null;
    private FloatingActionButton locate = null;
    private FloatingActionButton plan = null;
    private TextView search = null;
    private ImageButton menu = null;
    private NavigationView navigationView = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        checkPermission();
    }
    
    //??????????????????
    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.
                READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.
                RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
        }
        else {
            init();
        }
    }
    
    private void init() {
        initLayout();
        initMap();
    }
    
    //???????????????
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0)).radiusFillColor(Color.argb(0, 0, 0, 0)).
                interval(10000);
        aMap.setMyLocationStyle(myLocationStyle);
        registerMapListener();
    }
    
    //???????????????
    private void initLayout() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        locate = (FloatingActionButton) findViewById(R.id.fab_locate);
        plan = (FloatingActionButton) findViewById(R.id.fab_plan);
        textName = (TextView) findViewById(R.id.text_name);
        textName.setText("????????????");
        textDistance = (TextView) findViewById(R.id.text_distance);
        bottomSheet = (NestedScrollView) findViewById(R.id.bottom_sheet_main);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        search = (TextView) findViewById(R.id.search);
        menu = (ImageButton) findViewById(R.id.expanded_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigate_view);
        navigationView.setCheckedItem(R.id.map_standard);
        registerLayoutListener();
    }
    
    //????????????????????????
    private void registerMapListener() {
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnPOIClickListener(this);
    }
    
    //??????????????????????????????
    private void registerLayoutListener() {
        locate.setOnClickListener(this);
        plan.setOnClickListener(this);
        search.setOnClickListener(this);
        menu.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }
    
    //????????????????????????
    private void setMarkerLayout(LatLng location, String name) {
        if (marker != null) {
            marker.remove();
        }
        marker = aMap.addMarker(new MarkerOptions().position(location).draggable(false));
//        geocodeSearch(MapUtils.convertToLatLonPoint(location));
        marker_title = name;
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        textName.setText(marker_title);
        if (curLocation != null) {
            float distance = AMapUtils.calculateLineDistance(curLocation, location);
            textDistance.setText(String.format("%s",
                    "??????" + MapUtils.getLengthStr(distance)));
        }
        else {
            textDistance.setText("????????????");
        }
    }
    
    //???????????????????????????
    private void geocodeSearch(LatLonPoint location) {
        final GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        final RegeocodeQuery query = new RegeocodeQuery(location, 50, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }
    
    //????????????id?????????????????????
    private void POIIdSearch(String id) {
        final PoiSearch poiSearch = new PoiSearch(this, null);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIIdAsyn(id);
    }
    
    //??????id???????????????????????????????????????
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        if (i == 1000 && poiItem != null) {
            city = poiItem.getCityName();
        }
    }
    
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
    
    }
    
    //??????????????????????????????????????????????????????????????????
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_standard:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.map_night:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.map_satellite:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
    
    //???????????????????????????
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000) {
            city = regeocodeResult.getRegeocodeAddress().getCity();
        }
    }
    
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    
    }
    
    //?????????????????????????????????
    @Override
    public void onPOIClick(Poi poi) {
        setMarkerLayout(poi.getCoordinate(), poi.getName());
        aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(poi.getCoordinate().latitude,
                poi.getCoordinate().longitude)));
    }
    
    //??????????????????????????????
    @Override
    public void onMyLocationChange(Location location) {
        if (location != null && location.getExtras().getInt("errorCode", 1) == 0) {
            curLocation = MapUtils.convertToLatLng(location);
            if (isFirstLocate || isFirstLocateFailed) {
                isFirstLocate = false;
                isFirstLocateFailed = false;
                geocodeSearch(MapUtils.convertToLatLonPoint(location));
                aMap.animateCamera(CameraUpdateFactory.
                        newLatLngZoom(new LatLng(location.getLatitude(),
                                location.getLongitude()), 16f));
            }
        }
        else {
            if (isFirstLocate) {
                isFirstLocate = false;
                isFirstLocateFailed = true;
                Snackbar.make(mapView, "Locate failed. Please check your settings.",
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    
    //?????????????????????????????????
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_locate:
                if (curLocation != null) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLng(curLocation));
                    geocodeSearch(MapUtils.convertToLatLonPoint(curLocation));
                }
                else {
                    Snackbar.make(mapView, "Locating failed. Please check your settings.",
                            Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.fab_plan:
                if (marker != null) {
                    RouteActivity.startActivity(this, curLocation, marker.getPosition(),
                            marker_title, city);
                }
                else {
                    RouteActivity.startActivity(MapActivity.this, curLocation, null, null, city);
                }
                break;
            case R.id.search:
                SearchActivity.startActivity(MapActivity.this,
                        Constants.REQUEST_MAIN_ACTIVITY, city);
                break;
            case R.id.expanded_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            default:
        }
    }
    public void onBack(View view){
        finish();
    }
    
    //????????????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MapActivity.this, "You denied the permission",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    init();
                }
                else {
                    Toast.makeText(MapActivity.this, "Unknown mistake",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    
    //??????????????????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_MAIN_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getIntExtra("resultType", 1) == Constants.RESULT_TIP) {
                        Tip tip = data.getParcelableExtra("result");
                        POIIdSearch(tip.getPoiID());
                        setMarkerLayout(MapUtils.convertToLatLng(tip.getPoint()), tip.getName());
                        isOnResultBack = true;
                    }
                    else {
                        PoiItem poiItem = data.getParcelableExtra("result");
                        city = poiItem.getCityName();
                        setMarkerLayout(MapUtils.convertToLatLng(poiItem.getLatLonPoint()),
                                poiItem.getTitle());
                        isOnResultBack = true;
                    }
                }
                break;
            default:
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (isOnResultBack && marker != null) {
            aMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            isOnResultBack = false;
        }
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
}
