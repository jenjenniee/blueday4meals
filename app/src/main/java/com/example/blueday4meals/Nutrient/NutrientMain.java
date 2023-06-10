package com.example.blueday4meals.Nutrient;

import static com.example.blueday4meals.Nutrient.calendar.CalendarUtils.daysInWeekArray;
import static com.example.blueday4meals.Nutrient.calendar.CalendarUtils.monthYearFromDate;
import static com.example.blueday4meals.Nutrient.calendar.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.blueday4meals.Camera.CameraMain;
import com.example.blueday4meals.MainPages.ChildMainPage;
import com.example.blueday4meals.Function.navigationbar;
import com.example.blueday4meals.NaverMap.NaverMapMain;
import com.example.blueday4meals.Nutrient.calendar.CalendarAdapter;
import com.example.blueday4meals.Nutrient.calendar.CalendarUtils;
import com.example.blueday4meals.Nutrient.meal.MealAdapter;
import com.example.blueday4meals.Nutrient.rating.RatingMain;
import com.example.blueday4meals.Nutrient.requests.getdaynuti;
import com.example.blueday4meals.Nutrient.requests.getpoint;
import com.example.blueday4meals.R;
import com.example.blueday4meals.MainPages.SettingMain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NutrientMain extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    public String id;
    public int dpoint;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView mealRecyclerView;
    TextView cals, carbs, proteins, fats, fibers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_main);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();

        String userID = getIntent().getStringExtra("userID");
        id = userID;

        Date currentDate = new Date();
        // Calendar 객체를 생성하고 현재 날짜와 시간을 설정합니다
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        //calendar.add(Calendar.DAY_OF_MONTH, -10); //데베에 24일부터만 있어서 테스트용
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String birthdateString = dateFormat.format(calendar.getTime());

        Date today = null;
        try {
            today = dateFormat.parse(birthdateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Button btnMain, btnCam, btnNut, btnMap, btnSet, btnrat;


        btnMain = findViewById(R.id.button1);
        btnMap = findViewById(R.id.button2);
        btnNut = findViewById(R.id.button3);
        btnCam = findViewById(R.id.button4);
        btnSet = findViewById(R.id.button5);
        btnrat = findViewById(R.id.btn_rating);
//        TextView textViewResult = findViewById(R.id.textView);
        cals = findViewById(R.id.tv_energyIntake);
        carbs = findViewById(R.id.tv_carbsIntake);
        proteins = findViewById(R.id.tv_proteinIntake);
        fats = findViewById(R.id.tv_fatIntake);
        fibers = findViewById(R.id.tv_fiberIntake);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, ChildMainPage.class, userID);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, NaverMapMain.class, userID);
            }
        });

        btnNut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, NutrientMain.class, userID);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, CameraMain.class, userID);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, SettingMain.class, userID);
            }
        });

        btnrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NutrientMain.this, RatingMain.class, userID);
            }
        });



    }

    // 위젯 아이디로 연결
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.rv_calendar);
        monthYearText = findViewById(R.id.monthYearTV);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    // 주별 리사이클뷰 설정
    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this, dpoint);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    // 이전 주로 이동 액션
    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    // 다음 주로 이동 액션
    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    // 아이템 클릭
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        loadNutrient();
        setWeekView();
    }

    public void loadNutrient() {
        RequestQueue queue = Volley.newRequestQueue(NutrientMain.this);
        RequestQueue queue1 = Volley.newRequestQueue(NutrientMain.this);

        // LocalDate를 Date로 변환
        Date pickedDate = Date.from(CalendarUtils.selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String formattedDate = outputFormat.format(pickedDate);

        getdaynuti.nuturientgetter.getter(id, formattedDate, queue, queue1, new getdaynuti.ResultListener() {
            @Override
            public void onResult(int point) {
//                textViewResult.setText(String.valueOf(point));
            }

            @Override
            public void onDataResult(int cal, double carb, double protein, double fat, double fiber) {
//                String res = cal + " " + carb + " " + protein + " " + fat + " " + fiber;
                cals.setText(Integer.toString(cal));
                carbs.setText(Double.toString(carb));
                proteins.setText(Double.toString(protein));
                fats.setText(Double.toString(fat));
                fibers.setText(Double.toString(fiber));
            }
        });
    }

    // 식사 리사이클뷰 설정
    private void setMealView() {
        MealAdapter mealAdapter = new MealAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mealRecyclerView.setLayoutManager(layoutManager);
        mealRecyclerView.setAdapter(mealAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}