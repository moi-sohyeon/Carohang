package com.example.cafetotravel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Data> listData = new ArrayList<>();
    private Intent intent;
    public String user_id, result;

    @NonNull
    @Override

    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.textView1.setText(listData.get(position).getTitle());
        holder.textView2.setText(listData.get(position).getAddress());

        holder.onBind(listData.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user_id = listData.get(position).getUser_id();
                ResponseData responseData = new ResponseData();
                String serverURL = "http://128.134.65.126/response.php";
                try {
                    result = responseData.execute(serverURL).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intent = new Intent(v.getContext(), DetailActivity.class);
                if(result.contains(listData.get(position).getTitle())){
                    intent.putExtra("체크박스", "check");
                }
                intent.putExtra("사용자아이디", listData.get(position).getUser_id());
                intent.putExtra("카페이름", listData.get(position).getTitle());
                intent.putExtra("카페이름인스타용", listData.get(position).getTitleInsta());
                intent.putExtra("전화번호", listData.get(position).getCallNum());
                intent.putExtra("컨텐츠", listData.get(position).getContent());
                intent.putExtra("주소", listData.get(position).getAddress());
                intent.putExtra("카페이미지", listData.get(position).getImageUrl());
                intent.putExtra("운영시간", listData.get(position).getHour());
                v.getContext().startActivity(intent);
            }
        });
    }

    void clear(){
        listData.clear();
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void onBind(Data data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getAddress());
            Glide.with(imageView).load(data.getImageUrl()).into(imageView);
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