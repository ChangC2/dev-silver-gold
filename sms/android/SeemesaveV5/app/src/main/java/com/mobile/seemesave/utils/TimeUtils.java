package com.mobile.seemesave.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
   public static final String DATE_FORMAT_1 = "MM/dd/yyyy";
   public static final String DATE_FORMAT_2 = "MM/dd/yyyy HH:mm";
   public static final String DATE_FORMAT_3 = "MMM./dd/yyyy";
   public static final String DATE_FORMAT_4 = "dd/MM/yyyy";
   public static final String DATE_FORMAT_5 = "dd-MMM-yyyy";
   public static final String DATE_FORMAT_6 = "MMM-yyyy";
   public static final String DATE_FORMAT_7 = "yyyy-MM-dd HH:mm";
   public static final String DATE_FORMAT_8 = "EEE, MMM d, h:mm a"; 	// Tue, Nov 24, 10:58 AM
   public static final String DATE_FORMAT_9 = "EEE, d MMM yyyy"; 		// Thu, 15 Apr 2015
   public static final String DATE_FORMAT_10 = "h:mm a"; 				// 08:00 AM
   public static final String DATE_FORMAT_11 = "dd.MM.yyyy";
   public static final String DATE_FORMAT_12 = "yyyy-MM-dd HH:mm:ss";
   public static final String DATE_FORMAT_13 = "yyyy-MM-dd";
   public static final String DATE_FORMAT_14 = "yyyy/MM/dd";
   public static final String DATE_FORMAT_15 = "MM/dd/yyyy HH:mm:ss";
   public static final String DATE_FORMAT_16 = "yyyyMMddhhmmss";
   public static final String DATE_FORMAT_17 = "MM-dd-yyyy HH:mm";
   public static final String DATE_FORMAT_18 = "MM-dd h:mm a";
   public static final String DATE_FORMAT_19 = "yyyy-MM-ddTHH:mm:ss";
   public static final String DATE_FORMAT_20 = "HH:mm";

   public static final List<Long> times = Arrays.asList(
           TimeUnit.DAYS.toMillis(365),
           TimeUnit.DAYS.toMillis(30),
           TimeUnit.DAYS.toMillis(1),
           TimeUnit.HOURS.toMillis(1),
           TimeUnit.MINUTES.toMillis(1),
           TimeUnit.SECONDS.toMillis(1) );
   public static final List<String> timesString = Arrays.asList("year","month","day","hour","minute","second");
   public static String covertTimeToAgo(Date pasTime) {

      String convTime = null;

      String prefix = "";
      String suffix = "Ago";

      Date nowTime = new Date();

      long dateDiff = nowTime.getTime() - pasTime.getTime();

      long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
      long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
      long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
      long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

      if (second < 60) {
         convTime = second + " Seconds " + suffix;
      } else if (minute < 60) {
         convTime = minute + " Minutes "+suffix;
      } else if (hour < 24) {
         convTime = hour + " Hours "+suffix;
      } else if (day >= 7) {
         if (day > 360) {
            convTime = (day / 360) + " Years " + suffix;
         } else if (day > 30) {
            convTime = (day / 30) + " Months " + suffix;
         } else {
            convTime = (day / 7) + " Week " + suffix;
         }
      } else if (day < 7) {
         convTime = day+" Days "+suffix;
      }

      return convTime;
   }
   public static Date parseDataFromFormat12(String dateString) {
      Date retDate = new Date();
      SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_12);
      try {
         retDate = format.parse(dateString);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return retDate;
   }
   public static String getTimeAgo(long time) {

      if (time < 1000000000000L) {
         // if timestamp given in seconds, convert to millis
         time *= 1000;
      }

      long now = System.currentTimeMillis();
      if (time > now || time <= 0) {
         return "just now";
      }

      // TODO: localize
      final long duration = now - time;

      StringBuffer res = new StringBuffer();
      for(int i=0;i< times.size(); i++) {
         Long current = times.get(i);
         long temp = duration/current;
         if(temp>0) {
            res.append(temp).append(" ").append( timesString.get(i) ).append(temp != 1 ? "s" : "").append(" ago");
            break;
         }
      }
      if("".equals(res.toString()))
         return "just now";
      else
         return res.toString();
   }
   public static String toStringFormat_7(Date date) {
      if (date == null)
         return "";
      return dateToString(date, DATE_FORMAT_7);
   }
   public static String dateToString(Date date, String format) {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(date);
   }
}
