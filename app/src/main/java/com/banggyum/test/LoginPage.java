package com.banggyum.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;



public class LoginPage extends AppCompatActivity
{
    SignInButton btnSign;

    Button btnLogout;


    ActionBarDrawerToggle drawerToggle;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN= 123;
    private FirebaseAuth mAuth; //인증 생성

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_login);

        mAuth = FirebaseAuth.getInstance(); //값 할당
        btnSign = findViewById(R.id.sing_in_button);
        btnLogout = findViewById(R.id.signOutButton);

        requestGoogleSignIn();

        btnSign.setOnClickListener(view -> {
            signIn(); //클릭시 호출
        });
        btnLogout.setOnClickListener(view -> {
            signOut(); //클릭시 호출
        });

        // 사이드 메뉴바 입니다.
        // 액션바를 툴바로 대체하기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    private void signIn(){ // 구글 로그인 클라이언트를 사용하여 인텐트를 호출, rc로그인 코드를 코드에 전달
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //팝업창에서 자신의 이메일 칸을 클릭하면, 이 메소드가 호출되고  요청의 값이 RC와 같은지?
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken()); //구글 자격 증명

                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        .edit();
                editor.putString("username", account.getDisplayName());
                editor.putString("useremail", account.getEmail());
                editor.putString("userPhoto", account.getPhotoUrl().toString());
                editor.apply(); //구글 사용자의 이름, 이메일, 프로필사진을 가져옴

            } catch (ApiException e) {
                Toast.makeText(LoginPage.this, "Authentication Failed" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        //자격 증명? 을 가져옴
        mAuth.signInWithCredential(credential) //선택한 자격 증명을 전달함
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //성공시
                            // sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginPage.this, MainActivity.class);
                            startActivity(intent);
                        } else {
//                            updateUI(null);
                            Toast.makeText(LoginPage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestGoogleSignIn() {
        //보낼 요청 생성
        // 구글에 이메일 계정을 열어 팝업 표시하도록 요청하여 이 요청을 보낼것
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences.Editor client = getApplicationContext()
                .getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .edit();
        client.putString("mGoogleSignInClient", mGoogleSignInClient.toString());
        client.apply(); //구글 사용자의 이름, 이메일, 프로필사진을 가져옴

    }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(getApplicationContext(),LoginPage.class));
                    }
                });
        Toast.makeText(LoginPage.this, "Sign Out Success", Toast.LENGTH_SHORT).show();
    }

}

