package com.example.meals.Nutrient.requests;
//이준호 작성

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.meals.Nutrient.calculaors.DailyCalculator;
import com.example.meals.Nutrient.calculaors.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;//이준호 작성

import java.util.Arrays;

public class getpoint {

    private static int calorie = 0;
    static double carbohydrate= 0;
    static double protein= 0;
    static double fat= 0;
    static double dietary_fiber= 0;

    public interface ResultListener {
        void onResult(int point);
        void onDataResult(int cal, double carb, double protein, double fat, double fiber);
    }

    public static class RatingCalculator {
        public static void getter(String userID, String date, int needCal, int needDietFiber,int age, int gender, RequestQueue queue1, ResultListener listener) {
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

                            Arrays.fill(Cal, 0);
                            Arrays.fill(Carbon, 0);
                            Arrays.fill(Cal, 0);
                            Arrays.fill(Protain, 0);
                            Arrays.fill(Fat, 0);
                            Arrays.fill(DietFiber, 0);

                            calorie = 0;
                            carbohydrate = 0;
                            protein = 0;
                            fat = 0;
                            dietary_fiber = 0;



                            if (dataArray.length() > 0) {

                                for(int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    Cal[i] = dataObject.getInt("calorie");
                                    Carbon[i] = dataObject.getDouble("carbohydrate");
                                    Protain[i] = dataObject.getDouble("protein");
                                    Fat[i] = dataObject.getDouble("fat");
                                    DietFiber[i] = dataObject.getDouble("dietary_fiber");
                                }

                                for(int i = 0; i < dataArray.length(); i++) {
                                    calorie += Cal[i];
                                    carbohydrate += Carbon[i];
                                    protein += Protain[i];
                                    fat += Fat[i];
                                    dietary_fiber += DietFiber[i];
                                }

                                int[] need= DailyCalculator.calculate(age, gender);
                                int needCarbon = need[1];
                                int needProtein = need[2];
                                int needFat = need[3];

                                int point = Rating.calculateRating( calorie, carbohydrate, protein, fat,dietary_fiber, needCal, needCarbon, needProtein, needFat, needDietFiber);
                                listener.onResult(point);
                                listener.onDataResult(calorie, carbohydrate, protein, fat, dietary_fiber);
                            }
                        } else {

                            listener.onDataResult(calorie, carbohydrate, protein, fat, dietary_fiber);
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
}//이준호 작성