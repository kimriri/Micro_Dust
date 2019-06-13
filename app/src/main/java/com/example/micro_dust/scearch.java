package com.example.micro_dust;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface scearch {

    String BASE_URL = "http://openapi.airkorea.or.kr/";
    String  ServiceKey = "4rAiAT9%2BYGzi7AdZmEuWDTQF1GZfPVCqGTMhEEyDtlD92BvCODH7Vt6uly8M%2F%2FMCplomUe%2B8W1QZqXEEEe%2B35w%3D%3D";

    @GET("openapi/services/rest/MsrstnInfoInqireSvc/getTMStdrCrdnt")
    Call<ResponseBody> getComment(@Query("umdName") String umdName, @Query(value = "ServiceKey",encoded = true) String ServiceKey);
}




