package com.example.blueday4meals.NaverMap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.blueday4meals.Camera.CameraMain;
import com.example.blueday4meals.MainPages.ChildMainPage;
import com.example.blueday4meals.Function.navigationbar;
import com.example.blueday4meals.Nutrient.NutrientMain;
import com.example.blueday4meals.R;
import com.example.blueday4meals.MainPages.SettingMain;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NaverMapMain extends AppCompatActivity implements NaverMap.OnMapClickListener, OnMapReadyCallback {

    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private NaverMapItem naverMapList;
    private List<NaverMapData> naverMapInfo;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    Button btnMain, btnCam, btnNut, btnMap, btnSet;
    double longitude, latitude;

    //네이버 맵 호출
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naver_map);

        String userID = getIntent().getStringExtra("userID");
        Log.d("TAG", "변수 값: " + userID);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        btnMain = findViewById(R.id.button1);
        btnMap = findViewById(R.id.button2);
        btnNut = findViewById(R.id.button3);
        btnCam = findViewById(R.id.button4);
        btnSet = findViewById(R.id.button5);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, ChildMainPage.class, userID);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, NaverMapMain.class, userID);
            }
        });

        btnNut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, NutrientMain.class, userID);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, CameraMain.class, userID);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(NaverMapMain.this, SettingMain.class, userID);
            }
        });

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE); // 현재위치 표시할 때 권한 확인


        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setloc();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);  //현재위치 표시
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        // 클라이언트 객체 생성 -
        NaverMapApiInterface naverMapApiInterface = NaverMapRequest.getClient().create(NaverMapApiInterface.class);
        // 응답을 받을 콜백 구현
        Call<NaverMapItem> call = naverMapApiInterface.getMapData();
        // 클라이언트 객체가 제공하는 enqueue로 통신에 대한 요청, 응답 처리 방법 명시
        call.enqueue(new Callback<NaverMapItem>() {
            @Override
            public void onResponse(Call<NaverMapItem> call, Response<NaverMapItem> response) { // 통신 성공시 -
                naverMapList = response.body(); // naverMapList에 요청에 대한 응답 결과 저장
                naverMapInfo = naverMapList.data;

                // 현재 위치 기준으로 마커 표시
                LatLng currentLocation = new LatLng(latitude, longitude);
                for (int i = 0; i < naverMapInfo.size(); i++) {
                    NaverMapData mapData = naverMapInfo.get(i);
                    LatLng markerLocation = new LatLng(mapData.getlatitude(), mapData.getlongitude());

                    // 현재 위치와 마커의 거리 계산
                    double distance = currentLocation.distanceTo(markerLocation);

                    // 일정 범위 내에 있는 마커만 표시
                    if (distance <= 1000) { // 1000m(1km) 이내의 마커만 표시하도록 설정
                        Marker marker = new Marker();
                        marker.setPosition(markerLocation);
                        marker.setWidth(50); // 마커 크기 조절
                        marker.setHeight(75);
                        marker.setMap(naverMap);


                        int finalI = i;
                        marker.setOnClickListener(new Overlay.OnClickListener() {
                            @Override
                            public boolean onClick(@NonNull Overlay overlay) {
                                getClickHandler(finalI);
                                return false;
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<NaverMapItem> call, Throwable t) { // 통신 실패시

            }
        });
    }
// 현재 위치 정보 가져오기
    private void setloc(){
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NaverMapMain.this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        } else {
            // 가장 최근 위치정보 가져오기

            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            while (location == null) {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d("TAG", "변수 값: " + location);
            Log.d("TAG", "변수 값: " + latitude);
            Log.d("TAG", "변수 값: " + longitude);
        }
    }


    //마커 클릭시 가게 정보 제공
    private void getClickHandler(int index) {
        if (naverMapInfo != null && index >= 0 && index < naverMapInfo.size()) {
            NaverMapData selectedData = naverMapInfo.get(index);
            String name = selectedData.getfranchisee_name(); //가맹점명
            String tel_num = selectedData.gettel_num(); //전화번호
            String road_address = selectedData.getroad_address(); //도로명주소
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            TextView drawerInfo = findViewById(R.id.drawer_info);

            // 이미지와 텍스트를 함께 표시하기 위해 SpannableString 생성
            SpannableString spannableString = new SpannableString("call " +' ' + tel_num);


            // 리소스에서 이미지를 로드하고 ImageSpan 생성
            Drawable image = getResources().getDrawable(R.drawable.call); // 이미지 로드
            image.setBounds(0, 0, 30, 30); // 이미지 크기 설정
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);

            // SpannableString에 ImageSpan을 설정
            spannableString.setSpan(imageSpan, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


            drawerInfo.setText(name + "\n");
            drawerInfo.append(road_address+ "\n");
            drawerInfo.append(spannableString);
            drawerInfo.setClickable(true);

            //전화걸기
             drawerInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 전화 걸기 Intent 생성
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + tel_num ));
                    startActivity(intent);
//                    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            });



            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        setloc();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

    }
}