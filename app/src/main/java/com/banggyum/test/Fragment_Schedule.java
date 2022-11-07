package com.banggyum.test;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_Schedule extends Fragment implements ScheduleItemAdapter.ItemClickListener {
    private ArrayList<ScheduleDTO> listItem;
    List<ScheduleDTO> mList = new ArrayList<ScheduleDTO>();
    ArrayList<HashMap<String, String>> contactList;
    private RecyclerView recyclerView;
    private ScheduleItemAdapter scheduleItemAdapter;

    private LinearLayoutManager linearLayoutManager;
    private MyDatabaseHelper db;
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    String schedule_state;

    public Fragment_Schedule() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment__schedule, null);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_fab);
        recyclerView = view.findViewById(R.id.rcv_sc);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        contactList = new ArrayList<>();

        listItem = new ArrayList<>();

        new Handler().execute();

        //DB를 사용하기위한 생성자
        db = new MyDatabaseHelper(view.getContext());

        //리사이클러에 내용들을 추가해주기 위해 어댑터에 아이템들을 넘겨줌
        scheduleItemAdapter = new ScheduleItemAdapter(listItem, this);
        //리사이클에 어댑터를 통해 아이템 추가 및 수정, 삭제
        recyclerView.setAdapter(scheduleItemAdapter);
        //리사이클뷰 새로고침
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //플롱팅버튼 클릭시 일정 추가위한 팝업 띄어줌
                Intent intentDB = new Intent(view.getContext(), PopupActivity.class);
                startActivityForResult(intentDB, 1);
            }
        });

        //DB에 있는 정보 recyleview에 추가
        //add2();
        //addRecylerItem();
        return view;
    }

    //일정 추가 팝업 창에서 확인 버튼 클릭시 실행
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //리사이클뷰 초기화
                listItem.clear();
                contactList.clear();
                //다시 추가
                new Handler().execute();
                addRecylerItem();// 이거는 다시 확인할 수가 있나?

            }
        }
    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class Handler extends AsyncTask<Void, Void, Void> {

        private ListAdapter adapter;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getContext());// Showing progress dialog
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JsonParser sh = new JsonParser();
            String jsonStr = sh.convertJson(Constant.ScheduleAllSelect_URL);// Making a request to url and getting response
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray employeeArray = jsonObj.getJSONArray("result");// Getting JSON Array node
                    for (int i = 0; i < employeeArray.length(); i++) { // looping through All Contacts
                        ScheduleDTO scD = new ScheduleDTO();
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
                        schedule_list.put("schedule_state", schedule_state);
                        contactList.add(schedule_list);
                        db.addLocalSchedule(Integer.parseInt(schedule_id), schedule_email, schedule_context,
                                schedule_date, schedule_time, Integer.parseInt(schedule_state));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            a();

            if (pDialog.isShowing())
                pDialog.dismiss();
            for (int i = 0; i < contactList.size(); i++) {
                HashMap<String, String> sdSelect;
                sdSelect = contactList.get(i);

                ScheduleDTO SD = new ScheduleDTO(Integer.parseInt(sdSelect.get("schedule_id")),
                        sdSelect.get("schedule_context"),
                        sdSelect.get("schedule_date"),
                        sdSelect.get("schedule_time"),
                        Short.valueOf(sdSelect.get("schedule_state")),
                        sdSelect.get("schedule_registerDate"),
                        sdSelect.get("schedule_email")
                );
                Log.v("bbbb", schedule_state);
                Log.v("bbb1", String.valueOf(SD.getSchedule_state()));
                if (schedule_state.equals(String.valueOf(SD.getSchedule_state()))) {

                    listItem.add(SD);
                    scheduleItemAdapter.notifyDataSetChanged();
//                if( SD.getSchedule_state() == 1) {
//
//                    listItem.add(SD);
//                    scheduleItemAdapter.notifyDataSetChanged();
//                }

                }
                //b();
            }
        }
    }

    private void a(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("state", Context.MODE_PRIVATE);
        schedule_state = preferences.getString("schedule_state", "");
        Log.v("bbb",schedule_state);
    }
    private void b() {
        SharedPreferences.Editor state =  this.getActivity()
                .getSharedPreferences("state", Context.MODE_PRIVATE)
                .edit();
        state.putString("schedule_state", "0");
        state.apply();
    }

    public void addRecylerItem() {
        for (int i = 0; i < contactList.size(); i++) {
            HashMap<String, String> sdSelect;
            sdSelect = contactList.get(i);

            ScheduleDTO SD = new ScheduleDTO(Integer.parseInt(sdSelect.get("schedule_id")),
                    sdSelect.get("schedule_context"),
                    sdSelect.get("schedule_date"),
                    sdSelect.get("schedule_time"),
                    Short.valueOf(sdSelect.get("schedule_state")),
                    sdSelect.get("schedule_registerDate"),
                    sdSelect.get("schedule_email")
            );
            Log.v("aaaaaaa", Integer.toString(SD.getSchedule_state()));


            Log.v("bbbb", schedule_state);
            //if( SD.getSchedule_state() == 1) {

            if (schedule_state.equals("0")) {

                listItem.add(SD);
                scheduleItemAdapter.notifyDataSetChanged();
            }
        }


    }

    //일정의 텍스트 클릭시 자세한 일정 보여주는 창 띄어줌
    @Override
    public void onItemClick(ScheduleDTO scheduleDTO) {
        ScheduleBottomSheet scheduleBottomSheet = new ScheduleBottomSheet(scheduleDTO);
        scheduleBottomSheet.show(getActivity().getSupportFragmentManager(), scheduleBottomSheet.getTag());
    }
}
