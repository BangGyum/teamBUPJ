package com.banggyum.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Fragment_Schedule extends Fragment {

    String userEmail, userName, userPhotoUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__schedule, container, false);
        FloatingActionButton floatingActionButton = v.findViewById(R.id.btn_fab);
//
//        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//
//        userEmail = preferences.getString("useremail", "");
//        userName = preferences.getString("username", "");
//        //userName으로 하니깐 userEmail도 못가져옴.. 이건뭥미?
//        //생각보다 불안정한데?
//        //그냥 useremail을 해야 위에게 나오네?
//        //전부다 소문자로 하니깐 나옴
//        userPhotoUrl = preferences.getString("userPhoto","");



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PopupActivity.class));
            }
        });
        return v;
    }
}