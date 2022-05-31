package com.banggyum.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class testgeocode extends AppCompatActivity {
    private String[] array;
    private String[] address;
    private String[] roadAddress;
    private TextView tv;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testgeocode);

        tv = findViewById(R.id.tv);
        et = findViewById(R.id.et);
        Button btn = findViewById(R.id.btn);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            requestNaverLocal();
//                            requestGeocode();
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void requestNaverLocal(){
        try{
            BufferedReader br;
            HttpURLConnection conn;
            StringBuilder sb = new StringBuilder();
            int display = 5;
            String sc = et.getText().toString();
            String addr = URLEncoder.encode(sc, "UTF-8");

            String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + addr + "&display=" + display + "&"; //
            URL url = new URL(apiURL);
            conn = (HttpURLConnection) url.openConnection();

            if(conn != null) {
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-Naver-Client-Id", "XMHmR1mRBBOwIr6_QuDz");
                conn.setRequestProperty("X-Naver-Client-Secret", "uemXpwpf0D");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();

                if(responseCode == 200){
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }else{
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;

                while((line = br.readLine()) != null){
                    sb.append(line + "\n");
                }

                String data = sb.toString();

//                tv.setText(data);

                array = data.split("\"");
                address = new String[display];
                roadAddress = new String[display];

                int k = 0;
                for (int i = 0; i < array.length; i++) {
                    if (array[i].equals("address"))
                        address[k] = array[i + 2];
                    if (array[i].equals("roadAddress")){
                        roadAddress[k] = array[i + 2];
                        k++;
                    }
                }

                tv.setText("address: " + address[0] + "\n roadAddress: " + roadAddress[0] + "\n");
                for (int i = 1; i < array.length; i++) {
                    tv.append("address: " + address[i] + "\n roadAddress: " + roadAddress[i] + "\n");
                }

                br.close();
                conn.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void requestGeocode(){
        try{
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String addr = URLEncoder.encode(roadAddress[0], "UTF-8");
            String query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(conn != null) {
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "8dvuu6gc91");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", "L7HyUUXIRUoxUvLZkeawiNCOmxxclCCgaM0WFfwG");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();

                if(responseCode == 200){
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }else{
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;

                while((line = br.readLine()) != null){
                    sb.append(line + "\n");
                }

                int indexFirst, indexLast;

                indexFirst = sb.indexOf("\"x\":\"");
                indexLast = sb.indexOf("\",\"y\":");
                String x = sb.substring(indexFirst + 5, indexLast);

                indexFirst = sb.indexOf("\"y\":\"");
                indexLast = sb.indexOf("\",\"distance\":");
                String y = sb.substring(indexFirst + 5, indexLast);

//                tv.setText("x: " + x + ", y: " + y + "\n");

                br.close();
                conn.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}