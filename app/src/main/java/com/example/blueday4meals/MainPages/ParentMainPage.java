package com.example.blueday4meals.MainPages;

import static com.example.blueday4meals.Nutrient.calendar.CalendarUtils.daysInWeekArray;
import static com.example.blueday4meals.Nutrient.calendar.CalendarUtils.monthYearFromDate;
import static com.example.blueday4meals.Nutrient.calendar.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.blueday4meals.Camera.CameraMain;
import com.example.blueday4meals.MainPages.ChildMainPage;
import com.example.blueday4meals.Function.navigationbar;
import com.example.blueday4meals.NaverMap.NaverMapMain;
import com.example.blueday4meals.Nutrient.calculaors.DailyCalculator;
import com.example.blueday4meals.Nutrient.calendar.CalendarAdapter;
import com.example.blueday4meals.Nutrient.calendar.CalendarUtils;
import com.example.blueday4meals.Nutrient.meal.MealAdapter;
import com.example.blueday4meals.Nutrient.rating.RatingMain;
import com.example.blueday4meals.Nutrient.requests.getdaynuti;
import com.example.blueday4meals.Nutrient.requests.getpoint;
import com.example.blueday4meals.R;
import com.example.blueday4meals.MainPages.SettingMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
// 이진경 작성
public class ParentMainPage extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView textView;
    private Button startButton;
    private WebView webView;
    private String cardnum;
    public String id;
    public int dpoint;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView mealRecyclerView;
    private String newChildId = null, childid = null;
    TextView cals, carbs, proteins, fats, fibers, need_cal, need_carbs, need_protein, need_fat, need_fiber;
    ProgressBar pb_carbs, pb_protein, pb_fat, pb_fiber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parentmain);

        String userID = getIntent().getStringExtra("userID");

        textView = findViewById(R.id.textView);
        webView = findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true); // JavaScript 실행 허용
        startButton = findViewById(R.id.refreshbutton);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonResponse = response; // 받은 JSON 응답 문자열

                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        // success가 true인 경우에 대한 처리
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        if (dataArray.length() > 0) {
                            JSONObject dataObject = dataArray.getJSONObject(0);
                            newChildId = dataObject.getString("userChildsID");
                            if (newChildId != null) {
                                Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        String jsonResponse = response; // 받은 JSON 응답 문자열

                                        try {
                                            JSONObject jsonObject = new JSONObject(jsonResponse);
                                            boolean success = jsonObject.getBoolean("success");

                                            if (success) {
                                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                                if (dataArray.length() > 0) {
                                                    JSONObject dataObject = dataArray.getJSONObject(0);
                                                    cardnum = dataObject.getString("cardNum");
                                                }
                                            } else {
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                CardNumrequest Request = new CardNumrequest(newChildId, responseListener1);
                                RequestQueue queue1 = Volley.newRequestQueue(ParentMainPage.this);
                                queue1.add(Request);

                                startButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        webView.setWebViewClient(new WebViewClient() {
                                            @Override
                                            public void onPageFinished(WebView view, String url) {
                                                // 페이지 로딩이 완료되었을 때 호출됩니다.
                                                setCardValue(cardnum);
                                                clickButton();
                                                extractTableInfo();
                                            }
                                        });

                                        webView.loadUrl("https://www.purmeecard.com/public.do?request=popCardSelectForm");
                                    }
                                });
                                startButton.performClick();
                                initWidgets();
                                CalendarUtils.selectedDate = LocalDate.now();
                                setWeekView();

                                Date currentDate = new Date();
                                // Calendar 객체를 생성하고 현재 날짜와 시간을 설정합니다
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(currentDate);

                                // 섭취영양소 텍스트뷰 연결
                                cals = findViewById(R.id.tv_energyIntake);
                                carbs = findViewById(R.id.tv_carbsIntake);
                                proteins = findViewById(R.id.tv_proteinIntake);
                                fats = findViewById(R.id.tv_fatIntake);
                                fibers = findViewById(R.id.tv_fiberIntake);

                                // 기준영양소 텍스트뷰 연결
                                need_cal = findViewById(R.id.tv_energyStandard);
                                need_carbs = findViewById(R.id.tv_carbsStandard);
                                need_protein = findViewById(R.id.tv_proteinStandard);
                                need_fat = findViewById(R.id.tv_fatStandard);
                                need_fiber = findViewById(R.id.tv_fiberStandard);

                                // 프로그래스바 연결
                                pb_carbs = findViewById(R.id.pb_carbs);
                                pb_protein = findViewById(R.id.pb_protein);
                                pb_fat = findViewById(R.id.pb_fat);
                                pb_fiber = findViewById(R.id.pb_fiber);

                                loadNutrient();

                            }
                        }
                    } else {
                        // success가 false인 경우에 대한 처리
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        RequestQueue queue = Volley.newRequestQueue(ParentMainPage.this);
        parentchildidr request = new parentchildidr(userID, responseListener);
        queue.add(request);
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

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, (CalendarAdapter.OnItemListener) this, dpoint);
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
        RequestQueue queue = Volley.newRequestQueue(ParentMainPage.this);
        RequestQueue queue1 = Volley.newRequestQueue(ParentMainPage.this);

        // LocalDate를 Date로 변환
        Date pickedDate = Date.from(CalendarUtils.selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String formattedDate = outputFormat.format(pickedDate);

        getdaynuti.nuturientgetter.getter(newChildId, formattedDate, queue, queue1, new getdaynuti.ResultListener() {
            @Override
            public void onResult(int point) {
//                textViewResult.setText(String.valueOf(point));
            }

            @Override
            public void onDataResult(int cal, double carb, double protein, double fat, double fiber) {

                DecimalFormat decimalFormat = new DecimalFormat("#.#");

                // 기준영양소 표시
                int[] result = DailyCalculator.calculate(getdaynuti.age, getdaynuti.gender);
                need_cal.setText(Integer.toString(result[0]));

                need_carbs.setText(Integer.toString(result[1]));
                pb_carbs.setMax(result[1]);

                need_protein.setText(Integer.toString(result[2]));
                pb_protein.setMax(result[2]);

                need_fat.setText(Integer.toString(result[3]));
                pb_fat.setMax(result[3]);

                need_fiber.setText(Integer.toString(result[4]));
                pb_fiber.setMax(result[4]);
                // 섭취영양소 표시
                cals.setText(Integer.toString(cal));

                carbs.setText(decimalFormat.format(carb));
                pb_carbs.setProgress(Integer.parseInt(decimalFormat.format(Math.round(carb))));

                proteins.setText(decimalFormat.format(protein));
                pb_protein.setProgress(Integer.parseInt(decimalFormat.format(Math.round(protein))));

                fats.setText(decimalFormat.format(fat));
                pb_fat.setProgress(Integer.parseInt(decimalFormat.format(Math.round(fat))));

                fibers.setText(decimalFormat.format(fiber));
                pb_fiber.setProgress(Integer.parseInt(decimalFormat.format(Math.round(fiber))));
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

    private void setCardValue(String cardNum) {
        Log.d("num", ""+cardnum);
        String script = "document.getElementById('card').value = '" + cardNum + "';";
        webView.evaluateJavascript(script, null);
    }

    private void clickButton() {
        String script = "var buttons = document.getElementsByClassName('btn btn-primary py-3 px-4');"
                + "if (buttons.length > 0) {"
                + "    buttons[0].click();"
                + "}";
        webView.evaluateJavascript(script, null);
    }

    private void extractTableInfo() {
        String script = "var table = document.evaluate('/html/body/div/table/tbody/tr/td/table', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
                + "var rows = table.getElementsByTagName('tr');"
                + "var tableData = [];"
                + "for (var i = 0; i < rows.length; i++) {"
                + "    var row = rows[i];"
                + "    var rowData = [];"
                + "    var cells = row.getElementsByTagName('td');"
                + "    for (var j = 0; j < cells.length; j++) {"
                + "        var cell = cells[j];"
                + "        rowData.push(cell.textContent.trim());" // trim()을 추가하여 앞뒤 공백을 제거합니다.
                + "    }"
                + "    tableData.push(rowData);"
                + "}"
                + "var tableInfo = JSON.stringify(tableData);"
                + "tableInfo;";
        webView.evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.d("recieve", ""+value);
                if (value != null && value.startsWith("\"") && value.endsWith("\"")) {

                    value = value.substring(1, value.length() - 1); // 따옴표 제거
                    value = value.replaceAll("[^\\p{IsHangul}\\p{Digit}-~ :,()]", ""); // 특수 문자와 한글 숫자 제거
                    value = value.replaceAll(",", "\n"); // ,를 줄바꿈으로 대체
                    String[] lines = value.split("\n"); // 각 줄을 분리합니다.
                    String secondLine = ""; // 두 번째 줄을 저장할 변수

                    if (lines.length >= 2) {
                        secondLine = lines[1] + lines[2]; // 두 번째 줄을 저장합니다.
                    }
                    secondLine = secondLine.replaceAll("    ", "");
                    Log.d("cut", ""+secondLine);
                    textView.setText(secondLine); // 텍스트뷰에 두 번째 줄을 설정합니다.
                    webView.setWebViewClient(null);
                }
            }
        });
    }
}