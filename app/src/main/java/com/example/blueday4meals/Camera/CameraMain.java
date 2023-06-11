package com.example.blueday4meals.Camera;

import static com.doinglab.foodlens.sdk.NutritionRetrieveMode.TOP1_NUTRITION_ONLY;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.doinglab.foodlens.sdk.FoodLens;
import com.doinglab.foodlens.sdk.FoodLensBundle;
import com.doinglab.foodlens.sdk.NetworkService;
import com.doinglab.foodlens.sdk.NutritionResultHandler;
import com.doinglab.foodlens.sdk.RecognizeResultHandler;
import com.doinglab.foodlens.sdk.UIService;
import com.doinglab.foodlens.sdk.UIServiceMode;
import com.doinglab.foodlens.sdk.UIServiceResultHandler;
import com.doinglab.foodlens.sdk.errors.BaseError;
import com.doinglab.foodlens.sdk.network.model.FoodPosition;
import com.doinglab.foodlens.sdk.network.model.Nutrition;
import com.doinglab.foodlens.sdk.network.model.NutritionResult;
import com.doinglab.foodlens.sdk.network.model.RecognitionResult;
import com.doinglab.foodlens.sdk.network.model.UserSelectedResult;
import com.doinglab.foodlens.sdk.theme.BottomWidgetTheme;
import com.doinglab.foodlens.sdk.theme.DefaultWidgetTheme;
import com.doinglab.foodlens.sdk.theme.ToolbarTheme;
import com.example.blueday4meals.Function.navigationbar;
import com.example.blueday4meals.MainPages.ChildMainPage;
import com.example.blueday4meals.MainPages.SettingMain;
import com.example.blueday4meals.NaverMap.NaverMapMain;
import com.example.blueday4meals.Nutrient.NutrientMain;
import com.example.blueday4meals.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraMain extends AppCompatActivity {

    UIService uiService;
    NetworkService ns;
    AlertDialog.Builder builder;

    final int REQ_PICTURE = 0x02;

    String userID, timeDiv;

    RecognitionResult recognitionResult = null;

    @Override//이준호
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        userID = getIntent().getStringExtra("userID");
        Log.d("TAG", "변수 값: " + userID);

        Button btnMain, btnCam, btnNut, btnMap, btnSet, openCamera;

        Button bttt = findViewById(R.id.btttttt);

        btnMain = findViewById(R.id.button1);
        btnMap = findViewById(R.id.button2);
        btnNut = findViewById(R.id.button3);
        btnCam = findViewById(R.id.button4);
        btnSet = findViewById(R.id.button5);
        openCamera = findViewById(R.id.Btn_camera_open);//이준호

        uiService = FoodLens.createUIService(this);
        uiService.setUiServiceMode(UIServiceMode.USER_SELECTED_WITH_CANDIDATES);

        DefaultWidgetTheme defaultWidgetTheme = new DefaultWidgetTheme(this);
        ToolbarTheme toolbarTheme = new ToolbarTheme(this);
        BottomWidgetTheme bottomWidgetTheme =  new BottomWidgetTheme(this);

        bottomWidgetTheme.setButtonTextColor(0x6200EE);
        bottomWidgetTheme.setWidgetRadius(30);
        toolbarTheme.setBackgroundColor(0x6200EE);
        defaultWidgetTheme.setWidgetColor(0x6200EE);

        FoodLensBundle bundle = new FoodLensBundle();

        try {
            bundle.setEnableManualInput(true);
            bundle.setSaveToGallery(true);
            bundle.setUseImageRecordDate(false);
            bundle.setEnablePhotoGallery(false);
            uiService.setDataBundle(bundle);
        } catch (Exception e) {

        }

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] times = getResources().getStringArray(R.array.timediv);
                builder = new AlertDialog.Builder(CameraMain.this);
                builder.setTitle("시간을 선택하세요");

                builder.setItems(times, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timeDiv = times[which];

                        bundle.setEatType(which);
                        uiService.startFoodLensCamera(CameraMain.this, new UIServiceResultHandler() {
                            @Override
                            public void onSuccess(UserSelectedResult result) {
                                recognitionResult = result;
                                setRecognitionResultData(recognitionResult);
                            }

                            @Override
                            public void onCancel() {
                                Log.d("FOODLENS_LOG", "Recognition Cancel");
                                Toast.makeText(getApplicationContext(), "Recognition Cancel", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(BaseError error) {

                                Log.e("FOODLENS_LOG", error.getMessage());
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //이준호
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, ChildMainPage.class, userID);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, NaverMapMain.class, userID);
            }
        });

        btnNut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, NutrientMain.class, userID);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, CameraMain.class, userID);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, SettingMain.class, userID);
            }
        });
    }//이준호

    private void setRecognitionResultData(RecognitionResult recognitionResultData)
    {
        List<FoodPosition> foodPositions = recognitionResultData.getFoodPositions();

        String foodName = "";
        String foodNutritionInfo = "";


        float tcal=0, tcarbon=0, tprotein=0, tfat=0, tfiber=0;

        for(int i=0; i<foodPositions.size(); i++) {

            foodName = "";

            FoodPosition foodPosition = foodPositions.get(i);


            foodName = foodPosition.getUserSelectedFood().getFoodName();
            Nutrition nutrition = foodPosition.getUserSelectedFood().getNutrition();
            if (nutrition != null) {
                tcal += nutrition.getCalories();
                tcarbon += nutrition.getCarbonHydrate();
                tprotein += nutrition.getProtein();
                tfat += nutrition.getFat();
                tfiber += nutrition.getDietrayFiber();
            }
        }


        //오늘날짜 db에 입력
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String birthdateString = dateFormat.format(calendar.getTime());

        //파일명 db에 입력
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        File folder = new File(folderPath);
        String fileName = "";

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null && files.length > 0) {
                // 파일들을 수정일 기준으로 정렬
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File file1, File file2) {
                        long lastModified1 = file1.lastModified();
                        long lastModified2 = file2.lastModified();
                        return Long.compare(lastModified2, lastModified1);
                    }
                });

                File latestFile = files[0];
                fileName = latestFile.getName();
            }
        }
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                } catch (JSONException e){
                    e.printStackTrace();
                    System.out.println("오류");
                }
            }
        };


        String res = (tcal + " " + tcarbon +" " + tprotein +" "+ tfat +" " + tfiber + " " + timeDiv + " " + fileName + "\n");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("인사말").setMessage(res);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();


        InputNutritionRequest inputdaterequest = new InputNutritionRequest(foodName, tcal, tcarbon, tprotein, tfat, tfiber, timeDiv, fileName, birthdateString, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(CameraMain.this);
        queue.add(inputdaterequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            final byte[] byteData = readContentIntoByteArray(new File(picturePath));

            ns = FoodLens.createNetworkService(CameraMain.this);
            ns.setNutritionRetrieveMode(TOP1_NUTRITION_ONLY);
            ns.predictMultipleFood(byteData, new RecognizeResultHandler() {
                @Override
                public void onSuccess(RecognitionResult result) {
                    setRecognitionResultData(result);
                }

                @Override
                public void onError(BaseError errorReason) {
                    Log.e("FOODLENS_LOG", errorReason.getMessage());

                }
            });

            ns.getNutritionInfo(20, new NutritionResultHandler() {
                @Override
                public void onSuccess(NutritionResult result) {

                }

                @Override
                public void onError(BaseError errorReason) {
                    Toast.makeText(getApplicationContext(), errorReason.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        uiService.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }
}