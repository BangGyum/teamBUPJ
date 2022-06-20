//package com.banggyum.test;
//
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//import java.util.List;
//// 이거는 안쓸꺼임, 여기에 내 테스트 했던 정보들 들어가있음
//// DB 연결, 정보교환 테스트
//public class UserProfileActivity extends AppCompatActivity {
//
//    EditText context_input, date_input, location_input, alarm_input;
//    EditText holiday_name, holiday_date, holiday_nameUpdate, holiday_dateUpdate;
//    Button add_button,select_button, holiday_button, holiday_updateButton, dynamicButton;
//    TextView textResult,textview;
//    Button btn_1;
//    TextView tv_result;
//    List<ScheduleDTO> sList = new ArrayList<ScheduleDTO>();
//    MyDatabaseHelper myDb = new MyDatabaseHelper(UserProfileActivity.this);
//    LinearLayout li;
//    int scheduleId = 0 ; // 일정 아이디를 여기에 저장
//
//    TextView tvUserName;
//    TextView tvUserEmail;
//    ImageView userImageView;
//    Button btnUserInsert;
//
//    String userEmail, userName, userPhotoUrl;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment__schedule);
//        li = findViewById(R.id.dynamicLayout);
//
//        dynamicButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                //createTextView();
//
//            }
//        });
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
//
//        tvUserName.setText(userName);
//        tvUserEmail.setText(userEmail);
//
//        Glide.with(this).load(userPhotoUrl).into(userImageView); //url을 통해 이미지 다운?
//
//    }
//    //버튼
//
//
//}
//

//getApplicationContext 는 application context를 가르킴
//application context:  application 자체와 연동, 어플리케이션의 life cycle이 지속되는 동안 동일한 객체

// 텍스트뷰에 들어갈문자설정
//            //textViewNm.setText("텍스트생성");
//            btn.setText(text);

//텍스트뷰 글자크기 설정

//Button btn = new Button(getApplicationContext());

//설정한 레이아웃 텍스트뷰에 적용
//textViewNm.setLayoutParams(param);
//btn.setLayoutParams(param);

//텍스트뷰 백그라운드 색상 설정
//textViewNm.setBackgroundColor(Color.rgb(174,234,174));

//li.addView(textViewNm);
//li.addView(btn);

//setContentView(li);

//li.setOrientation(LinearLayout.VERTICAL);

              /*
            String sql =
                    "SELECT * FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_STATE + " = '1' AND "
                            + COLUMN_DATE + " >= strftime('%Y-%m-%d','now')" ;
                            */
            /*
            String sql =
                    "SELECT * FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_STATE + " = '1' AND "
                            + COLUMN_DATE + " < '20220601'" ;

             */

// 테이블 데이터를 읽기 위한 Cursor
//mCursor = db.query(TABLE_NAME, null, "AGE" + " < ?"
//        , new String[]{age.toString()}, null, null, "NAME");
//Cursor mCursor = db.query(TABLE_NAME, null, null, null, null, null, null);
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
