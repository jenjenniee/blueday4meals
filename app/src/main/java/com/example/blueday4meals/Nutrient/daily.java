package com.example.blueday4meals.Nutrient;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
//이준호 작성
public class daily extends AppCompatActivity {
    public daily(int age, int gender) {
    }

    public void daily(int age, int gender){
        int needcal = 0;
        if(gender == 0){
            switch(age){
                case 6:
                case 7:
                case 8:
                    needcal = 1700;
                    break;
                case 9:
                case 10:
                case 11:
                    needcal = 2100;
                    break;
                case 12:
                case 13:
                case 14:
                    needcal = 2500;
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    needcal = 2700;
                    break;
            }
        }else{
            switch(age){
                case 6:
                case 7:
                case 8:
                    needcal = 1500;
                    break;
                case 9:
                case 10:
                case 11:
                    needcal = 1800;
                    break;
                case 12:
                case 13:
                case 14:
                    needcal = 2000;
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                    needcal = 2000;
                    break;
            }
        }
        Intent intent = new Intent(daily.this, NutrientMain.class);
        startActivity(intent);
        intent.putExtra("Cal", needcal);
    }
}//이준호 작성
