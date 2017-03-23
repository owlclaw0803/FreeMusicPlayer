package com.ypyproductions.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ResolutionUtils
{

    public static final String LANSCAPE = "Landscape";
    public static final String PORTRAIT = "Portrait";

    public ResolutionUtils()
    {
    }

    public static float convertDpToPixel(Context context, float f)
    {
        return f * ((float)context.getResources().getDisplayMetrics().densityDpi / 160F);
    }

    public static float convertPixelsToDp(Context context, float f)
    {
        return f / ((float)context.getResources().getDisplayMetrics().densityDpi / 160F);
    }

    public static float convertPixelsToSp(Context context, float f)
    {
        return f / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static float convertSpToPixel(Context context, float f)
    {
        return f * (context.getResources().getDisplayMetrics().scaledDensity / 160F);
    }

    public static int[] getDeviceResolution(Activity activity)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int i = display.getWidth();
        int j = display.getHeight();
        int k = activity.getResources().getConfiguration().orientation;
        int ai[];
        if(k == 1)
        {
            ai = new int[2];
            int j1;
            int k1;
            if(j >= i)
            {
                j1 = i;
            } else
            {
                j1 = j;
            }
            if(j <= i)
            {
                k1 = i;
            } else
            {
                k1 = j;
            }
            ai[0] = j1;
            ai[1] = k1;
        } else
        {
            ai = null;
            if(k == 2)
            {
                int ai1[] = new int[2];
                int l;
                int i1;
                if(j <= i)
                {
                    l = i;
                } else
                {
                    l = j;
                }
                if(j >= i)
                {
                    i1 = i;
                } else
                {
                    i1 = j;
                }
                ai1[0] = l;
                ai1[1] = i1;
                return ai1;
            }
        }
        return ai;
    }
}
