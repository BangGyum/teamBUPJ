package com.banggyum.test;

import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;


public class BackgroundWorker {// Make a POST Request Handler
    public String postRequestHandler(String requestUrl, HashMap<String, String> requestedDataParams) throws UnsupportedEncodingException {

        URL url;   // Set an Empty URL obj in system

        StringBuilder stringBuilder = new StringBuilder(); // Set a String Builder to store result as string
        String requestUrl1 = requestUrl;
        try {
            url = new URL(requestUrl1); // Now Initialize URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Make a HTTP url connection
            connection.setRequestMethod(Constant.POST_METHOD); // Set Method Type
            connection.setConnectTimeout(10000);    // Set Connection Time
            connection.setReadTimeout(10000);
            connection.setDoInput(true);    // set Input output ok
            connection.setDoOutput(true);             // Remove Caches
            // connection.setUseCaches(false);
            // connection.setDefaultUseCaches(false);
            // Creating a url as String with params
            StringBuilder url_string = new StringBuilder();
            boolean ampersand = false;
            for (Map.Entry<String, String> params : requestedDataParams.entrySet()) {
                if (ampersand)
                    url_string.append("&");
                else
                    ampersand = true;
                url_string.append(URLEncoder.encode(params.getKey(), "UTF-8"));
                url_string.append("=");
                url_string.append(URLEncoder.encode(params.getValue(), "UTF-8"));
            }
            Log.d("Final Url===", url_string.toString());
            OutputStream outputStream = connection.getOutputStream();//Creating an output stream
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            // Write Output Steam
            bufferedWriter.write(url_string.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();//        Log.d("Response===", connection.getResponseMessage());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader
                        = new BufferedReader(new InputStreamReader(connection.getInputStream()));// Local String
                String result;
                while ((result = bufferedReader.readLine()) != null) {
                    stringBuilder.append(result);
                }                 //            Log.d("Result===", result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    // Get Request Handler
    public String getRequestHandler(String requestUrl1){
        StringBuilder stringBuilder = new StringBuilder();// To Store response
        try {
            URL url = new URL(requestUrl1);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Open Connection
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));// Local
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                stringBuilder.append(result + "\n");
            }
            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }          return null;
    }
}


