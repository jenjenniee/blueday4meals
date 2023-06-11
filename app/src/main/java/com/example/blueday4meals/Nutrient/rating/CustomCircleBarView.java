package com.example.blueday4meals.Nutrient.rating;
//이준호 작성
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomCircleBarView extends View {

    public CustomCircleBarView(Context context) {
        super(context);
    }

    public CustomCircleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Paint 객체 생성
        Paint paint = new Paint();

        // 1. 회색 원(배경) 그리기
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30f);
        // 그래프 선의 테두리/가장자리를 부드럽게 처리
        paint.setAntiAlias(true);
        canvas.drawArc(30f, 30f, 110f, 110f, 0f, 360f, false, paint);

        // 2. 파란 원(프로그레스) 그리기
        paint.setColor(Color.GREEN);
        // 그래프 선의 끝을 둥글게 처리!
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(30f, 30f, 110f, 110f, -90f, 180f, false, paint);
    }
}//이준호 작성