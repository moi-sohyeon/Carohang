package com.example.cafetotravel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.concurrent.ExecutionException;

public class FragRecommend extends AppCompatActivity {

    ImageButton btn_jjim, btn_main, btn_mypage, btn_search;
    private RecyclerAdapter adapter;
    Chip chipType_1, chipType_2, chipType_3, chipType_4, chipType_5, chipType_6, chipType_7, chipType_8, chipType_9, chipType_10, chipType_11, chipType_12, chipType_13, chipType_14;
    Chip chipCharacter_1, chipCharacter_2, chipCharacter_3, chipCharacter_4, chipCharacter_5, chipCharacter_6, chipCharacter_7, chipCharacter_8, chipCharacter_9, chipCharacter_10, chipCharacter_11, chipCharacter_12, chipCharacter_13, chipCharacter_14, chipCharacter_15, chipCharacter_16, chipCharacter_17, chipCharacter_18, chipCharacter_19;
    Chip chipMood_1, chipMood_2, chipMood_3, chipMood_4, chipMood_5, chipMood_6, chipMood_7, chipMood_8, chipMood_9, chipMood_10, chipMood_11, chipMood_12, chipMood_13, chipMood_14, chipMood_15, chipMood_16, chipMood_17;
    TextView location_name;
    List<String> listTitle = new ArrayList<String>(Arrays.asList());
    List<String> listTitleInsta = new ArrayList<String>(Arrays.asList());
    List<String> listAddress = new ArrayList<String>(Arrays.asList());
    List<String> listContent = new ArrayList<String>(Arrays.asList());
    List<String> listImageUrl = new ArrayList<String>(Arrays.asList());
    List<String> listCallNum = new ArrayList<String>(Arrays.asList());
    List<String> listHour = new ArrayList<String>(Arrays.asList());

    HashMap<String, HashMap<String, ArrayList>> place_cafe = new HashMap<String, HashMap<String, ArrayList>>();

    List<String> PlaceList = new ArrayList<String>(Arrays.asList());
    ArrayList<String> KeywordList = new ArrayList<>();

    HashMap<String, HashMap<String, HashMap<String, String>>> place_map = new HashMap<>();

    public static ArrayList<String> chip_text = new ArrayList<>();
    String user_id, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_recommend);

        btn_jjim = (ImageButton) findViewById(R.id.btn_jjim);
        btn_main = (ImageButton) findViewById(R.id.btn_main);
        btn_mypage = (ImageButton) findViewById(R.id.btn_mypage);
        btn_search = (ImageButton) findViewById(R.id.btn_search);

        location_name = findViewById(R.id.location_name);

        chipType_1 = findViewById(R.id.chipType_1);
        chipType_2 = findViewById(R.id.chipType_2);
        chipType_3 = findViewById(R.id.chipType_3);
        chipType_4 = findViewById(R.id.chipType_4);
        chipType_5 = findViewById(R.id.chipType_5);
        chipType_6 = findViewById(R.id.chipType_6);
        chipType_7 = findViewById(R.id.chipType_7);
        chipType_8 = findViewById(R.id.chipType_8);
        chipType_9 = findViewById(R.id.chipType_9);
        chipType_10 = findViewById(R.id.chipType_10);
        chipType_11 = findViewById(R.id.chipType_11);
        chipType_12 = findViewById(R.id.chipType_12);
        chipType_13 = findViewById(R.id.chipType_13);
        chipType_14 = findViewById(R.id.chipType_14);
        chipCharacter_1 = findViewById(R.id.chipCharacter_1);
        chipCharacter_2 = findViewById(R.id.chipCharacter_2);
        chipCharacter_3 = findViewById(R.id.chipCharacter_3);
        chipCharacter_4 = findViewById(R.id.chipCharacter_4);
        chipCharacter_5 = findViewById(R.id.chipCharacter_5);
        chipCharacter_6 = findViewById(R.id.chipCharacter_6);
        chipCharacter_7 = findViewById(R.id.chipCharacter_7);
        chipCharacter_8 = findViewById(R.id.chipCharacter_8);
        chipCharacter_9 = findViewById(R.id.chipCharacter_9);
        chipCharacter_10 = findViewById(R.id.chipCharacter_10);
        chipCharacter_11 = findViewById(R.id.chipCharacter_11);
        chipCharacter_12 = findViewById(R.id.chipCharacter_12);
        chipCharacter_13 = findViewById(R.id.chipCharacter_13);
        chipCharacter_14 = findViewById(R.id.chipCharacter_14);
        chipCharacter_15 = findViewById(R.id.chipCharacter_15);
        chipCharacter_16 = findViewById(R.id.chipCharacter_16);
        chipCharacter_17 = findViewById(R.id.chipCharacter_17);
        chipCharacter_18 = findViewById(R.id.chipCharacter_18);
        chipCharacter_19 = findViewById(R.id.chipCharacter_19);
        chipMood_1 = findViewById(R.id.chipMood_1);
        chipMood_2 = findViewById(R.id.chipMood_2);
        chipMood_3 = findViewById(R.id.chipMood_3);
        chipMood_4 = findViewById(R.id.chipMood_4);
        chipMood_5 = findViewById(R.id.chipMood_5);
        chipMood_6 = findViewById(R.id.chipMood_6);
        chipMood_7 = findViewById(R.id.chipMood_7);
        chipMood_8 = findViewById(R.id.chipMood_8);
        chipMood_9 = findViewById(R.id.chipMood_9);
        chipMood_10 = findViewById(R.id.chipMood_10);
        chipMood_11 = findViewById(R.id.chipMood_11);
        chipMood_12 = findViewById(R.id.chipMood_12);
        chipMood_13 = findViewById(R.id.chipMood_13);
        chipMood_14 = findViewById(R.id.chipMood_14);
        chipMood_15 = findViewById(R.id.chipMood_15);
        chipMood_16 = findViewById(R.id.chipMood_16);
        chipMood_17 = findViewById(R.id.chipMood_17);

        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragRecommend.this, MainActivity.class);
                intent.putExtra("location", location_name.getText().toString());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_jjim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragRecommend.this, FragJjim.class);
                intent.putExtra("location", location_name.getText().toString());
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
                Intent intent = new Intent(FragRecommend.this, FragMypage.class);
                intent.putExtra("location", location_name.getText().toString());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragRecommend.this, FragSearch.class);
                intent.putExtra("location", location_name.getText().toString());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String text = intent.getStringExtra("location");
        user_id = intent.getStringExtra("user_id");
        location_name.setText(text);

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

        chipType_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_1.getText().toString();
                if (chipType_1.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_2.getText().toString();
                if (chipType_2.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_3.getText().toString();
                if (chipType_3.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_4.getText().toString();
                if (chipType_4.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_5.getText().toString();
                if (chipType_5.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_6.getText().toString();
                if (chipType_6.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_7.getText().toString();
                if (chipType_7.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_8.getText().toString();
                if (chipType_8.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_9.getText().toString();
                if (chipType_9.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_10.getText().toString();
                if (chipType_10.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_11.getText().toString();
                if (chipType_11.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_12.getText().toString();
                if (chipType_12.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_13.getText().toString();
                if (chipType_13.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipType_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipType_14.getText().toString();
                if (chipType_14.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });

        chipCharacter_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_1.getText().toString();
                if (chipCharacter_1.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_2.getText().toString();
                if (chipCharacter_2.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_3.getText().toString();
                if (chipCharacter_3.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_4.getText().toString();
                if (chipCharacter_4.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_5.getText().toString();
                if (chipCharacter_5.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_6.getText().toString();
                if (chipCharacter_6.isChecked()) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_7.getText().toString();
                if (chipCharacter_7.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });

        chipCharacter_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_8.getText().toString();
                if (chipCharacter_8.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_9.getText().toString();
                if (chipCharacter_9.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_10.getText().toString();
                if (chipCharacter_10.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });

        chipCharacter_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_11.getText().toString();
                if (chipCharacter_11.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });

        chipCharacter_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_12.getText().toString();
                if (chipCharacter_12.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_13.getText().toString();
                if (chipCharacter_13.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_14.getText().toString();
                if (chipCharacter_14.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_15.getText().toString();
                if (chipCharacter_15.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_16.getText().toString();
                if (chipCharacter_16.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_17.getText().toString();
                if (chipCharacter_17.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_18.getText().toString();
                if (chipCharacter_18.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipCharacter_19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipCharacter_19.getText().toString();
                if (chipCharacter_19.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });

        chipMood_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_1.getText().toString();
                if (chipMood_1.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_2.getText().toString();
                if (chipMood_2.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_3.getText().toString();
                if (chipMood_3.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_4.getText().toString();
                if (chipMood_4.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_5.getText().toString();
                if (chipMood_5.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_6.getText().toString();
                if (chipMood_6.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_7.getText().toString();
                if (chipMood_7.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_8.getText().toString();
                if (chipMood_8.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_9.getText().toString();
                if (chipMood_9.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_10.getText().toString();
                if (chipMood_10.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_11.getText().toString();
                if (chipMood_11.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_12.getText().toString();
                if (chipMood_12.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_13.getText().toString();
                if (chipMood_13.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_14.getText().toString();
                if (chipMood_14.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_15.getText().toString();
                if (chipMood_15.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_16.getText().toString();
                if (chipMood_16.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
        chipMood_17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chip_content = chipMood_17.getText().toString();
                if (chipMood_17.isChecked()==true) {
                    chip_text.add(chip_content);
                    getClickData();
                }
                else {
                    chip_text.remove(chip_content);
                    getClickData();
                }
            }
        });
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
            while(place_iter.hasNext()) {
                String place = place_iter.next();
                PlaceList.add(place);
            }

            // 키워드 저장하기
            JSONObject jsonObj = (JSONObject) jsonObject.get(PlaceList.get(0));
            Iterator<String> keyword_iter = jsonObj.keySet().iterator();
            while(keyword_iter.hasNext()) {
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
        if (PlaceList.contains(location_name.getText().toString())) {
            for (int i = 0; i < PlaceList.size(); i++) {
                String place = PlaceList.get(i);

                if (place.equals(location_name.getText().toString())) {
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
                    listContent.add(String.valueOf(place_map.get(place).get(cafe).get("content")));
                    listHour.add(String.valueOf(place_map.get(place).get(cafe).get("hour")));
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
            data.setContent(listContent.get(i));
            data.setHour(listHour.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

    private void getClickData() {
        adapter.clear();
        adapter.notifyDataSetChanged();

        if (PlaceList.contains(location_name.getText().toString())) {
            for (int p = 0; p < PlaceList.size(); p++) {
                String place = PlaceList.get(p);

                if (place.equals(location_name.getText().toString())) {

                    HashMap<String, ArrayList<String>> result_map = new HashMap<String, ArrayList<String>>();
                    HashMap<Integer, ArrayList<String>> filter_map = new HashMap<Integer, ArrayList<String>>();
                    if(chip_text.size()==0) {
                        setData();
                    } else {
                        for (int num = 0; num < chip_text.size(); num++) {
                            String keyword = chip_text.get(num);
                            if (num == 0) {
                                filter_map.put(num, place_cafe.get(place).get(keyword));
                            } else if (chip_text.contains(keyword)) {
                                ArrayList<String> pre_filter_cafe = filter_map.get(num - 1);
                                ArrayList<String> current_cafe = place_cafe.get(place).get(keyword);
                                ArrayList<String> filter_cafe = new ArrayList<String>();
                                for (int i = 0; i < current_cafe.size(); i++) {
                                    if (pre_filter_cafe.contains(current_cafe.get(i))) {
                                        filter_cafe.add(current_cafe.get(i));
                                    }
                                }
                                filter_map.put(num, filter_cafe);
                            }
                            if (num + 1 == chip_text.size()) {
                                result_map.put("result", filter_map.get(num));
                            }
                        }

                        ArrayList<String> cafe_list_current = new ArrayList<>();
                        for(int i=0; i < result_map.get("result").size(); i++){
                            cafe_list_current.add(result_map.get("result").get(i));
                        }

                        for (int i = 0; i < cafe_list_current.size(); i++) {
                            String cafe = cafe_list_current.get(i);

                            for(int j=0; j < listTitle.size(); j++){
                                if(cafe.equals(listTitle.get(j))){
                                    Data data = new Data();
                                    data.setTitle(cafe);
                                    data.setUser_id(user_id);
                                    data.setTitle(listTitle.get(j));
                                    data.setTitleInsta(listTitleInsta.get(j));
                                    data.setAddress(listAddress.get(j));
                                    data.setImageUrl(listImageUrl.get(j));
                                    data.setCallNum(listCallNum.get(j));
                                    data.setHour(listHour.get(j));
                                    data.setContent(listContent.get(i));

                                    adapter.addItem(data);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
        // 지역을 선택하지 않았을 때 --> else
        else{
            for (int p = 0; p < PlaceList.size(); p++) {
                String place = PlaceList.get(p);

                HashMap<String, ArrayList<String>> result_map = new HashMap<String, ArrayList<String>>();
                HashMap<Integer, ArrayList<String>> filter_map = new HashMap<Integer, ArrayList<String>>();
                if(chip_text.size()==0){
                    setData();
                }
                else{
                    for (int num = 0; num < chip_text.size(); num++) {
                        String keyword = chip_text.get(num);
                        if (num == 0) {
                            filter_map.put(num, place_cafe.get(place).get(keyword));   // 카페 리스트 초기화
                        } else if (chip_text.contains(keyword)) {   // 클릭된 키워드들 중에서 키워드가 중복되지 않으면
                            ArrayList<String> pre_filter_cafe = filter_map.get(num - 1);   // 이전 카페 리스트
                            ArrayList<String> current_cafe = place_cafe.get(place).get(keyword);   // 현재 카페 리스트
                            ArrayList<String> filter_cafe = new ArrayList<String>();   // filter한 카페 리스트
                            for (int i = 0; i < current_cafe.size(); i++) {
                                if (pre_filter_cafe.contains(current_cafe.get(i))) {   // 이전 카페리스트에 현재 카페가 포함되나?
                                    filter_cafe.add(current_cafe.get(i));
                                }
                            }
                            filter_map.put(num, filter_cafe);
                        }
                        if (num + 1 == chip_text.size()) {
                            result_map.put("result", filter_map.get(num));
                        }
                    }

                    ArrayList<String> cafe_list_current = new ArrayList<>();
                    for(int i=0; i < result_map.get("result").size(); i++){
                        cafe_list_current.add(result_map.get("result").get(i));
                    }
                    for (int i = 0; i < cafe_list_current.size(); i++) {
                        String cafe = cafe_list_current.get(i);
                        // 각 List의 값들을 data 객체에 set 해줍니다.
                        for(int j=0; j < listTitle.size(); j++){
                            if(cafe.equals(listTitle.get(j))){
                                Data data = new Data();
                                data.setTitle(cafe);

                                data.setUser_id(user_id);
                                data.setTitle(listTitle.get(j));
                                data.setTitleInsta(listTitleInsta.get(j));
                                data.setAddress(listAddress.get(j));
                                data.setImageUrl(listImageUrl.get(j));
                                data.setCallNum(listCallNum.get(j));
                                data.setHour(listHour.get(j));
                                data.setContent(listContent.get(i));
                                // 각 값이 들어간 data를 adapter에 추가합니다.
                                adapter.addItem(data);
                            }
                        }
                    }
                }
                // adapter의 값이 변경되었다는 것을 알려줍니다.
                adapter.notifyDataSetChanged();
            }
        }
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