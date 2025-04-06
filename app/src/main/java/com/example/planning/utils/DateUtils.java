package com.example.planning.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateUtils {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getCurrentDate(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM");
        return currentDate.format(formatter);
    }

    public static List<String> generateYearDates() {
        List<String> dates = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_YEAR); i++) {
            dates.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return dates;
    }
}
