    package com.example.blueday4meals.Nutrient.rating;
    //이준호 작성
    import android.os.Bundle;
    import android.os.Handler;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.toolbox.Volley;
    import com.example.blueday4meals.Function.navigationbar;
    import com.example.blueday4meals.Nutrient.NutrientMain;
    import com.example.blueday4meals.Nutrient.calculaors.DailyCalculator;
    import com.example.blueday4meals.Nutrient.requests.getdaynuti;
    import com.example.blueday4meals.Nutrient.requests.mindateRequest;
    import com.example.blueday4meals.R;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.text.DateFormat;
    import java.text.DecimalFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.Queue;

    public class RatingMain extends AppCompatActivity {

        private RecyclerView recyclerView;
        private RecyclerViewAdapter adapter;
        private ArrayList<String> items = new ArrayList<>();
        private Button btn;
        private String mindate, maxdate, formattedDate;
        private int week, j =0, k=0, count =0;
        private int[][] dpoint;
        private  int[] wpoint;
        private Date edate = null;
        private Date sdate = null;
        private Date date = null;


        private boolean isLoading = false;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ratingmainpage);

            String userID = getIntent().getStringExtra("userID");

            recyclerView = findViewById(R.id.recyclerView);

            btn = findViewById(R.id.button);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new navigationbar(RatingMain.this, NutrientMain.class, userID);
                }
            });

            Response.Listener<String> responseListener = new Response.Listener<String>() {

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
                                mindate = dataObject.getString("mindate");
                                maxdate = dataObject.getString("maxdate");
                                week = count(mindate, maxdate);

                                if (mindate != null) {
                                    RequestQueue queue1 = Volley.newRequestQueue(RatingMain.this);
                                    RequestQueue queue2 = Volley.newRequestQueue(RatingMain.this);
                                    dpoint = new int[7][week-1];
                                    wpoint = new int[week];

                                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar calendar = Calendar.getInstance();
                                    Date day = null;

                                    try {
                                        Date date = inputFormat.parse(mindate);
                                        if (date != null) {
                                            day = date;
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Arrays.fill(wpoint, 23);
                                    for (int i = 0; i < week * 7; i++) {
                                        LocalDate localDate = day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        String formattedDate = localDate.format(formatter);

                                        getdaynuti.nuturientgetter.getter(userID, formattedDate, queue1, queue2, new getdaynuti.ResultListener() {
                                            @Override
                                            public void onResult(int point) {
                                                dpoint[j][k] = point;

                                                wpoint[k] = dpoint[0][k] + dpoint[1][k] + dpoint[2][k] + dpoint[3][k] + dpoint[4][k] + dpoint[5][k] + dpoint[6][k];
                                                Log.d("", "" + wpoint[week-2]);
                                                j++;

                                                if (j > 6) {
                                                    j = 0;
                                                    k++;
                                                    if(k==week-1){
                                                        k=0;
                                                    }
                                                }
                                                count++;
                                                if(count == week*7){
                                                    populateData(week);
                                                    initAdapter();
                                                    initScrollListener();
                                                }
                                            }

                                            @Override
                                            public void onDataResult(int cal, double carb, double protein, double fat, double fiber) {

                                            }
                                        });

                                        calendar.setTime(day);
                                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                                        day = calendar.getTime();

                                    }
                                }



                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            mindateRequest Request = new mindateRequest(userID, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RatingMain.this);
            queue.add(Request);

        }


        private int count(String startDate, String endDate) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                sdate = dateFormat.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int weeksBetweenDates = 0;
            if (sdate != null) {

                try {
                    edate = dateFormat.parse(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (edate != null) {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(sdate);
                    startCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // 작은 날짜부터의 첫 번째 주의 일요일로 설정

                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(edate);
                    endCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // 큰 날짜부터의 마지막 주의 토요일로 설정

                    weeksBetweenDates = 0;
                    while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
                        startCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                        weeksBetweenDates++;
                    }
                }
            }

            return weeksBetweenDates-1;
        }

        private void populateData(int week) {
            int j = 0;
            for (int i = 0; i < 10; i++) {
                if (j % 10 == 0 && j != 0) {
                    week -= 10;
                }
                items.add("Week " + (week - j) + "," + wpoint[i]);
                j++;
            }
        }

        private void initAdapter() {
            adapter = new RecyclerViewAdapter(items);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }

        private void initScrollListener() {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    if (!isLoading) {
                        if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == items.size() - 1) {
                            //리스트 마지막
                            loadMore();
                            isLoading = true;
                        }
                    }
                }
            });
        }

        private void loadMore() {
            items.add(null);
            adapter.notifyItemInserted(items.size() - 1);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    items.remove(items.size() - 1);
                    int scrollPosition = items.size();
                    adapter.notifyItemRemoved(scrollPosition);
                    int currentSize = scrollPosition;
                    int nextLimit = currentSize + 10;

                    int j = 0;
                    while (currentSize - 1 < nextLimit) {
                        if (week - currentSize <= 0) {
                            break;  // week가 1 이하이면 더 이상 추가하지 않음
                        }
                        items.add("Week " + (week - currentSize) + "," + wpoint[currentSize]);
                        currentSize++;
                        j++;
                    }

                    adapter.notifyDataSetChanged();
                    isLoading = false;
                }
            }, 2000);
        }

    }//이준호 작성