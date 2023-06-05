package com.example.blueday4meals.Camera;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class InputNutritionRequest extends StringRequest {

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://animalnara.kr/nutrition_upload.php";
    private Map<String, String> map;

    public InputNutritionRequest(String foodName, double cal, double carbon, double protain, double fat, double fiber, String timeDiv ,String fileName, String date, String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        map = new HashMap<>();
        map.put("food_name", foodName);
        map.put("calorie", decimalFormat.format(cal));
        map.put("carbohydrate", decimalFormat.format(carbon));
        map.put("protain", decimalFormat.format(protain));
        map.put("fat", decimalFormat.format(fat));
        map.put("dietary_fiber", decimalFormat.format(fiber));
        map.put("time_division", timeDiv);
        map.put("photo_url", fileName);
        map.put("insert_date", date);
        map.put("child_id", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}