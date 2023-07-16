package com.example.meals.Nutrient.calculaors;

public class DailyCalculator {
    //이준호 작성
    public static int[] calculate(int age, int gender) {

        int need_cal = 0;
        int need_carb = 0;
        int need_protein = 0;
        int need_fat = 0;
        int need_fiber = 0;

        if (gender == 0) {
            switch (age) {
                case 6:
                case 7:
                case 8:
                    need_cal = 1700;
                    need_carb = 213;
                    need_protein = 85;
                    need_fat = 57;
                    need_fiber = 25;
                    break;
                case 9:
                case 10:
                case 11:
                    need_cal = 2100;
                    need_carb = 263;
                    need_protein = 105;
                    need_fat = 70;
                    need_fiber = 25;
                    break;
                case 12:
                case 13:
                case 14:
                    need_cal = 2500;
                    need_carb = 313;
                    need_protein = 125;
                    need_fat = 83;
                    need_fiber = 30;
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    need_cal = 2700;
                    need_carb = 338;
                    need_protein = 135;
                    need_fat = 90;
                    need_fiber = 30;
                    break;
            }
        } else {
            switch (age) {
                case 6:
                case 7:
                case 8:
                    need_cal = 1500;
                    need_carb = 188;
                    need_protein = 75;
                    need_fat = 50;
                    need_fiber = 25;
                    break;
                case 9:
                case 10:
                case 11:
                    need_cal = 1800;
                    need_carb = 225;
                    need_protein = 90;
                    need_fat = 60;
                    need_fiber = 25;
                    break;
                case 12:
                case 13:
                case 14:
                    need_cal = 2000;
                    need_carb = 163;
                    need_protein = 65;
                    need_fat = 98;
                    need_fiber = 25;
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    need_cal = 2000;
                    need_carb = 163;
                    need_protein = 65;
                    need_fat = 98;
                    need_fiber = 25;
                    break;
            }
        }
        int[] result = {need_cal, need_carb, need_protein, need_fat, need_fiber};
        return result;
    }
}//이준호 작성