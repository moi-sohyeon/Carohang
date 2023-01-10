package com.example.cafetotravel;

import static android.view.View.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class FragMypage extends AppCompatActivity {
    private static final String TAG = "FragMypage";
    private LinearLayout loginButton, logoutButton;
    private TextView nickName, email;
    private ImageView profileImage;
    ImageButton btn_jjim, btn_recommend, btn_main, btn_search;
    String result, user_id;

    private static String ID, Nickname, Email, Gender, Age;
    ArrayList<String> UserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_mypage);

        btn_jjim = (ImageButton) findViewById(R.id.btn_jjim);
        btn_recommend = (ImageButton) findViewById(R.id.btn_recommend);
        btn_main = (ImageButton) findViewById(R.id.btn_main);
        btn_search = (ImageButton) findViewById(R.id.btn_search);


        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.profile);
        email = findViewById(R.id.email);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        user_id = intent.getStringExtra("user_id");

        btn_recommend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragMypage.this, FragRecommend.class);
                intent.putExtra("location", location);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_jjim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragMypage.this, FragJjim.class);
                intent.putExtra("location", location);
                intent.putExtra("user_id", user_id);

                ResponseData responseData = new ResponseData();
                String serverURL = "http://128.134.65.126/response.php";
                try {
                    result = responseData.execute(serverURL).get();
                    intent.putExtra("response_jjim", result);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        btn_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragMypage.this, MainActivity.class);
                intent.putExtra("location", location);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragMypage.this, FragSearch.class);
                intent.putExtra("location", location);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        // 카카오가 설치되어 있는지 확인 하는 메서드또한 카카오에서 제공 콜백 객체를 이용함
        Function2<OAuthToken, Throwable, Unit> callback = new  Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null) {
                }
                if (throwable != null) {
                }
                updateKakaoLoginUi();
                return null;
            }
        };

        // 로그인 버튼
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(FragMypage.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(FragMypage.this, callback);
                }else {
                    UserApiClient.getInstance().loginWithKakaoAccount(FragMypage.this, callback);
                }
            }
        });
        // 로그 아웃 버튼
        logoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });
            }
        });
        updateKakaoLoginUi();
    }

    private void updateKakaoLoginUi(){
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user!=null){
                    // 로그인이 되어있으면
                    ID = user.getId().toString();
                    Nickname = user.getKakaoAccount().getProfile().getNickname();
                    Email = user.getKakaoAccount().getEmail();
                    Gender = user.getKakaoAccount().getGender().toString();
                    Age = user.getKakaoAccount().getAgeRange().toString();

                    nickName.setText(Nickname);
                    email.setText(Email);
                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getProfileImageUrl()).into(profileImage);
                    loginButton.setVisibility(GONE);
                    logoutButton.setVisibility(VISIBLE);
                }else {
                    // 로그인이 되어 있지 않다면 위와 반대로
                    nickName.setText(null);
                    profileImage.setImageBitmap(null);
                    email.setText(null);
                    loginButton.setVisibility(VISIBLE);
                    logoutButton.setVisibility(GONE);
                }
                return null;
            }
        });
    }

    public class ResponseData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection;
            String postParameters = "userID=" + user_id;
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
            System.out.println(result);
            return result;
        }
    }
}
