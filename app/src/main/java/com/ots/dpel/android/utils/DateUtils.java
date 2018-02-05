package com.ots.dpel.android.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tasos on 13/10/2017.
 */

public class DateUtils {

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormatter.format(date);
    }

    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return dateTimeFormatter.format(date);
    }


}
