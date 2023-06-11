package com.example.blueday4meals.Nutrient.requests;
//이준호 작성
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.blueday4meals.Nutrient.calculaors.AgeCalculator;
import com.example.blueday4meals.Nutrient.calculaors.DailyCalculator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getdaynuti {

    public static int gender = 0;
    public static int age = 0;
    private static int needCal = 0;
    private static int needDietFiber = 0;

    public interface ResultListener {
        void onResult(int point);
        void onDataResult(int cal, double carb, double protein, double fat, double fiber);
    }

    public static class nuturientgetter {
        public static void getter(String userID, String date, RequestQueue queue, RequestQueue queue1, ResultListener listener) {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String jsonResponse = response; // 받은 JSON 응답 문자열

                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            // success가 true인 경우에 대한 처리
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            if (dataArray.length() > 0) {
                                JSONObject dataObject = dataArray.getJSONObject(0);
                                gender = dataObject.getInt("userGender");
                                String birthdate = dataObject.getString("userBirth");
                                age = AgeCalculator.calculateAge(birthdate);

                                // 사용자의 성별(gender)과 생년월일(birthdate) 값을 사용하여 처리
                                int[] result = DailyCalculator.calculate(age, gender);
                                needCal = result[0];
                                needDietFiber = result[4];
                                Log.d("TAG", "gen 값: " + gender);
                                Log.d("TAG", "age 값: " + age);
                                Log.d("TAG", "need 값: " + needCal);
                                Log.d("TAG", "need2 값: " + needDietFiber);

                                getpoint.RatingCalculator.getter(userID, date, needCal, needDietFiber, queue1, new getpoint.ResultListener() {
                                    @Override
                                    public void onResult(int point) {
                                        listener.onResult(point);
                                    }

                                    @Override
                                    public void onDataResult(int cal, double carb, double protein, double fat, double fiber) {
                                        listener.onDataResult(cal, carb, protein, fat, fiber);
                                    }
                                });
                            }
                        } else {
                            // success가 false인 경우에 대한 처리
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            CalRequest request = new CalRequest(userID, responseListener);
            queue.add(request);
        }
    }
}//이준호 작성