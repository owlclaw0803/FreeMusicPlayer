package com.mzdevelopment.freemusicplayer.playerservice;

import android.content.ComponentName;
import android.media.AudioManager;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MediaButtonHelper
{

    private static final String TAG = "MediaButtonHelper";
    static Method sMethodRegisterMediaButtonEventReceiver;
    static Method sMethodUnregisterMediaButtonEventReceiver;

    public MediaButtonHelper()
    {
    }

    static void initializeStaticCompatMethods()
    {
        try
        {
            sMethodRegisterMediaButtonEventReceiver = android.media.AudioManager.class.getMethod("registerMediaButtonEventReceiver", new Class[] {
                android.content.ComponentName.class
            });
            sMethodUnregisterMediaButtonEventReceiver = android.media.AudioManager.class.getMethod("unregisterMediaButtonEventReceiver", new Class[] {
                android.content.ComponentName.class
            });
            return;
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            nosuchmethodexception.printStackTrace();
        }
    }

    public static void registerMediaButtonEventReceiverCompat(AudioManager audiomanager, ComponentName componentname)
    {
        if(sMethodRegisterMediaButtonEventReceiver == null)
        {
            return;
        }
        try
        {
            sMethodRegisterMediaButtonEventReceiver.invoke(audiomanager, new Object[] {
                componentname
            });
            return;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            Throwable throwable = invocationtargetexception.getCause();
            if(throwable instanceof RuntimeException)
            {
                throw (RuntimeException)throwable;
            }
            if(throwable instanceof Error)
            {
                throw (Error)throwable;
            } else
            {
                throw new RuntimeException(invocationtargetexception);
            }
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            Log.e("MediaButtonHelper", "IllegalAccessException invoking registerMediaButtonEventReceiver.");
            illegalaccessexception.printStackTrace();
            return;
        }
    }

    public static void unregisterMediaButtonEventReceiverCompat(AudioManager audiomanager, ComponentName componentname)
    {
        if(sMethodUnregisterMediaButtonEventReceiver == null)
        {
            return;
        }
        try
        {
            sMethodUnregisterMediaButtonEventReceiver.invoke(audiomanager, new Object[] {
                componentname
            });
            return;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            Throwable throwable = invocationtargetexception.getCause();
            if(throwable instanceof RuntimeException)
            {
                throw (RuntimeException)throwable;
            }
            if(throwable instanceof Error)
            {
                throw (Error)throwable;
            } else
            {
                throw new RuntimeException(invocationtargetexception);
            }
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            Log.e("MediaButtonHelper", "IllegalAccessException invoking unregisterMediaButtonEventReceiver.");
            illegalaccessexception.printStackTrace();
            return;
        }
    }

    static 
    {
        initializeStaticCompatMethods();
    }
}
