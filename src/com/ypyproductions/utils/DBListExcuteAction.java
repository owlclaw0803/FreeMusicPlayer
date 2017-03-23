package com.ypyproductions.utils;

import com.ypyproductions.task.IDBCallback;
import java.util.Stack;

public class DBListExcuteAction
{
    protected class ActionLoaderThread extends Thread
    {
        public void run()
        {
        	try{
	        	while(true){
		            if(!DBListExcuteAction.isRunning)
		                return;
		            int i = DBListExcuteAction.mListDBCallback.size();
		            if(i != 0){
		            	if(i > 0){
		    	            Stack stack1 = DBListExcuteAction.mListDBCallback;
		    	            synchronized(stack1){
		    		            boolean flag1 = DBListExcuteAction.mListDBCallback.isEmpty();
		    		            IDBCallback idbcallback;
		    		            idbcallback = null;
		    		            if(!flag1)
		    		            	idbcallback = (IDBCallback)DBListExcuteAction.mListDBCallback.pop();
		    		            if(idbcallback != null)
			    	            	idbcallback.onAction();
		    	            }
		                }
		            }else{
			            synchronized(DBListExcuteAction.mListDBCallback)
			            {
			                DBListExcuteAction.mListDBCallback.wait();
			            }
		            }
		            boolean flag = Thread.interrupted();
		            if(flag)
		            	return;
	        	}
        	}catch(Exception e){}
        }

        protected ActionLoaderThread()
        {
            super();
        }
    }


    public static final String TAG = "DBListExcuteAction";
    private static boolean isRunning = false;
    private static ActionLoaderThread mActionLoaderThread;
    private static Stack mListDBCallback;
    public static DBListExcuteAction mListExcuteAction;

    private DBListExcuteAction()
    {
        mListDBCallback = new Stack();
        mActionLoaderThread = new ActionLoaderThread();
        mActionLoaderThread.setPriority(4);
    }

    public static DBListExcuteAction getInstance()
    {
        if(mListExcuteAction == null)
        {
            mListExcuteAction = new DBListExcuteAction();
        }
        return mListExcuteAction;
    }

    public void onDestroy()
    {
        if(mListDBCallback != null)
        {
            synchronized(mListDBCallback)
            {
                mListDBCallback.clear();
                mListDBCallback = null;
            }
        }
        if(mActionLoaderThread != null)
        {
            isRunning = false;
            mActionLoaderThread.interrupt();
            mActionLoaderThread = null;
        }
        if(mListExcuteAction != null)
        {
            mListExcuteAction = null;
        }
    }

    public void queueAction(IDBCallback idbcallback)
    {
        if(mListDBCallback != null){
        	synchronized(mListDBCallback)
            {
                mListDBCallback.push(idbcallback);
                mListDBCallback.notifyAll();
            }
            if(mActionLoaderThread.getState() != Thread.State.NEW)
            	return;
            isRunning = true;
            mActionLoaderThread.start();
        }
    }
}
