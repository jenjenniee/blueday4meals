package com.example.meals.Nutrient.requests;
//이준호 작성
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class getnutrient extends StringRequest {

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://animalnara.kr/nutrient_query.php";
    private Map<String, String> map;

    public getnutrient(String userID, String date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("insert_date", String.valueOf(date));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}//이준호 작성
