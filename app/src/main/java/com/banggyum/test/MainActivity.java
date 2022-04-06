package com.banggyum.test;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    EditText title_input, author_input, pages_input;
    Button add_button;
    TextView textResult;
    Button btn_1;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        btn_1 = (Button)findViewById(R.id.btn_dialog);
        tv_result=(TextView)findViewById(R.id.tv_result);


        btn_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("제목");
                ad.setMessage("팝업입니까");

                final EditText et = new EditText(MainActivity.this);
                ad.setView(et); //add해주고 view에 추가해줘라

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = et.getText().toString();
                        tv_result.setText(result);
                        dialogInterface.dismiss(); //전부실행하고 닫아라
                    }
                });
                ad.show(); //그래야 다이아로그가 정상적으로 뜸
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });

//        title_input = findViewById(R.id.title_input);
//        author_input = findViewById(R.id.author_input);
//        pages_input = findViewById(R.id.pages_input);
//        add_button = findViewById(R.id.add_button);
//        add_button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                MyDatabaseHelper myDb = new MyDatabaseHelper(MainActivity.this);
//                myDb.addBook(title_input.getText().toString().trim(), author_input.getText().toString().trim(), Integer.parseInt(pages_input.getText().toString().trim()));
//            }
//        });
    }
//    //버튼
//    public void mOnPopupClick(View v){
//        //데이터 담아서 팝업(액티비티) 호출
//        Intent intent = new Intent(this, PopupActivity.class);
//        intent.putExtra("data", "Test Popup");
//        startActivityForResult(intent, 0);
//    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //메인액티비티에서 섭액티비티를 호출하여 넘어갔다가, 다시 main 액티비티로 돌아올때 사용되는 기본 메소드 이다.
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                //데이터 받기
//                String result = data.getStringExtra("result");
//                textResult.setText(result);
//            }
//        }
//    }




}