package com.banggyum.test;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// input 팝업창
public class PopupActivityUpdate extends Activity {

    private android.content.Context context;
    MainActivity ma = new MainActivity();
    String userEmail = "";
    String date; //날짜
    String time;
    String Context;
    String LocationName="미정";


    EditText text1;
    LinearLayout li;
    Button alarmBtn ;
    Button mapBtn;
    Calendar myCalendar = Calendar.getInstance();
    EditText alarmEdit[];

    MyDatabaseHelper db ;
    int alarmIds[] = new int[]{1000023,1000021,1000029,1000027,1000014,1000011,1000019,1000016,1000035,1000022
                            ,1000024,1000028,1000030,1000012,1000015,1000017,1000020,1000034,1000036};
    int addCount = 0 ; // db에 넣을때, 알람이 어려개라 for ( int i ... 에서 int i 대신에 만든 것.
                // 리스너에서도 이걸 사용해서 alarmEdit[addCount] 로 id값 찾으려고

    int btn_count=0; //버튼 생성에
    //데이트피커다이얼로그
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            String y = Integer.toString(year);
            String m = Integer.toString(month); // 5월이면 05가 아니라 5임
            String d = Integer.toString(day);
            // 10보다 작으면 앞에 0 붙여주기
            if (month<10) {
                m = "0" + m;
            }

            if (day<10){
                d = "0" + d;
            }
            date = y + "-" + m + "-" + d ;
            Toast.makeText(getApplicationContext(),date, Toast.LENGTH_SHORT).show();
            updateLabel();
        }
    };

    private EditText addr_name;
    private TextView addr_view;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없앨래
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity_update);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        userEmail = preferences.getString("useremail", "");

        li = findViewById(R.id.dynamicLayout);
        alarmBtn = findViewById(R.id.alarmBtn);
        mapBtn = findViewById(R.id.mapBtn);
        addr_name = findViewById(R.id.addra_name);
        addr_view = findViewById(R.id.addr_view);

        db = new MyDatabaseHelper(this);

        //UI 객체생성
        text1 = (EditText) findViewById(R.id.text1);
        //데이터 가져오

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        text1.setText(data);
        TextView data_view = (TextView) findViewById(R.id.date_view);
        data_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PopupActivityUpdate.this, myDatePicker, myCalendar.get(
                        Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
}
});
        //타임피커다이얼로그 뜨게 만들기
        final TextView time_view = (TextView) findViewById(R.id.time_view);
        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PopupActivityUpdate.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        //선택한 시간이 12시를 넘을 경우 "PM"으로 변경

                        String h = Integer.toString(selectedHour);
                        String m = Integer.toString(selectedMinute);
                        if (selectedHour<10){
                            h = "0"+h;
                        }
                        if (selectedMinute<10){
                            m = "0"+m;
                        }

                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        //TextView에 출력할 형식 지정
                        time_view.setText(state + " " + selectedHour + "시 " + selectedMinute + "분 ");
                        //Log.v("test","test:"+h + m);
                        time=h + m;

                    }
                }, hour, minute, false); //true의 경우24시간 형식의 TimePicker출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        alarmBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                createTextView("ㅇ");
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentmap = new Intent(getApplicationContext(), InsertMapDB.class);
                startActivityIfNeeded(intentmap, 1);
            }
        });
    }

    String roadAddress, searchName;
    Double lat, lng;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                roadAddress = data.getStringExtra("roadAddress");
                searchName = data.getStringExtra("searchname");
                lat = data.getDoubleExtra("lat", 0);
                lng = data.getDoubleExtra("lng", 0);

                addr_name.setText(searchName);
                addr_view.setText(roadAddress);
            }
        }
    }

    public void createTextView(String text){
        //텍스트뷰 객체 생성
        //TextView textViewNm = new TextView(getApplicationContext());
        if (btn_count < 19){
            Button btn = new Button(getApplicationContext());
            EditText editNm = new EditText(getApplicationContext());

            // 텍스트뷰에 들어갈 문자설정
            //textViewNm.setText("텍스트생성");
            btn.setText(text);

            //텍스트뷰 글자크기 설정
            //textViewNm.setTextSize(12);
            //textViewNm.setId(0);
            editNm.setTextSize(9);
            //String a= alarmIds[btn_count];
            editNm.setId(alarmIds[btn_count]);
            editNm.setOnClickListener(TimeAddClickList);

            // 레이아웃설정
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    ,LinearLayout.LayoutParams.WRAP_CONTENT
            );
            param.leftMargin=30;

            //설정한 레이아웃 텍스트뷰에 적용
            //textViewNm.setLayoutParams(param);
            btn.setLayoutParams(param);
            editNm.setLayoutParams(param);
            btn_count++;
            //텍스트뷰 백그라운드 색상 설정
            //textViewNm.setBackgroundColor(Color.rgb(174,234,174));

            //li.addView(textViewNm);
            //li.addView(btn);
            li.addView(editNm);

            //li.setOrientation(LinearLayout.VERTICAL);
        }else{
            Toast.makeText(this, "더 이상 알람을 설정할 수 없습니다",Toast.LENGTH_SHORT).show();
        }

    }
    //확인 버튼 클릭
    public void mOnClose(View v){
        Intent intent = new Intent();
        intent.putExtra("result","Close");
        setResult(RESULT_OK, intent);
//        int a = 0; //해당 스케줄 아이디
//
//        //Toast.makeText(context, userEmail, Toast.LENGTH_SHORT).show();
//        a = db.addSchedule(userEmail
//                      ,text1.getText().toString()
//                      ,date
//                      ,Location);
//
//        //Toast.makeText(context, a +"", Toast.LENGTH_SHORT).show();
//
//        //알람 갯수만큼 입력
//        for (; addCount<btn_count;addCount++){
//            TextView time_view = (TextView) findViewById(alarmIds[addCount]); //메소드에 getText해서 넣어서 db에 넣어야되니깐 생성한거임
//            db.addAlarm(a
//                        ,time_view.getText().toString());
//        }

        //팝업닫기
        finish();
    }

    public void mOnConfirm(View v){
        Intent intent = new Intent();
        intent.putExtra("result","Close");
        setResult(RESULT_OK, intent);
        int a = 0; //해당 스케줄 아이디
//        searchName = " d";
        //Toast.makeText(context, userEmail, Toast.LENGTH_SHORT).show();
        a = db.addSchedule(userEmail
                ,text1.getText().toString()
                ,date
                ,time
                ,searchName);

        //Toast.makeText(context, a +"", Toast.LENGTH_SHORT).show();

        //알람 갯수만큼 입력
        for (; addCount<btn_count;addCount++){
            TextView time_view = (TextView) findViewById(alarmIds[addCount]); //메소드에 getText해서 넣어서 db에 넣어야되니깐 생성한거임
            db.addAlarm(a
                    ,time_view.getText().toString());
        }

//        lat=1.1;
//        lng=1.1;
        db.addMap(a
                ,searchName
                , lat
                , lng) ;

        finish();
    }


    View.OnClickListener TimeAddClickList = new View.OnClickListener(){
        public void onClick(View v) {
            Calendar mcurrentTime = Calendar.getInstance();
            //TextView time_view = (TextView) findViewById(alarmIds[btn_count]);
            TextView time_view = (TextView) findViewById(v.getId());
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(PopupActivityUpdate.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String state = "AM";
                    //선택한 시간이 12시를 넘을 경우 "PM"으로 변경
                    if (selectedHour > 12) {
                        selectedHour -= 12;
                        state = "PM";
                    }
//                    Log.v("state","state:" + state);
//                    Log.v("state1","hour:" + selectedHour);
//                    Log.v("state2","min:" + selectedMinute);

                    //TextView에 출력할 형식 지정
                    time_view.setText(state + " " + selectedHour + "시 " + selectedMinute + "분 ");
                }
            }, hour, minute, false); //true의 경우24시간 형식의 TimePicker출현
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    };



    // 밖 레이어 클릭시 문제없게
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    //안드로이드 뒤로가기 막기
    public void onBackPressed(){
        return;
    }
// 데이트피커 다이얼로그 뜨게만들기
    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //출력형식
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView date_view = (TextView) findViewById(R.id.date_view);
        date_view.setText(sdf.format(myCalendar.getTime()));
    }
}
