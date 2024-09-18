package Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    /**
     * Difference between Filetime epoch and Unix epoch (in ms).
     */
    private static final long FILETIME_EPOCH_DIFF = 11644473600000L;

    /**
     * One millisecond expressed in units of 100s of nanoseconds.
     */
    private static final long FILETIME_ONE_MILLISECOND = 10 * 1000;

    public static long filetimeToMillis(final long filetime) {
        return (filetime / FILETIME_ONE_MILLISECOND) - FILETIME_EPOCH_DIFF;
    }

    public static long millisToFiletime(final long millis) {
        return (millis + FILETIME_EPOCH_DIFF) * FILETIME_ONE_MILLISECOND;
    }

    public byte[] longToByteArray(long value) {
        return new byte[] {
                (byte) (value >> 56),
                (byte) (value >> 48),
                (byte) (value >> 40),
                (byte) (value >> 32),
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    private static final int HOUR_IN_SECONDS = 3600;
    private static final int HOUR_IN_MINUTES = 60;
    public static final String DATE_FORMAT_1 = "MM/dd/yyyy";
    public static final String DATE_FORMAT_2 = "MM/dd/yyyy HH:mm";
    public static final String DATE_FORMAT_3 = "MMM./dd/yyyy";
    public static final String DATE_FORMAT_4 = "dd/MM/yyyy";
    public static final String DATE_FORMAT_5 = "dd-MMM-yyyy";
    public static final String DATE_FORMAT_6 = "MMM-yyyy";
    public static final String DATE_FORMAT_7 = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_8 = "EEE, MMM d, h:mm a";        // Tue, Nov 24, 10:58 AM
    public static final String DATE_FORMAT_9 = "EEE, d MMM yyyy";           // Thu, 15 Apr 2015
    public static final String DATE_FORMAT_10 = "hh:mm a";                  // 08:00 AM
    public static final String DATE_FORMAT_11 = "dd.MM.yyyy";
    public static final String DATE_FORMAT_12 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_13 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_14 = "yyyy/MM/dd";
    public static final String DATE_FORMAT_15 = "MM/dd/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_16 = "yyyyMMddhhmmss";
    public static final String DATE_FORMAT_17 = "MM-dd-yyyy HH:mm";
    public static final String DATE_FORMAT_18 = "MM-dd h:mm a";
    public static final String DATE_FORMAT_19 = "MMM dd h:mm a";            // Aug 21 11:15 AM,
    public static final String DATE_FORMAT_20 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_21 = "EEE MM-dd h:mm a";         // Aug 21 11:15 AM,
    public static final String DATE_FORMAT_22 = "MM-dd-yyyy";
    public static final String DATE_FORMAT_23 = "HH:mm";                    // Aug 21 11:15 AM,
    public static final String DATE_FORMAT_24 = "yyyy-MM-dd'T'HH:mm:ss.SSS";// Aug 21 11:15 AM,
    public static final String DATE_FORMAT_25 = "MM-dd";                    // 08-21
    public static final String DATE_FORMAT_26 = "MMddYY";                   // 08-21
    public static final String DATE_FORMAT_27 = "EEEE, MMMM dd, yyyy";      // Thursday, August 5, 2021
    public static final String DATE_FORMAT_28 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_29 = "EEE, MMM d, yyyy";         // Thu, 15 Apr 2015
    public static final String DATE_FORMAT_30 = "h a";                      // Thu, 15 Apr 2015
    public static final String DATE_FORMAT_31 = "MMMM dd, yyyy";            // Thursday, August 5, 2021
    public static final String DATE_FORMAT_32 = "MMddyyyy_HHmmss";

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getCurrentTimeString() {
        return dateToString(Calendar.getInstance().getTime(), "MM/dd/yyyy");
    }

    public static String getFileSuffixString() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_32); // "MMddyyyy_HHmm:s"
        return format.format(now);
    }

    public static String toStringFormat_12(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_12); // "MM/dd/yyyy HH:mm:ss"
        return format.format(date);
    }

    public static String toStringFormat_13(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_13); // "yyyy-MM-dd"
        return format.format(date);
    }

    public static String toStringFormat_20(Date date) {
        if (date == null)
            return "";
        return dateToString(date, DATE_FORMAT_20);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parseDataFromFormat(String dateString, String dateFormat) {
        Date retDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            retDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retDate;
    }

    public static String getElapsedTimeMinutesSecondsStringFromMilis(long miliseconds) {
        long elapsedTime = miliseconds;
        String format = String.format("%%0%dd", 2);

        elapsedTime = elapsedTime / 1000;

        int hour = (int) (elapsedTime / 3600);
        int min = (int) ((elapsedTime - hour * 3600) / 60);
        int sec = (int) (elapsedTime - hour * 3600 - min * 60);

        /*String seconds = String.format(format, sec);
        String hours = String.format(format, hour);
        String minutes = String.format(format, min);
        return hours + ":" + minutes + ":" + seconds;
        */

        return String.format("%02d:%02d:%02d", hour, min, sec);
    }

    public static String getElapsedTimeMinutesSecondsStringFromSecs(long seconds) {
        long elapsedTime = seconds;
        String format = String.format("%%0%dd", 2);

        int hour = (int) (elapsedTime / 3600);
        int min = (int) ((elapsedTime - hour * 3600) / 60);
        int sec = (int) (elapsedTime - hour * 3600 - min * 60);

        /*String seconds = String.format(format, sec);
        String hours = String.format(format, hour);
        String minutes = String.format(format, min);
        return hours + ":" + minutes + ":" + seconds;
        */

        return String.format("%02d:%02d:%02d", hour, min, sec);
    }

    public static String getElapsedTimeMinutesSecondsStringFromMins(long minutes) {
        long elapsedTime = minutes;
        String format = String.format("%%0%dd", 2);

        int hour = (int) (elapsedTime / 60);
        int min = (int) (elapsedTime - hour * 60) ;
        int sec = 0;

        /*String seconds = String.format(format, sec);
        String hours = String.format(format, hour);
        String minutes = String.format(format, min);
        return hours + ":" + minutes + ":" + seconds;
        */

        return String.format("%02d:%02d:%02d", hour, min, sec);
    }

    public static long getTimeInSecondsFromMidnight() {

        // Way1
        ZonedDateTime nowZoned = ZonedDateTime.now();
        Instant midnight = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).toInstant();
        Duration duration = Duration.between(midnight, Instant.now());
        long seconds = duration.getSeconds();
        return seconds;

        // Way2
        /*Calendar calStartTimeToday = Calendar.getInstance();
        long now = calStartTimeToday.getTimeInMillis();

        calStartTimeToday.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calStartTimeToday.clear(Calendar.MINUTE);
        calStartTimeToday.clear(Calendar.SECOND);
        calStartTimeToday.clear(Calendar.MILLISECOND);

        return Math.max(0, (now - calStartTimeToday.getTimeInMillis()) / 1000);*/
    }

    public static long getTimeInMiliSecondsFromMidnight() {

        // Way2
        Calendar calStartTimeToday = Calendar.getInstance();
        long now = calStartTimeToday.getTimeInMillis();

        calStartTimeToday.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calStartTimeToday.clear(Calendar.MINUTE);
        calStartTimeToday.clear(Calendar.SECOND);
        calStartTimeToday.clear(Calendar.MILLISECOND);

        return Math.max(0, (now - calStartTimeToday.getTimeInMillis()) / 1000);
    }

    public static String getDayTimeStringInUniformat() {
        return dateToString(Calendar.getInstance().getTime(), "MM/dd/yyyy");
    }

    public static long getCurrentTime() {
        //return new Date().getTime();
        return System.currentTimeMillis();
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd,yy  HH:mm");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek + " " + dateFormat.format(date);
    }
}
