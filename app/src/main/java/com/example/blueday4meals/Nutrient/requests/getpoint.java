package com.example.blueday4meals.Nutrient.requests;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.blueday4meals.Nutrient.calculaors.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getpoint {

    private static int calorie = 0;
    static double carbohydrate= 0;
    static double protein= 0;
    static double fat= 0;
    static double dietary_fiber= 0;

    public interface ResultListener {
        void onResult(int point);
    }

    public static class RatingCalculator {
        public static void getter(String userID, String date, int needCal, int needDietFiber, RequestQueue queue1, ResultListener listener) {
            Response.Listener<String> responseListener2 = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    String jsonResponse = response; // 받은 JSON 응답 문자열

                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            int[] Cal = new int[dataArray.length()];
                            double[] Carbon =  new double[dataArray.length()];
                            double[] Protain =  new double[dataArray.length()];
                            double[] Fat =  new double[dataArray.length()];
                            double[] DietFiber =  new double[dataArray.length()];
                            if (dataArray.length() > 0) {
                                JSONObject dataObject = dataArray.getJSONObject(0);
                                for(int i = 0; i < dataArray.length(); i++) {
                                    Cal[i] =dataObject.getInt("calorie");
                                    Carbon[i] =dataObject.getDouble("carbohydrate");
                                    Protain[i] =dataObject.getDouble("protein");
                                    Fat[i] =dataObject.getDouble("fat");
                                    DietFiber[i] =dataObject.getDouble("dietary_fiber");
                                }
                                // 사용자의 성별(gender)과 생년월일(birthdate) 값을 사용하여 처리
                                for(int i = 0; i < dataArray.length(); i++) {
                                    calorie += Cal[i];
                                    carbohydrate += Carbon[i];
                                    protein += Protain[i];
                                    fat += Fat[i];
                                    dietary_fiber += DietFiber[i];
                                }
                                Log.d("TAG", "co 값: " + calorie);
                                Log.d("TAG", "car 값: " + carbohydrate);
                                Log.d("TAG", "pr 값: " + protein);
                                Log.d("TAG", "f 값: " + fat);
                                Log.d("TAG", "di 값: " + dietary_fiber);
                                int point = Rating.calculateRating(needCal, calorie, carbohydrate, protein, fat, needDietFiber, dietary_fiber);
                                listener.onResult(point);
                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            getnutrient nutrientRequest = new getnutrient(userID, date, responseListener2);
            queue1.add(nutrientRequest);

        }
    }
}