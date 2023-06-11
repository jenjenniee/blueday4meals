package com.example.blueday4meals.Nutrient.calculaors;
//이준호 작성
import android.util.Log;

public class Rating {
    public static int calculateRating(int needCal, int Cal, double Carbon, double Protein, double Fat, int needDietFiber, double DietFiber) {
        int points = 0;
        int point = 0;

        if (Carbon * 4 > needCal * 0.5 * 1.2) {
            points += 3;
        } else if (Carbon * 4 < needCal * 0.5 * 0.8) {
            points += 3;
        }

        if (Protein * 4 > needCal * 0.3 * 0.8) {
            points += 2;
        } else if (Protein * 4 < needCal * 0.3 * 0.8) {
            points += 2;
        }

        if (Fat * 9 > needCal * 0.2 * 1.2) {
            points += 2;
        } else if (Fat * 9 < needCal * 0.2 * 0.8) {
            points += 2;
        }

        if (needDietFiber > DietFiber * 1.2) {
            points += 1;
        } else if (needDietFiber < DietFiber * 0.8) {
            points += 1;
        }

        if (needCal * 1.2 < Cal) {
            points += 3;
        } else if (needCal * 0.8 > Cal) {
            points += 3;
        }

        switch (points) {
            case 1:
            case 2:
                point = 0;
                break;
            case 3:
            case 4:
            case 5:
                point = 1;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                point = 2;
                break;
        }
        Log.d("TAG", "일일점수 값: " + points);
        return point;
    }
}//이준호 작성