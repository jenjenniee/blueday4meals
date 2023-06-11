package com.example.blueday4meals.Function;

import android.app.Activity;
import android.content.Intent;
//이준호 작성
public class navigationbar extends Activity {
    public navigationbar(Activity activity, Class<?> changingClass, String userID){
        Intent intent = new Intent(activity, changingClass);
        intent.putExtra("userID", userID);
        activity.startActivity(intent);
    }
}//이준호 작성