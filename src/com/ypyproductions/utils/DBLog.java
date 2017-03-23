package com.ypyproductions.utils;

import android.util.Log;

public class DBLog
{

    public static boolean LOG = true;

    public DBLog()
    {
    }

    public static void d(String s, String s1)
    {
        if(LOG)
        {
            Log.d(s, s1);
        }
    }

    public static void e(String s, String s1)
    {
        if(LOG)
        {
            Log.e(s, s1);
        }
    }

    public static void i(String s, String s1)
    {
        if(LOG)
        {
            Log.i(s, s1);
        }
    }

    public static void setDebug(boolean flag)
    {
        LOG = flag;
    }

    public static void v(String s, String s1)
    {
        if(LOG)
        {
            Log.v(s, s1);
        }
    }

    public static void w(String s, String s1)
    {
        if(LOG)
        {
            Log.w(s, s1);
        }
    }

}
