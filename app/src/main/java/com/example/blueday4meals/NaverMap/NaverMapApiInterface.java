package com.example.blueday4meals.NaverMap;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NaverMapApiInterface {
    @GET("shop_data.php")
    Call<NaverMapItem> getMapData();
}