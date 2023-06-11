package com.example.blueday4meals.Nutrient.calculaors;
//이준호 작성
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AgeCalculator {

    public static int calculateAge(String birthdateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date birthdate = null;
        try {
            birthdate = dateFormat.parse(birthdateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar birthdateCalendar = Calendar.getInstance();
        birthdateCalendar.setTime(birthdate);

        Calendar todayCalendar = Calendar.getInstance();

        int age = todayCalendar.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR);

        // 생일이 지났는지 확인
        if (todayCalendar.get(Calendar.MONTH) < birthdateCalendar.get(Calendar.MONTH)
                || (todayCalendar.get(Calendar.MONTH) == birthdateCalendar.get(Calendar.MONTH)
                && todayCalendar.get(Calendar.DAY_OF_MONTH) < birthdateCalendar.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;

    }
}//이준호 작성