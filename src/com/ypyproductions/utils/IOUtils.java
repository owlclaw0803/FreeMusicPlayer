package com.ypyproductions.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import java.io.*;

public class IOUtils
{

    private static final String TAG = "IOUtils";

    public IOUtils()
    {
    }

    public static void clearCache(Context context, String s)
    {
        File file;
        if(ApplicationUtils.hasSDcard())
        {
            file = new File(Environment.getExternalStorageDirectory(), s);
        } else
        {
            file = context.getCacheDir();
        }
        if(!file.exists())
        {
            return;
        } else
        {
            deleteAllFileInDirectory(file);
            return;
        }
    }

    public static void copyStream(InputStream inputstream, OutputStream outputstream)
    {
        byte abyte0[] = new byte[1024];
        try{
	        while(true){
		        int i = inputstream.read(abyte0, 0, 1024);
		        if(i == -1)
		            return;
		        outputstream.write(abyte0, 0, i);
	        }
        }catch(Exception e){}
    }

    public static void deleteAllFileInDirectory(File file)
    {
        if(file.exists()){
        	File afile[] = file.listFiles();
            int i = afile.length;
            if(i > 0)
            {
                int j = 0;
                while(j < i) 
                {
                    afile[j].delete();
                    j++;
                }
            }
        }
    }

    public static File getDiskCacheDir(Context context, String s)
    {
        String s1;
        try{
	        if("mounted".equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable())
	        {
	            s1 = getExternalCacheDir(context).getPath();
	        } else
	        {
	            s1 = context.getCacheDir().getPath();
	        }
	        return new File((new StringBuilder(String.valueOf(s1))).append(File.separator).append(s).toString());
        }catch(Exception e){
        	return null;
        }
    }

    public static File getExternalCacheDir(Context context)
    {
        if(hasFroyo())
        {
            return context.getExternalCacheDir();
        } else
        {
            String s = (new StringBuilder("/Android/data/")).append(context.getPackageName()).append("/cache/").toString();
            return new File((new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath()))).append(s).toString());
        }
    }

    public static boolean hasFroyo()
    {
        return android.os.Build.VERSION.SDK_INT >= 8;
    }

    public static boolean hasGingerbread()
    {
        return android.os.Build.VERSION.SDK_INT >= 9;
    }

    public static boolean hasHoneycomb()
    {
        return android.os.Build.VERSION.SDK_INT >= 11;
    }

    public static boolean hasHoneycombMR1()
    {
        return android.os.Build.VERSION.SDK_INT >= 12;
    }

    public static boolean hasJellyBean()
    {
        return android.os.Build.VERSION.SDK_INT >= 16;
    }

    public static boolean isExternalStorageRemovable()
    {
        if(hasGingerbread())
        {
            return Environment.isExternalStorageRemovable();
        } else
        {
            return true;
        }
    }

    public static String readString(Context context, String s, String s1)
    {
        DataInputStream datainputstream;
        BufferedReader bufferedreader;
        StringBuilder stringbuilder;
        File file = new File(s, s1);
        if(!file.exists())
        	return null;
        try{
	        datainputstream = new DataInputStream(new FileInputStream(file));
	        bufferedreader = new BufferedReader(new InputStreamReader(datainputstream));
	        stringbuilder = new StringBuilder();
	        while(true){
		        String s2 = bufferedreader.readLine();
		        if(s2 == null)
		            break;
		        stringbuilder.append((new StringBuilder(String.valueOf(s2))).append("\n").toString());
	        }
	        bufferedreader.close();
	        datainputstream.close();
	        return stringbuilder.toString();
        }catch(Exception e){
        	return null;
        }
    }

    public static String readStringFromAssets(Context context, String s)
    {
        BufferedReader bufferedreader;
        StringBuilder stringbuilder;
        try{
	        bufferedreader = new BufferedReader(new InputStreamReader(context.getAssets().open(s)));
	        stringbuilder = new StringBuilder();
	        while(true){
		        String s1 = bufferedreader.readLine();
		        if(s1 == null)
		        	break;
		        stringbuilder.append(s1);
		        stringbuilder.append("\n");
	        }
	        return stringbuilder.toString();
        }catch(Exception e){
        	return null;
        }
    }

    public static void writeStream(InputStream inputstream, OutputStream outputstream)
    {
        byte abyte0[] = new byte[1024];
        try
        {
        	while(true){
    	        int i = inputstream.read(abyte0, 0, 1024);
    	        if(i == -1)
    	            break;
    	        outputstream.write(abyte0, 0, i);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static void writeString(String s, String s1, String s2)
    {
        if(s == null || s1 == null || s2 == null)
        {
            (new Exception((new StringBuilder(String.valueOf(TAG))).append(": Some content can not null").toString())).printStackTrace();
            return;
        }
        File file = new File(s);
        if(!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            FileWriter filewriter = new FileWriter(new File(s, s1), false);
            filewriter.write(s2);
            filewriter.close();
            return;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

}
