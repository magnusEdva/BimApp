package com.bimapp.model.entity;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateMapper {
    private static DateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
    private static DateFormat MILLIS_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
    private static DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
    private static int LENGTH_WITHOUT_MILLIS = 24;

    public static String map(Date date) {
        if (date == null) {
            return null;
        }
        return formatter.format(date);
    }


    /**
     * Transform ISO 8601 string to Date.
     */
    public static Date toDate(final String iso8601string) throws ParseException {
        String formatted = formatTimeZone(iso8601string);
        if (formatted.length() == LENGTH_WITHOUT_MILLIS) {
            return FORMATTER.parse(formatted);
        } else {
            return MILLIS_FORMATTER.parse(formatted);
        }
    }

    private static String formatTimeZone(String iso8601string) {
        String withTimeZone = iso8601string.replace("Z", "+00:00");
        String[] parts = withTimeZone.split("\\+");
        String timeZonePart = "+" + parts[1];
        String withoutColon = timeZonePart.replaceAll("\\+([^<]*):", "\\+$1");
        if (withoutColon.length() == 3) {
            withoutColon += "00";
        }
        return parts[0] + withoutColon;
    }
}

