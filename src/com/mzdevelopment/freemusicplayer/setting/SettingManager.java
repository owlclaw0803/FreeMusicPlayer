package com.mzdevelopment.freemusicplayer.setting;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingManager
    implements ISettingConstants
{

    public static final String DOBAO_SHARPREFS = "dobao_prefs";
    public static final String TAG = com.mzdevelopment.freemusicplayer.setting.SettingManager.class.getSimpleName();

    public SettingManager()
    {
    }

    public static boolean getEqualizer(Context context)
    {
        return Boolean.parseBoolean(getSetting(context, "equalizer", "false"));
    }

    public static String getEqualizerParams(Context context)
    {
        return getSetting(context, "equalizer_param", "");
    }

    public static String getEqualizerPreset(Context context)
    {
        return getSetting(context, "equalizer_preset", "0");
    }

    public static boolean getFirstTime(Context context)
    {
        return Boolean.parseBoolean(getSetting(context, "first_time", "false"));
    }

    public static String getLanguage(Context context)
    {
        return getSetting(context, "langue", "VN");
    }

    public static String getLastKeyword(Context context)
    {
        return getSetting(context, "last_keyword", "");
    }

    public static boolean getOnline(Context context)
    {
        return Boolean.parseBoolean(getSetting(context, "online", "false"));
    }

    public static boolean getPlayingState(Context context)
    {
        return Boolean.parseBoolean(getSetting(context, "state", "false"));
    }

    public static boolean getRepeat(Context context)
    {
        return Boolean.parseBoolean(getSetting(context, "repeat", "false"));
    }

    public static int getSearchType(Context context)
    {
        return Integer.parseInt(getSetting(context, "type", String.valueOf(1)));
    }

    public static String getSetting(Context context, String s, String s1)
    {
        return context.getSharedPreferences("dobao_prefs", 0).getString(s, s1);
    }

    public static boolean getShuffle(Context context)
    {
        return Boolean.parseBoolean(getSetting(context, "shuffle", "false"));
    }

    public static void saveSetting(Context context, String s, String s1)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("dobao_prefs", 0).edit();
        editor.putString(s, s1);
        editor.commit();
    }

    public static void setEqualizer(Context context, boolean flag)
    {
        saveSetting(context, "equalizer", String.valueOf(flag));
    }

    public static void setEqualizerParams(Context context, String s)
    {
        saveSetting(context, "equalizer_param", s);
    }

    public static void setEqualizerPreset(Context context, String s)
    {
        saveSetting(context, "equalizer_preset", s);
    }

    public static void setFirstTime(Context context, boolean flag)
    {
        saveSetting(context, "first_time", String.valueOf(flag));
    }

    public static void setLanguage(Context context, String s)
    {
        saveSetting(context, "langue", s);
    }

    public static void setLastKeyword(Context context, String s)
    {
        saveSetting(context, "last_keyword", s);
    }

    public static void setOnline(Context context, boolean flag)
    {
        saveSetting(context, "online", String.valueOf(flag));
    }

    public static void setPlayingState(Context context, boolean flag)
    {
        saveSetting(context, "state", String.valueOf(flag));
    }

    public static void setRepeat(Context context, boolean flag)
    {
        saveSetting(context, "repeat", String.valueOf(flag));
    }

    public static void setSearchType(Context context, int i)
    {
        saveSetting(context, "type", String.valueOf(i));
    }

    public static void setShuffle(Context context, boolean flag)
    {
        saveSetting(context, "shuffle", String.valueOf(flag));
    }

}
