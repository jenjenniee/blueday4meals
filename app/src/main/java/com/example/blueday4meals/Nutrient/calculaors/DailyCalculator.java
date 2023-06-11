package com.example.blueday4meals.Nutrient.calculaors;

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
                    need_carb = 130;
                    need_protein = 35;
                    need_fat = 40;
                    need_fiber = 25;
                    break;
                case 9:
                case 10:
                case 11:
                    need_cal = 2100;
                    need_carb = 130;
                    need_protein = 50;
                    need_fat = 50;
                    need_fiber = 25;
                    break;
                case 12:
                case 13:
                case 14:
                    need_cal = 2500;
                    need_carb = 130;
                    need_protein = 60;
                    need_fat = 50;
                    need_fiber = 30;
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    need_cal = 2700;
                    need_carb = 130;
                    need_protein = 65;
                    need_fat = 50;
                    need_fiber = 30;
                    break;
            }
        } else {
            switch (age) {
                case 6:
                case 7:
                case 8:
                    need_cal = 1500;
                    need_carb = 130;
                    need_protein = 35;
                    need_fat = 40;
                    need_fiber = 25;
                    break;
                case 9:
                case 10:
                case 11:
                    need_cal = 1800;
                    need_carb = 130;
                    need_protein = 45;
                    need_fat = 50;
                    need_fiber = 25;
                    break;
                case 12:
                case 13:
                case 14:
                    need_cal = 2000;
                    need_carb = 130;
                    need_protein = 55;
                    need_fat = 50;
                    need_fiber = 25;
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    need_cal = 2000;
                    need_carb = 130;
                    need_protein = 55;
                    need_fat = 50;
                    need_fiber = 25;
                    break;
            }
        }
        int[] result = {need_cal, need_carb, need_protein, need_fat, need_fiber};
        return result;
    }
}//이준호 작성