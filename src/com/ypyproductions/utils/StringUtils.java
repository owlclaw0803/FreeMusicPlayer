package com.ypyproductions.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{

    public static final String REGEX_SPECIAL_CHARACTER = "[^a-zA-Z0-9_]";
    public static final String TAG = "StringUtils";

    public StringUtils()
    {
    }

    public static String formatHtmlBoldKeyword(String s, String s1)
    {
    	try{
	        if(s == null || s1 == null || s1.equals("") || !s.contains(s1))
	            return s;
	        String s2 = s.replace(s1, (new StringBuilder("<b>")).append(s1).append("</b>").toString());
	        return s2;
    	}catch(Exception e){
    		return s;
    	}
    }

    public static float formatStringNumber(float f)
    {
        Locale locale = Locale.US;
        Object aobj[] = new Object[1];
        aobj[0] = Float.valueOf(f);
        String s = String.format(locale, "%.2f", aobj);
        float f1;
        try
        {
            f1 = Float.parseFloat(s.replace(",", "."));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return f;
        }
        return f1;
    }

    public static String getSplitString(String s, int i)
    {
        if(s != null)
        {
            if(s.length() > i)
            {
                s = (new StringBuilder(String.valueOf(s.substring(0, i)))).append("...").toString();
            }
            return s;
        } else
        {
            return null;
        }
    }

    public static boolean isContainsSpecialCharacter(String s)
    {
        return s != null && !s.equals("") && Pattern.compile("[^a-zA-Z0-9_]").matcher(s).find();
    }

    public static boolean isEmptyString(String s)
    {
        return s == null || s.equals("");
    }

    public static boolean isNumber(String s)
    {
        return s.matches("[+-]?\\d*(\\.\\d+)?");
    }

    public static String urlDecodeString(String s)
    {
    	try{
	        if(s == null || s.equals(""))
	            return s;
	        String s1 = URLDecoder.decode(s, "UTF-8");
	        return s1;
    	}catch(Exception e){
    		return s;
    	}
    }

    public static String urlEncodeString(String s)
    {
    	try{
	        if(s == null || s.equals(""))
	        	return s;
	        String s1 = URLEncoder.encode(s, "UTF-8");
	        return s1;
    	}catch(Exception e){
    		return s;
    	}
    }
}
