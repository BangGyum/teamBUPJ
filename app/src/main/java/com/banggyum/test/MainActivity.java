package com.banggyum.test;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
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
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String TAG = MainActivity.class.getSimpleName();
    private Context context;
    private GoogleSignInClient mGoogleSignInClient; //로그아웃을 위해서
    private String userEmail, userName, userPhotoUrl;
    private ImageView userImageView;
    private MyDatabaseHelper db ;
    ArrayList<HashMap<String, String>> contactList;
    private long backKeyPressedTime = 0;
    private AlarmManager alarmManager;// 5줄 알림에 필요
    private GregorianCalendar mCalender;
    private boolean noti = true;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    private ArrayList<ScheduleDTO> listItem; //알림을 위해 필요
    private ScheduleItemAdapter scheduleItemAdapter;
    private List<ScheduleDTO> schedule = new ArrayList<ScheduleDTO>();
    private List<AlarmDTO> alarm = new ArrayList<AlarmDTO>();


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

    BottomNavigationView bottomNavigationView; //네비게이션뷰
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;    //권한 코드 번호
    private MapView mapView;
    private FusedLocationSource locationSource;
    @Nullable
    private Runnable locationActivationCallback;
    private NaverMap map;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDatabaseHelper(this);
        userImageView =  findViewById(R.id.userImageView);

        new Handler().execute();
        contactList = new ArrayList<>();

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();

        if (noti == true) {
            setAlarm();
        }

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
        //db.deleteUser(userEmail);

        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);

        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.loginform:
                        Intent loginintent = new Intent(MainActivity.this,LoginPage.class);
                        startActivity(loginintent);
                        break;
                    case R.id.gone_schedule:
                        SharedPreferences.Editor state = getApplicationContext()
                                .getSharedPreferences("state", MODE_PRIVATE)
                                .edit();
                        state.putString("schedule_state", "0");
                        state.apply();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Schedule()).commit();
                        break;
                }

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
                        SharedPreferences.Editor state =  getApplicationContext()
                                .getSharedPreferences("state", MODE_PRIVATE)
                                .edit();
                        state.putString("schedule_state", "1");
                        state.apply();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Schedule()).commit();
                        break;
                    case R.id.calender_fragment:
                        state =  getApplicationContext()
                                .getSharedPreferences("state", MODE_PRIVATE)
                                .edit();
                        state.putString("schedule_state", "1");
                        state.apply();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Calendar()).commit();
                        break;
                    case R.id.navmap_fragment:
                        state =  getApplicationContext()
                                .getSharedPreferences("state", MODE_PRIVATE)
                                .edit();
                        state.putString("schedule_state", "1");
                        state.apply();
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
    long mNow;
    Date mDate;
    private Vector<Marker> activeMarkers;
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
//        naverMap.setLocationSource(locationSource);
//        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        selectMapList = db.selectMap();

        //위치 추적 버튼 클릭시 마다 위치추적모드를 변경
        naverMap.addOnOptionChangeListener(() -> {
            LocationTrackingMode mode = naverMap.getLocationTrackingMode();
            locationSource.setCompassEnabled(mode == LocationTrackingMode.Follow || mode == LocationTrackingMode.Face);
        });

        // 카메라 이동 되면 호출 되는 이벤트
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int reason, boolean animated) {
                freeActiveMarkers();
                // 정의된 마커위치들중 가시거리 내에있는것들만 마커 생성
                LatLngBounds currentPosition = getCurrentPosition(naverMap);

                for (int i=0;i<selectMapList.size();i++) {
                    MapDTO mapDTO = selectMapList.get(i);
                    //!withinSightMarker (currentPosition, new LatLng(mapDTO.getMap_latitude(), mapDTO.getMap_longitude()))
                    if (!currentPosition.contains(new LatLng(mapDTO.getMap_latitude(), mapDTO.getMap_longitude())))
                        continue;
                    Marker marker = new Marker();
                    marker.setPosition(new LatLng(mapDTO.getMap_latitude(), mapDTO.getMap_longitude()));
                    marker.setOnClickListener(overlay -> {
                        return true;
                    });
                    marker.setCaptionText(mapDTO.getMap_name());
                    marker.setCaptionAligns(Align.Top);
                    marker.setCaptionOffset(20);
                    marker.setCaptionTextSize(16);
                    marker.setHideCollidedSymbols(true);
                    marker.setIconPerspectiveEnabled(true);
                    marker.setMap(naverMap);
                    activeMarkers.add(marker);
                }
            }
        });
    }

    // 현재 카메라가 보고있는 위치
    public LatLngBounds getCurrentPosition(NaverMap naverMap) {
        LatLngBounds cureentPosition = naverMap.getContentBounds();
        return cureentPosition;
    }

    // 지도상에 표시되고있는 마커들 지도에서 삭제
    private void freeActiveMarkers() {
        if (activeMarkers == null) {
            activeMarkers = new Vector<Marker>();
            return;
        }
        for (Marker activeMarker: activeMarkers) {
            activeMarker.setMap(null);
        }
        activeMarkers = new Vector<Marker>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N) //알림 설정
    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, 0);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            Log.v("qqww","true");
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
        }else {
            Log.v("qqww","false");
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        ArrayList<Date> CalList = new ArrayList<>();


        Calendar cal = Calendar.getInstance();
        schedule = db.selectSchedules();


        //캘린더 등록된 일정 이벤트 리스트에 추가
        int i, j;  // 알림 울리는 시점
        for (i = 0; i < schedule.size(); i++) {
            ScheduleDTO SD = new ScheduleDTO();
            SD = schedule.get(i);
            alarm = db.selectAlarm();
            for(j = 0; j < alarm.size(); j++) { // schedule에 대한 alramTime
                AlarmDTO AD = new AlarmDTO();
                AD = alarm.get(j);
                Log.d("alarm",""+alarm.get((j)));
                // DB 스케줄 id에 대한 날짜와 시간

                // 알림 날짜 및 시간
                Log.v("alarm",AD.getAlarm_date() + AD.getAlarm_time());

                //날짜 포맷을 바꿔주는 소스코드
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                try {
                    Date date1 = sdf.parse(AD.getAlarm_date() + AD.getAlarm_time());
                    Log.d("date", "" + date1);
                    CalList.add(date1);
                    cal.setTime(CalList.get(j));
                    Log.d("123", " " + CalList.get(j));


                    Log.d("Time : ", "1 : " + new Date(cal.getTimeInMillis()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("XXXXX", "xxxxx");
                }

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                //        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
            }
        }
    }


    /**      * Async task class to get json by making HTTP call */
    private class Handler extends AsyncTask<Void, Void, Void> {
        private ListAdapter adapter;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            JsonParser sh = new JsonParser();
            String jsonStr = sh.convertJson(Constant.MapAllSelect_URL);// Making a request to url and getting response
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray employeeArray = jsonObj.getJSONArray("result");// Getting JSON Array node
                    for (int i = 0; i < employeeArray.length(); i++) { // looping through All Contacts
                        ScheduleDTO scD = new ScheduleDTO();
                        JSONObject c = employeeArray.getJSONObject(i);
                        String schedule_id_fk = c.getString("schedule_id_fk");
                        String map_name = c.getString("map_name");
                        String map_latitude = c.getString("map_latitude");
                        String map_longitude = c.getString("map_longitude");


                        HashMap<String, String> map_list = new HashMap<>();
                        // adding each child node to HashMap key => value
                        map_list.put("schedule_id_fk", schedule_id_fk);
                        map_list.put("map_name", map_name);
                        map_list.put("map_latitude", map_latitude);
                        map_list.put("map_longitude", map_longitude);
                        contactList.add(map_list);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }
    }
}

