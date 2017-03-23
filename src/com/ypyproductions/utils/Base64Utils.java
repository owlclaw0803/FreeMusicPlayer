package com.ypyproductions.utils;

import java.io.UnsupportedEncodingException;

public class Base64Utils
{

    private static final String TAG = "Base64Utils";
    public static final String encodingChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+=";

    public Base64Utils()
    {
    }

    public static String decode(String s)
    {
    	try{
	        byte abyte0[] = Base64.decode(s.getBytes("UTF-8"));
	        if(abyte0 == null)
	        	return null;
	        String s1 = new String(abyte0);
	        return s1;
    	}catch(Exception e){
    		return null;
    	}
    }

    public static String encode(String s)
    {
        String s1;
        try
        {
            s1 = Base64.encode(s.getBytes("UTF-8"));
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            DBLog.d(TAG, (new StringBuilder("EncodeBase64 Error")).append(unsupportedencodingexception.getMessage()).toString());
            unsupportedencodingexception.printStackTrace();
            return null;
        }
        return s1;
    }
}
