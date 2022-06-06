package com.banggyum.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Mappopup extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없앨래
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_marker_popup);

        Button btnok = findViewById(R.id.btn_ok);
        Button btncle = findViewById(R.id.btn_cle);

        btnok.setOnClickListener(btnListener);
        btncle.setOnClickListener(btnListener);
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_ok:
                    Intent data = new Intent();
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                case R.id.btn_cle:
                    finish();
                    break;
            }
        }
    };
}
