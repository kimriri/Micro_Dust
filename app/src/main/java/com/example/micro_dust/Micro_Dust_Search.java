package com.example.micro_dust;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Micro_Dust_Search extends AppCompatActivity {

    EditText mSearch;
    ImageView imageView2;
    Context context;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listview;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro__dust__search);
        mSearch = (EditText) findViewById(R.id.search);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
 //      ListView listview;
   //     ListViewAdapter adapter;
     //   // Adapter 생성
       // adapter = new ListViewAdapter();
        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        // 두 번째 아이템 추가.
       // adapter.addItem("2");
        // 세 번째 아이템 추가.
      //  adapter.addItem("Ind");
      //  adapter.addItem("Asdf");
        // 두 번째 아이템 추가.
      //  adapter.addItem("Account Circle Black 36dp");
        // 세 번째 아이템 추가.
      //  adapter.addItem("Ind");
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Daum_map(mSearch);
            }});

    }

    private void Daum_map(TextView mSearch)
    {
        String x = mSearch.getText().toString();
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Daum_map.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();

        Daum_map daum_map = retrofit.create(Daum_map.class);
        Call<ResponseBody> comment = daum_map.getComment(x,Daum_map.Authorization);

        comment.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ArrayList<Scearch_item> scarch_itemArrayList;
                scarch_itemArrayList = new ArrayList<Scearch_item>();
                String string = null;
                String x,y,address_name,total_count;
                try {
                    string = response.body().string();
                    System.out.println(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JsonParser Parser = new JsonParser();
                JsonObject jsonObj = (JsonObject) Parser.parse(string);
                JsonArray memberArray = (JsonArray) jsonObj.get("documents");

                int i =0;
                JsonObject object = (JsonObject) memberArray.get(i);
                address_name = String.valueOf(object.get("address_name"));
                x = String.valueOf(object.get("x"));
                y = String.valueOf(object.get("y"));
                ListView listview;
                ListViewAdapter adapter;
                // Adapter 생성
                adapter = new ListViewAdapter();
                // 리스트뷰 참조 및 Adapter달기
                listview = (ListView) findViewById(R.id.listview);
                listview.setAdapter(adapter);
                adapter.addItem(address_name);
             //   if (!address_name.isEmpty()) {                        // 입력된 text 문자열이 비어있지 않으면
               //     items.add(address_name);                          // items 리스트에 입력된 문자열 추가
                    // EditText 입력란 초기화
                 //   adapter.notifyDataSetChanged();
               // }
                //json 따움표 제거
                address_name = address_name.replace("\"","");
                x = x.replace("\"", "");
                y = y.replace("\"", "");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                 String T =t.toString();
                 System.out.println(T);

            }});
    }}
