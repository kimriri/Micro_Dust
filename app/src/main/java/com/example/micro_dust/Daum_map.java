package com.example.micro_dust;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Daum_map {

    String BASE_URL = "https://dapi.kakao.com";
    String Authorization = "KakaoAK 8109951d066d71a847397f9f9922dc10";

    // qury
    @GET("/v2/local/search/address.json")
    Call<ResponseBody> getComment(@Query("query") String query,
                                  @Header("Authorization") String Authorization
            );
}

            // @Query(value = "ServiceKey", encoded = true)  String ServiceKey