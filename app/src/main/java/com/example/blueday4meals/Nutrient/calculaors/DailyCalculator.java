package com.example.blueday4meals.Nutrient.calculaors;

import android.util.Log;

public class DailyCalculator {

    public static int[] calculate(int age, int gender) {


        int needcal = 0;
        int DietFiber = 0;
        if (gender == 0) {
            switch (age) {
                case 6:
                case 7:
                case 8:
                    needcal = 1700;
                    DietFiber = 25;
                    break;
                case 9:
                case 10:
                case 11:
                    needcal = 2100;
                    DietFiber = 25;
                    break;
                case 12:
                case 13:
                case 14:
                    needcal = 2500;
                    DietFiber = 30;
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    needcal = 2700;
                    DietFiber = 30;
                    break;
            }
        } else {
            switch (age) {
                case 6:
                case 7:
                case 8:
                    needcal = 1500;
                    DietFiber = 20;
                    break;
                case 9:
                case 10:
                case 11:
                    needcal = 1800;
                    DietFiber = 25;
                    break;
                case 12:
                case 13:
                case 14:
                    needcal = 2000;
                    DietFiber = 25;
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    needcal = 2000;
                    DietFiber = 25;
                    break;
            }
        }
        int[] result = {needcal, DietFiber};
        return result;
    }
}