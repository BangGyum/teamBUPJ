package com.banggyum.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Mappopup extends Activity {
    private String roadAddress;
    private Double lat, lng;

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

        Intent secondIntent = getIntent();
        roadAddress = secondIntent.getStringExtra("roadAddress");
        lat = secondIntent.getDoubleExtra("lat", 0);
        lng = secondIntent.getDoubleExtra("lng", 0);

        Log.v("road", roadAddress);
        Log.v("lat", String.valueOf(lat));
        Log.v("lng", String.valueOf(lng));
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_ok:

                    break;
                case R.id.btn_cle:
                    finish();
                    break;
            }
        }
    };
}
