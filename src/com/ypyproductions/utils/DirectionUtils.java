package com.ypyproductions.utils;

import android.app.Activity;
import android.content.Intent;

public class DirectionUtils
{

    public DirectionUtils()
    {
    }

    public static void changeActivity(Activity activity, int i, int j, boolean flag, Intent intent)
    {
        if(activity != null && intent != null)
        {
            activity.startActivity(intent);
            activity.overridePendingTransition(i, j);
            if(flag)
            {
                activity.finish();
                return;
            }
        }
    }
}
