package com.example.cafetotravel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "LoginSub";

    private View loginButton;
    private static String ID, Nickname, Email, Gender, Age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginButton = findViewById(R.id.login);

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                //토큰 있으면 로그인 성공, 없으면 실패
                if (oAuthToken != null) {

                }
                //오류있으면 throwable은 null 아님
                if (throwable != null) {
                    //TBD
                }
                updateKakaoLoginUi();
                return null;
            }
        };

        //클릭시 설치 여부
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(SplashActivity.this)) {
                    //true라면 설치되어있으니 카카오톡 로그인
                    UserApiClient.getInstance().loginWithKakaoTalk(SplashActivity.this, callback);
                } else {
                    //설치 안됨
                    UserApiClient.getInstance().loginWithKakaoAccount(SplashActivity.this, callback);
                }
            }
        });
        updateKakaoLoginUi();
    }

    private void updateKakaoLoginUi() {
        //로그인되어있는지 확인
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {
                    //유저 있으면 로그인 버튼 사라지기

                    ID = user.getId().toString();
                    Nickname = user.getKakaoAccount().getProfile().getNickname();
                    Email = user.getKakaoAccount().getEmail();
                    Gender = user.getKakaoAccount().getGender().toString();
                    Age = user.getKakaoAccount().getAgeRange().toString();

                    Log.i(TAG, "invoke: id=" + user.getId());
                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("user_id", ID);
                    startActivity(intent);

                    loginButton.setVisibility(View.GONE);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                }
                if (throwable != null) {
                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                }

                InsertData insertData = new InsertData();
                String serverURL = "http://128.134.65.126/register.php";
                insertData.execute(serverURL);


                String postParameters = "userID="+ID+"&userNickname="+Nickname+"&userEmail="+Email+"&userGender="+Gender+"&userAge="+Age;
                return null;
            }
        });
    }

    public class InsertData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection;
            String postParameters = "userID="+ID+"&userNickname="+Nickname+"&userEmail="+Email+"&userGender="+Gender+"&userAge="+Age;
            String result = null;
            try {
                //Connect
                urlConnection = (HttpURLConnection) (new URL(params[0]).openConnection());
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();

                //Write
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                //Read
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();


                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                result = sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
