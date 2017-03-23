package com.ypyproductions.utils;

import java.io.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class StringResponseHandler extends BaseHttpResponseHandler
{

    public StringResponseHandler()
    {
    }

    protected String handleResponseInternal(HttpResponse httpresponse)
        throws IOException
    {
        InputStream inputstream = null;
        BufferedReader bufferedreader1;
        try{
	        inputstream = httpresponse.getEntity().getContent();
	        bufferedreader1 = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"), 8);
	        StringBuilder stringbuilder = new StringBuilder();
	        while(true){
		        String s = bufferedreader1.readLine();
		        if(s == null)
		        	break;
		        stringbuilder.append((new StringBuilder(String.valueOf(s))).append("\n").toString());
	        }
	        String s1 = stringbuilder.toString();
	        if(bufferedreader1 != null)
	        {
	            bufferedreader1.close();
	        }
	        if(inputstream != null)
	        {
	            inputstream.close();
	        }
	        return s1;
        }catch(Exception e){
        	return null;
        }
    }
}
