package com.example.meals.Function;

import android.app.Activity;
import android.content.Intent;
//이준호 작성
public class navigationbar extends Activity {
    public navigationbar(Activity activity, Class<?> changingClass, String userID){
        Intent intent = new Intent(activity, changingClass);
        intent.putExtra("userID", userID);
        activity.startActivity(intent);
        finish();
    }
}//이준호 작성