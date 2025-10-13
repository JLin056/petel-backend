package com.example.petel.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    /**
     * Calculates the difference in days between two dates provided as String objects.
     *
     * @param stringDate1 The first date (inclusive) in "yyyy-MM-dd" format.
     * @param stringDate2 The second date (exclusive) in "yyyy-MM-dd" format.
     * @return The number of days between {@code stringDate1} and {@code stringDate2}.
     * A positive value means {@code stringDate2} is after {@code stringDate1}.
     * A negative value means {@code stringDate2} is before {@code stringDate1}.
     */
    public static long getDifferenceOfDays(String stringDate1, String stringDate2) {
        return ChronoUnit.DAYS.between(LocalDate.parse(stringDate1), LocalDate.parse(stringDate2));
    }

    /**
     * Calculates the difference in days between two {@code LocalDate} objects.
     *
     * @param date1 The first date (inclusive).
     * @param date2 The second date (exclusive).
     * @return The number of days between {@code date1} and {@code date2}.
     * A positive value means {@code date2} is after {@code date1}.
     * A negative value means {@code date2} is before {@code date1}.
     */
    public static long getDifferenceOfDays(LocalDate date1, LocalDate date2) {
        return ChronoUnit.DAYS.between(date1, date2);
    }

    /**
     * Convert the time to the format required by ECPay.
     *
     * @param timestamp The Timestamp Object.
     * @return "yyyy/MM/dd HH:mm:ss"
     */
    public static String getMerchantTradeDate (Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(timestamp);
    }
}
