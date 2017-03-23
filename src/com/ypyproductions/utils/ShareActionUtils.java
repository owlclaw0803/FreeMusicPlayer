package com.ypyproductions.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.*;
import android.net.Uri;
import android.widget.Toast;
import java.util.Iterator;
import java.util.List;

public class ShareActionUtils
{

    public static final String TAG = "ShareActionUtils";

    public ShareActionUtils()
    {
    }

    public static void callNumber(Activity activity, String s)
    {
        if(!StringUtils.isEmptyString(s))
        {
            String s1 = s.replaceAll("\\)+", "").replaceAll("\\(+", "").replaceAll("\\-+", "");
            activity.startActivity(new Intent("android.intent.action.CALL", Uri.parse((new StringBuilder("tel:")).append(s1).toString())));
            return;
        }
        try
        {
            Toast.makeText(activity, "No phonenumber to call!", 0).show();
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        Toast.makeText(activity, "Phonenumber error!Please try again", 0).show();
        return;
    }

    public static void goToUrl(Activity activity, String s)
    {
        try
        {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(s));
            intent.addFlags(0x40000000);
            intent.addFlags(0x20000000);
            intent.addFlags(4);
            activity.startActivity(intent);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static void shareToTwitter(Activity activity, String s)
    {
        Intent intent;
        ResolveInfo resolveinfo;
        ActivityInfo activityinfo;
        ComponentName componentname;
        Iterator iterator;
        boolean flag1;
        intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", s);
        intent.setType("text/plain");
        iterator = activity.getPackageManager().queryIntentActivities(intent, 0).iterator();
        while(true){
	        boolean flag = iterator.hasNext();
	        flag1 = false;
	        if(!flag)
	        	break;
	        resolveinfo = (ResolveInfo)iterator.next();
            if("com.twitter.android.PostActivity".equals(resolveinfo.activityInfo.name)){
            	activityinfo = resolveinfo.activityInfo;
                componentname = new ComponentName(activityinfo.applicationInfo.packageName, activityinfo.name);
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setFlags(0x10200000);
                intent.setComponent(componentname);
                activity.startActivity(intent);
                flag1 = true;
                break;
            }
        }
        if(!flag1)
        	Toast.makeText(activity, "Please install the twitter application!", 1).show();
    }

    public static void shareViaEmail(Activity activity, String s, String s1, String s2)
    {
        try
        {
            Intent intent = new Intent("android.intent.action.SEND");
            if(!StringUtils.isEmptyString(s) && EmailUtils.isEmailAddressValid(s))
            {
                intent.putExtra("android.intent.extra.EMAIL", new String[] {
                    s
                });
            }
            intent.setType("message/rfc822");
            if(!StringUtils.isEmptyString(s1))
            {
                intent.putExtra("android.intent.extra.SUBJECT", s1);
            }
            if(!StringUtils.isEmptyString(s2))
            {
                intent.putExtra("android.intent.extra.TEXT", s2);
            }
            activity.startActivity(intent);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        Toast.makeText(activity, "Can not share via email!Please try again", 1).show();
    }

    public static void shareViaSMS(Activity activity, String s)
    {
        try
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.putExtra("sms_body", s);
            intent.setType("vnd.android-dir/mms-sms");
            activity.startActivity(intent);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        Toast.makeText(activity, "Can not share via sms!Please try again", 1).show();
    }

}
