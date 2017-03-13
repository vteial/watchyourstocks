package io.wybis.watchyourstocks.util

import org.joda.time.DateTime

class DateTimeHelper {

    static DateTime createStartOfTheDay() {
        DateTime dt = new DateTime()
        dt = new DateTime(dt.getYear(), dt.getMonthOfYear(),
                dt.getDayOfMonth(), 0, 0, 0)
        return dt
    }

    static DateTime createEndOfTheDay() {
        DateTime dt = new DateTime()
        dt = new DateTime(dt.getYear(), dt.getMonthOfYear(),
                dt.getDayOfMonth(), 23, 59, 59)
        return dt
    }

    static DateTime createStartOfTheDay(Date date) {
        DateTime dt = new DateTime(date)
        dt = new DateTime(dt.getYear(), dt.getMonthOfYear(),
                dt.getDayOfMonth(), 0, 0, 0)
        return dt
    }

    static DateTime createEndOfTheDay(Date date) {
        DateTime dt = new DateTime(date)
        dt = new DateTime(dt.getYear(), dt.getMonthOfYear(),
                dt.getDayOfMonth(), 23, 59, 59)
        return dt
    }

    static DateTime createWithTimeOffFromNow() {
        DateTime dt = new DateTime()
        dt = new DateTime(dt.getYear(), dt.getMonthOfYear(),
                dt.getDayOfMonth(), 0, 0, 0)
        return dt
    }

    static DateTime createWithTimeOffFromDate(Date date) {
        DateTime dt = new DateTime(date)
        dt = new DateTime(dt.getYear(), dt.getMonthOfYear(),
                dt.getDayOfMonth(), 0, 0, 0)
        return dt
    }
}
