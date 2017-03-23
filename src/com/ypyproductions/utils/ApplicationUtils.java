package com.ypyproductions.utils;

import android.content.Context;
import android.content.pm.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ApplicationUtils
{

    public ApplicationUtils()
    {
    }

    public static String getDeviceId(Context context)
    {
        String s = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
        if(s == null || s.equals("0"))
        {
            s = android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
        }
        return s;
    }

    public static String getHashKey(Context context)
    {
        String s;
        try{
	        Signature asignature[] = context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures;
	        if(asignature.length == 0)
	        	return null;
	        Signature signature = asignature[0];
	        MessageDigest messagedigest = MessageDigest.getInstance("SHA");
	        messagedigest.update(signature.toByteArray());
	        s = Base64.encodeToString(messagedigest.digest(), 0);
	        return s;
        }catch(Exception e){
        	return null;
        }
    }

    public static String getMd5Hash(String s)
    {
    	try{
	        String s1 = (new BigInteger(1, MessageDigest.getInstance("MD5").digest(s.getBytes()))).toString(16);
	        do
	        {
	            if(s1.length() >= 32)
	            {
	                return s1;
	            }
	            String s2;
	            s2 = (new StringBuilder("0")).append(s1).toString();
	            s1 = s2;
	        } while(true);
    	}catch(Exception e){
    		return null;
    	}
    }

    public static String getNameApp(Context context)
    {
    	android.content.pm.ApplicationInfo applicationinfo;
        PackageManager packagemanager = context.getPackageManager();
        try{
	        android.content.pm.ApplicationInfo applicationinfo1 = packagemanager.getApplicationInfo(context.getPackageName(), 0);
	        applicationinfo = applicationinfo1;
        }catch(Exception e){
        	applicationinfo = null;
        }
        android.content.pm.PackageManager.NameNotFoundException namenotfoundexception;
        Object obj;
        if(applicationinfo != null)
        {
            obj = packagemanager.getApplicationLabel(applicationinfo);
        } else
        {
            obj = "(unknown)";
        }
        return (String)obj;
    }

    public static String getSignature(Context context)
    {
        String s;
        try
        {
            s = context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures[0].toString();
        }
        catch(android.content.pm.PackageManager.NameNotFoundException namenotfoundexception)
        {
            namenotfoundexception.printStackTrace();
            return null;
        }
        return s;
    }

    public static int getVersionCode(Context context)
    {
        int i;
        try
        {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch(android.content.pm.PackageManager.NameNotFoundException namenotfoundexception)
        {
            namenotfoundexception.printStackTrace();
            return 0;
        }
        return i;
    }

    public static String getVersionName(Context context)
    {
        String s;
        try
        {
            s = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch(android.content.pm.PackageManager.NameNotFoundException namenotfoundexception)
        {
            namenotfoundexception.printStackTrace();
            return null;
        }
        return s;
    }

    public static boolean hasSDcard()
    {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static void hiddenVirtualKeyboard(Context context, View view)
    {
        try
        {
            ((InputMethodManager)context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static boolean isOnline(Context context)
    {
        NetworkInfo networkinfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnectedOrConnecting();
    }

    public static void showVirtualKeyboad(Context context, EditText edittext)
    {
        try
        {
            ((InputMethodManager)context.getSystemService("input_method")).showSoftInput(edittext, 1);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
