package com.ypyproductions.webservice;

import com.ypyproductions.utils.DBLog;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

public class RestClient
{
    public enum RequestMethod
    {
    	GET("GET", 0),
        POST("POST", 1);
        static 
        {
            RequestMethod arequestmethod[] = new RequestMethod[2];
            arequestmethod[0] = GET;
            arequestmethod[1] = POST;
        }

        private RequestMethod(String s, int i)
        {
        }
    }


    private static int $SWITCH_TABLE$com$ypyproductions$webservice$RestClient$RequestMethod[];
    private static final String TAG = "RestClient";
    private static final int TIMEOUT_CONNECTION = 3000;
    private static final int TIMEOUT_SOCKET = 5000;
    private ArrayList headers;
    private InputStream inputStreamRes;
    private String message;
    private ArrayList params;
    private String response;
    private int responseCode;
    private String url;

    public RestClient(String s)
    {
        url = s;
        params = new ArrayList();
        headers = new ArrayList();
    }

    private String convertStreamToString(InputStream inputstream)
    {
        BufferedReader bufferedreader;
        StringBuilder stringbuilder;
        bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        stringbuilder = new StringBuilder();
        try
        {
        	while(true){
    	        String s = bufferedreader.readLine();
    	        if(s == null)
    	        	break;
    	        stringbuilder.append((new StringBuilder(String.valueOf(s))).append("\n").toString());
            }
            inputstream.close();
        }
        catch(IOException ioexception3)
        {
            ioexception3.printStackTrace();
        }
        return stringbuilder.toString();
    }

    private void executeRequest(HttpUriRequest httpurirequest, boolean flag)
    {
        DefaultHttpClient defaulthttpclient;
        BasicHttpParams basichttpparams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basichttpparams, 3000);
        HttpConnectionParams.setSoTimeout(basichttpparams, 5000);
        defaulthttpclient = new DefaultHttpClient(basichttpparams);
        HttpEntity httpentity;
        try
        {
        	HttpResponse httpresponse = defaulthttpclient.execute(httpurirequest);
            responseCode = httpresponse.getStatusLine().getStatusCode();
            DBLog.d(TAG, (new StringBuilder("================>responseCode=")).append(responseCode).toString());
            httpentity = httpresponse.getEntity();
            if(httpentity == null)
            	return;
            InputStream inputstream = httpentity.getContent();
            if(!flag)
            {
            	response = convertStreamToString(inputstream);
                inputstream.close();
            }else{
    	        inputStreamRes = inputstream;
            }
        }
        catch(ClientProtocolException clientprotocolexception)
        {
            defaulthttpclient.getConnectionManager().shutdown();
            clientprotocolexception.printStackTrace();
            return;
        }
        catch(IOException ioexception)
        {
            defaulthttpclient.getConnectionManager().shutdown();
            ioexception.printStackTrace();
        }
    }

    public void addHeader(String s, String s1)
    {
        headers.add(new BasicNameValuePair(s, s1));
    }

    public void addParams(String s, String s1)
    {
        params.add(new BasicNameValuePair(s, s1));
    }

    public void excute(RequestMethod requestmethod, boolean flag)
        throws UnsupportedEncodingException
    {
    	String s = "";
    	int i;
        int j;
        switch(requestmethod){
        default:
        	return;
        case GET:
        	if(params.isEmpty())
            	return;
            i = params.size();
            j = 0;
            NameValuePair namevaluepair2;
            String s1;
            while(j < i)
            {
            	namevaluepair2 = (NameValuePair)params.get(j);
                s1 = (new StringBuilder(String.valueOf(namevaluepair2.getName()))).append("=").append(URLEncoder.encode(namevaluepair2.getValue(), "UTF-8")).toString();
                if(j > 0)
                {
                    s = (new StringBuilder(String.valueOf(s))).append("&").append(s1).toString();
                } else
                {
                    s = (new StringBuilder(String.valueOf(s))).append("&").append(s1).toString();
                }
                j++;
                
            }
            HttpGet httpget = new HttpGet((new StringBuilder(String.valueOf(url))).append(s).toString());
            Iterator iterator1 = headers.iterator();
            do
            {
                if(!iterator1.hasNext())
                {
                    DBLog.d(TAG, (new StringBuilder("---->URL=")).append(url).append(s).toString());
                    executeRequest(httpget, flag);
                    return;
                }
                NameValuePair namevaluepair1 = (NameValuePair)iterator1.next();
                httpget.addHeader(namevaluepair1.getName(), namevaluepair1.getValue());
            } while(true);
        case POST:
        	HttpPost httppost = new HttpPost(url);
            Iterator iterator = headers.iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    if(!params.isEmpty())
                    {
                        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    }
                    executeRequest(httppost, flag);
                    return;
                }
                NameValuePair namevaluepair = (NameValuePair)iterator.next();
                httppost.addHeader(namevaluepair.getName(), namevaluepair.getValue());
            } while(true);
        }
    }

    public InputStream getInputStreamRes()
    {
        return inputStreamRes;
    }

    public String getMessage()
    {
        return message;
    }

    public String getResponse()
    {
        return response;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public String getUrl()
    {
        return url;
    }

    public void setInputStreamRes(InputStream inputstream)
    {
        inputStreamRes = inputstream;
    }

    public void setMessage(String s)
    {
        message = s;
    }

    public void setResponse(String s)
    {
        response = s;
    }

    public void setResponseCode(int i)
    {
        responseCode = i;
    }

    public void setUrl(String s)
    {
        url = s;
    }

}
