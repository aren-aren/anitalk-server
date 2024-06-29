package com.anitalk.app.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateManager {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String today(){
        return LocalDate.now().format(dateFormatter);
    }

    public static String nowDateTime(){
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getDate(long dates) {
        return LocalDate.now().plusDays(dates).format(dateFormatter);
    }

    public static Long getDiffToday(String date) {
        LocalDateTime inputDate = LocalDateTime.parse(date, dateTimeFormatter);
        LocalDateTime now = LocalDateTime.now();

        return Duration.between(inputDate, now).toDays();
    }

    public static Long getNowMilliseconds() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
