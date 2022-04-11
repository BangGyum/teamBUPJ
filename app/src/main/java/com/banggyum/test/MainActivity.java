package com.banggyum.test;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
{
    EditText title_input, author_input, pages_input;
    Button add_button;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 사이드 메뉴바 입니다.
        // 액션바를 툴바로 대체하기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.layout_drawer);
        navigationView = findViewById(R.id.nav);
        // 네비게이션뷰의 메뉴아이콘의 색조 제거
        navigationView.setItemIconTintList(null);

        // 드로우어 조절용 토글버튼 객체 생성
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,R.string.app_name); //마지막 두 매개변수는 그냥 써주자
        drawerToggle.syncState(); //삼선 메뉴 만들기
        drawerLayout.addDrawerListener(drawerToggle); // 뱅그르 돌게 만들기

        // 액션바에 제목이 자동 표시 되지 않도록
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        pages_input = findViewById(R.id.pages_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MyDatabaseHelper myDb = new MyDatabaseHelper(MainActivity.this);
                myDb.addBook(title_input.getText().toString().trim(), author_input.getText().toString().trim(), Integer.parseInt(pages_input.getText().toString().trim()));
            }
        });
    }
}