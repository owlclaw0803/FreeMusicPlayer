package com.ypyproductions.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MediaUtils
{

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String TAG = "MediaUtils";

    public MediaUtils()
    {
    }

    public static boolean checkCameraHardware(Context context)
    {
        return context.getPackageManager().hasSystemFeature("android.hardware.camera");
    }

    public static int findBackCamera()
    {
        int i = Camera.getNumberOfCameras();
        int j = 0;
        do
        {
            if(j >= i)
            {
                return -1;
            }
            android.hardware.Camera.CameraInfo camerainfo = new android.hardware.Camera.CameraInfo();
            Camera.getCameraInfo(j, camerainfo);
            if(camerainfo.facing == 0)
            {
                return j;
            }
            j++;
        } while(true);
    }

    public static int findFrontFacingCamera()
    {
        int i = Camera.getNumberOfCameras();
        int j = 0;
        do
        {
            if(j >= i)
            {
                return -1;
            }
            android.hardware.Camera.CameraInfo camerainfo = new android.hardware.Camera.CameraInfo();
            Camera.getCameraInfo(j, camerainfo);
            if(camerainfo.facing == 1)
            {
                return j;
            }
            j++;
        } while(true);
    }

    public static Camera getCameraInstance()
    {
        Camera camera;
        try
        {
            camera = Camera.open();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
        return camera;
    }

    public static File getOutputMediaFile(int i, String s)
    {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), s);
        if(!file.exists())
        {
            file.mkdirs();
        }
        String s1 = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())).format(new Date());
        if(i == 1)
        {
            return new File((new StringBuilder(String.valueOf(file.getPath()))).append(File.separator).append("IMG_").append(s1).append(".jpg").toString());
        }
        if(i == 2)
        {
            return new File((new StringBuilder(String.valueOf(file.getPath()))).append(File.separator).append("VID_").append(s1).append(".mp4").toString());
        } else
        {
            return null;
        }
    }

    public static Uri getOutputMediaFileUri(int i, String s)
    {
        return Uri.fromFile(getOutputMediaFile(i, s));
    }

    public static boolean isSupportFlashlight(Context context)
    {
        return context.getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

}
