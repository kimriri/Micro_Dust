//    private void getAppKeyHash() {
//        try {
//            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                Log.e("Hash key", something);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            Log.e("name not found", e.toString());
//        }
//  }
package com.example.micro_dust;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.LOCATION_SERVICE;

public class Micro_Dust extends Fragment {
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;


    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    TextView micro_dust_today;
    TextView micro_dust;
    TextView micro_dust_now_gps;
    ImageView imageView;
    ImageView m_micro_Dust_Search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.micro_dust, container, false);


        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {

            checkRunTimePermission();
        }
        finish();
        micro_dust_today = (TextView) v.findViewById(R.id.micro_dust_today);
        micro_dust_now_gps = (TextView)v.findViewById(R.id.micro_dust_now_gps);
        imageView = (ImageView)v.findViewById(R.id.imageView);
        micro_dust = (TextView)v.findViewById(R.id.micro_dust);
        micro_dust_today.setText(getDay());
        m_micro_Dust_Search = (ImageView)v.findViewById(R.id.micro_dust_search);
        imageView = (ImageView) v.findViewById(R.id.imageView);
// gpsTracker 을 선언한다 (gpsTracker.java 파일을 실행시켜 gps 를 받아온다.)
        gpsTracker = new GpsTracker(getContext());
        //현재 기기의 gps (경도, 위도)를 표시한다.
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        // address 라는 string 변수에 경도와 위도를 넣어서 주소로 바꾸어 준다.
        String LAT = String.valueOf((double) latitude);
        String LON = String.valueOf((double) longitude);
        setRetrofitInit(LAT,LON);

        m_micro_Dust_Search.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //SubActivity로 가는 인텐트를 생성
                        Intent intent = new Intent(getActivity(), Micro_Dust_Search.class);
                        //액티비티 시작!
                        startActivity(intent);
                       // String x = "사당동";
                       // Daum_map(x);
                    }
                }
        );
        //       container.removeView(v);
        return v;
    }

    ///////////////////////////////// 미세먼지 단계 이미지  및 TEXT  START /////////////////////////////////
    private void Img_view(String stationName) {

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(img_View.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();
        img_View img_view = retrofit.create(img_View.class);
        Call<ResponseBody> comment = img_view.getComment(stationName, "DAILY",OOAPI.ServiceKey,"json");
        comment.enqueue(new Callback<ResponseBody>() {
                                @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string = null;
                try {
                    string = response.body().string();
                    System.out.println(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JsonParser Parser = new JsonParser();
                JsonObject jsonObj = (JsonObject) Parser.parse(string);
                JsonArray memberArray = (JsonArray) jsonObj.get("list");

                int i = 0;
                JsonObject object = (JsonObject) memberArray.get(i);
                String stationName = String.valueOf(object.get("pm10Value"));
                String pm25Value   = String.valueOf(object.get("pm25Value"));
                stationName =stationName.replace("\"", "");
                pm25Value =pm25Value.replace("\"", "");

                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
                    System.out.println("미세먼지" + stationName);
                    System.out.println("초 미세먼지" + pm25Value);
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
                    String text = null;
                    String text1 = null;
                if(Integer.parseInt(stationName)<=30){
                    imageView.setImageResource(R.mipmap.kiss);
                     text = "좋음 ";
                }else if(Integer.parseInt(stationName)<=50){
                    imageView.setImageResource(R.mipmap.happiness);
                    text = "양호 ";
                }else if(Integer.parseInt(stationName)<=100){
                    imageView.setImageResource(R.mipmap.embarrassed);
                    text = "나쁨 ";
                }else {
                    imageView.setImageResource(R.mipmap.dead);
                    text = "최악 ";
                }
                if(Integer.parseInt(pm25Value)<=15){
                    text1 = "좋음 ";
                }else if(Integer.parseInt(stationName)<=35){
                    text1 = "양호 ";
                }else if(Integer.parseInt(stationName)<=75){
                    text1 = "나쁨 ";
                }else {
                    text1 = "최악 ";
                }
                micro_dust.setText("  "+text+"  "+stationName+"        "+ text1+"  "+ pm25Value);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //error massage 출력 < Throwable t >
                // t 를 출력하면 error를 확인 할 수 있습니다.

            }}); }
    ///////////////////////////////// 미세먼지 단계 이미지  및 TEXT END /////////////////////////////////

    ///////////////////////////////// WGS84 -> TM 좌표 변환 STSART /////////////////////////////////
    private void setRetrofitInit(final String LAT, final String LON) {
         Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Map_interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();

        Map_interface map_interface = retrofit.create(Map_interface.class);
        Call<ResponseBody> comment = map_interface.getComment(Map_interface.Authorization, LON, LAT, "WGS84", "TM");

        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string = null;
                String x;
                String y;
                try {
                    string = response.body().string();
                    System.out.println(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JsonParser Parser = new JsonParser();
                JsonObject jsonObj = (JsonObject) Parser.parse(string);
                JsonArray memberArray = (JsonArray) jsonObj.get("documents");
                for (int i = 0; i < memberArray.size(); i++) {
                    JsonObject object = (JsonObject) memberArray.get(i);
                    x = String.valueOf(object.get("x"));
                    y = String.valueOf(object.get("y"));
                    mea_st(x,y);

                } }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("onFailure"); }});
    }
        ///////////////////////////////// WGS84 -> TM 좌표 변환 END /////////////////////////////////


    /////////////////////////////////  TM 좌표 -> 한글 주소(동) 변환 STSART /////////////////////////////////
    private void mea_st(String x, String y)
    {
        final String[] stationName = new String[1];
         Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(OOAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();

        OOAPI ooapi = retrofit.create(OOAPI.class);
        Call<ResponseBody> comment = ooapi.getComment(x, y,OOAPI.ServiceKey,"json");
        comment.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string = null;
                try {
                    string = response.body().string();
                    System.out.println(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JsonParser Parser = new JsonParser();
                JsonObject jsonObj = (JsonObject) Parser.parse(string);
                JsonArray memberArray = (JsonArray) jsonObj.get("list");

                int i =0;
                JsonObject object = (JsonObject) memberArray.get(i);
                stationName[0] = String.valueOf(object.get("stationName"));
                //json 따움표 제거
                stationName[0] = stationName[0].replace("\"", "");
                micro_dust_now_gps.setText(stationName[0]);
                String Stat = (stationName[0]);
                Img_view(Stat);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { }}); }
    /////////////////////////////////  TM 좌표 -> 한글 주소(동) 변환 END /////////////////////////////////


    private void Daum_map(String x)
    {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Daum_map.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();

        Daum_map daum_map = retrofit.create(Daum_map.class);
        Call<ResponseBody> comment = daum_map.getComment(x,Daum_map.Authorization);
        comment.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string = null;
                String x,y;

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
                x = String.valueOf(object.get("x"));
                y = String.valueOf(object.get("y"));


                //json 따움표 제거
                x = x.replace("\"", "");
                y = y.replace("\"", "");
                set(x,y);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { }}); }
    /////////////////////////////////  TM 좌표 -> 한글 주소(동) 변환 END /////////////////////////////////


    private void set(String x, String y) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Map_interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();

        Map_interface map_interface = retrofit.create(Map_interface.class);
        Call<ResponseBody> comment = map_interface.getComment(Map_interface.Authorization, x, y, "WGS84", "TM");

        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string = null;
                String x;
                String y;
                try {
                    string = response.body().string();
                    System.out.println(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JsonParser Parser = new JsonParser();
                JsonObject jsonObj = (JsonObject) Parser.parse(string);
                JsonArray memberArray = (JsonArray) jsonObj.get("documents");
                for (int i = 0; i < memberArray.size(); i++) {
                    JsonObject object = (JsonObject) memberArray.get(i);
                    x = String.valueOf(object.get("x"));
                    y = String.valueOf(object.get("y"));
                    mea_st(x,y);

                } }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("onFailure"); }});
    }
    ///////////////////////////////// WGS84 -> TM 좌표 변환 END /////////////////////////////////

    /////////////////////////////////  위치정보 권환 확인  STSART /////////////////////////////////
            @Override
            public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
             if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

                boolean check_result = true;
                for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                } }

        // 퍼미션 체크
            if ( check_result ) {
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. " +
                            "앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. " +
                            "설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                } } } }

    private void finish() { }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 위치 퍼미션
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {


        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    /////////////////////////////////  위치권환 확인  END /////////////////////////////////


    //여기부터는 GPS 활성화를 위한 메소드들

  ///////////////////////////////// 권환 요청 START /////////////////////////////////

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    ///////////////////////////////// 권환 요청 END /////////////////////////////////

    ///////////////////////////////// GPS 활성 확인 START /////////////////////////////////


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //GPS 활성 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                         // gps 확인 로그
                        checkRunTimePermission();
                        return;
                    } }
                break;
        } }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    ///////////////////////////////// GPS 활성 확인 END /////////////////////////////////

    ///////////////////////////////// 요일 확인 START /////////////////////////////////
       public String getDay() {
            Calendar cal = Calendar.getInstance();
            String MD_week = null;
             int dWeek = cal.get(Calendar.DAY_OF_WEEK);
                 switch (dWeek) {
            case 1:
                MD_week = "일";
                break;
            case 2:
                MD_week = "월";
                break;
            case 3:
                MD_week = "화";
                break;
            case 4:
                MD_week = "수";
                break;
            case 5:
                MD_week = "목";
                break;
            case 6:
                MD_week = "금";
                break;
            case 7:
                MD_week = "토";
                break;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  ", Locale.KOREA);
        Date date = new Date();
        String currentDate = formatter.format(date);
        return currentDate + MD_week +"요일";
    }

    ///////////////////////////////// 요일 확인 END /////////////////////////////////
}

