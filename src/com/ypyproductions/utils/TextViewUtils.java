package com.ypyproductions.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class TextViewUtils
{

    public static final String TAG = "TextViewUtils";

    public TextViewUtils()
    {
    }

    public static void setFont(Context context, TextView textview, String s)
    {
        if(textview == null || s == null)
        {
            (new Exception((new StringBuilder(String.valueOf(TAG))).append(" setFont: data can not null").toString())).printStackTrace();
        } else
        if(!StringUtils.isEmptyString(s))
        {
            textview.setTypeface(Typeface.createFromAsset(context.getAssets(), s));
            return;
        }
    }

    public static void setTextItalic(Context context, TextView textview, String s)
    {
        if(textview == null || s == null)
        {
            (new Exception((new StringBuilder(String.valueOf(TAG))).append(" setTextItalic: data can not null").toString())).printStackTrace();
            return;
        } else
        {
            SpannableString spannablestring = new SpannableString((new StringBuilder(String.valueOf(s))).append(" ").toString());
            spannablestring.setSpan(new StyleSpan(2), 0, spannablestring.length(), 0);
            textview.setText(spannablestring);
            return;
        }
    }

    public static void setTypeface(Context context, TextView textview, Typeface typeface)
    {
        if(textview == null || typeface == null)
        {
            (new Exception((new StringBuilder(String.valueOf(TAG))).append(" setTypeface: data can not null").toString())).printStackTrace();
            return;
        } else
        {
            textview.setTypeface(typeface);
            return;
        }
    }

}
