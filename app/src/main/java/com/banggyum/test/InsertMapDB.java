package com.banggyum.test;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class InsertMapDB extends AppCompatActivity implements OnMapReadyCallback {
    private String[] array;
    private String[] address;
    private String[] roadAddress;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;    //권한 코드 번호
    private MapView mapView;
    private FusedLocationSource locationSource;
    @Nullable
    private Runnable locationActivationCallback;
    private NaverMap map;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);

        Button btnOpen = findViewById(R.id.btn_open);
        btnOpen.setOnClickListener(btnListener);

        //mapfragment 사용하여 지도를 이용
        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            //location 버튼 활성화, camera 지도의 초기 카메라 위치를 지정합니다. target - 카메라의 좌표 zoom - 카메라의 줌 레벨
            mapFragment = MapFragment.newInstance(new NaverMapOptions().locationButtonEnabled(true).camera(new CameraPosition(
                    NaverMap.DEFAULT_CAMERA_POSITION.target, NaverMap.DEFAULT_CAMERA_POSITION.zoom, 30, 45)));
            //main_frame에 mapFragment를 커밋
            getSupportFragmentManager().beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        //현재위치 사용을 위한 생성자 권한요청코드(LOCATION_PERMISSION_REQUEST_CODE) = 100
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    requestNaverLocal();
                    clickOpenBottomSheetFragment();
                }
            }).start();
        }
    };

    @SuppressLint("SetTextI18n")
    public void requestNaverLocal(){
        try{
            BufferedReader br;
            HttpURLConnection conn;
            StringBuilder sb = new StringBuilder();
            int display = 5;
            String sc = "그린팩토리";
            String addr = URLEncoder.encode(sc, "UTF-8");

            String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + addr + "&display=" + display + "&"; //
            URL url = new URL(apiURL);
            conn = (HttpURLConnection) url.openConnection();

            if(conn != null) {
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-Naver-Client-Id", "XMHmR1mRBBOwIr6_QuDz");
                conn.setRequestProperty("X-Naver-Client-Secret", "uemXpwpf0D");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();

                if(responseCode == 200){
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }else{
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;

                while((line = br.readLine()) != null){
                    sb.append(line + "\n");
                }

                String data = sb.toString();

                Log.v("결과: ", data);

                array = data.split("\"");
                address = new String[display];
                roadAddress = new String[display];

                int k = 0;
                for (int i = 0; i < array.length; i++) {
                    if (array[i].equals("address"))
                        address[k] = array[i + 2];
                    if (array[i].equals("roadAddress")){
                        roadAddress[k] = array[i + 2];
                        k++;
                    }
                }

                br.close();
                conn.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clickOpenBottomSheetFragment() {
        List<ItemObject> list = new ArrayList<>();

        for (int i =0; i< address.length; i++){
            Log.v("주소: ", address[i]);
            list.add(new ItemObject(address[i]));
        }

        MyBottomSheetFragment myBottomSheetFragment = new MyBottomSheetFragment(list, new IClickListener() {
            @Override
            public void clickItem(ItemObject itemObject) {
                Toast.makeText(InsertMapDB.this, itemObject.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        myBottomSheetFragment.show(getSupportFragmentManager(), myBottomSheetFragment.getTag());
    }

    //핸드폰의 위치 추적 권한이 활성화 되어있는지 판단하여 비활성화 면 권한을 얻기위한 코드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                map.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private double lat;
    private double lng;

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        map = naverMap;

        naverMap.setLocationSource(locationSource);
        //위치 추적 버튼 클릭시 마다 위치추적모드를 변경
        naverMap.addOnOptionChangeListener(() -> {
            LocationTrackingMode mode = naverMap.getLocationTrackingMode();
            locationSource.setCompassEnabled(mode == LocationTrackingMode.Follow || mode == LocationTrackingMode.Face);
        });
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        Marker marker = new Marker();
        naverMap.setOnMapClickListener((point, coord) -> {
            marker.setPosition(new LatLng(coord.latitude, coord.longitude));
            marker.setMap(naverMap);
            lat = coord.latitude;
            lng = coord.longitude;
            Toast.makeText(InsertMapDB.this, "위도" + lat + "경도" + lng, Toast.LENGTH_SHORT).show();
        });
    }
}

