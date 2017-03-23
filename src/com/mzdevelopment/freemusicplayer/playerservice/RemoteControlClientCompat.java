package com.mzdevelopment.freemusicplayer.playerservice;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;

import com.ypyproductions.utils.DBLog;

import java.lang.reflect.*;

public class RemoteControlClientCompat
{
    public class MetadataEditorCompat
    {

        public static final int METADATA_KEY_ARTWORK = 100;
        private Object mActualMetadataEditor;
        private Method mApplyMethod;
        private Method mClearMethod;
        private Method mPutBitmapMethod;
        private Method mPutLongMethod;
        private Method mPutStringMethod;

        public void apply()
        {
            if(RemoteControlClientCompat.sHasRemoteControlAPIs)
				try {
					mApplyMethod.invoke(mActualMetadataEditor, (Object[])null);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }

        public void clear()
        {
            if(RemoteControlClientCompat.sHasRemoteControlAPIs)
				try {
					mClearMethod.invoke(mActualMetadataEditor, (Object[])null);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }

        public MetadataEditorCompat putBitmap(int i, Bitmap bitmap)
        {
            if(RemoteControlClientCompat.sHasRemoteControlAPIs)
            {
                try
                {
                    Method method = mPutBitmapMethod;
                    Object obj = mActualMetadataEditor;
                    Object aobj[] = new Object[2];
                    aobj[0] = Integer.valueOf(i);
                    aobj[1] = bitmap;
                    method.invoke(obj, aobj);
                }
                catch(Exception exception)
                {
                    throw new RuntimeException(exception.getMessage(), exception);
                }
            }
            return this;
        }

        public MetadataEditorCompat putLong(int i, long l)
        {
            if(RemoteControlClientCompat.sHasRemoteControlAPIs)
            {
                try
                {
                    Method method = mPutLongMethod;
                    Object obj = mActualMetadataEditor;
                    Object aobj[] = new Object[2];
                    aobj[0] = Integer.valueOf(i);
                    aobj[1] = Long.valueOf(l);
                    method.invoke(obj, aobj);
                }
                catch(Exception exception)
                {
                    throw new RuntimeException(exception.getMessage(), exception);
                }
            }
            return this;
        }

        public MetadataEditorCompat putString(int i, String s)
        {
            if(RemoteControlClientCompat.sHasRemoteControlAPIs)
            {
                try
                {
                    Method method = mPutStringMethod;
                    Object obj = mActualMetadataEditor;
                    Object aobj[] = new Object[2];
                    aobj[0] = Integer.valueOf(i);
                    aobj[1] = s;
                    method.invoke(obj, aobj);
                }
                catch(Exception exception)
                {
                    throw new RuntimeException(exception.getMessage(), exception);
                }
            }
            return this;
        }

        private MetadataEditorCompat(Object obj)
        {
            super();
            if(RemoteControlClientCompat.sHasRemoteControlAPIs && obj == null)
            {
                throw new IllegalArgumentException("Remote Control API's exist, should not be given a null MetadataEditor");
            }
            if(RemoteControlClientCompat.sHasRemoteControlAPIs)
            {
                Class class1 = obj.getClass();
                try
                {
                    Class aclass[] = new Class[2];
                    aclass[0] = Integer.TYPE;
                    aclass[1] = java.lang.String.class;
                    mPutStringMethod = class1.getMethod("putString", aclass);
                    Class aclass1[] = new Class[2];
                    aclass1[0] = Integer.TYPE;
                    aclass1[1] = android.graphics.Bitmap.class;
                    mPutBitmapMethod = class1.getMethod("putBitmap", aclass1);
                    Class aclass2[] = new Class[2];
                    aclass2[0] = Integer.TYPE;
                    aclass2[1] = Long.TYPE;
                    mPutLongMethod = class1.getMethod("putLong", aclass2);
                    mClearMethod = class1.getMethod("clear", new Class[0]);
                    mApplyMethod = class1.getMethod("apply", new Class[0]);
                }
                catch(Exception exception)
                {
                    throw new RuntimeException(exception.getMessage(), exception);
                }
            }
            mActualMetadataEditor = obj;
        }

    }


    private static final String TAG;
    private static boolean sHasRemoteControlAPIs;
    private static Method sRCCEditMetadataMethod;
    private static Method sRCCSetPlayStateMethod;
    private static Method sRCCSetTransportControlFlags;
    private static Class sRemoteControlClientClass;
    private Object mActualRemoteControlClient;

    public RemoteControlClientCompat(PendingIntent pendingintent)
    {
        if(!sHasRemoteControlAPIs)
        {
            return;
        }
        try
        {
            mActualRemoteControlClient = sRemoteControlClientClass.getConstructor(new Class[] {
                android.app.PendingIntent.class
            }).newInstance(new Object[] {
                pendingintent
            });
            return;
        }
        catch(Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    public RemoteControlClientCompat(PendingIntent pendingintent, Looper looper)
    {
        if(!sHasRemoteControlAPIs)
        {
            return;
        }
        try
        {
            mActualRemoteControlClient = sRemoteControlClientClass.getConstructor(new Class[] {
                android.app.PendingIntent.class, android.os.Looper.class
            }).newInstance(new Object[] {
                pendingintent, looper
            });
            return;
        }
        catch(Exception exception)
        {
            Log.e(TAG, (new StringBuilder()).append("Error creating new instance of ").append(sRemoteControlClientClass.getName()).toString(), exception);
        }
    }

    public static Class getActualRemoteControlClientClass(ClassLoader classloader)
        throws ClassNotFoundException
    {
        return classloader.loadClass("android.media.RemoteControlClient");
    }

    public MetadataEditorCompat editMetadata(boolean flag)
    {
        Object obj;
        if(sHasRemoteControlAPIs)
        {
            Object obj2;
            try
            {
                Method method = sRCCEditMetadataMethod;
                Object obj1 = mActualRemoteControlClient;
                Object aobj[] = new Object[1];
                aobj[0] = Boolean.valueOf(flag);
                obj2 = method.invoke(obj1, aobj);
            }
            catch(Exception exception)
            {
                throw new RuntimeException(exception);
            }
            obj = obj2;
        } else
        {
            obj = null;
        }
        return new MetadataEditorCompat(obj);
    }

    public final Object getActualRemoteControlClientObject()
    {
        return mActualRemoteControlClient;
    }

    public void setPlaybackState(int i)
    {
        if(!sHasRemoteControlAPIs)
        	return;
        Method method = sRCCSetPlayStateMethod;
        Object obj = mActualRemoteControlClient;
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(i);
        try {
			method.invoke(obj, aobj);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void setTransportControlFlags(int i)
    {
        if(!sHasRemoteControlAPIs)
        	return;
        Method method = sRCCSetTransportControlFlags;
        Object obj = mActualRemoteControlClient;
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(i);
        try {
			method.invoke(obj, aobj);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    static 
    {
        int i;
        i = 0;
        TAG = "RemoteControlClientCompat";
        sHasRemoteControlAPIs = false;
        Field afield[];
        int j;
        try
        {
        	sRemoteControlClientClass = getActualRemoteControlClientClass(com.mzdevelopment.freemusicplayer.playerservice.RemoteControlClientCompat.class.getClassLoader());
            afield = com.mzdevelopment.freemusicplayer.playerservice.RemoteControlClientCompat.class.getFields();
            j = afield.length;
            while(i < j)
            {
            	try{
    	        	Field field = afield[i];
    	            field.set(null, sRemoteControlClientClass.getField(field.getName()).get(null));
            	}catch(Exception e){}
            	i++;
            }
            Class class1 = sRemoteControlClientClass;
            Class aclass[] = new Class[1];
            aclass[0] = Boolean.TYPE;
            sRCCEditMetadataMethod = class1.getMethod("editMetadata", aclass);
            Class class2 = sRemoteControlClientClass;
            Class aclass1[] = new Class[1];
            aclass1[0] = Integer.TYPE;
            sRCCSetPlayStateMethod = class2.getMethod("setPlaybackState", aclass1);
            Class class3 = sRemoteControlClientClass;
            Class aclass2[] = new Class[1];
            aclass2[0] = Integer.TYPE;
            sRCCSetTransportControlFlags = class3.getMethod("setTransportControlFlags", aclass2);
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
