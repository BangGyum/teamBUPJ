package com.banggyum.test;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
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
    private static final String COLUMN_LOCATION = "schedule_location"; //일정 지도 fk 예정
    private static final String COLUMN_STATE = "schedule_state"; //평범상태 = 1 , 삭제상태 = 0
    private static final String COLUMN_REGISTER_DATE = "schedule_registerDate"; //만든 날짜

    private static final int DATABASE_VERSION2 = 1;
    private static final String TABLE2_NAME = "alarm"; //테이블명 (알람 테이블)
    private static final String COLUMN2_ID = "schedule_id_fk";
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
    //List mList = new ArrayList();; //select해서 가져올 객체

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //일정 table
    String query_schedule = "CREATE TABLE " + TABLE_NAME
            + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_CONTEXT + " TEXT NOT NULL, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_TIME + " TEXT NOT NULL, "
            + COLUMN_LOCATION + " TEXT,"
            + COLUMN_STATE + " INTEGER NOT NULL,"
            + COLUMN_REGISTER_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(" + COLUMN_EMAIL + ")"
            + "REFERENCES " + TABLE3_NAME + "(" + COLUMN3_EMAIL + ")); ";

    //알람 table
    String query_alarm = "CREATE TABLE " + TABLE2_NAME
            + " (" + COLUMN2_ID + " INTEGER NOT NULL, "
            + COLUMN2_TIME + " TIMESTAMP, "
            + "FOREIGN KEY(" + COLUMN2_ID + ")"
            + "REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")); ";

    //외래키가 활성화가 안돼있을수도 있기에
    String query_pragma = "PRAGMA foreign_keys = 1;";

    //사용자 table
    String query_user = "CREATE TABLE " + TABLE3_NAME
            + " (" + COLUMN3_EMAIL + " TEXT  PRIMARY KEY NOT NULL, "
            + COLUMN3_NAME + " TEXT );";

    String query_calender = "CREATE TABLE " + TABLE4_NAME
            + " (" + COLUMN4_NAME + " TEXT  PRIMARY KEY NOT NULL,"
            + COLUMN4_DATE + " TIMESTAMP ); ";

    String query_map = "CREATE TABLE " + TABLE5_NAME
            + " (" + COLUMN5_ID + " INTEGER NOT NULL, "
            + COLUMN5_NAME + " TEXT, "
            + COLUMN5_LATITUDE + " DOUBLE NOT NULL, "
            + COLUMN5_LONGITUDE + " DOUBLE NOT NULL, "
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

    @Override
    public void onCreate(SQLiteDatabase db)
    //db가 처음 생성될때 호출되는 메소드, execsql을 호출해 데이터를 채우는 작업
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
            SQLiteDatabase db = this.getReadableDatabase();
            UserDTO uD = new UserDTO();
            // 쿼리
            String sql = "SELECT * FROM " + TABLE3_NAME + " WHERE " + COLUMN3_EMAIL + " = '" + addEmail + "';";

            Cursor mCursor = db.rawQuery(sql, null);

            uD.setUser_email(mCursor.getString(0));
            Toast.makeText(context, "e", Toast.LENGTH_SHORT).show();

            if ("".equals("")) {// 테이블 끝까지 읽기
                Toast.makeText(context, "응애2", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, mCursor.getString(0) + "응애", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(context, "응애3", Toast.LENGTH_SHORT).show();
                SQLiteDatabase db2 = this.getWritableDatabase();
                ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

                cv.put(COLUMN3_EMAIL, addEmail);
                cv.put(COLUMN3_NAME, addName);

                long result = db2.insert(TABLE3_NAME, null, cv);
                if (result == -1) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "알람 데이터 추가 성공", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addMap(int schedule_id, String addName, Double lat, Double lng)
    //사용자 id, 내용, 일정날짜, 알람 정보(이건 배열로?),
    {
        if (addName.equals("")) {
            addName = "null";
        }
        if (lat.equals(null)) {
            lat = 0.0;
        }
        if (lng.equals(null)) {
            lng = 0.0;
        }

        int scheduleId = 0; //
        ScheduleDTO scD = new ScheduleDTO();

        SQLiteDatabase db = this.getWritableDatabase(); //SQLiteDatabase 객체를 만든 뒤 이 객체를 쓰기(Write)가 가능하도록 설정한다는 내용이다.
        // 이 처리를 해줘야 테이블에 데이터를 추가할 수 있다.
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        //cv.put();// 사용자 id 추가
        cv.put(COLUMN5_ID, schedule_id);
        cv.put(COLUMN5_NAME, addName);
        cv.put(COLUMN5_LATITUDE, lat);
        cv.put(COLUMN5_LONGITUDE, lng);

        long result = db.insert(TABLE5_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db2 = this.getReadableDatabase();
            Toast.makeText(context, "맵 데이터 추가 성공", Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressLint("Range")
    public List<MapDTO> selectMap() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MapDTO> mList = new ArrayList<MapDTO>();
        List<ScheduleDTO> sList = new ArrayList<ScheduleDTO>();
        //Cursor mCursor = null;
        try {
            // 테이블 정보를 저장할 List

            // 쿼리
            String sql = "SELECT * FROM " + TABLE5_NAME + ", " + TABLE_NAME + " WHERE " + COLUMN5_ID + " = " + COLUMN_ID + ";";

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

                    MD.getMap_name(mCursor.getString(1));
                    MD.getMap_latitude(mCursor.getDouble(2));
                    MD.getMap_longitude(mCursor.getDouble(3));

                    SD.setSchedule_id(mCursor.getInt(4));
                    SD.setSchedule_context(mCursor.getString(5));
                    SD.setSchedule_date(mCursor.getString(6));
                    SD.setSchedule_location(mCursor.getString(7));
                    SD.setSchedule_state(mCursor.getShort(8));

                    //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //scD.setSchedule_registerDate1(sdf.format(scD.getTimestamp(7)));

                    //scD.setSchedule_registerDate(mCursor.getString(6));
                    //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                    //위가 문제
                    // List에 해당 Row 추가
                    mList.add(MD);
                    sList.add(SD);
                }
            } else {
                Toast.makeText(context, "데이터 안읽힘", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }

    public int addSchedule(String addEmail, String addContext, String addDate, String addTime, String addLocation)
    //사용자 id, 내용, 일정날짜, 알람 정보(이건 배열로?),
    {
        if(addEmail.equals("")){ addEmail="null"; }
        if(addContext.equals("")){ addContext="null"; }
        if(addDate.equals("")){ addDate="null"; }
        if(addLocation.equals("")){ addLocation="null"; }

        int scheduleId =0 ; //
        ScheduleDTO scD = new ScheduleDTO();

        SQLiteDatabase db = this.getWritableDatabase(); //SQLiteDatabase 객체를 만든 뒤 이 객체를 쓰기(Write)가 가능하도록 설정한다는 내용이다.
        // 이 처리를 해줘야 테이블에 데이터를 추가할 수 있다.
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        //cv.put();// 사용자 id 추가
        cv.put(COLUMN_EMAIL, addEmail);
        cv.put(COLUMN_CONTEXT, addContext);
        cv.put(COLUMN_DATE, addDate);
        cv.put(COLUMN_TIME, addTime);

        cv.put(COLUMN_LOCATION, addLocation);
        cv.put(COLUMN_STATE, 1); //이거 근데 다른테이블에 넣어야될것같은데

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
        {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SQLiteDatabase db2 = this.getReadableDatabase();
            Toast.makeText(context, "데이터 추가 성공", Toast.LENGTH_SHORT).show();
            try {
                // 쿼리
                String sql = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1;";

                Cursor mCursor = db2.rawQuery(sql, null);

                if (mCursor != null) {// 테이블 끝까지 읽기

                    while (mCursor.moveToNext()) {// 다음 Row로 이동
                        // 해당 Row 저장
                        scD.setSchedule_id(mCursor.getInt(0));
                    }
                }
            }catch (Exception e ) {
                e.printStackTrace();
            }
        }
        //return scheduleId;
        return scD.getSchedule_id(); //일정 id값 반환
    }
    public void addAlarm(int addScheduleId, String addAlarmTime)
    //알람 테이블에 삽입
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        cv.put(COLUMN2_ID ,addScheduleId);
        cv.put(COLUMN2_TIME ,addAlarmTime);

        long result = db.insert(TABLE2_NAME, null, cv);
        if (result == -1)
        {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
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
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATE + " = '1'";

            // 테이블 데이터를 읽기 위한 Cursor
            //mCursor = db.query(TABLE_NAME, null, "AGE" + " < ?"
            //        , new String[]{age.toString()}, null, null, "NAME");
            //Cursor mCursor = db.query(TABLE_NAME, null, null, null, null, null, null);

            Cursor mCursor = db.rawQuery(sql, null);

            if (mCursor != null) {// 테이블 끝까지 읽기

                while (mCursor.moveToNext()) {// 다음 Row로 이동
                    // 해당 Row 저장
                    ScheduleDTO scD = new ScheduleDTO();

                    scD.setSchedule_id(mCursor.getInt(0));
                    int a= mCursor.getInt(0);

                    //1은 사용자
                    scD.setSchedule_context(mCursor.getString(2));
                    //위가 문제

                    scD.setSchedule_date(mCursor.getString(3));
                    scD.setSchedule_location(mCursor.getString(4));
                    scD.setSchedule_state(mCursor.getShort(5));

                    //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //scD.setSchedule_registerDate1(sdf.format(scD.getTimestamp(7)));

                    //scD.setSchedule_registerDate(mCursor.getString(6));
                    //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                    //위가 문제
                    // List에 해당 Row 추가
                    mList.add(scD);
                }
            } else{
                Toast.makeText(context, "데이터 안읽힘", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e ) {
            e.printStackTrace();
        }
        return mList;
    }

    public ScheduleDTO selectSchedule(int state) {
        SQLiteDatabase db = this.getReadableDatabase();
        ScheduleDTO scD = new ScheduleDTO();
        try {
            // 테이블 정보를 저장할 List


            // 쿼리
            String sql = "SELECT "
                    + COLUMN_ID+", "
                    + COLUMN_CONTEXT+", "
                    + COLUMN_DATE+", "
                    + COLUMN_LOCATION+", "
                    + COLUMN_STATE+", "
                    + "to_char("+COLUMN_REGISTER_DATE+") "
                    + " FROM " + TABLE_NAME + "WHERE state =" + state;

            // 테이블 데이터를 읽기 위한 Cursor
            Cursor mCursor = db.rawQuery(sql, null);

            // 테이블 끝까지 읽기
            if (mCursor != null) {

                // 다음 Row로 이동
                while (mCursor.moveToNext()) {

                    // 해당 Row 저장


                    scD.setSchedule_id(mCursor.getInt(0));
                    int a= mCursor.getInt(0);
                    //1은 사용자
                    scD.setSchedule_context(mCursor.getString(2));
                    scD.setSchedule_date(mCursor.getString(3));
                    scD.setSchedule_location(mCursor.getString(4));
                    scD.setSchedule_state(mCursor.getShort(5));

                    //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //scD.setSchedule_registerDate1(sdf.format(scD.getTimestamp(7)));
                    scD.setSchedule_registerDate(mCursor.getString(6));

                    // List에 해당 Row 추가
                    Toast.makeText(context, "가져옴", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e ) {
            e.printStackTrace();
        }
        return scD;
    }

    public void addHoliday(String addHolidayName, String addHolidayDate)
    //알람 테이블에 삽입
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        cv.put(COLUMN4_NAME ,addHolidayName);
        cv.put(COLUMN4_DATE ,addHolidayDate);

        long result = db.insert(TABLE4_NAME, null, cv);
        if (result == -1)
        {
            Toast.makeText(context, "추가 Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "알람 데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateHoliday(String updateHolidayName, String updateHolidayDate)
    //알람 테이블에 삽입
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //. ContentValues란 addBook()에 들어오는 데이터를 저장하는 객체다

        cv.put(COLUMN4_DATE ,updateHolidayDate);

        long result = db.update(TABLE4_NAME, cv,COLUMN4_NAME + "='" + updateHolidayName + "'",  null);
        if (result == -1)
        {
            Toast.makeText(context, "수정 Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "알람 데이터 수정 성공", Toast.LENGTH_SHORT).show();
        }
    }
}