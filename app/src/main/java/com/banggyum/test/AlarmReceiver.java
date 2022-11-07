package com.banggyum.test;


import android.content.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {
    private MyDatabaseHelper db;

    private ScheduleDTO SD;
    private AlarmDTO AD;
    private List<ScheduleDTO> schedule = new ArrayList<ScheduleDTO>();
    private List<AlarmDTO> alarm = new ArrayList<AlarmDTO>();

    public AlarmReceiver(){ }

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";


    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        final String notiGroup = "group";
        int SUMMARY_ID = 0;


        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.v("qqqq","qqqq");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );

            Log.v("ooooo","ooooo");
        } else {

            Log.v("xxxxx","xxxxxx");
        }
        // DB가져오기
        db = new MyDatabaseHelper(context);
        schedule = db.selectSchedules();
        alarm = db.selectAlarm();
        String NotiText = "";


        //알림창 클릭 시 activity 화면 부름
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

//        for (int i = 0; i < schedule.size(); i++) {
//            SD = schedule.get(i);
//
//            NotiText = SD.getSchedule_context(); //알림제목

        for (int j = 0; j < alarm.size(); j++) { // schedule에 대한 alramTime
            AD = alarm.get(j);

            Log.v("y", "" + AD.getSchedule_id_fk());
            Log.v("yy", "" + AD.getAlarm_date());
            Log.v("yyy", "" + AD.getAlarm_time());
            Log.v("yyyy", "" + AD.getSchedule_date());
            Log.v("yyyy", "" + AD.getSchedule_time());
            String q = "";
            SD = db.getOneContext(AD.getSchedule_id_fk());

            Notification Builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(SD.getSchedule_context()) //알림창 제목
                    .setSmallIcon(R.drawable.ic_launcher_background) //알림창 아이콘
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) //알림창 터치시 자동 삭제
                    .setContentText(SD.getSchedule_date() + " " + SD.getSchedule_time() + "에 일정이 있습니다.")
                    .setGroup(notiGroup)
                    .build();
            manager.notify(AD.getSchedule_id_fk(), Builder);
        }

        Notification summaryNotification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        //set content text to support devices running API level < 24
                        .setContentText("Two new messages")
                        .setSmallIcon(R.drawable.todolist)
                        .setAutoCancel(true)
                        //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setBigContentTitle("2 new messages"))
                        //specify which group this notification belongs to
                        .setGroup(notiGroup)
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();

        manager.notify(SUMMARY_ID, summaryNotification);
    }
}