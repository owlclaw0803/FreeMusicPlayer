package com.ypyproductions.utils;

import android.content.Context;
import android.content.res.Resources;

public class ResourceUtils
{

    public static final int RESOURCE_ERROR = 0;
    private static final String TAG = "ResourceUtils";

    public ResourceUtils()
    {
    }

    public static int getIdMenuByName(Context context, String s)
    {
        int i = 0;
        if(s == null)
        {
            DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
        } else
        {
            if(s.equals(""))
            {
                DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
                return 0;
            }
            String s1 = context.getPackageName();
            i = context.getResources().getIdentifier(s, "menu", s1);
            if(i == 0)
            {
                DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
                return i;
            }
        }
        return i;
    }

    public static int getIdStringArrayByName(Context context, String s)
    {
        int i = 0;
        if(s == null)
        {
            DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
        } else
        {
            if(s.equals(""))
            {
                DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
                return 0;
            }
            String s1 = context.getPackageName();
            i = context.getResources().getIdentifier(s, "string-array", s1);
            if(i == 0)
            {
                DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
                return i;
            }
        }
        return i;
    }

    public static int getIdStringByName(Context context, String s)
    {
        int i = 0;
        if(s == null)
        {
            DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
        } else
        {
            if(s.equals(""))
            {
                DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
                return 0;
            }
            String s1 = context.getPackageName();
            i = context.getResources().getIdentifier(s, "string", s1);
            if(i == 0)
            {
                DBLog.e(TAG, (new StringBuilder("------>RESOURCE ")).append(s).append(" error").toString());
                return i;
            }
        }
        return i;
    }

}
