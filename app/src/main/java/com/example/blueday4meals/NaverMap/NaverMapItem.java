package com.example.blueday4meals.NaverMap;
//강태광 작성
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NaverMapItem {
    @SerializedName("data")
    public List<NaverMapData> data;
}