package com.anitalk.app.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
