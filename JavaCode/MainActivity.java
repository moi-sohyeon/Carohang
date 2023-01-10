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

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    ImageButton btn_jjim, btn_recommend, btn_mypage, btn_search, btn_locationfix;
    LinearLayout search_page;
    FragmentPagerAdapter adapterViewPager;
    TextView location_result, recommend_1;

    private MainAdapter adapter_main;

    HashMap<String, HashMap<String, ArrayList>> place_cafe = new HashMap<String, HashMap<String, ArrayList>>();

    ArrayList<String> PlaceList = new ArrayList<>();
    ArrayList<String> KeywordList = new ArrayList<>();

    HashMap<String, HashMap<String, HashMap<String, String>>> place_map = new HashMap<>();

    List<String> listTitle = new ArrayList<String>(Arrays.asList());
    List<String> listTitleInsta = new ArrayList<String>(Arrays.asList());
    List<String> listAddress = new ArrayList<String>(Arrays.asList());
    List<String> listImageUrl = new ArrayList<String>(Arrays.asList());
    List<String> listCallNum = new ArrayList<String>(Arrays.asList());
    List<String> listContent = new ArrayList<String>(Arrays.asList());
    List<String> listHour = new ArrayList<String>(Arrays.asList());

    String user_id, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("Debug", Utility.INSTANCE.getKeyHash(this));

        btn_jjim = (ImageButton) findViewById(R.id.btn_jjim);
        btn_recommend = (ImageButton) findViewById(R.id.btn_recommend);
        btn_mypage = (ImageButton) findViewById(R.id.btn_mypage);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_locationfix = (ImageButton) findViewById(R.id.btn_locationfix);
        search_page = (LinearLayout) findViewById(R.id.search_page);
        location_result = (TextView) findViewById(R.id.location_result);
        recommend_1 = (TextView) findViewById(R.id.recommend_1);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        user_id = intent.getStringExtra("user_id");
        location_result.setText(location);

        btn_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FragRecommend.class);
                intent.putExtra("location", location_result.getText().toString());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_jjim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FragJjim.class);
                intent.putExtra("location", location_result.getText().toString());
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

        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FragMypage.class);
                intent.putExtra("location", location_result.getText().toString());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FragSearch.class);
                intent.putExtra("location", location_result.getText().toString());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_locationfix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDialogFragment e = EventDialogFragment.getInstance();
                e.show(getSupportFragmentManager(), EventDialogFragment.TAG_EVENT_DIALOG);
            }
        });

        search_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FragSearch.class);
                intent.putExtra("location", location_result.getText().toString());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);

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

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FirstFragment.newInstance(0, "하이앤드라이");
                case 1:
                    return SecondFragment.newInstance(1, "할아버지 공장");
                case 2:
                    return ThirdFragment.newInstance(2, "시즈더데이");
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter_main = new MainAdapter();
        recyclerView.setAdapter(adapter_main);
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
        listCallNum.clear();
        listHour.clear();
        listAddress.clear();
        listContent.clear();
        listImageUrl.clear();
        listTitleInsta.clear();
        listTitle.clear();
        /*
         *   - 지역 선택하기 전
         *       지역, 키워드 랜덤으로 추천
         *   - 지역 선택 후
         *       해당 지역에 따라 키워드 랜덤으로 추천
         */
        // 지역을 선택했을 때
        if (PlaceList.contains(location_result.getText().toString())) {
            Random random = new Random();

            for (int i = 0; i < PlaceList.size(); i++) {
                String place = PlaceList.get(i);

                if (place.equals(location_result.getText().toString())) {
                    while (true) {
                        String keyword = KeywordList.get(random.nextInt(KeywordList.size()));
                        ArrayList cafe_name = place_cafe.get(place).get(keyword);

                        if (cafe_name.size() >= 10) {
                            recommend_1.setText(place+"에서\n"+keyword+" 카페는 어때요?");
                            Iterator<String> cafe_iter = place_map.get(place).keySet().iterator();

                            while (cafe_iter.hasNext()) {
                                String cafe = cafe_iter.next();

                                if (cafe_name.contains(cafe)) { // 해당 지역의 카페들이 카페 list(in keyword)에 포함되면
                                    listTitle.add(cafe);
                                    listTitleInsta.add(cafe);
                                    listImageUrl.add(String.valueOf(place_map.get(place).get(cafe).get("image")));
                                    listCallNum.add(String.valueOf(place_map.get(place).get(cafe).get("call")));
                                    listContent.add(String.valueOf(place_map.get(place).get(cafe).get("content")));
                                    listAddress.add(String.valueOf(place_map.get(place).get(cafe).get("location")));
                                    listHour.add(String.valueOf(place_map.get(place).get(cafe).get("hour")));
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        // 지역을 선택하지 않았을 때 --> else
        else {

            // "OO지역 이런 느낌의 카페는 어때요?"
            // random place / cafe 개수가 10개 이상인 keyword
            Random random = new Random();

            while (true) {
                String place = PlaceList.get(random.nextInt(PlaceList.size()));
                String keyword = KeywordList.get(random.nextInt(KeywordList.size()));
                ArrayList cafe_name = place_cafe.get(place).get(keyword);

                if (cafe_name.size() >= 10) {
                    recommend_1.setText(place+"에서\n"+keyword+" 카페는 어때요?");
                    Iterator<String> cafe_iter = place_map.get(place).keySet().iterator();

                    while (cafe_iter.hasNext()) {
                        String cafe = cafe_iter.next();

                        if (cafe_name.contains(cafe)) { // 해당 지역의 카페들이 카페 list(in keyword)에 포함되면
                            listTitle.add(cafe);
                            listTitleInsta.add(cafe);
                            listImageUrl.add(String.valueOf(place_map.get(place).get(cafe).get("image")));
                            listContent.add(String.valueOf(place_map.get(place).get(cafe).get("content")));
                            listCallNum.add(String.valueOf(place_map.get(place).get(cafe).get("call")));
                            listAddress.add(String.valueOf(place_map.get(place).get(cafe).get("location")));
                            listHour.add(String.valueOf(place_map.get(place).get(cafe).get("hour")));
                        }
                    }
                    break;
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
            adapter_main.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter_main.notifyDataSetChanged();
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