package com.example.cafetotravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakao.sdk.common.util.Utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class FragJjim extends AppCompatActivity {

    ImageButton btn_main, btn_recommend, btn_mypage, btn_search;
    private RecyclerAdapter adapter;
    public String user_id, result;

    List<String> listTitle = new ArrayList<String>(Arrays.asList());
    List<String> listTitleInsta = new ArrayList<String>(Arrays.asList());
    List<String> listAddress = new ArrayList<String>(Arrays.asList());
    List<String> listContent = new ArrayList<String>(Arrays.asList());
    List<String> listImageUrl = new ArrayList<String>(Arrays.asList());
    List<String> listCallNum = new ArrayList<String>(Arrays.asList());
    List<String> listHour = new ArrayList<String>(Arrays.asList());

    List<String> PlaceList = new ArrayList<String>(Arrays.asList());

    HashMap<String, HashMap<String, HashMap<String, String>>> place_map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_jjim);

        btn_main = (ImageButton) findViewById(R.id.btn_main);
        btn_recommend = (ImageButton) findViewById(R.id.btn_recommend);
        btn_mypage = (ImageButton) findViewById(R.id.btn_mypage);
        btn_search = (ImageButton) findViewById(R.id.btn_search);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        user_id = intent.getStringExtra("user_id");
        result = intent.getStringExtra("response_jjim");

        System.out.println(result);

        btn_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragJjim.this, FragRecommend.class);
                intent.putExtra("location", location);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragJjim.this, MainActivity.class);
                intent.putExtra("location", location);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragJjim.this, FragMypage.class);
                intent.putExtra("location", location);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragJjim.this, FragSearch.class);
                intent.putExtra("location", location);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        Thread thread = new Thread(){
            @Override
            public void run(){
                jsonData();
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        init();
        getData();
        setData();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void jsonData(){
        String CafeDATA = new String();

        String cafedbURL = "http://128.134.65.126/cafeDB.php";

        try {   // cafeDATA.json 불러오기
            StringBuilder sb = new StringBuilder();
            InputStreamReader in = null;
            URL url = new URL(cafedbURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            if (conn != null)
                conn.setReadTimeout(60 * 1000);
            if (conn != null && conn.getInputStream() != null) {
                in = new InputStreamReader(conn.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                }
            }
            in.close();
            CafeDATA = sb.toString();
        } catch (Exception e) {
            System.out.println("Error result: "+ e.toString());
        }

        JSONParser jsonParse = new JSONParser();

        try {
            Object obj = jsonParse.parse(CafeDATA);
            JSONObject jsonObject = (JSONObject)obj;

            // 지역 이름 저장하기
            Iterator<String> place_iter = jsonObject.keySet().iterator();
            while(place_iter.hasNext())
            {
                String place = place_iter.next();
                PlaceList.add(place);
            }

            for (int i=0; i< PlaceList.size(); i++) {
                // 지역에 해당하는 카페 내용
                String place = PlaceList.get(i);
                JSONArray jsonArray = (JSONArray) jsonObject.get(place);

                HashMap<String, HashMap<String, String>> cafe_map = new HashMap<>();

                for (int j=0; j< jsonArray.size(); j++) {
                    // 지역에 해당하는 카페 이름 list
                    JSONObject jsonObj = (JSONObject) jsonArray.get(j);

                    HashMap<String, String> info_map = new HashMap<>();

                    info_map.put("location", String.valueOf(jsonObj.get("location")));
                    info_map.put("image", String.valueOf(jsonObj.get("image")));
                    info_map.put("call", String.valueOf(jsonObj.get("num")));
                    info_map.put("hour", String.valueOf(jsonObj.get("runtime")));
                    info_map.put("content", String.valueOf(jsonObj.get("content")));

                    cafe_map.put(String.valueOf(jsonObj.get("cafe")), info_map);
                }
                place_map.put(place, cafe_map);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        for (int i = 0; i < PlaceList.size(); i++) {
            String place = PlaceList.get(i);

            Iterator<String> cafe_iter = place_map.get(place).keySet().iterator();

            while (cafe_iter.hasNext()) {
                String cafe = cafe_iter.next();

                if(result.contains(cafe)){
                    listTitle.add(cafe);
                    listTitleInsta.add(cafe);
                    listImageUrl.add(String.valueOf(place_map.get(place).get(cafe).get("image")));
                    listCallNum.add(String.valueOf(place_map.get(place).get(cafe).get("call")));
                    listAddress.add(String.valueOf(place_map.get(place).get(cafe).get("location")));
                    listContent.add(String.valueOf(place_map.get(place).get(cafe).get("content")));
                    listHour.add(String.valueOf(place_map.get(place).get(cafe).get("hour")));
                }
            }
        }
    }

    private void setData() {
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.

            Data data = new Data();

            data.setUser_id(user_id);
            data.setTitle(listTitle.get(i));
            data.setTitleInsta(listTitleInsta.get(i));
            data.setAddress(listAddress.get(i));
            data.setImageUrl(listImageUrl.get(i));
            data.setCallNum(listCallNum.get(i));
            data.setContent(listContent.get(i));
            data.setHour(listHour.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }


}
