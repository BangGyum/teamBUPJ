package com.banggyum.test;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class InsertMapDB extends AppCompatActivity implements OnMapReadyCallback {

    //location 버튼을 클릭시 현재위치를 찾을지에 대한 다이아로그를 보여주기 위한 클래스
    public static class LocationConfirmDialogFragment extends DialogFragment {
        @SuppressLint("DialogFragmentCallbacksDetector")
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.location_activation_confirm)                    //dialog에 보여질 메세지
                    .setPositiveButton(R.string.yes, (dialog, whichButton) -> {        //dialog에 네(yes) 버튼 추가
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((InsertMapDB)activity).continueLocationTracking(); //클릭시 tracking 모드 활성화
                        }
                    })
                    .setNegativeButton(R.string.no, (dialog, whichButton) -> {        //dialog에 아니오(no) 버튼 추가
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((InsertMapDB)activity).cancelLocationTracking();  //클릭시 tracking 모드 활성화 취소
                        }
                    })
                    .setOnCancelListener(dialog -> {
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((InsertMapDB)activity).cancelLocationTracking();
                        }
                    })
                    .create();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    BottomNavigationView bottomNavigationView; //네비게이션뷰
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

        //화면 맨 위에 뒤로가기 버튼 생성
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //mapfragment 사용하여 지도를 이용
        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            //location 버튼 활성화, camera 지도의 초기 카메라 위치를 지정합니다. target - 카메라의 좌표 zoom - 카메라의 줌 레벨
            mapFragment = MapFragment.newInstance(new NaverMapOptions().locationButtonEnabled(true).camera(new CameraPosition(
                    NaverMap.DEFAULT_CAMERA_POSITION.target, NaverMap.DEFAULT_CAMERA_POSITION.zoom, 30, 45)));
            //main_frame에 mapFragment를 커밋
            getSupportFragmentManager().beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        //현재위치 사용을 위한 생성자 권한요청코드(LOCATION_PERMISSION_REQUEST_CODE) = 100
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        //setActivationHook 위치 기능의 활성화에 대한 훅 객체를 지정합니다.
        locationSource.setActivationHook(continueCallback -> {
            locationActivationCallback = continueCallback;
            new InsertMapDB.LocationConfirmDialogFragment().show(getSupportFragmentManager(), null);
        });

        mapFragment.getMapAsync(this);
    }

    //위치 추적 모드를 run을 통해 실행후 재 실행 방지를 위해 null로 바꿔준다.
    private void continueLocationTracking() {
        if (locationActivationCallback != null) {
            locationActivationCallback.run();
            locationActivationCallback = null;
            locationSource.setActivationHook(null);
        }
    }

    //위치 추적 모드를 지정 none, follow, face
    private void cancelLocationTracking() {
        map.setLocationTrackingMode(LocationTrackingMode.None);
    }

    // 뒤로가기 버튼 클릭시 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {        //R.id.home = 뒤로가기
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

