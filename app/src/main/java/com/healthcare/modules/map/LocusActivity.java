package com.healthcare.modules.map;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.healthcare.R;
import com.healthcare.common.Constants;
import com.healthcare.modules.modle.locus;
import com.socks.library.KLog;

import java.util.Date;
import java.util.List;

/**
 * project     healthcare
 * <p/>
 * 混合定位
 *
 * @author hewei
 * @verstion 15/12/1
 */
public class LocusActivity extends Activity implements LocationSource, AMapLocationListener {

    private AMap map;
    private MapView mapView;
    private Marker mPosMakrer;
    private OnLocationChangedListener mLocationChangedListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    private Handler mMainHandler, mThreadHandler;
    private RecorderThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locus_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        //
        mThread = new RecorderThread();
        mThread.start();
        mThreadHandler = mThread.getHandler();


        if (map == null && mapView != null) {
            map = mapView.getMap();
        }

        mPosMakrer = map.addMarker(
                new MarkerOptions().icon(
                        BitmapDescriptorFactory
                                .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker))
                ).anchor((float) 0.5, (float) 0.5).setFlat(true));

        map.setLocationSource(this);// 设置定位监听
        map.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        map.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        map.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);

        //loadShow();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mThread.getHandler().removeCallbacksAndMessages(null);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mLocationChangedListener = listener;

        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getApplicationContext());
            mLocationOption = new AMapLocationClientOption();

            mlocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(Constants.SAMPLING_RATE);
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mLocationChangedListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }

        mlocationClient = null;
    }

    private LatLng lastPos ;
    private LatLng currentPos;
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // from amp location
        if (mLocationChangedListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mLocationChangedListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                String str = "lat:" + aMapLocation.getLatitude() + ", long:" + aMapLocation.getLongitude() + ", loctionTYpe:" + aMapLocation.getLocationType();

                currentPos = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                if (lastPos == null){
                    lastPos = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    saveToDb(aMapLocation);
                    showLocus(lastPos);
                }else {

                    if (AMapUtils.calculateLineDistance(lastPos, currentPos) >= 10){
                        showLocus(currentPos);
                        saveToDb(aMapLocation);

                        lastPos = currentPos;
                    }
                }

            } else {
                KLog.d(aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo());
            }
        }
    }


    private void saveToDb(AMapLocation aMapLocation){
        if (mThreadHandler != null) {
            locus tmpLocus = new locus(System.currentTimeMillis(), aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getAccuracy(), new Date(), "hewei");
            KLog.d(tmpLocus.toString());
            Bundle bundle = new Bundle();
            bundle.putParcelable("loc", tmpLocus);
            Message message = mThreadHandler.obtainMessage();
            message.setData(bundle);
            mThreadHandler.sendMessage(message);
        } else {
            mThreadHandler = mThread.getHandler();
        }

    }

    class RecorderThread extends Thread {

        private Handler handler;

        public Handler getHandler() {
            return handler;
        }

        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg != null) {
                        Bundle bundle = msg.getData();
                        locus locus = (locus) bundle.get("loc");
                        KLog.d(locus.toString());
                        if (locus != null) {
                            Constants.locusDao.insert(locus);
                        }
                    }
                }
            };

            Looper.loop();
        }
    }


    private PolylineOptions polylineOptions;
    private Polyline polyline;

    private void showLocus(LatLng lglat){
        if (polylineOptions == null){
            polylineOptions = new PolylineOptions();
            polylineOptions.width(10).setDottedLine(false).geodesic(false).color(Color.RED);
        }

        //polyline = map.addPolyline(polylineOptions);
/*
        if (!isTest){
            polylineOptions.add(lglat, new());
            //polylineOptions.add(new LatLng(43.828, 87.621), new LatLng(45.808, 126.55));
            isTest = true;
            map.addPolyline(polylineOptions);
        }*/

        polylineOptions.add(lglat);
        map.addPolyline(polylineOptions);
        //polylineOptions.add(new LatLng(43.828, 87.621), new LatLng(45.808, 126.55));
    }


    public void loadShow(){
        List<locus> locusList = Constants.locusDao.loadAll();

        for (int i = 0; i < locusList.size(); i++){
            showLocus(new LatLng(locusList.get(i).getLattitude(), locusList.get(i).getLongtitude()));
        }
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
