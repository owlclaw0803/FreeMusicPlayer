package com.ypyproductions.task;

import android.os.AsyncTask;

public class DBTask extends AsyncTask
{

    private IDBTaskListener mDownloadListener;

    public DBTask(IDBTaskListener idbtasklistener)
    {
        mDownloadListener = idbtasklistener;
    }

    protected Object doInBackground(Object aobj[])
    {
        return doInBackground((Void[])aobj);
    }

    protected Void doInBackground(Void avoid[])
    {
        if(mDownloadListener != null)
        {
            mDownloadListener.onDoInBackground();
        }
        return null;
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((Void)obj);
    }

    protected void onPostExecute(Void void1)
    {
        if(mDownloadListener != null)
        {
            mDownloadListener.onPostExcute();
        }
    }

    protected void onPreExecute()
    {
        if(mDownloadListener != null)
        {
            mDownloadListener.onPreExcute();
        }
    }
}
