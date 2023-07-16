package com.example.meals.Nutrient.calculaors;
//이준호 작성
import android.util.Log;

public class Rating {
    public static int calculateRating(int Cal, double Carbon, double Protein, double Fat, double DietFiber, int needCal, int needCarbon, int needProtein, int needFat, int needDietFiber) {
        int points = 0;
        int point = 0;

        if (Carbon  > needCarbon  * 1.3) {
            points += 3;
        } else if (Carbon * 4 < needCarbon * 0.7) {
            points += 3;
        }else {}

        if (Protein  > needProtein  * 1.3) {
            points += 2;
        } else if (Protein * 4 < needProtein * 0.7) {
            points += 2;
        }else {}

        if (Fat  > needFat  * 1.3) {
            points += 2;
        } else if (Fat  < needFat * 0.7) {
            points += 2;
        }else {}

        if (needDietFiber > DietFiber * 1.3) {
            points += 1;
        } else if (needDietFiber < DietFiber * 0.7) {
            points += 1;
        }else {}

        if (needCal * 1.3 < Cal) {
            points += 3;
        } else if (needCal * 0.7 > Cal) {
            points += 3;
        }else {}

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

        Log.d("", "" + needCarbon);
        Log.d("", "" + Carbon);
        Log.d("", "" + (needProtein));
        Log.d("", "" + Protein);
        Log.d("", "" + (needFat));
        Log.d("", "" + Fat);

        return point;

    }
}//이준호 작성