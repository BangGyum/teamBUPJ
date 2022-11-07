///**
// * Copyright 2021 Google Inc. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.banggyum.test;
////구글 ID토큰을 사용하여 Google Firebase 인증
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//public class GoogleSignInActivity extends Activity {
//
//    private static final String TAG = "GoogleActivity";
//    private static final int RC_SIGN_IN = 9001;
//
//    // [START declare_auth]
//    private FirebaseAuth mAuth;
//    // [END declare_auth]
//
//    private GoogleSignInClient mGoogleSignInClient;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // [START config_signin]
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                //.requestIdToken("teambupj")
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        mAuth = FirebaseAuth.getInstance();
//
//    }
//
//    // [START on_start_check_user]
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//    // [END on_start_check_user]
//
//    // [START onactivityresult]
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//            }
//        }
//    }
//    // [END onactivityresult]
//
//    // [START auth_with_google]
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            updateUI(null);
//                        }
//                    }
//                });
//    }
//    // [END auth_with_google]
//
//    // [START signin]
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//    // [END signin]
//
//    private void updateUI(FirebaseUser user) {
//
//    }
//}



//fragment schedule

//리사이클뷰에 DB에 있는 일정들 추가
//    @SuppressLint("NotifyDataSetChanged")
//    public void addRecylerItem(){
//        //일정 select
//        //List<ScheduleDTO> selectScheList = db.selectSchedules();
//
//        //DB에 일정들 순서대로 추가
//        for (int i = 0; i< contactList.size(); i++){
//            HashMap<String, String> sdSelect;
//            sdSelect = contactList.get(i);
//
//            ScheduleDTO SD = new ScheduleDTO(Integer.parseInt(sdSelect.get("schedule_id")),
//                                    sdSelect.get("schedule_context"),
//                                    sdSelect.get("schedule_date"),
//                                    sdSelect.get("schedule_date"),
//                                    sdSelect.get("schedule_time"),
//                                    (short)1,
//                                    sdSelect.get("schedule_time"),
//                                    sdSelect.get("schedule_time")
//                    );
//            listItem.add(SD);
//            scheduleItemAdapter.notifyDataSetChanged();
//        }
//    }
/*
    public void add2() {

//        pDialog = new ProgressDialog(getContext());// Showing progress dialog
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
        JsonParser sh = new JsonParser();
        String jsonStr = sh.convertJson(Constant.ScheduleAllSelect_URL);// Making a request to url and getting response
        Log.e(TAG, "Response from url: " + jsonStr);
        //Log.v("ad",jsonStr);
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
                    // Phone node is JSON Object //
                    // JSONObject phone = c.getJSONObject("phone");
                    // String mobile = phone.getString("mobile");
                    // String home = phone.getString("home");
                    // String office = phone.getString("office");
                    // tmp hash map for single contact
                    //                        HashMap<String, String> employee = new HashMap<>();
                    // adding each child node to HashMap key => value
                    //                        employee.put("id", schedule_id);
                    //                        employee.put("email", email);
                    //                        employee.put("schedule_context", schedule_context);
                    //                        employee.put("salary", salary);
                    // adding contact to contact list

                    HashMap<String, String> schedule_list = new HashMap<>();
                    // adding each child node to HashMap key => value
                    schedule_list.put("schedule_id", schedule_id);
                    schedule_list.put("schedule_email", schedule_email);
                    schedule_list.put("schedule_context", schedule_context);
                    schedule_list.put("schedule_date", schedule_date);
                    schedule_list.put("schedule_time", schedule_time);
                    contactList.add(schedule_list);
                    Log.v("태그","check");
//
//                    scD.setSchedule_id(Integer.parseInt(schedule_id));
//                    scD.setschedule_email(schedule_email);
//                    scD.setSchedule_context(schedule_context);
//                    scD.setSchedule_date(schedule_date);
//                    scD.setSchedule_time(schedule_time);
//                    scD.setSchedule_state(Short.parseShort(schedule_state));
//
//
//                    mList.add(scD);
//                    //scD.add(employee);
//                    listItem.add(scD);
//
//                        ////리사이클러에 내용들을 추가해주기 위해 어댑터에 아이템들을 넘겨줌
//                        scheduleItemAdapter = new ScheduleItemAdapter(mList, (ScheduleItemAdapter.ItemClickListener) this);
//                        ////리사이클에 어댑터를 통해 아이템 추가 및 수정, 삭제
//                        recyclerView.setAdapter(scheduleItemAdapter);
//
//                        scheduleItemAdapter.notifyDataSetChanged(); //리스트의 크기와 아이템이 둘 다 변경되는 경우에 사용
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(),
//                                "Json parsing error: " + e.getMessage(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        }
    }
*/



//            super.onPostExecute(result);
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            /**              * Updating parsed JSON data into ListView              */
//            ListAdapter adapter = new SimpleAdapter(
//                    getContext(),
//                    contactList,
//                    R.layout.recyleview_item,
//                    new String[]{"schedule_id", "schedule_context", "schedule_date"}
//                    , new int[]{R.id.itemtv}); //원래 editText네
//            recyclerView.setAdapter((RecyclerView.Adapter) adapter);



//
//                        scD.setSchedule_id(Integer.parseInt(schedule_id));
//                        scD.setschedule_email(schedule_email);
//                        scD.setSchedule_context(schedule_context);
//                        scD.setSchedule_date(schedule_date);
//                        scD.setSchedule_time(schedule_time);
//                        scD.setSchedule_state(Short.parseShort(schedule_state));
//
//
//                        mList.add(scD);
//                        //scD.add(employee);
//                        listItem.add(scD);
//
//                        ////리사이클러에 내용들을 추가해주기 위해 어댑터에 아이템들을 넘겨줌
//                        scheduleItemAdapter = new ScheduleItemAdapter(mList, (ScheduleItemAdapter.ItemClickListener) this);
//                        ////리사이클에 어댑터를 통해 아이템 추가 및 수정, 삭제
//                        recyclerView.setAdapter(scheduleItemAdapter);
//
//                        scheduleItemAdapter.notifyDataSetChanged(); //리스트의 크기와 아이템이 둘 다 변경되는 경우에 사용

// Phone node is JSON Object //
// JSONObject phone = c.getJSONObject("phone");
// String mobile = phone.getString("mobile");
// String home = phone.getString("home");
// String office = phone.getString("office");
// tmp hash map for single contact
//                        HashMap<String, String> employee = new HashMap<>();
//                            // adding each child node to HashMap key => value
//    //                        employee.put("id", schedule_id);
//                        employee.put("email", email);
//                        employee.put("schedule_context", schedule_context);
//                        employee.put("salary", salary);
// adding contact to contact list
