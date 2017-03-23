package com.ypyproductions.utils;

import android.text.format.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils
{

    public static final String DAY_ENGLISH[] = {
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };
    public static final String MONTH_ENGLISH[] = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", 
        "Nov", "Dec"
    };
    public static final String MONTH_VIET_NAM[] = {
        "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", 
        "T11", "T12"
    };
    public static final String TAG = "DateTimeUtils";
    public static final int TIME_ENGLISH = 2;
    public static final int TIME_VIET_NAM = 1;

    public DateTimeUtils()
    {
    }

    public static String convertDateToString(Date date, String s)
    {
        if(date == null || s == null)
        {
            return null;
        } else
        {
            return (new SimpleDateFormat(s)).format(date);
        }
    }

    public static String convertMilliToStrDate(long l, String s)
    {
        if(l > 0L)
        {
            return DateFormat.format(s, l).toString();
        } else
        {
            return null;
        }
    }

    public static String getCurrentDate(String s)
    {
        Date date = new Date();
        return (new SimpleDateFormat(s)).format(date);
    }

    public static Date getDateFromString(String s, String s1)
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s1);
        Date date;
        try
        {
            date = simpledateformat.parse(s);
        }
        catch(ParseException parseexception)
        {
            parseexception.printStackTrace();
            return null;
        }
        return date;
    }

    public static String getFullDate()
    {
        Calendar calendar = Calendar.getInstance();
        String s = String.valueOf(calendar.get(12));
        if(s.length() == 1)
        {
            s = (new StringBuilder("0")).append(s).toString();
        }
        String s1 = String.valueOf(calendar.get(11));
        if(s1.length() == 1)
        {
            s1 = (new StringBuilder("0")).append(s1).toString();
        }
        String s2 = String.valueOf(calendar.get(1));
        int i = calendar.get(2);
        int j = calendar.get(7);
        String s3 = MONTH_ENGLISH[i];
        String s4 = DAY_ENGLISH[j - 1];
        String s5 = String.valueOf(calendar.get(5));
        if(s5.length() == 1)
        {
            s5 = (new StringBuilder("0")).append(s5).toString();
        }
        return (new StringBuilder(String.valueOf(s4))).append(" ").append(s5).append("-").append(s3).append("-").append(s2).append(" ").append(s1).append(":").append(s).toString();
    }

    public static String getStringDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String s = String.valueOf(calendar.get(1));
        int i = calendar.get(2);
        int j = calendar.get(7);
        String s1 = DAY_ENGLISH[j - 1];
        String s2 = String.valueOf(calendar.get(5));
        String s3 = MONTH_ENGLISH[i];
        return (new StringBuilder(String.valueOf(s1))).append(", ").append(s3).append(" ").append(s2).append(", ").append(s).toString();
    }

    public static String getStringHours(Date date)
    {
        Calendar calendar;
        String s;
        String s1;
        String s2;
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        s = String.valueOf(calendar.get(11));
        s1 = String.valueOf(calendar.get(12));
        s2 = "";
        if(calendar.get(9) != 0){
        	if(calendar.get(9) == 1)
                s2 = "PM";
        }else
        	s2 = "AM";
        if(s.length() == 1)
        {
            s = (new StringBuilder("0")).append(s).toString();
        }
        if(s1.length() == 1)
        {
            s1 = (new StringBuilder("0")).append(s1).toString();
        }
        return (new StringBuilder(String.valueOf(s))).append(":").append(s1).append(" ").append(s2).toString();
    }

}
