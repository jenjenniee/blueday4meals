package com.example.blueday4meals.Nutrient;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class Rating extends AppCompatActivity {
    int points;
    public Rating(int Cal, Double Carbon, Double Protain, Double Fat){
        if((Carbon*4)>(Cal*0.5*1.2)){

        }else if((Carbon*4)<(Cal*0.5*0.8)){

        }
        if((Protain*4)>(Cal*0.3*0.8)){

        }else if((Protain*4)<(Cal*0.3*0.8)){

        }
        if((Fat*9)>(Cal*0.2*1.2)){

        }else if((Fat*9)<(Cal*0.2*0.8)){

        }
        if(Cal*1.2 < ((Carbon*4)+(Protain*4)+(Fat*9))){

        }else if(Cal*0.8 > ((Carbon*4)+(Protain*4)+(Fat*9))){

        }
        Intent intent = new Intent(Rating.this, NutrientMain.class);
        startActivity(intent);
        intent.putExtra("point", points);
    }

}
