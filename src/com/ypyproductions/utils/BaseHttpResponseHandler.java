package com.ypyproductions.utils;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

public abstract class BaseHttpResponseHandler
    implements ResponseHandler
{

    public BaseHttpResponseHandler()
    {
    }

    public Object handleResponse(HttpResponse httpresponse)
        throws ClientProtocolException, IOException
    {
        if(httpresponse.getStatusLine().getStatusCode() >= 400)
        {
            throw new IOException();
        } else
        {
            return handleResponseInternal(httpresponse);
        }
    }

    protected abstract Object handleResponseInternal(HttpResponse httpresponse)
        throws IOException;
}
