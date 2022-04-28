package com.banggyum.test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.ZoomControlView;

import java.security.acl.Permission;

public class MapsNaverActivity extends AppCompatActivity implements OnMapReadyCallback {
    //location 버튼을 클릭시 현재위치를 찾을지에 대한 다이아로그를 보여주기 위한 클래스
    public static class LocationConfirmDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.location_activation_confirm)                    //dialog에 보여질 메세지
                    .setPositiveButton(R.string.yes, (dialog, whichButton) -> {        //dialog에 네(yes) 버튼 추가
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((MapsNaverActivity)activity).continueLocationTracking(); //클릭시 tracking 모드 활성화
                        }
                    })
                    .setNegativeButton(R.string.no, (dialog, whichButton) -> {        //dialog에 아니오(no) 버튼 추가
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((MapsNaverActivity)activity).cancelLocationTracking();  //클릭시 tracking 모드 활성화 취소
                        }
                    })
                    .setOnCancelListener(dialog -> {
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((MapsNaverActivity)activity).cancelLocationTracking();
                        }
                    })
                    .create();
        }
    }

    //마커에 정보를 표시해주는 창
    private static class InfoWindowAdapter extends InfoWindow.ViewAdapter {
        @NonNull
        private final Context context;
        private View rootView;
        private ImageView icon;
        private TextView text;

        private InfoWindowAdapter(@NonNull Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(@NonNull InfoWindow infoWindow) {
            if (rootView == null) {
                rootView = View.inflate(context, R.layout.view_custom_info_window, null);
                icon = rootView.findViewById(R.id.icon);
                text = rootView.findViewById(R.id.text);
            }

            if (infoWindow.getMarker() != null) {
                icon.setImageResource(R.drawable.ic_place_black_24dp);
                text.setText((String)infoWindow.getMarker().getTag());
            } else {
                icon.setImageResource(R.drawable.ic_my_location_black_24dp);
                text.setText(context.getString(
                        R.string.format_coord, infoWindow.getPosition().latitude, infoWindow.getPosition().longitude));
            }

            return rootView;
        }
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;    //권한 코드 번호
    private MapView mapView;
    private FusedLocationSource locationSource;
    @Nullable
    private Runnable locationActivationCallback;
    private NaverMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_naver);

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
            //getSupportFragmentManager 를 통해 layout에 위 기능들을 추가해줌
            getSupportFragmentManager().beginTransaction().add(R.id.map_fragment, mapFragment).commit();

        }

        //getMapAsync()를 사용하여 지도 콜백을 등록합니다.
        mapFragment.getMapAsync(this);

        //현재위치 사용을 위한 생성자 권한요청코드(LOCATION_PERMISSION_REQUEST_CODE) = 100
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        //setActivationHook 위치 기능의 활성화에 대한 훅 객체를 지정합니다.
        locationSource.setActivationHook(continueCallback -> {
            locationActivationCallback = continueCallback;
            new LocationConfirmDialogFragment().show(getSupportFragmentManager(), null);
        });
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

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        map = naverMap;

        // 정보창을 띄어주는 코드
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAnchor(new PointF(0, 1));
        infoWindow.setOffsetX(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_x));
        infoWindow.setOffsetY(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_y));
        infoWindow.setAdapter(new InfoWindowAdapter(this));
        infoWindow.setOnClickListener(overlay -> {
            infoWindow.close();
            return true;
        });

        //원하는 위치에 마커를 표시
        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.5666102, 126.9783881));
        marker.setOnClickListener(overlay -> {
            infoWindow.open(marker);
            return true;
        });
        marker.setTag("Marker");
        marker.setMap(naverMap);

        naverMap.setLocationSource(locationSource);
        //위치 추적 버튼 클릭시 마다 위치추적모드를 변경
        naverMap.addOnOptionChangeListener(() -> {
            LocationTrackingMode mode = naverMap.getLocationTrackingMode();
            locationSource.setCompassEnabled(mode == LocationTrackingMode.Follow || mode == LocationTrackingMode.Face);
        });

        infoWindow.open(marker);

        naverMap.setOnMapClickListener((point, coord) -> {
            infoWindow.setPosition(coord);
            infoWindow.open(naverMap);
        });
    }
}