package com.banggyum.test;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    EditText context_input, date_input, location_input, alarm_input;
    EditText holiday_name, holiday_date, holiday_nameUpdate, holiday_dateUpdate;
    Button add_button,select_button, holiday_button, holiday_updateButton, dynamicButton;
    TextView textResult,textview;
    Button btn_1;
    TextView tv_result;
    List<ScheduleDTO> sList = new ArrayList<ScheduleDTO>();
    MyDatabaseHelper myDb = new MyDatabaseHelper(UserProfileActivity.this);
    LinearLayout li;
    int scheduleId = 0 ; // 일정 아이디를 여기에 저장

    TextView tvUserName;
    TextView tvUserEmail;
    ImageView userImageView;
    Button btnUserInsert;

    String userEmail, userName, userPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment__schedule);
        li = findViewById(R.id.dynamicLayout);

        dynamicButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //createTextView();


            }
        });


        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        userEmail = preferences.getString("useremail", "");
        userName = preferences.getString("username", "");
        //userName으로 하니깐 userEmail도 못가져옴.. 이건뭥미?
        //생각보다 불안정한데?
        //그냥 useremail을 해야 위에게 나오네?
        //전부다 소문자로 하니깐 나옴
        userPhotoUrl = preferences.getString("userPhoto","");

        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);

        Glide.with(this).load(userPhotoUrl).into(userImageView); //url을 통해 이미지 다운?

    }
    //버튼


}

