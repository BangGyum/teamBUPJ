package com.banggyum.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PopupActivity extends Activity {

    EditText text1;
    LinearLayout li;
    Button alarmBtn ;
    Calendar myCalendar = Calendar.getInstance();
    EditText alarmEdit[];
    /*String alarmIds[] = new String []{"R.id.alarm1","R.id.alarm2","R.id.alarm3","R.id.alarm4","R.id.alarm5"
                                        ,"R.id.alarm6","R.id.alarm7","R.id.alarm8","R.id.alarm9","R.id.alarm10","R.id.alarm11"
                                        ,"R.id.alarm12","R.id.alarm13","R.id.alarm14","R.id.alarm15","R.id.alarm16"
                                        ,"R.id.alarm17","R.id.alarm18","R.id.alarm19"};
*/
    int alarmIds[] = new int[]{1000023,1000021,1000029,1000027,1000014,1000011,1000019,1000016,1000035,1000022
                            ,1000024,1000028,1000030,1000012,1000015,1000017,1000020,1000034,1000036};


    int btn_count=0; //버튼 생성에
    //데이트피커다이얼로그
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없앨래
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        li = findViewById(R.id.dynamicLayout);
        alarmBtn = findViewById(R.id.alarmBtn);

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
                                new DatePickerDialog(PopupActivity.this, myDatePicker, myCalendar.get(
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
                mTimePicker = new TimePickerDialog(PopupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        //선택한 시간이 12시를 넘을 경우 "PM"으로 변경
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        //TextView에 출력할 형식 지정
                        time_view.setText(state + " " + selectedHour + "시 " + selectedMinute + "분 ");
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

        //팝업닫기
        finish();
    }
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
