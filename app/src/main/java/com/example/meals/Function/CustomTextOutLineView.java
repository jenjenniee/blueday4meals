package com.example.meals.Function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.meals.R;
//이준호 작성
@SuppressLint("AppCompatCustomView")
public class CustomTextOutLineView extends TextView {
    private boolean stroke = false;
    private float strokeWidth = 0.0f;
    private int strokeColor;

    public CustomTextOutLineView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public CustomTextOutLineView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView(context, attrs);
    }

    public CustomTextOutLineView(Context context){
        super(context);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextOutLineView);
        stroke = a.getBoolean(R.styleable.CustomTextOutLineView_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.CustomTextOutLineView_textStrokeWidth, 0.0f);
        strokeColor = a.getColor(R.styleable.CustomTextOutLineView_textStrokeColor, 0xffffff);
    }

    @Override
    protected  void onDraw(Canvas canvas){
        if(stroke){
            ColorStateList states = getTextColors();
            getPaint().setStyle(Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);
            getPaint().setStyle(Style.FILL);
            setTextColor(states);
        }
        super.onDraw(canvas);
    }
}//이준호 작성
