package com.ypyproductions.utils;

import android.graphics.*;
import java.io.*;

public class ImageProcessingUtils
{

    private static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final String TAG = "ImageProcessingUtils";

    public ImageProcessingUtils()
    {
    }

    public static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int i, int j)
    {
        int l;
label0:
        {
            int k = options.outHeight;
            l = options.outWidth;
            int i1 = 1;
            if(k > j || l > i)
            {
                if(l <= k)
                {
                    break label0;
                }
                i1 = Math.round((float)k / (float)j);
            }
            return i1;
        }
        return Math.round((float)l / (float)i);
    }

    public static byte[] convertInputStreamToArray(InputStream inputstream)
    {
        if(inputstream == null)
        	return null;
        ByteArrayOutputStream bytearrayoutputstream;
        byte abyte0[];
        bytearrayoutputstream = new ByteArrayOutputStream();
        abyte0 = new byte[4096];
        try{
	        while(true){
		        int i = inputstream.read(abyte0);
		        if(i == -1)
		        	break;
		        bytearrayoutputstream.write(abyte0, 0, i);
	        }
	        bytearrayoutputstream.flush();
	        return bytearrayoutputstream.toByteArray();
        }catch(Exception e){
        	return null;
        }
    }

    public static Bitmap decodePortraitBitmap(InputStream inputstream, int i, int j)
    {
        if(inputstream == null)
        	return null;
        Bitmap bitmap;
        try{
	        byte abyte0[] = convertInputStreamToArray(inputstream);
	        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, options);
	        options.inJustDecodeBounds = false;
	        options.inSampleSize = calculateInSampleSize(options, i, j);
	        bitmap = BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, options);
	        return bitmap;
        }catch(Exception e){
        	return null;
        }
    }

    public static Bitmap getRotatedBitmap(Bitmap bitmap)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(90F);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if(bitmap1 != null)
        {
            bitmap.recycle();
        }
        return bitmap1;
    }

}
