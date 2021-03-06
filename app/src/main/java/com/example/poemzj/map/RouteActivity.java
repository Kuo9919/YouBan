package com.example.poemzj.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.example.poemzj.R;
import com.example.poemzj.adapter.BusPathAdapter;
import com.example.poemzj.adapter.RouteDetailAdapter;
import com.example.poemzj.utils.Constants;
import com.example.poemzj.utils.MapUtils;
import com.amap.api.navi.view.RouteOverLay;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements View.OnClickListener,
        AMapNaviListener, TabLayout.OnTabSelectedListener, RouteSearch.OnRouteSearchListener,
        PoiSearch.OnPoiSearchListener{

    private static final String MY_LOCATION="????????????";
    private static final String DRIVE_TAB="Drive";
    private static final String WALK_TAB="Walk";
    private static final String RIDE_TAB="Ride";
    private static final String BUS_TAB="Bus";
    private static final int DRIVE_MODE=0;
    private static final int WALK_MODE=1;
    private static final int RIDE_MODE=2;
    private static final int BUS_MODE=3;

    private int isSearchingText= R.id.text_destination;
    private int curMode=0;
    private String city=null;

    private NaviLatLng locationDeparture=null;
    private NaviLatLng locationDestination=null;
    private List<NaviLatLng> from=new ArrayList<>();
    private List<NaviLatLng> to=new ArrayList<>();
    private List<NaviLatLng> wayPoints=new ArrayList<>();
    private RouteOverLay routeOverLay=null;
    private RouteSearch routeSearch=null;

    private TextView textDeparture=null;
    private TextView textDestination=null;
    private MapView mapView=null;
    private AMap aMap=null;
    private AMapNavi aMapNavi=null;
    private ProgressDialog loadingDialog=null;
    private TextView textEmpty=null;
    private TextView textDistance=null;
    private TextView textTime=null;
    private LinearLayout bottomSheet=null;
    private FloatingActionButton navigate=null;
    private ImageButton swap=null;
    private RecyclerView busPathList=null;
    private RecyclerView detailList=null;

    //??????????????????
    public static void startActivity(Context context, LatLng curLocation,
                                     LatLng targetLocation, String targetName, String city){
        Intent intent=new Intent(context, RouteActivity.class);
        if(curLocation!=null){
            intent.putExtra("hasCur", true);
            intent.putExtra("curLocation", curLocation);
        } else {
            intent.putExtra("hasCur", false);
        }
        if(targetLocation!=null){
            intent.putExtra("hasTarget", true);
            intent.putExtra("targetLocation", targetLocation);
            intent.putExtra("name", targetName);
        } else {
            intent.putExtra("hasTarget", false);
        }
        intent.putExtra("city", city);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        mapView=(MapView)findViewById(R.id.map_view_route);
        mapView.onCreate(savedInstanceState);
        initMap();
        initLayout();
        Intent intent=getIntent();
        if(intent.getBooleanExtra("hasTarget", false)){
            textDestination.setText(intent.getStringExtra("name"));
            locationDestination= MapUtils.convertToNaviLatLng(
                    (LatLng)intent.getParcelableExtra("targetLocation"));
        }
        if(intent.getBooleanExtra("hasCur", false)){
            textDeparture.setText(MY_LOCATION);
            locationDeparture=MapUtils.convertToNaviLatLng(
                    (LatLng)intent.getParcelableExtra("curLocation"));
        }
        city=getIntent().getStringExtra("city");
        routeSearch=new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        if(locationDeparture!=null&&locationDestination!=null) {
            aMapNavi = AMapNavi.getInstance(getApplicationContext());
            aMapNavi.addAMapNaviListener(this);
        }
    }

    //???????????????
    private void initMap(){
//        mapView.setVisibility(View.GONE);
        if(aMap==null){
            aMap=mapView.getMap();
        }
        UiSettings uiSettings=aMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        uiSettings.setAllGesturesEnabled(false);
    }

    //???????????????
    private void initLayout(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_route);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(DRIVE_TAB));
        tabLayout.addTab(tabLayout.newTab().setText(WALK_TAB));
        tabLayout.addTab(tabLayout.newTab().setText(RIDE_TAB));
        tabLayout.addTab(tabLayout.newTab().setText(BUS_TAB));
        tabLayout.addOnTabSelectedListener(this);
        TabLayout.Tab firstTab=tabLayout.getTabAt(0);
        if(firstTab!=null){
            firstTab.select();
        }
        navigate=(FloatingActionButton)findViewById(R.id.fab_navigate);
//        navigate.setVisibility(View.GONE);
        textDistance=(TextView)findViewById(R.id.text_distance);
        textTime=(TextView)findViewById(R.id.text_time);
        bottomSheet=(LinearLayout)findViewById(R.id.bottom_sheet_route);
//        bottomSheet.setVisibility(View.GONE);
        textEmpty=(TextView)findViewById(R.id.text_empty);
        textEmpty.setText(String.format("%s", "No viable route. Please try other ways."));
        textDeparture=(TextView)findViewById(R.id.text_departure);
        textDestination=(TextView)findViewById(R.id.text_destination);
        swap=(ImageButton)findViewById(R.id.button_swap);
        busPathList=(RecyclerView)findViewById(R.id.recyclerView_route);
        busPathList.setLayoutManager(new LinearLayoutManager(this));
        detailList=(RecyclerView)findViewById(R.id.recyclerView_detail);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        detailList.setLayoutManager(layoutManager);
        registerListener();
    }

    //??????????????????????????????
    private void registerListener(){
        navigate.setOnClickListener(this);
        textDeparture.setOnClickListener(this);
        textDestination.setOnClickListener(this);
        swap.setOnClickListener(this);
    }

    //?????????????????????
    private void showLoadingDialog(){
        if(loadingDialog==null){
            loadingDialog=new ProgressDialog(this);
            loadingDialog.setTitle("Please wait for a moment");
            loadingDialog.setMessage("Loading...");
            loadingDialog.setCancelable(true);
            loadingDialog.show();
        }
    }

    //?????????????????????
    private void dismissLoadingDialog(){
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialog=null;
        }
    }

    //??????????????????
    private void calculateRoute(){
        showLoadingDialog();
        switch (curMode) {
            case DRIVE_MODE:
                int strategy = 0;
                try {
                    strategy = aMapNavi.strategyConvert(false, false, false, false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                from.add(locationDeparture);
                to.add(locationDestination);
                aMapNavi.calculateDriveRoute(from, to, wayPoints, strategy);
                break;
            case WALK_MODE:
                aMapNavi.calculateWalkRoute(locationDeparture, locationDestination);
                break;
            case RIDE_MODE:
                aMapNavi.calculateRideRoute(locationDeparture, locationDestination);
                break;
            case BUS_MODE:
                RouteSearch.BusRouteQuery query=new RouteSearch.BusRouteQuery(
                        new RouteSearch.FromAndTo(MapUtils.convertToLatLonPoint(locationDeparture),
                                MapUtils.convertToLatLonPoint(locationDestination)),
                        RouteSearch.BUS_DEFAULT, city, 0);
                routeSearch.calculateBusRouteAsyn(query);
                break;
            default:
        }
    }

    //????????????????????????
    private void clearOverLay(){
        if(routeOverLay!=null){
            routeOverLay.removeFromMap();
            routeOverLay.destroy();
        }
    }

    //????????????????????????
    private void drawOverLay(AMapNaviPath path){
        routeOverLay=new RouteOverLay(aMap, path, this);
        routeOverLay.addToMap();
        routeOverLay.zoomToSpan(200);
    }

    //????????????
    private void startNavigate(){
        Intent intent = new Intent(getApplicationContext(), NavigateActivity.class);
        startActivity(intent);
    }

    //??????????????????????????????????????????????????????????????????
    private void setSearchingResult(NaviLatLng location, String name){
        switch (isSearchingText){
            case R.id.text_departure:
                locationDeparture=location;
                textDeparture.setText(name);
                break;
            case R.id.text_destination:
                locationDestination=location;
                textDestination.setText(name);
                break;
            default:
        }
    }

    //????????????????????????????????????
    private void setBusRouteView(){
        busPathList.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.GONE);
        navigate.setVisibility(View.GONE);
        textEmpty.setVisibility(View.GONE);
    }

    //????????????????????????????????????????????????
    private void setMapRouteView(){
        navigate.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.VISIBLE);
        bottomSheet.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);
        busPathList.setVisibility(View.GONE);
    }

    //???????????????????????????????????????
    private void setNoResultView(){
        mapView.setVisibility(View.GONE);
        busPathList.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.GONE);
        navigate.setVisibility(View.GONE);
        textEmpty.setVisibility(View.VISIBLE);
    }

    //??????????????????????????????
    private void resetView(){
        mapView.setVisibility(View.GONE);
        busPathList.setVisibility(View.GONE);
        bottomSheet.setVisibility(View.GONE);
        navigate.setVisibility(View.GONE);
        textEmpty.setVisibility(View.GONE);
    }

    //????????????id?????????????????????
    private void POIIdSearch(String id){
        final PoiSearch poiSearch=new PoiSearch(this, null);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIIdAsyn(id);
    }

    //???????????????????????????
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            BottomSheetBehavior behavior=BottomSheetBehavior.from(bottomSheet);
            if(behavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                finish();
            }
        }
        return false;
    }

    //??????id???????????????????????????????????????
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        if(i==1000&&poiItem!=null){
            city=poiItem.getCityName();
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    //??????????????????????????????
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        dismissLoadingDialog();
        if(i==1000){
            if(busRouteResult!=null&& busRouteResult.getPaths()!=null){
                if(busRouteResult.getPaths().size()>0){
                    BusPathAdapter adapter=new BusPathAdapter(this, busRouteResult.getPaths());
                    busPathList.setAdapter(adapter);
                    setBusRouteView();
                } else {
                    setNoResultView();
                }
            } else {
                setNoResultView();
            }
        } else {
            resetView();
            Snackbar.make(mapView, "Route searching failed. Error code "+i,
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //Tab?????????????????????
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getText()!=null && tab.getText().equals(DRIVE_TAB)){
            if(curMode!=DRIVE_MODE){
                curMode=DRIVE_MODE;
                if(locationDeparture!=null&&locationDestination!=null){
                    calculateRoute();
                }
            }
        } else if(tab.getText().equals(WALK_TAB)){
            if(curMode!=WALK_MODE){
                curMode=WALK_MODE;
                if(locationDeparture!=null&&locationDestination!=null){
                    calculateRoute();
                }
            }
        } else if(tab.getText().equals(RIDE_TAB)){
            if(curMode!=RIDE_MODE){
                curMode=RIDE_MODE;
                if(locationDeparture!=null&&locationDestination!=null){
                    calculateRoute();
                }
            }
        } else if(tab.getText().equals(BUS_TAB)){
            if(curMode!=BUS_MODE){
                curMode=BUS_MODE;
                if(locationDeparture!=null&&locationDestination!=null){
                    calculateRoute();
                }
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    //toolbar??????????????????????????????
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    //????????????????????????????????????
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_swap:
                String tmp=textDestination.getText().toString();
                textDestination.setText(textDeparture.getText());
                textDeparture.setText(tmp);
                NaviLatLng tmp_location=locationDeparture;
                locationDeparture=locationDestination;
                locationDestination=tmp_location;
                if(locationDeparture!=null&locationDestination!=null){
                    calculateRoute();
                }
                break;
            case R.id.fab_navigate:
                startNavigate();
                break;
            case R.id.text_departure:
                isSearchingText=R.id.text_departure;
                SearchActivity.startActivity(RouteActivity.this,
                        Constants.REQUEST_ROUTE_ACTIVITY, city);
                break;
            case R.id.text_destination:
                isSearchingText=R.id.text_destination;
                SearchActivity.startActivity(RouteActivity.this,
                        Constants.REQUEST_ROUTE_ACTIVITY, city);
                break;
            default:
        }
    }

    //??????????????????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constants.REQUEST_ROUTE_ACTIVITY:
                if(resultCode==RESULT_OK){
                    if(data.getIntExtra("resultType", 1)==Constants.RESULT_TIP) {
                        Tip tip=data.getParcelableExtra("result");
                        POIIdSearch(tip.getPoiID());
                        setSearchingResult(MapUtils.convertToNaviLatLng(tip.getPoint()),
                                tip.getName());

                    } else {
                        PoiItem poiItem=data.getParcelableExtra("result");
                        city=poiItem.getCityName();
                        setSearchingResult(MapUtils.convertToNaviLatLng(poiItem.getLatLonPoint()),
                                poiItem.getTitle());
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearOverLay();
        if(aMapNavi!=null) {
            aMapNavi.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if(locationDeparture!=null&&locationDestination!=null){
            if(aMapNavi!=null)
                aMapNavi.destroy();
            aMapNavi = AMapNavi.getInstance(getApplicationContext());
            aMapNavi.addAMapNaviListener(this);
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

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {
            calculateRoute();
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    //??????????????????????????????????????????????????????
    @Override
    public void onCalculateRouteSuccess() {
        dismissLoadingDialog();
        if(curMode==DRIVE_MODE) {
            to.clear();
            from.clear();
        }
        AMapNaviPath path=aMapNavi.getNaviPath();
        if(path!=null){
            clearOverLay();
            drawOverLay(path);
            String distanceStr=MapUtils.getLengthStr(path.getAllLength());
            String timeStr=MapUtils.getTimeStr(path.getAllTime());
            textDistance.setText(distanceStr);
            textTime.setText(timeStr);
            RouteDetailAdapter adapter=new RouteDetailAdapter(aMapNavi.getNaviGuideList());
            detailList.setAdapter(adapter);
            setMapRouteView();
        } else {
            setNoResultView();
        }
    }

    //????????????????????????
    @Override
    public void onCalculateRouteFailure(int i) {
        dismissLoadingDialog();
        Snackbar.make(mapView, "Route searching failed. Error code "+i,
                Snackbar.LENGTH_SHORT).show();
        resetView();
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(
            AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(
            AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(
            AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }
}
