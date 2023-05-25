package com.example.blueday4meals.NaverMap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NaverMapItem {
    @SerializedName("data")
    public List<NaverMapData> data;
}