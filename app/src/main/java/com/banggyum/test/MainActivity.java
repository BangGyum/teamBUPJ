package com.banggyum.test;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; //네비게이션뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);

        //네비바 처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new Fragment_Schedule()).commit();
        //네비바 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
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
                    case R.id.maps_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Map()).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }
}

