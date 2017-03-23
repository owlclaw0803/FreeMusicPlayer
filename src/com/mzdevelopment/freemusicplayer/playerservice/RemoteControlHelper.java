package com.mzdevelopment.freemusicplayer.playerservice;

import android.media.AudioManager;
import android.util.Log;
import java.lang.reflect.Method;

public class RemoteControlHelper
{

    private static final String TAG = "RemoteControlHelper";
    private static boolean sHasRemoteControlAPIs = false;
    private static Method sRegisterRemoteControlClientMethod;
    private static Method sUnregisterRemoteControlClientMethod;

    public RemoteControlHelper()
    {
    }

    public static void registerRemoteControlClient(AudioManager audiomanager, RemoteControlClientCompat remotecontrolclientcompat)
    {
        if(!sHasRemoteControlAPIs)
        {
            return;
        }
        try
        {
            Method method = sRegisterRemoteControlClientMethod;
            Object aobj[] = new Object[1];
            aobj[0] = remotecontrolclientcompat.getActualRemoteControlClientObject();
            method.invoke(audiomanager, aobj);
            return;
        }
        catch(Exception exception)
        {
            Log.e("RemoteControlHelper", exception.getMessage(), exception);
        }
    }

    public static void unregisterRemoteControlClient(AudioManager audiomanager, RemoteControlClientCompat remotecontrolclientcompat)
    {
        if(!sHasRemoteControlAPIs)
        {
            return;
        }
        try
        {
            Method method = sUnregisterRemoteControlClientMethod;
            Object aobj[] = new Object[1];
            aobj[0] = remotecontrolclientcompat.getActualRemoteControlClientObject();
            method.invoke(audiomanager, aobj);
            return;
        }
        catch(Exception exception)
        {
            Log.e("RemoteControlHelper", exception.getMessage(), exception);
        }
    }

    static 
    {
        try
        {
            Class class1 = RemoteControlClientCompat.getActualRemoteControlClientClass(com.mzdevelopment.freemusicplayer.playerservice.RemoteControlHelper.class.getClassLoader());
            sRegisterRemoteControlClientMethod = android.media.AudioManager.class.getMethod("registerRemoteControlClient", new Class[] {
                class1
            });
            sUnregisterRemoteControlClientMethod = android.media.AudioManager.class.getMethod("unregisterRemoteControlClient", new Class[] {
                class1
            });
            sHasRemoteControlAPIs = true;
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            classnotfoundexception.printStackTrace();
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            nosuchmethodexception.printStackTrace();
        }
        catch(IllegalArgumentException illegalargumentexception)
        {
            illegalargumentexception.printStackTrace();
        }
        catch(SecurityException securityexception)
        {
            securityexception.printStackTrace();
        }
    }
}
