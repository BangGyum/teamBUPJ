package com.banggyum.test;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.daimajia.androidanimations.library.Techniques;
//import com.daimajia.androidanimations.library.YoYo;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

// input 팝업창
public class PopupActivity extends Activity{

    private android.content.Context context;
    MainActivity ma = new MainActivity();
    String userEmail = "";
    String date,date1; //날짜
    String time;
    int scheduleId =(int) (Math.random()*1000000000);
    //10자리 난수

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


    EditText text1;
    LinearLayout li,liBottom;
    Button alarmBtn ;
    Button mapBtn;
    Calendar myCalendar = Calendar.getInstance();
    EditText alarmEdit[];
    /*String alarmIds[] = new String []{"R.id.alarm1","R.id.alarm2","R.id.alarm3","R.id.alarm4","R.id.alarm5"
                                        ,"R.id.alarm6","R.id.alarm7","R.id.alarm8","R.id.alarm9","R.id.alarm10","R.id.alarm11"
                                        ,"R.id.alarm12","R.id.alarm13","R.id.alarm14","R.id.alarm15","R.id.alarm16"
                                        ,"R.id.alarm17","R.id.alarm18","R.id.alarm19"};
*/
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
            String m = Integer.toString(month+1); // 5월이면 05가 아니라 5임
            //month가 인덱스라 그런지 1개월 어디에 버리고 나오기 때문에 +1
            String d = Integer.toString(day);
            // 10보다 작으면 앞에 0 붙여주기
            if (month<9) {
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
        setContentView(R.layout.popup_activity);
        Log.v("qwerqwer",scheduleId+"");

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        userEmail = preferences.getString("useremail", "");

        li = findViewById(R.id.dynamicLayout);
        liBottom = findViewById(R.id.dynamicLayout2);
        Log.v("qwer",li.getId()+"");
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
        final TextView date_view = (TextView) findViewById(R.id.date_view);
        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, day);

                        String y = Integer.toString(year);
                        String m = Integer.toString(month+1); // 5월이면 05가 아니라 5임
                        //month가 인덱스라 그런지 1개월 어디에 버리고 나오기 때문에 +1
                        String d = Integer.toString(day);
                        // 10보다 작으면 앞에 0 붙여주기
                        if (month<9) {
                            m = "0" + m;
                        }

                        if (day<10){
                            d = "0" + d;
                        }
                        date = y + "-" + m + "-" + d ;
                        Toast.makeText(getApplicationContext(),date, Toast.LENGTH_SHORT).show();
                        String myFormat = "yyyy-MM-dd"; //출력형식
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);


                        date_view.setText(date);
                    }
                };
                new DatePickerDialog(PopupActivity.this, myDatePicker, myCalendar.get(
                        Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TextView time_view = (TextView) findViewById(R.id.time_view);
        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PopupActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
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
                        time=h +":"+ m; //sql에 저장되는 시간
                    }
                }, hour, minute, false); //true의 경우24시간 형식의 TimePicker출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        alarmBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                createTextView();
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

    public void createTextView(){
        //텍스트뷰 객체 생성
        //TextView textViewNm = new TextView(getApplicationContext());
        if (btn_count < 19){
            Button btnCancel = new Button(getApplicationContext());
            EditText editTime = new EditText(getApplicationContext());
            EditText editDate = new EditText(getApplicationContext());
            //getApplicationContext 는 application context를 가르킴
            //application context:  application 자체와 연동, 어플리케이션의 life cycle이 지속되는 동안 동일한 객체

            // 텍스트뷰에 들어갈문자설정
            //            //textViewNm.setText("텍스트생성");
            btnCancel.setText("취소");
            editDate.setText("날짜선택");
            editTime.setText("시간선택");

            //텍스트뷰 글자크기 설정
            editTime.setTextSize(12);
            editDate.setTextSize(12);
            //textViewNm.setId(0);
            //String a= alarmIds[btn_count];
            editTime.setId(alarmIds[btn_count]);
            editDate.setId(alarmIds[btn_count]+40000); //date id부여
            btnCancel.setId(alarmIds[btn_count]+100000);
            editTime.setOnClickListener(TimeAddClickList);
            editDate.setOnClickListener(DateAddClickList);
            btnCancel.setOnClickListener(btnCancelListener);
            editTime.setFocusable(false);
            editTime.setClickable(true);
            editDate.setFocusable(false);
            editDate.setClickable(true);


            Log.v("qwe","qwe");

            // 레이아웃설정
            ScrollView.LayoutParams param = new ScrollView.LayoutParams(
                    ScrollView.LayoutParams.WRAP_CONTENT
                    ,ScrollView.LayoutParams.WRAP_CONTENT
            );
            param.leftMargin=30;



            //설정한 레이아웃 텍스트뷰에 적용
            //textViewNm.setLayoutParams(param);
            btnCancel.setLayoutParams(param);
            editTime.setLayoutParams(param);
            editDate.setLayoutParams(param);
            editTime.getLayoutParams().width=240; //edit size 조절
            editDate.getLayoutParams().width=240;
            btn_count++;
            //텍스트뷰 백그라운드 색상 설정
            //textViewNm.setBackgroundColor(Color.rgb(174,234,174));

            //li.addView(textViewNm);
            //li.addView(btn);
            Log.v("wert",editTime.getId()+"");
            liBottom.addView(editTime);
            liBottom.addView(editDate);
            liBottom.addView(btnCancel);
            //setContentView(li);

                    liBottom.setOrientation(LinearLayout.VERTICAL);
        }else{
            Toast.makeText(this, "더 이상 알람을 설정할 수 없습니다",Toast.LENGTH_SHORT).show();
        }

    }
    DatePickerDialog.OnDateSetListener myDatePickerAlarm = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            String y = Integer.toString(year);
            String m = Integer.toString(month+1); // 5월이면 05가 아니라 5임
            //month가 인덱스라 그런지 1개월 어디에 버리고 나오기 때문에 +1
            String d = Integer.toString(day);
            // 10보다 작으면 앞에 0 붙여주기
            if (month<9) {
                m = "0" + m;
            }

            if (day<10){
                d = "0" + d;
            }
            TextView date_view = (TextView) findViewById(alarmIds[btn_count]);
            date_view.setText(y + "-" + m + "-" + d) ;
            Toast.makeText(getApplicationContext(),date, Toast.LENGTH_SHORT).show();
            updateLabel();
        }
    };

    //닫기 버튼 클릭
    public void mOnClose(View v){
        //팝업닫기
  //      RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
  //      view.setLayoutManager(mLayoutManager);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        ExampleFragment fragment =
//                (ExampleFragment) fragmentManager.findFragmentById(R.id.fragment_container);
//
//        fragmentManager().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Schedule()).commit();
//        Intent Fragment_Schedule1 = new Intent(getApplicationContext(), Fragment_Schedule.class);
//        startActivityForResult(Fragment_Schedule1, 1);
        finish();
    }

    //확인 버튼 클릭
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void mOnConfirm(View v){
        Intent intent = new Intent();
        intent.putExtra("result","Close");
        setResult(RESULT_OK, intent);
        int a = 0; //해당 스케줄 아이디

        db.addSchedule(scheduleId
                ,userEmail
                ,text1.getText().toString()
                ,date
                ,time
                ,addr_name.getText().toString());
        //맵 정보 저장
        db.addMap(scheduleId
                , addr_name.getText().toString()
                , lat
                , lng) ;

        //알람 갯수만큼 입력
        for (; addCount<btn_count;addCount++){
            TextView time_view = (TextView) findViewById(alarmIds[addCount]); //메소드에 getText해서 넣어서 db에 넣어야되니깐 생성한거임
            TextView date_view = (TextView) findViewById(alarmIds[addCount]+40000);
            db.addAlarm(scheduleId
                    ,date_view.getText().toString()
                    ,time_view.getText().toString());
        }
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();

        if (noti == true) {
            setAlarm();
        }

        //startActivity(new Intent(getApplicationContext(),Fragment_Schedule.class));
        finish();

    }


    View.OnClickListener TimeAddClickList = new View.OnClickListener(){
        public void onClick(View v) {
            Calendar mcurrentTime = Calendar.getInstance();
            //TextView time_view = (TextView) findViewById(alarmIds[btn_count]);
            TextView time_view1 = (TextView) findViewById(v.getId());
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(PopupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour1, int selectedMinute1) {

//                    Log.v("state","state:" + state);
//                    Log.v("state1","hour:" + selectedHour);
//                    Log.v("state2","min:" + selectedMinute);

                    //TextView에 출력할 형식 지정
                    time_view1.setText(selectedHour1 + ":" + selectedMinute1);
                }
            }, hour, minute, false); //true의 경우24시간 형식의 TimePicker출현
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    };
    View.OnClickListener btnCancelListener = new View.OnClickListener(){
        public void onClick(View v) {
            EditText editNm = findViewById(v.getId() -100000); // 생성했던 View의 ID 가져오기
            liBottom.removeView(editNm);
            EditText editNm2 = findViewById(v.getId() -60000); // 생성했던 View의 ID 가져오기
            liBottom.removeView(editNm2);
            Button dynamicButton = findViewById(v.getId()); // 생성했던 View의 ID 가져오기
            liBottom.removeView(dynamicButton);

        }
    };

    View.OnClickListener DateAddClickList = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year1, int month1, int day1) {
                    myCalendar.set(Calendar.YEAR, year1);
                    myCalendar.set(Calendar.MONTH, month1);
                    myCalendar.set(Calendar.DAY_OF_MONTH, day1);

                    String y1 = Integer.toString(year1);
                    String m1 = Integer.toString(month1+1); // 5월이면 05가 아니라 5임
                    //month가 인덱스라 그런지 1개월 어디에 버리고 나오기 때문에 +1
                    String d1 = Integer.toString(day1);
                    // 10보다 작으면 앞에 0 붙여주기
                    if (month1<9) {
                        m1 = "0" + m1;
                    }

                    if (day1<10){
                        d1 = "0" + d1;
                    }
                    date1 = y1 + "-" + m1 + "-" + d1 ;
                    String myFormat = "yyyy-MM-dd"; //출력형식
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

                    TextView date_view1 = (TextView) findViewById(v.getId());
                    date_view1.setText(date1);

                }
            };
            new DatePickerDialog(PopupActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

    @RequiresApi(api = Build.VERSION_CODES.N) //알림 설정
    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(PopupActivity.this, AlarmReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, 0);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            Log.v("qqww","true");
            pendingIntent = PendingIntent.getBroadcast(PopupActivity.this, 0, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
        }else {
            Log.v("qqww","false");
            pendingIntent = PendingIntent.getBroadcast(PopupActivity.this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
}