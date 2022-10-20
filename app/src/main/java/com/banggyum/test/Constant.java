package com.banggyum.test;

public class Constant {
    //private static final String BASE_PATH = "C:\\xampp\\htdocs\\";
    //private static final String BASE_PATH = "C:\\xampp\\htdocs\\";
    //private static final String BASE_PATH = "C:/xampp/htdocs/";
    //private static final String BASE_PATH = "file:///C:/xampp/htdocs/";
    //private static final String BASE_PATH = "https://127.0.0.1/";
    private static final String BASE_PATH = "http://172.16.10.139/";
    //private static final String BASE_PATH = "https://172.16.1.81/";
    //private static final String BASE_PATH = "http://192.168.0.14/";
    //private static final String BASE_PATH = "http://172.17.11.224/";
    public static final String CREATE_URL = BASE_PATH + "project/addUser.php";
    public static final String READ = BASE_PATH + "project/getAllEmp.php";
    public static final String UPDATE = BASE_PATH + "project/updateEmp.php";
    public static final String DELETE = BASE_PATH + "project/deleteUser.php";
    public static final String ScheduleCREATE_URL = BASE_PATH + "project/addSchedule.php";
    public static final String ScheduleDELETE_URL = BASE_PATH + "project/deleteSchedule.php";
    public static final String ScheduleAllSelect_URL = BASE_PATH + "project/getAllSchedule.php";
    public static final String ScheduleDateSelect_URL = BASE_PATH + "project/getAllScheduleDate.php";
    public static final String ScheduleUpdate_URL = BASE_PATH + "project/updateSchedule.php";
    public static final String MapCREATE_URL = BASE_PATH + "project/addMap.php";
    public static final String MapAllSelect_URL = BASE_PATH + "project/getAllMap.php";


    public static final String GET_METHOD = "GET";
    static final String POST_METHOD = "POST";
}

