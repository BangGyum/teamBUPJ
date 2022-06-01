package com.banggyum.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;

public class DrawerHeader extends AppCompatActivity {
    String userEmail, userName, userPhotoUrl;
    ImageView userImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_header);

        userImageView = findViewById(R.id.userImage);
        //shared에 저장되어있는 값 가져오기
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        userEmail = preferences.getString("useremail", "");
        userName = preferences.getString("username", "");
        userPhotoUrl = preferences.getString("userPhoto", "");

        //tvUserName.setText(userName);
        //tvUserEmail.setText(userEmail);

        Glide.with(this).load(userPhotoUrl).into(userImageView); //url을 통해 이미지 다운?
    }
}