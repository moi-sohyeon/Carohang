package com.example.cafetotravel;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    double lat, lon;
    private MapView mapView;
    private static NaverMap naverMap;
    private final String  TAG = "DetailActivity";
    String cafe_server, user_id;
    private String cafeCheck;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        ImageButton btn_main, btn_back, instaLink;
        TextView cafename, hour, cafecontent;
        Button cafeloc, cafenum;
        ImageView cafeimage;
        CheckBox check;

        btn_back = (ImageButton) findViewById(R.id.back_step);
        btn_main = (ImageButton) findViewById(R.id.main_btn);
        instaLink = (ImageButton) findViewById(R.id.instaLink);
        cafeloc = (Button) findViewById(R.id.cafeloc);
        cafename = (TextView) findViewById(R.id.cafename);
        cafenum = (Button) findViewById(R.id.cafenum);
        cafeimage = (ImageView) findViewById(R.id.cafeimage);
        cafecontent = (TextView) findViewById(R.id.cafecontent);
        hour = (TextView) findViewById(R.id.hour_text);
        check = (CheckBox) findViewById(R.id.check);

        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        user_id = intent.getStringExtra("사용자아이디");
        String cafeTitleInsta = intent.getStringExtra("카페이름인스타용");
        String cafeAddress = intent.getStringExtra("주소");
        String cafeNum = intent.getStringExtra("전화번호");
        String cafeImage = intent.getStringExtra("카페이미지");
        String cafeContent = intent.getStringExtra("컨텐츠");
        String cafeHour = intent.getStringExtra("운영시간");
        cafeCheck = intent.getStringExtra("체크박스");

        cafe_server = cafeTitleInsta;

        cafename.setText(cafeTitleInsta);
        cafecontent.setText(cafeContent);
        cafeloc.setText(cafeAddress);
        cafenum.setText(cafeNum);
        Glide.with(this).load(cafeImage).into(cafeimage);
        hour.setText(cafeHour);

        final Geocoder geocoder = new Geocoder( getApplicationContext() );

        List<Address> list = null;

        try {
            list = geocoder.getFromLocationName(cafeAddress,10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list != null) {
            Address address = list.get(0);
            lat = address.getLatitude();
            lon = address.getLongitude();
        }

        instaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView cafename = (TextView) findViewById(R.id.cafename);
                String param = cafename.getText().toString();
                String param4 = param.replace(" ","");
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/explore/tags/" + param4));
                startActivity(myIntent);
            }
        });

        check.setChecked((cafeCheck != null) ? true : false);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(),"찜 목록에 추가됨",Toast.LENGTH_SHORT).show();
                    InsertData insertData = new InsertData();
                    String serverURL = "http://128.134.65.126/cafejjim.php";
                    insertData.execute(serverURL);
                }
                //체크 안됐을때
                else{
                    Toast.makeText(getApplicationContext(),"찜 목록에서 삭제",Toast.LENGTH_SHORT).show();
                    InsertData insertData = new InsertData();
                    String serverURL = "http://128.134.65.126/cafejjim.php";
                    insertData.execute(serverURL);
                }
            }
        });

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    public void on1(View v){
        TextView cafenum = (TextView) findViewById(R.id.cafenum);
        String param2 = "tel:" + cafenum.getText().toString();
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(param2));
        startActivity(myIntent);
    }

    public void on2(View v) {
        TextView cafename = (TextView) findViewById(R.id.cafename);
        String param3 = cafename.getText().toString();
        String param7 = param3.replace(" ", "");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://map.naver.com/v5/search/" + param7));
        startActivity(myIntent);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(lat, lon),
                17
        );
        naverMap.setCameraPosition(cameraPosition);
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        Log.d( TAG, "onMapReady");

        Marker marker = new Marker();
        marker.setWidth(105);
        marker.setHeight(150);
        marker.setIcon(OverlayImage.fromResource(R.mipmap.locmark));
        marker.setPosition(new LatLng(lat, lon));
        marker.setMap(naverMap);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public class InsertData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection;
            String postParameters = "userID=" + user_id + "&loveCafe=" + cafe_server;
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
            System.out.println(result);
            return result;
        }
    }

}


