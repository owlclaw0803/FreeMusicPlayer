package com.ypyproductions.webservice;

import com.ypyproductions.utils.DBLog;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadUtils
{

    private static final String TAG = "DownloadUtils";
    private static final Pattern URI_FILENAME_PATTERN = Pattern.compile("[^/]+$");

    public DownloadUtils()
    {
    }

    public static String convertStreamToString(InputStream inputstream)
    {
        BufferedReader bufferedreader;
        StringBuilder stringbuilder;
        try{
	        bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
	        stringbuilder = new StringBuilder();
	        while(true){
		        String s = bufferedreader.readLine();
		        if(s == null)
		        	break;
		        stringbuilder.append(s);
	        }
	        inputstream.close();
	        return stringbuilder.toString();
        }catch(Exception e){
        	return null;
        }
    }

    public static InputStream download(String s)
    {
        HttpURLConnection httpurlconnection;
        int i;
        try{
	        httpurlconnection = (HttpURLConnection)(new URL(s)).openConnection();
	        httpurlconnection.setReadTimeout(10000);
	        httpurlconnection.setConnectTimeout(15000);
	        httpurlconnection.setRequestMethod("GET");
	        httpurlconnection.setDoInput(true);
	        httpurlconnection.setUseCaches(false);
	        httpurlconnection.connect();
	        i = httpurlconnection.getResponseCode();
	        DBLog.d(TAG, (new StringBuilder("The response is: ")).append(i).toString());
	        if(i != 200)
	        	return null;
	        InputStream inputstream = httpurlconnection.getInputStream();
	        return inputstream;
        }catch(Exception e){
        	return null;
        }
    }

    public static String downloadString(String s)
    {
        HttpURLConnection httpurlconnection;
        int i;
        try{
	        httpurlconnection = (HttpURLConnection)(new URL(s)).openConnection();
	        httpurlconnection.setReadTimeout(10000);
	        httpurlconnection.setConnectTimeout(15000);
	        httpurlconnection.setRequestMethod("GET");
	        httpurlconnection.setDoInput(true);
	        httpurlconnection.connect();
	        i = httpurlconnection.getResponseCode();
	        DBLog.d(TAG, (new StringBuilder("The response of is: ")).append(i).toString());
	        if(i != 200)
	            return null;
	        String s1 = convertStreamToString(httpurlconnection.getInputStream());
	        return s1;
        }catch(Exception e){
        	return null;
        }
    }

    public static String getFileName(String s)
    {
        Matcher matcher = URI_FILENAME_PATTERN.matcher(s);
        if(!matcher.find())
        {
            throw new IllegalArgumentException("uri");
        } else
        {
            return matcher.group();
        }
    }
}
