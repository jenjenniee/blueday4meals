package com.example.blueday4meals.Nutrient.meal;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.blueday4meals.R;

public class MealViewHolder extends RecyclerView.ViewHolder{
    public ImageView iv_foodImg;
    public TextView tv_foodName;
    public TextView tv_timeOfDay;
    public TextView tv_foodEnergy;

    MealViewHolder(Context context, View itemView){
        super(itemView);

        iv_foodImg = itemView.findViewById(R.id.iv_foodImg);
        tv_foodName = itemView.findViewById(R.id.tv_foodName);
        tv_timeOfDay = itemView.findViewById(R.id.tv_timeOfDay);
        tv_foodEnergy = itemView.findViewById(R.id.tv_foodEnergy);

        
    }
}
