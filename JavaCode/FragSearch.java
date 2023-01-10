package com.example.cafetotravel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FragSearch extends AppCompatActivity {

    private EditText editSearch;        // 검색어를 입력할 Input 창
    private RecyclerAdapter adapter;
    List<String> listTitle = new ArrayList<String>(Arrays.asList());
    List<String> listTitleInsta = new ArrayList<String>(Arrays.asList());
    List<String> listAddress = new ArrayList<String>(Arrays.asList());
    List<String> listImageUrl = new ArrayList<String>(Arrays.asList());
    List<String> listCallNum = new ArrayList<String>(Arrays.asList());
    List<String> listHour = new ArrayList<String>(Arrays.asList());
    List<String> listContent= new ArrayList<String>(Arrays.asList());
    private TextView location_search;

    String user_id;

    HashMap<String, HashMap<String, ArrayList>> place_cafe = new HashMap<String, HashMap<String, ArrayList>>();

    ArrayList<String> PlaceList = new ArrayList<>();
    ArrayList<String> KeywordList = new ArrayList<>();

    HashMap<String, HashMap<String, HashMap<String, String>>> place_map = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editSearch = (EditText) findViewById(R.id.editSearch);
        location_search = (TextView) findViewById(R.id.location_search);

        Intent intent = getIntent();
        String text = intent.getStringExtra("location");
        user_id = intent.getStringExtra("user_id");
        location_search.setText(text);

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

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });


    }


    public void search(String charText) {
        adapter.clear();

        if (charText.length() == 0) {
            init();
            getData();
            setData();
        } else {
            for(int i = 0;i < listTitle.size(); i++) {
                if (listTitle.get(i).toLowerCase().contains(charText)) {
                    Data data = new Data();

                    data.setUser_id(user_id);
                    data.setTitle(listTitle.get(i));
                    data.setTitleInsta(listTitleInsta.get(i));
                    data.setAddress(listAddress.get(i));
                    data.setImageUrl(listImageUrl.get(i));
                    data.setCallNum(listCallNum.get(i));
                    data.setContent(listContent.get(i));
                    data.setHour(listHour.get(i));

                    adapter.addItem(data);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void jsonData(){
        String Cafe_Classification = new String();
        String CafeDATA = new String();

        String keywordURL = "http://128.134.65.126/test1.php";
        String cafedbURL = "http://128.134.65.126/cafeDB.php";

        try {   // Cafe_Classification.json 불러오기
            StringBuilder sb = new StringBuilder();
            InputStreamReader in = null;
            URL url = new URL(keywordURL);
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
            Cafe_Classification = sb.toString();
        } catch (Exception e) {
            System.out.println("Error result: "+ e.toString());
        }

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
            JSONObject jsonObject = (JSONObject) jsonParse.parse(Cafe_Classification);
            // 지역 이름 저장하기
            Iterator<String> place_iter = jsonObject.keySet().iterator();
            while(place_iter.hasNext())
            {
                String place = place_iter.next();
                PlaceList.add(place);
            }

            // 키워드 저장하기
            JSONObject jsonObj = (JSONObject) jsonObject.get(PlaceList.get(0));
            Iterator<String> keyword_iter = jsonObj.keySet().iterator();
            while(keyword_iter.hasNext())
            {
                String keyword = keyword_iter.next();
                KeywordList.add(keyword);
            }

            // outer hashmap: key=place, value=inner hashmap
            for (int k=0; k<PlaceList.size(); k++) {
                String place = PlaceList.get(k);
                JSONObject Objcafe = (JSONObject) jsonObject.get(place);

                HashMap<String, ArrayList> keyword_cafe = new HashMap<>();

                // inner hashmap: key=keyword, value=cafelist
                for (int i=0; i<KeywordList.size(); i++) {
                    String keyword = KeywordList.get(i);
                    JSONArray jsonArray = (JSONArray) Objcafe.get(keyword);
                    // 키워드에 해당하는 카페 리스트
                    ArrayList<String> CafeList = new ArrayList<>();
                    for (int j=0; j<jsonArray.size(); j++) {
                        String cafe = jsonArray.get(j).toString();
                        CafeList.add(cafe);
                    }
                    keyword_cafe.put(keyword, CafeList);
                }
                place_cafe.put(place, keyword_cafe);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Object obj = jsonParse.parse(CafeDATA);
            JSONObject jsonObject = (JSONObject) obj;

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
        if (PlaceList.contains(location_search.getText().toString())) {
            for (int i = 0; i < PlaceList.size(); i++) {
                String place = PlaceList.get(i);

                if (place.equals(location_search.getText().toString())) {
                    Iterator<String> cafe_iter = place_map.get(place).keySet().iterator();

                    while (cafe_iter.hasNext()) {
                        String cafe = cafe_iter.next();

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
        // 지역을 선택하지 않았을 때 --> else
        else {
            for (int i = 0; i < PlaceList.size(); i++) {
                String place = PlaceList.get(i);

                Iterator<String> cafe_iter = place_map.get(place).keySet().iterator();

                while (cafe_iter.hasNext()) {
                    String cafe = cafe_iter.next();
                    listTitle.add(cafe);
                    listTitleInsta.add(cafe);
                    listImageUrl.add(String.valueOf(place_map.get(place).get(cafe).get("image")));
                    listCallNum.add(String.valueOf(place_map.get(place).get(cafe).get("call")));
                    listAddress.add(String.valueOf(place_map.get(place).get(cafe).get("location")));
                    listHour.add(String.valueOf(place_map.get(place).get(cafe).get("hour")));
                    listContent.add(String.valueOf(place_map.get(place).get(cafe).get("content")));
                }
            }
        }
        init();
        setData();
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
            data.setHour(listHour.get(i));
            data.setContent(listContent.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
