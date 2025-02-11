package com.brightbeam.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverterUtil {
    /**
     * Convert String to Date
     * @param dateStr
     * @return
     */
    public static Date convertStringToDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Invalid date format: " + e.getMessage());
            return null;
        }
    }
}
