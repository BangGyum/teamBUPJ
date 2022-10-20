package com.banggyum.test;

import android.os.AsyncTask;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
public class PostRequestHandler extends AsyncTask<Void, Void, String> {
    // php URL 주소
    String url;     // Key, Value 값
    HashMap<String, String> requestedParams;
    PostRequestHandler(String url, HashMap<String, String> params){
        this.url = url;
        this.requestedParams = params;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(Void... voids) {
        // post request 보냄
        BackgroundWorker backgroundWorker = new BackgroundWorker();
        try {
            String s = backgroundWorker.postRequestHandler(url, requestedParams);
            String a = backgroundWorker.getRequestHandler(url);
            return s.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }          return null;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    } }


