package com.example.blueday4meals.NaverMap;

import com.google.gson.annotations.SerializedName;

public class NaverMapData {
    @SerializedName("가맹점명")
    private String franchisee_name;
    @SerializedName("소재지도로명주소")
    private String road_address;
    @SerializedName("위도")
    private double latitude;
    @SerializedName("경도")
    private double longitude;
    @SerializedName("전화번호")
    private String tel_num;

    public String getfranchisee_name() {
        return franchisee_name;
    }
    public String getroad_address() {
        return road_address;
    }
    public double getlatitude() {
        return latitude;
    }
    public double getlongitude() {
        return longitude;
    }
    public String gettel_num() {
        return tel_num;
    }
}