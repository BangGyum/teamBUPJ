package com.banggyum.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
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
    private String[] roadAddress;
    private double[] lat;
    private double[] lng;
    private double llat = 0;
    private double llng = 0;
    private MyBottomSheetFragment myBottomSheetFragment;
    private String searchthing;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;    //권한 코드 번호
    private FusedLocationSource locationSource;
    private NaverMap map;
    private int searchNum;
    private Marker onemarker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);

        //toolBar를 통해 App Bar 생성
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);

        onemarker = new Marker();

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

    //툴바에 main_menu.xml 을 추가함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        //검색 버튼 클릭했을 때 searchview 길이 꽉차게 늘려주기
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        //검색 버튼 클릭했을 때 searchview 에 힌트 추가
        searchView.setQueryHint("장소명으로 검색합니다.");
        searchView.setOnQueryTextListener(searchListener);
        return true;
    }

    //키보드의 검색 버튼을 눌렀을 시 리스너
    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchthing = query;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //네이버 장소 검색을 이용함
                    requestNaverLocal();
//                    requestGeocode();
                    //검색된 장소들을 바텀시트에 리사이클 뷰로 보여줌
                    clickOpenBottomSheetFragment();
//                    onMapSearch();
                }
            }).start();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    //네이버 검색 openapi
    @SuppressLint("SetTextI18n")
    public void requestNaverLocal(){
        try{
            BufferedReader br;
            HttpURLConnection conn;
            StringBuilder sb = new StringBuilder();
            //검색할 내용을 인코딩
            String addr = URLEncoder.encode(searchthing, "UTF-8");
            int display = 5;
            //url을 통해 검색
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

                //openapi 상태를 알기위함
                int responseCode = conn.getResponseCode();

                //정상 작동시
                if(responseCode == 200){
                    //검색된 값을 저장
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }else{
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;

                while((line = br.readLine()) != null){
                    sb.append(line + "\n");
                }

                //data 에 검색된 json 파일 저장
                String data = sb.toString();

                String[] array = data.split("\"");
                roadAddress = new String[display];

                //검색된 값들중 도로명 주소만 저장
                int k = 0;
                for (int i = 0; i < array.length; i++) {
                    if (array[i].equals("roadAddress")){
                        roadAddress[k] = array[i + 2];
                        k++;
                    }
                }

                lat = new double[display];
                lng = new double[display];
                searchNum = 0;

                br.close();
                conn.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //아직 미사용 추후 예정
    @SuppressLint("SetTextI18n")
    public void requestGeocode() {
        try {
            if(searchNum == roadAddress.length){
                return;
            }

            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String addr = URLEncoder.encode(roadAddress[searchNum], "UTF-8");
            String query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "8dvuu6gc91");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", "L7HyUUXIRUoxUvLZkeawiNCOmxxclCCgaM0WFfwG");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                int indexFirst, indexLast;

                indexFirst = sb.indexOf("\"x\":\"");
                indexLast = sb.indexOf("\",\"y\":");
                lng[searchNum] = Double.parseDouble(sb.substring(indexFirst + 5, indexLast));

                indexFirst = sb.indexOf("\"y\":\"");
                indexLast = sb.indexOf("\",\"distance\":");
                lat[searchNum] = Double.parseDouble(sb.substring(indexFirst + 5, indexLast));

                if(searchNum < roadAddress.length){
                    searchNum += 1;
                    br.close();
                    conn.disconnect();
                    requestGeocode();
                }
                br.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //검색된 장소들중 선택된 주소를 받아 정확학 위치 좌표값을 얻기위해 사용
    @SuppressLint("SetTextI18n")
    public void requestGeocodeOne(String addrone) {
        try {
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
//            선택된 주소 인코딩
            addrone = URLEncoder.encode(addrone, "UTF-8");
            String query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addrone;
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "8dvuu6gc91");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", "L7HyUUXIRUoxUvLZkeawiNCOmxxclCCgaM0WFfwG");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                int indexFirst, indexLast;

                //위도 값을 얻음
                indexFirst = sb.indexOf("\"x\":\"");
                indexLast = sb.indexOf("\",\"y\":");
                llng = Double.parseDouble(sb.substring(indexFirst + 5, indexLast));

                //경도값을 얻음
                indexFirst = sb.indexOf("\"y\":\"");
                indexLast = sb.indexOf("\",\"distance\":");
                llat = Double.parseDouble(sb.substring(indexFirst + 5, indexLast));

                br.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //하단에 검색된 장소들을 보여주고 클릭시 동작을 위함
    private void clickOpenBottomSheetFragment() {
        List<ItemObject> list = new ArrayList<>();

        for (int i =0; i< roadAddress.length; i++){
            list.add(new ItemObject(roadAddress[i]));
        }

        myBottomSheetFragment = new MyBottomSheetFragment(list, new IClickListener() {
            @Override
            public void clickItem(ItemObject itemObject) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //선택된 주소의 위치 값을 얻음
                        requestGeocodeOne(itemObject.getName());
                        //선택된 주소를 맵을 통해 마커로 보여줌
                        onMapSearch(itemObject.getName());
                    }
                }).start();
            }
        });

        //검색된 장소들을 보여줌
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

    //선택된 위치를 맵을 통해 마커로 표시하고 마커 선택시 동작 구현
   public void onMapSearch(String roadAddress){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //마커의 위치 설정
                        onemarker.setPosition(new LatLng(llat, llng));
                        //마커를 맵에 표시
                        onemarker.setMap(map);
                        //카메라를 마커위치에 이동
                        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(llat, llng));
                        map.moveCamera(cameraUpdate);
                        //바텀시트를 내려줌
                        myBottomSheetFragment.dismiss();
                        //마커클릭시 동작
                        onemarker.setOnClickListener(overlay -> {
                            //팝업을 띄어줌
                            Intent mapintent = new Intent(InsertMapDB.this, Mappopup.class);
                            startActivityIfNeeded(mapintent, 1);

                            //일정 추가 팝업에 값을 넘겨주기위함
                            Intent data = new Intent();
                            data.putExtra("roadAddress", roadAddress);
                            data.putExtra("searchname", searchthing);
                            data.putExtra("lat", llat);
                            data.putExtra("lng", llng);
                            setResult(RESULT_OK, data);
                            return true;
                        });
//                        for(int i=0;i<roadAddress.length;i++){
//                            Marker marker = new Marker();
//                            marker.setPosition(new LatLng(lat[i], lng[i]));
//                            marker.setMap(map);
//                            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(lat[0], lng[0]));
//                            map.moveCamera(cameraUpdate);
//                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //팝업에서 확인을 클릭시 지도 종료
                finish();
            }
        }
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        map = naverMap;
        naverMap.setLocationSource(locationSource);

        //위치 추적 버튼 클릭시 마다 위치추적모드를 변경
        naverMap.addOnOptionChangeListener(() -> {
            LocationTrackingMode mode = naverMap.getLocationTrackingMode();
            locationSource.setCompassEnabled(mode == LocationTrackingMode.Follow || mode == LocationTrackingMode.Face);
        });

        //위치추적모드 실행
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        //지도에서 선택시 마커를 생성
        naverMap.setOnMapClickListener((point, coord) -> {
            onemarker.setPosition(new LatLng(coord.latitude, coord.longitude));
            onemarker.setMap(naverMap);

            llat = coord.latitude;
            llng = coord.longitude;
            onemarker.setOnClickListener(overlay -> {
                //팝업을 띄어줌
                Intent mapintent = new Intent(InsertMapDB.this, Mappopup.class);
                startActivityIfNeeded(mapintent, 1);

                //일정 추가 팝업에 값을 넘겨주기위함
                Intent data = new Intent();
                data.putExtra("roadAddress", roadAddress);
                data.putExtra("searchname", searchthing);
                data.putExtra("lat", llat);
                data.putExtra("lng", llng);
                setResult(RESULT_OK, data);
                return true;
            });
        });
    }
}

