package com.example.blueday4meals.Nutrient.meal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blueday4meals.R;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealViewHolder>{
    private ArrayList<String> arrayList;

    public MealAdapter(){
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.meal_cell, parent, false);

        MealViewHolder viewholder = new MealViewHolder(context, view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        String text = arrayList.get(position);
        holder.iv_foodImg.setImageResource(R.drawable.temp1);
        holder.tv_foodName.setText(text);
        holder.tv_timeOfDay.setText(text);
        holder.tv_foodEnergy.setText(text);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // 데이터를 입력
    public void setArrayData(String strData) {
        arrayList.add(strData);
    }
}
