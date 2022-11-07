package com.banggyum.test;
//SQLite 관할
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = MainActivity.class.getSimpleName();;
    private ProgressDialog pDialog;
    private Context context;
    private Context context_alarm;
    private static final String DATABASE_NAME = "schedule.db"; //db이름
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "schedule"; //테이블명 (일정 테이블)
    private static final String COLUMN_ID = "schedule_id"; //일정 id (pk)
    private static final String COLUMN_EMAIL = "user_email"; //사용자 id (fk)
    private static final String COLUMN_CONTEXT = "schedule_context"; //내용
    private static final String COLUMN_DATE = "schedule_date"; //일정 날짜
    private static final String COLUMN_TIME = "schedule_time"; //일정 날짜
    //private static final String COLUMN_LOCATION = "schedule_location"; //일정 지도 fk 예정
    private static final String COLUMN_STATE = "schedule_state"; //평범상태 = 1 , 삭제상태 = 0
    private static final String COLUMN_REGISTER_DATE = "schedule_registerDate"; //만든 날짜
    private static final String COLUMN_MODI_DATE = "schedule_modiRegisterDate"; //만든 날짜

    private static final int DATABASE_VERSION2 = 1;
    private static final String TABLE2_NAME = "alarm"; //테이블명 (알람 테이블)
    private static final String COLUMN2_ID = "schedule_id_fk";
    private static final String COLUMN2_DATE = "alarm_date";
    private static final String COLUMN2_TIME = "alarm_time";


    private static final String TABLE3_NAME = "user"; //테이블명 (유저 테이블)
    private static final String COLUMN3_EMAIL = "user_email";
    private static final String COLUMN3_NAME = "user_name";

    private static final String TABLE4_NAME = "holiday"; //테이블명 (공휴일 테이블)
    private static final String COLUMN4_NAME = "holiday_name";
    private static final String COLUMN4_DATE = "holiday_date";

    private static final String TABLE5_NAME = "map"; //테이블명 (지도 테이블)
    private static final String COLUMN5_ID = "schedule_id_fk";
    private static final String COLUMN5_NAME = "map_name";
    private static final String COLUMN5_LATITUDE = "map_latitude";
    private static final String COLUMN5_LONGITUDE = "map_longitude";
    private static final String COLUMN5_STATE = "schedule_state"; //평범상태 = 1 , 삭제상태 = 0
    //List mList = new ArrayList();; //select해서 가져올 객체

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //일정 table
    String query_schedule = "CREATE TABLE " + TABLE_NAME
            + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_CONTEXT + " TEXT NOT NULL, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_TIME + " TEXT NOT NULL, "
            + COLUMN_STATE + " INTEGER NOT NULL,"
            + COLUMN_REGISTER_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + COLUMN_MODI_DATE + " TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_EMAIL + ")"
            + "REFERENCES " + TABLE3_NAME + "(" + COLUMN3_EMAIL + ")); ";

    //알람 table
    String query_alarm = "CREATE TABLE " + TABLE2_NAME
            + " (" + COLUMN2_ID + " INTEGER NOT NULL, "
            + COLUMN2_DATE + " TIMESTAMP, "
            + COLUMN2_TIME + " TIMESTAMP, "
            + "FOREIGN KEY(" + COLUMN2_ID + ")"
            + "REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")); ";

    //외래키가 활성화가 안돼있을수도 있기에
    String query_pragma = "PRAGMA foreign_keys = 1;";

    //사용자 table
    String query_user = "CREATE TABLE " + TABLE3_NAME
            + " (" + COLUMN3_EMAIL + " TEXT  PRIMARY KEY NOT NULL, "
            +  COLUMN3_NAME + " TEXT );";

    String query_calender = "CREATE TABLE " + TABLE4_NAME
            + " (" + COLUMN4_NAME + " TEXT  PRIMARY KEY NOT NULL,"
            + COLUMN4_DATE + " TIMESTAMP ); ";

    String query_map = "CREATE TABLE " + TABLE5_NAME
            + " (" + COLUMN5_ID + " INTEGER NOT NULL, "
            + COLUMN5_NAME + " TEXT, "
            + COLUMN5_LATITUDE + " DOUBLE NOT NULL, "
            + COLUMN5_LONGITUDE + " DOUBLE NOT NULL, "
            + COLUMN_STATE + " INTEGER NOT NULL,"
            + "FOREIGN KEY(" + COLUMN5_ID + ")"
            + "REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")); ";


    public void a() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query_user);
        db.execSQL(query_schedule);
        db.execSQL(query_alarm);
        db.execSQL(query_pragma);
        db.execSQL(query_calender);
    }

    public List<AlarmDTO> selectAlarm() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<AlarmDTO> mList = new ArrayList<AlarmDTO>();
        //Cursor mCursor = null;
        try {

            String sql = "SELECT * FROM " + TABLE2_NAME +", "+ TABLE_NAME + " WHERE "+ COLUMN_ID + "=" + COLUMN2_ID + " AND " +" "+ COLUMN_STATE + " = '1'";
            Cursor mCursor = db.rawQuery(sql, null);

            if (mCursor != null) {// 테이블 끝까지 읽기

                while (mCursor.moveToNext()) {// 다음 Row로 이동
                    // 해당 Row 저장
                    AlarmDTO AD = new AlarmDTO();
                    ScheduleDTO SD = new ScheduleDTO();

                    AD.setSchedule_id_fk(mCursor.getInt(0));
                    AD.setAlarm_date(mCursor.getString(1));
                    AD.setAlarm_time(mCursor.getString(2));
                    AD.setSchedule_date(mCursor.getString(6));
                    AD.setSchedule_time(mCursor.getString(7));

                    // List에 해당 Row 추가
                    mList.add(AD);
//                    sList.add(SD);
                }
            } else {
                Toast.makeText(context, "Alarm 데이터 안읽힘", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    //db가 처음 생성될때 호출되는 메소드, execSQL을 호출해 데이터를 채우는 작업
    {
        db.execSQL(query_user);
        db.execSQL(query_schedule);
        db.execSQL(query_alarm);
        db.execSQL(query_pragma);
        db.execSQL(query_calender);
        db.execSQL(query_map);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade()는 DB를 업그레이드해야 할 때, 업그레이드는 존재하는 DB 파일에 저장된 버전의 번호가 생성자에서 요청하는 것보다 낮은 경우,
        // 기존의 DB를 삭제하고 다시 생성
        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL("DROP TABLE " + TABLE2_NAME);
        db.execSQL("DROP TABLE " + TABLE3_NAME);
        onCreate(db);
    }

    public void addUser(String addEmail, String addName)
    //알람 테이블에 삽입
    {
        try {

                SQLiteDatabase db2 = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                //. ContentValues = ContentResolver 가 처리할 수 있는 값 집합을 저장
                //ContentResolver 는 contentProvider 와 비지니스 로직의 중계자 역할 (운송)
                cv.put(COLUMN3_EMAIL, addEmail);
                cv.put(COLUMN3_NAME, addName);

                long result = db2.insert(TABLE3_NAME, null, cv);
                if (result == -1) {
                    Toast.makeText(context, "기존 사용자", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "유저 데이터 추가 성공", Toast.LENGTH_SHORT).show();
                }
            HashMap<String, String> requestedParams = new HashMap<>();
            requestedParams.put("email", addEmail);
            requestedParams.put("name", addName);
            Log.d("HashMap", requestedParams.get("email"));
            PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.CREATE_URL, requestedParams);
            postRequestHandler.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void deleteUser(String addEmail)
    //알람 테이블에 삽입
    {
        try {
            HashMap<String, String> requestedParams = new HashMap<>();
            requestedParams.put("email", addEmail);
            Log.d("HashMap", requestedParams.get("email"));
            PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.DELETE, requestedParams);
            postRequestHandler.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addMap(int schedule_id, String addName, Double lat, Double lng)
    //사용자 id, 내용, 일정날짜, 알람 정보(이건 배열로?),
    {
        if (addName.equals("")) {
            addName = null;
        }
        if (lat == null) {
            lat = null;
        }
        if (lng == null) {
            lng = null;
        }

        ScheduleDTO scD = new ScheduleDTO();

        SQLiteDatabase db = this.getWritableDatabase(); //SQLiteDatabase 객체를 만든 뒤 이 객체를 쓰기(Write)가 가능하도록 설정한다는 내용이다.
        // 이 처리를 해줘야 테이블에 데이터를 추가할 수 있다.
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        //cv.put();// 사용자 id 추가
        cv.put(COLUMN5_ID, schedule_id);
        cv.put(COLUMN5_NAME, addName);
        cv.put(COLUMN5_LATITUDE, lat);
        cv.put(COLUMN5_LONGITUDE, lng);
        cv.put(COLUMN5_STATE, 1);

        long result = db.insert(TABLE5_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db2 = this.getReadableDatabase();
            Toast.makeText(context, "맵 데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }
        HashMap<String, String> requestedParams = new HashMap<>();
        requestedParams.put("schedule_id", Integer.toString(schedule_id));
        requestedParams.put("map_name", addName);
        requestedParams.put("lat", Double.toString(lat));
        requestedParams.put("lng", Double.toString(lng));
        Log.d("HashMap", requestedParams.get("map_name"));
        PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.MapCREATE_URL, requestedParams);
        postRequestHandler.execute();
    }

    public void updateMap (int scheduleId, String locName, Double lat, Double lng)
    //일정 데이터 표면상 삭제
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다
        cv.put(COLUMN5_NAME, locName);
        cv.put(COLUMN5_LATITUDE, lat);
        cv.put(COLUMN5_LONGITUDE, lng);
        long resultScd = db.update(TABLE5_NAME, cv, COLUMN5_ID + "='" + scheduleId + "'", null);
        if (resultScd == -1) {
            Toast.makeText(context, "수정 Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "알람 데이터 수정 성공", Toast.LENGTH_SHORT).show();
        }

        HashMap<String, String> requestedParams = new HashMap<>();
        requestedParams.put("schedule_id", Integer.toString(scheduleId));
        requestedParams.put("map_name", locName);
        requestedParams.put("lat", Double.toString(lat));
        requestedParams.put("lng", Double.toString(lng));

        Log.d("HashMap", requestedParams.get("schedule_id"));
        PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.MapUpdate_URL, requestedParams);
        postRequestHandler.execute();

    }

    @SuppressLint("Range")
    public List<MapDTO> selectMap() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MapDTO> mList = new ArrayList<MapDTO>();
        List<ScheduleDTO> sList = new ArrayList<ScheduleDTO>();
        try {
            // 테이블 정보를 저장할 List

            // 쿼리
//            String sql = "SELECT * FROM " + TABLE5_NAME + ", " + TABLE_NAME + " WHERE " + COLUMN5_ID + " = " + COLUMN_ID + ";";
            String sql = "SELECT * FROM " + TABLE5_NAME + " WHERE " + COLUMN_STATE + " = '1'";
            // 테이블 데이터를 읽기 위한 Cursor
            //mCursor = db.query(TABLE_NAME, null, "AGE" + " < ?"
            //        , new String[]{age.toString()}, null, null, "NAME");
            //Cursor mCursor = db.query(TABLE_NAME, null, null, null, null, null, null);

            Cursor mCursor = db.rawQuery(sql, null);

            if (mCursor != null) {// 테이블 끝까지 읽기

                while (mCursor.moveToNext()) {// 다음 Row로 이동
                    // 해당 Row 저장
                    MapDTO MD = new MapDTO();
                    ScheduleDTO SD = new ScheduleDTO();

                    MD.setMap_name(mCursor.getString(1));
                    MD.setMap_latitude(mCursor.getDouble(2));
                    MD.setMap_longitude(mCursor.getDouble(3));

                    // List에 해당 Row 추가
                    mList.add(MD);
//                    sList.add(SD);
                }
            } else {
                Toast.makeText(context, "데이터 안읽힘", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }


    public void addSchedule(int scheduleId, String addEmail, String addContext,
                           String addDate, String addTime, String addLocation)
    //사용자 id, 내용, 일정날짜, 알람 정보(이건 배열로?),
    {
        if (addEmail.equals("")) {
            addEmail = null;
        }
        if (addContext.equals("")) {
            addContext = null;
        }
        if (addDate.equals("")) {
            addDate = null;
        }
        if (addTime.equals("")) {
            addTime = null;
        }
        if (addLocation.equals("")) {
            addLocation = null;
        }

        ScheduleDTO scD = new ScheduleDTO();

        SQLiteDatabase db = this.getWritableDatabase(); //SQLiteDatabase 객체를 만든 뒤 이 객체를 쓰기(Write)가 가능하도록 설정한다는 내용이다.
        // 이 처리를 해줘야 테이블에 데이터를 추가할 수 있다.
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        //cv.put();// 사용자 id 추가
        cv.put(COLUMN_ID, scheduleId);
        cv.put(COLUMN_EMAIL, addEmail);
        cv.put(COLUMN_CONTEXT, addContext);
        cv.put(COLUMN_DATE, addDate);
        cv.put(COLUMN_TIME, addTime);
        cv.put(COLUMN_STATE, 1);

        long result = db.insert(TABLE_NAME, null, cv);

        HashMap<String, String> requestedParams = new HashMap<>();
        requestedParams.put("email", addEmail);
        requestedParams.put("schedule_id", Integer.toString(scheduleId));
        requestedParams.put("schedule_context", addContext);
        requestedParams.put("schedule_date", addDate);
        requestedParams.put("schedule_time", addTime);

        Log.d("HashMap", requestedParams.get("email"));
        PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.ScheduleCREATE_URL, requestedParams);
        postRequestHandler.execute();

        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            //SQLiteDatabase db2 = this.getReadableDatabase();
            Toast.makeText(context, "데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }
        //return scD.getSchedule_id(); //일정 id값 반환
    }
    public void addLocalSchedule(int scheduleId, String addEmail, String addContext,
                            String addDate, String addTime, int schedule_state)
    //사용자 id, 내용, 일정날짜, 알람 정보(이건 배열로?),
    {
        if (addEmail.equals("")) {
            addEmail = null;
        }
        if (addContext.equals("")) {
            addContext = null;
        }
        if (addDate.equals("")) {
            addDate = null;
        }
        if (addTime.equals("")) {
            addTime = null;
        }


        ScheduleDTO scD = new ScheduleDTO();

        SQLiteDatabase db = this.getWritableDatabase(); //SQLiteDatabase 객체를 만든 뒤 이 객체를 쓰기(Write)가 가능하도록 설정한다는 내용이다.
        // 이 처리를 해줘야 테이블에 데이터를 추가할 수 있다.
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        //cv.put();// 사용자 id 추가
        cv.put(COLUMN_ID, scheduleId);
        cv.put(COLUMN_EMAIL, addEmail);
        cv.put(COLUMN_CONTEXT, addContext);
        cv.put(COLUMN_DATE, addDate);
        cv.put(COLUMN_TIME, addTime);
        cv.put(COLUMN_STATE, schedule_state);

        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) {
      //      Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            //SQLiteDatabase db2 = this.getReadableDatabase();
      //      Toast.makeText(context, "데이터 추가 성공", Toast.LENGTH_SHORT).show();
//            try {
//                // 쿼리
//                String sql = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1;";
//
//                Cursor mCursor = db2.rawQuery(sql, null);
//
//                if (mCursor != null) {// 테이블 끝까지 읽기
//
//                    while (mCursor.moveToNext()) {// 다음 Row로 이동
//                        // 해당 Row 저장
//                        scD.setSchedule_id(mCursor.getInt(0));
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }
    public void deleteSchedule(int scheduleId)
    //알람 테이블에 삽입
    {
        try {
            HashMap<String, String> requestedParams = new HashMap<>();
            requestedParams.put("schedule_id", Integer.toString(scheduleId));
            Log.d("HashMap", requestedParams.get("email"));
            PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.ScheduleDELETE_URL, requestedParams);
            postRequestHandler.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void updateSchedule (int scheduleId,  String addContext,
                                String addDate, String addTime, String addLocation)
    //일정 수정
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //sdf.format(timestamp)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다
        cv.put(COLUMN_CONTEXT, addContext);
        cv.put(COLUMN_DATE, addDate);
        cv.put(COLUMN_TIME, addTime);

        long resultScd = db.update(TABLE_NAME, cv, COLUMN_ID + "='" + scheduleId + "'", null);
        if (resultScd == -1) {
            Toast.makeText(context, "수정 Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "알람 데이터 수정 성공", Toast.LENGTH_SHORT).show();
        }
        HashMap<String, String> requestedParams = new HashMap<>();
        Log.v("rrrr",sdf.format(timestamp));
        requestedParams.put("schedule_modiRegisterDate", sdf.format(timestamp));
        requestedParams.put("schedule_id", Integer.toString(scheduleId));
        requestedParams.put("schedule_context", addContext);
        requestedParams.put("schedule_date", addDate);
        requestedParams.put("schedule_time", addTime);

        Log.d("HashMap", requestedParams.get("schedule_id"));
        PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.ScheduleUpdate_URL, requestedParams);
        postRequestHandler.execute();
    }

    public void addAlarm(int addScheduleId,String addAlarmDate, String addAlarmTime)
    //알람 테이블에 삽입
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        cv.put(COLUMN2_ID, addScheduleId);
        cv.put(COLUMN2_DATE, addAlarmDate);
        cv.put(COLUMN2_TIME, addAlarmTime);

        long result = db.insert(TABLE2_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "알람 데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }

    }



    @SuppressLint("Range")
    public List<ScheduleDTO> selectSchedules() {

        SQLiteDatabase db = this.getReadableDatabase();
        List<ScheduleDTO> mList = new ArrayList<ScheduleDTO>();
        //Cursor mCursor = null;
        try {
            // 테이블 정보를 저장할 List

            // 쿼리
            String sql =
                    "SELECT * FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_STATE + " = '1' AND strftime('%Y-%m-%d',"
                            + COLUMN_DATE + ") >= strftime('%Y-%m-%d','now')" + " ORDER BY " + COLUMN_DATE;

            Cursor mCursor = db.rawQuery(sql, null);

            if (mCursor != null) {// 테이블 끝까지 읽기

                while (mCursor.moveToNext()) {// 다음 Row로 이동
                    // 해당 Row 저장
                    ScheduleDTO scD = new ScheduleDTO();

                    scD.setSchedule_id(mCursor.getInt(0));
                    int a = mCursor.getInt(0);

                    //1은 사용자
                    scD.setSchedule_context(mCursor.getString(2));
                    //위가 문제

                    scD.setSchedule_date(mCursor.getString(3));
                    scD.setSchedule_time(mCursor.getString(4));
                    //scD.setSchedule_location(mCursor.getString(5));
                    scD.setSchedule_state(mCursor.getShort(6));

                    //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //scD.setSchedule_registerDate1(sdf.format(scD.getTimestamp(7)));

                    //scD.setSchedule_registerDate(mCursor.getString(6));
                    //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                    //위가 문제
                    // List에 해당 Row 추가
                    mList.add(scD);
                }
            } else {
                Toast.makeText(context, "데이터 안읽힘", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }

    @SuppressLint("Range")
    public List<ScheduleDTO> selectDateSchedules (String scheduleDate)
    //날짜를 기준으로 select
    {
        ArrayList<HashMap<String, String>> contactList = null;
        SQLiteDatabase db = this.getReadableDatabase();
        List<ScheduleDTO> mList = new ArrayList<ScheduleDTO>();
        //Cursor mCursor = null;
        try {
            // 테이블 정보를 저장할 List

            // 쿼리
            String sql =
                    "SELECT * FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_STATE + " = '1' AND "
                            + COLUMN_DATE + " = '" + scheduleDate + "'";


            Cursor mCursor = db.rawQuery(sql, null);

            if (mCursor != null) {// 테이블 끝까지 읽기

                while (mCursor.moveToNext()) {// 다음 Row로 이동
                    // 해당 Row 저장
                    ScheduleDTO scD = new ScheduleDTO();

                    scD.setSchedule_id(mCursor.getInt(0));

                    //1은 사용자
                    scD.setSchedule_context(mCursor.getString(2));
                    //위가 문제

                    scD.setSchedule_date(mCursor.getString(3));
                    scD.setSchedule_time(mCursor.getString(4));
                    //scD.setSchedule_location(mCursor.getString(5));
                    scD.setSchedule_state(mCursor.getShort(6));
                    //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //scD.setSchedule_registerDate1(sdf.format(scD.getTimestamp(7)));

                    //scD.setSchedule_registerDate(mCursor.getString(6));

                    // List에 해당 Row 추가
                    mList.add(scD);
                }
            }
            HashMap<String, String> requestedParams = new HashMap<>();
            requestedParams.put("scheduleDate", scheduleDate);

            Log.d("HashMap", requestedParams.get("scheduleDate"));
            PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.ScheduleDateSelect_URL, requestedParams);


            String jsonStr = postRequestHandler.doInBackground();
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray employeeArray = jsonObj.getJSONArray("result");// Getting JSON Array node
                    for (int i = 0; i < employeeArray.length(); i++) { // looping through All Contacts
                        JSONObject c = employeeArray.getJSONObject(i);
                        String schedule_id = c.getString("schedule_id");
                        String schedule_email = c.getString("schedule_email");
                        String schedule_context = c.getString("schedule_context");
                        String schedule_date = c.getString("schedule_date");
                        String schedule_time = c.getString("schedule_time");
                        String schedule_state = c.getString("schedule_state");
                        String schedule_registerDate = c.getString("schedule_registerDate");


                        HashMap<String, String> schedule_list = new HashMap<>();
                        // adding each child node to HashMap key => value
                        schedule_list.put("schedule_id", schedule_id);
                        schedule_list.put("schedule_email", schedule_email);
                        schedule_list.put("schedule_context", schedule_context);
                        schedule_list.put("schedule_date", schedule_date);
                        schedule_list.put("schedule_time", schedule_time);
                        contactList.add(schedule_list);

                    }
                    for (int i = 0; i < contactList.size(); i++) {
                        HashMap<String, String> sdSelect;
                        sdSelect = contactList.get(i);

                        ScheduleDTO SD = new ScheduleDTO(Integer.parseInt(sdSelect.get("schedule_id")),
                                sdSelect.get("schedule_context"),
                                sdSelect.get("schedule_date"),
                                sdSelect.get("schedule_time"),
                                (short) 1,
                                sdSelect.get("schedule_registerDate"),
                                sdSelect.get("schedule_email")
                        );
                    }


//                        mList.add(SD);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }

            }
            postRequestHandler.execute();


    } catch (Exception e) {
        e.printStackTrace();
    }

//            } else {
//                Toast.makeText(context, "데이터 안읽힘", Toast.LENGTH_SHORT).show();
//            }

        return mList;
    }

    public ScheduleDTO selectSchedule ( int state)
    //일정 데이터 하나만 select
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ScheduleDTO scD = new ScheduleDTO();
        try {
            // 테이블 정보를 저장할 List


            // 쿼리
            String sql = "SELECT "
                    + COLUMN_ID + ", "
                    + COLUMN_CONTEXT + ", "
                    + COLUMN_DATE + ", "
                    + COLUMN_STATE + ", "
                    + "to_char(" + COLUMN_REGISTER_DATE + ") "
                    + " FROM " + TABLE_NAME + "WHERE state =" + state;

            // 테이블 데이터를 읽기 위한 Cursor
            Cursor mCursor = db.rawQuery(sql, null);

            // 테이블 끝까지 읽기
            if (mCursor != null) {

                // 다음 Row로 이동
                while (mCursor.moveToNext()) {

                    // 해당 Row 저장


                    scD.setSchedule_id(mCursor.getInt(0));
                    int a = mCursor.getInt(0);
                    //1은 사용자
                    scD.setSchedule_context(mCursor.getString(2));
                    scD.setSchedule_date(mCursor.getString(3));
                    //scD.setSchedule_location(mCursor.getString(4));
                    scD.setSchedule_state(mCursor.getShort(5));

                    //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //scD.setSchedule_registerDate1(sdf.format(scD.getTimestamp(7)));
                    scD.setSchedule_registerDate(mCursor.getString(6));

                    // List에 해당 Row 추가
                    Toast.makeText(context, "가져옴", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scD;
    }

    public ScheduleDTO getOneContext(int schedule_fk)
    //일정 데이터 하나만 select
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ScheduleDTO scD = new ScheduleDTO();
        try {
            // 테이블 정보를 저장할 List


            // 쿼리
            String sql = "SELECT "
                    + COLUMN_CONTEXT + ", "
                    + COLUMN_DATE + ", "
                    + COLUMN_TIME
                    + " FROM " + TABLE_NAME + " WHERE schedule_id =" + schedule_fk;
            // 테이블 데이터를 읽기 위한 Cursor
            Cursor mCursor = db.rawQuery(sql, null);

            // 테이블 끝까지 읽기
            if (mCursor != null) {

                // 다음 Row로 이동
                while (mCursor.moveToNext()) {

                    scD.setSchedule_context(mCursor.getString(0));
                    scD.setSchedule_date(mCursor.getString(1));
                    //1은 사용자
                    scD.setSchedule_time(mCursor.getString(2));
                    Log.v("eee","eee");
                    // List에 해당 Row 추가
                    Toast.makeText(context, "가져옴", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scD;
    }

    public int selectState(int scheduleId){
        SQLiteDatabase db = this.getReadableDatabase();
        int state = 0;
        try {
            // 쿼리
            String sql = "SELECT "
                    + COLUMN_STATE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +" = " + scheduleId;

            // 테이블 데이터를 읽기 위한 Cursor
            Cursor mCursor = db.rawQuery(sql, null);

            // 테이블 끝까지 읽기
            if (mCursor != null) {
                // 다음 Row로 이동
                while (mCursor.moveToNext()) {
                    // 해당 Row 저장
                    state = mCursor.getInt(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }


    public void updateScheduleState (int scheduleId, int state)
    //일정 데이터 표면상 삭제
    {
        HashMap<String, String> requestedParams = new HashMap<>();
        requestedParams.put("schedule_id", Integer.toString(scheduleId));
        Log.d("HashMap", requestedParams.get("schedule_id"));
        PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.ScheduleDELETE_URL, requestedParams);
        postRequestHandler.execute();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        if(state == 0){
            cv.put(COLUMN_STATE, 1);
            cv.put(COLUMN5_STATE, 1);
        } else{
            cv.put(COLUMN_STATE, 0);
            cv.put(COLUMN5_STATE, 0);
        }

        long resultScd = db.update(TABLE_NAME, cv, COLUMN_ID + "='" + scheduleId + "'", null);
        long resultMap = db.update(TABLE5_NAME, cv, COLUMN5_ID + "='" + scheduleId + "'", null);
        if (resultScd == -1 || resultMap == -1) {
            Toast.makeText(context, "수정 Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "알람 데이터 수정 성공", Toast.LENGTH_SHORT).show();
        }

    }

//    public void deleteSchedule (int scheduleId)
//    //일정 데이터 실제로 삭제
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        long result = db.delete(TABLE_NAME, COLUMN_ID + "='" + scheduleId + "'", null);
//        if (result == -1) {
//            Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "데이터 삭제 성공", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void addHoliday (String addHolidayName, String addHolidayDate)
    //알람 테이블에 삽입
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        cv.put(COLUMN4_NAME, addHolidayName);
        cv.put(COLUMN4_DATE, addHolidayDate);

        long result = db.insert(TABLE4_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "추가 Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "알람 데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateHoliday (String updateHolidayName, String updateHolidayDate)
    //알람 테이블에 삽입
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        cv.put(COLUMN4_DATE, updateHolidayDate);

        long result = db.update(TABLE4_NAME, cv, COLUMN4_NAME + "='" + updateHolidayName + "'", null);
        if (result == -1) {
            Toast.makeText(context, "수정 Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "알람 데이터 수정 성공", Toast.LENGTH_SHORT).show();
        }
    }
}