package com.example.micro_dust;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Map_interface {

    String BASE_URL = "https://dapi.kakao.com";
    String Authorization = "KakaoAK 6ef825b9d039919a59ffa7cdf4f95c24";

    // qury
    @GET("/v2/local/geo/transcoord.json")
    Call<ResponseBody> getComment(@Header("Authorization") String Authorization, @Query("x") String lon, @Query("y") String lat,
                                  @Query("input_coord") String in, @Query("output_coord") String out);

}


