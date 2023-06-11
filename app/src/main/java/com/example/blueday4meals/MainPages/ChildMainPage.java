package com.example.blueday4meals.MainPages;
//이준호 작성
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.blueday4meals.Camera.CameraMain;
import com.example.blueday4meals.Function.navigationbar;
import com.example.blueday4meals.Login.LoginActivity;
import com.example.blueday4meals.NaverMap.NaverMapMain;
import com.example.blueday4meals.Nutrient.NutrientMain;
import com.example.blueday4meals.Nutrient.calculaors.Rating;
import com.example.blueday4meals.Nutrient.requests.getdaynuti;
import com.example.blueday4meals.Nutrient.requests.getnutrient;
import com.example.blueday4meals.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChildMainPage extends AppCompatActivity {
    private TextView textView;
    private Button startButton;
    private WebView webView;
    private String cardnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.childmain);

        String userID = getIntent().getStringExtra("userID");

        Button btnMain, btnCam, btnNut, btnMap, btnSet;

        btnMain = findViewById(R.id.button1);
        btnMap = findViewById(R.id.button2);
        btnNut = findViewById(R.id.button3);
        btnCam = findViewById(R.id.button4);
        btnSet = findViewById(R.id.button5);


        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(ChildMainPage.this, ChildMainPage.class, userID);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(ChildMainPage.this, NaverMapMain.class, userID);
            }
        });

        btnNut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(ChildMainPage.this, NutrientMain.class, userID);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(ChildMainPage.this, CameraMain.class, userID);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(ChildMainPage.this, SettingMain.class, userID);
            }
        });
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
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        if (dataArray.length() > 0) {
                            Log.d("TAG", "co 값: " + jsonResponse);
                            JSONObject dataObject = dataArray.getJSONObject(0);
                            cardnum = dataObject.getString("cardNum");
                            Log.d("num", ""+cardnum);
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        CardNumrequest Request = new CardNumrequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ChildMainPage.this);
        queue.add(Request);


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
}//이준호 작성