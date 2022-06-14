package com.banggyum.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;
    private GoogleSignInClient mGoogleSignInClient; //로그아웃을 위해서
    private String userEmail, userName, userPhotoUrl;
    private ImageView userImageView;
    private MyDatabaseHelper db ;

    //마커에 정보를 표시해주는 창
    private static class InfoWindowAdapter extends InfoWindow.ViewAdapter {
        @NonNull
        private final Context context;
        private View rootView;
        private TextView text;

        private InfoWindowAdapter(@NonNull Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(@NonNull InfoWindow infoWindow) {
            if (rootView == null) {
                rootView = View.inflate(context, R.layout.view_custom_info_window, null);
                text = rootView.findViewById(R.id.text);
            }

            //마커 클릭시 정보창 보여줌
            if (infoWindow.getMarker() != null) {
                text.setText((String)infoWindow.getMarker().getTag());      //marker.setTag를 통해 일정 내용 보여줌
            } else {    //다른 곳 정보창 닫음
                infoWindow.close();
            }

            return rootView;
        }
    }

    //이전 버튼 클릭시 Drawer 닫기
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        setContentView(R.layout.activity_main);
        db = new MyDatabaseHelper(this);
        userImageView =  findViewById(R.id.userImageView);

        //toolBar를 통해 App Bar 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //shared에 저장되어있는 값 가져오기
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        userEmail = preferences.getString("useremail", "");
        userName = preferences.getString("username", "");
        userPhotoUrl = preferences.getString("userPhoto","");
        //db.a();
        db.addUser(userEmail,userName);

        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);

        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.loginform) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(loginIntent);
                }
                /*else if (id == R.id.logoutform){
//                    Log.v("sds","sdsd");
                    LoginPage lp = new LoginPage();
                    mGoogleSignInClient=lp.getmGoogleSignInClient();
                    //Log.v("ddd",mGoogleSignInClient.toString());
                    //mGoogleSignInClient.signOut()
                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
//                    lp.requestGoogleSignIn();
                    lp.signOut();
                    //revokeAccess(googleSignInClient);
                    //signOut();
//                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                            .requestEmail()
//                            .build();
//
//// Build a GoogleSignInClient with the options specified by gso.
//                    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                 */

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawLayout,
                toolbar,
                R.string.open,
                R.string.closed
        );
        drawLayout.addDrawerListener(actionBarDrawerToggle);

        //mapfragment 사용하여 지도를 이용
        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if (mapFragment == null) {
            //location 버튼 활성화, camera 지도의 초기 카메라 위치를 지정합니다. target - 카메라의 좌표 zoom - 카메라의 줌 레벨
            mapFragment = MapFragment.newInstance(new NaverMapOptions().locationButtonEnabled(true));
        }

        MapFragment finalMapFragment = mapFragment;

        //현재위치 사용을 위한 생성자 권한요청코드(LOCATION_PERMISSION_REQUEST_CODE) = 100
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        bottomNavigationView = findViewById(R.id.bottomNav);
        //네비바 처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new Fragment_Schedule()).commit();
        //네비바 안의 아이템 설정
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //네비바 버튼을 클릭시 id값을 가져와 FrameLayout에 fragment띄우기
                    case R.id.schedule_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Schedule()).commit();
                        break;
                    case R.id.calender_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Calendar()).commit();
                        break;
                    case R.id.navmap_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, finalMapFragment).commit();
                        finalMapFragment.getMapAsync(MainActivity.this);
                        break;
                }
                return true;
            }
        });

        //drawer 안에 사용자 정보
        View headerView = navigationView.getHeaderView(0);

        TextView navUserEmail = (TextView) headerView.findViewById(R.id.email);
        navUserEmail.setText(userEmail);
        TextView navUserName = (TextView) headerView.findViewById(R.id.name);
        navUserName.setText(userName);
        ImageView userImageView = (ImageView) headerView.findViewById(R.id.userImageView) ;

        Glide.with(this).load(userPhotoUrl).into(userImageView);
    }

    private void revokeAccess(GoogleSignInClient googleSignInClient){
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    //툴바에 main_menu.xml 을 추가함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        //검색 버튼 클릭했을 때 searchview 길이 꽉차게 늘려주기
        SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        //검색 버튼 클릭했을 때 searchview 에 힌트 추가
        searchView.setQueryHint("장소명으로 검색합니다.");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
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

    private List<MapDTO> selectMapList = new ArrayList<MapDTO>();
    private InfoWindow infoWindow;

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        map = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 정보창을 띄어주는 코드
        infoWindow = new InfoWindow();
        infoWindow.setAnchor(new PointF(0, 1));
        //
        infoWindow.setOffsetX(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_x));
        infoWindow.setOffsetY(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_y));
        infoWindow.setAdapter(new MainActivity.InfoWindowAdapter(this));
        infoWindow.setOnClickListener(overlay -> {
            infoWindow.close();
            return true;
        });

        selectMapList = db.selectMap();
//        LatLngBounds bounds = map.getCoveringBounds();
        for (int i=0; i<selectMapList.size(); i++){
            MapDTO mdselect;
            mdselect = selectMapList.get(i);

            //마커를 표시하고 마커 클릭시 정보창 띄워줌
            Marker marker = new Marker();
            marker.setPosition(new LatLng(mdselect.getMap_latitude(), mdselect.getMap_longitude()));
            marker.setOnClickListener(overlay -> {
                infoWindow.open(marker);
                return true;
            });

            marker.setMap(map);
            marker.setTag(mdselect.getMap_name());
//            LatLng position = marker.getPosition();

//            if(bounds.contains(position)){
//                marker.setVisible(false);
//            }else{
//                marker.setVisible(true);
//            }
        }
        //위치 추적 버튼 클릭시 마다 위치추적모드를 변경
        naverMap.addOnOptionChangeListener(() -> {
            LocationTrackingMode mode = naverMap.getLocationTrackingMode();
            locationSource.setCompassEnabled(mode == LocationTrackingMode.Follow || mode == LocationTrackingMode.Face);
        });

        naverMap.setOnMapClickListener((point, coord) -> {
            infoWindow.setPosition(coord);
            infoWindow.open(naverMap);
        });
    }
}

