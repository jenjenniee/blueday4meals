package com.example.blueday4meals.Function;

import android.app.Activity;
import android.content.Intent;

public class navigationbar extends Activity {
    public navigationbar(Activity activity, Class<?> changingClass, String userID){
        Intent intent = new Intent(activity, changingClass);
        activity.startActivity(intent);
        intent.putExtra("userID", userID);
    }
}
