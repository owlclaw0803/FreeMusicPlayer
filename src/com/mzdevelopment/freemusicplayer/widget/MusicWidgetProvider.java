package com.mzdevelopment.freemusicplayer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.mzdevelopment.freemusicplayer.MainActivity;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.playerservice.IMusicConstant;
import com.mzdevelopment.freemusicplayer.soundclound.SoundCloundDataMng;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.utils.StringUtils;
import com.mzdevelopment.freemusicplayer.R;

public class MusicWidgetProvider extends AppWidgetProvider
    implements ICloudMusicPlayerConstants, IMusicConstant
{

    public static final String TAG = "MusicWidgetProvider";

    public MusicWidgetProvider()
    {
    }

    public static void updateWidget(Context context, AppWidgetManager appwidgetmanager, int ai[], boolean flag, boolean flag1, int i)
    {
        TrackObject trackobject = SoundCloundDataMng.getInstance().getCurrentTrackObject();
        String s = context.getPackageName();
        int j = ai.length;
        if(j > 0)
        {
            int k = 0;
            while(k < j) 
            {
                int l = ai[k];
                RemoteViews remoteviews = new RemoteViews(s, R.layout.layout_widget);
                int i1;
                int j1;
                int k1;
                int l1;
                int i2;
                Intent intent;
                Intent intent1;
                Intent intent2;
                Intent intent3;
                String s1;
                if(trackobject != null)
                {
                    if(StringUtils.isEmptyString(trackobject.getUsername()))
                    {
                        s1 = context.getString(R.string.title_unknown);
                    } else
                    {
                        s1 = trackobject.getUsername();
                    }
                    remoteviews.setTextViewText(R.id.tv_song, (new StringBuilder()).append(trackobject.getTitle()).append(" - ").append(s1).toString());
                } else
                {
                    remoteviews.setTextViewText(R.id.tv_song, "");
                }
                if(flag)
                {
                    i1 = R.drawable.ic_pause_white_36dp;
                } else
                {
                    i1 = R.drawable.ic_play_arrow_white_36dp;
                }
                remoteviews.setImageViewResource(R.id.btn_play, i1);
                intent = new Intent((new StringBuilder()).append(s).append(".action.TOGGLE_PLAYBACK").toString());
                intent.setPackage(s);
                remoteviews.setOnClickPendingIntent(R.id.btn_play, PendingIntent.getBroadcast(context, 100, intent, 0));
                intent1 = new Intent((new StringBuilder()).append(s).append(".action.NEXT").toString());
                intent1.setPackage(s);
                remoteviews.setOnClickPendingIntent(R.id.btn_next, PendingIntent.getBroadcast(context, 100, intent1, 0));
                intent2 = new Intent((new StringBuilder()).append(s).append(".action.PREVIOUS").toString());
                intent2.setPackage(s);
                remoteviews.setOnClickPendingIntent(R.id.btn_prev, PendingIntent.getBroadcast(context, 100, intent2, 0));
                intent3 = new Intent(context, com.mzdevelopment.freemusicplayer.MainActivity.class);
                intent3.setPackage(s);
                if(trackobject != null)
                {
                    intent3.putExtra("songId", trackobject.getId());
                    intent3.addFlags(0x10000000);
                }
                remoteviews.setOnClickPendingIntent(R.id.img_play, PendingIntent.getActivity(context, 1000, intent3, 0x10000000));
                remoteviews.setImageViewResource(R.id.img_play, i);
                if(!flag1)
                {
                    j1 = 0;
                } else
                {
                    j1 = 4;
                }
                remoteviews.setViewVisibility(R.id.btn_play, j1);
                if(!flag1)
                {
                    k1 = 0;
                } else
                {
                    k1 = 4;
                }
                remoteviews.setViewVisibility(R.id.btn_next, k1);
                if(!flag1)
                {
                    l1 = 0;
                } else
                {
                    l1 = 4;
                }
                remoteviews.setViewVisibility(R.id.btn_prev, l1);
                if(flag1)
                {
                    i2 = 0;
                } else
                {
                    i2 = 4;
                }
                remoteviews.setViewVisibility(R.id.progressBar1, i2);
                appwidgetmanager.updateAppWidget(l, remoteviews);
                k++;
            }
        }
    }

    public static void updateWidget(Context context, AppWidgetManager appwidgetmanager, int ai[], boolean flag, boolean flag1, Bitmap bitmap)
    {
        TrackObject trackobject = SoundCloundDataMng.getInstance().getCurrentTrackObject();
        String s = context.getPackageName();
        int i = ai.length;
        if(i > 0)
        {
            int j = 0;
            while(j < i) 
            {
                int k = ai[j];
                RemoteViews remoteviews = new RemoteViews(s, R.layout.layout_widget);
                int l;
                int i1;
                int j1;
                int k1;
                int l1;
                Intent intent;
                Intent intent1;
                PendingIntent pendingintent;
                Intent intent2;
                Intent intent3;
                String s1;
                if(trackobject != null)
                {
                    if(StringUtils.isEmptyString(trackobject.getUsername()))
                    {
                        s1 = context.getString(R.string.title_unknown);
                    } else
                    {
                        s1 = trackobject.getUsername();
                    }
                    remoteviews.setTextViewText(R.id.tv_song, (new StringBuilder()).append(trackobject.getTitle()).append(" - ").append(s1).toString());
                } else
                {
                    remoteviews.setTextViewText(R.id.tv_song, "");
                }
                if(flag)
                {
                    l = R.drawable.ic_pause_white_36dp;
                } else
                {
                    l = R.drawable.ic_play_arrow_white_36dp;
                }
                remoteviews.setImageViewResource(R.id.btn_play, l);
                intent = new Intent((new StringBuilder()).append(s).append(".action.TOGGLE_PLAYBACK").toString());
                intent.setPackage(s);
                remoteviews.setOnClickPendingIntent(R.id.btn_play, PendingIntent.getBroadcast(context, 100, intent, 0));
                intent1 = new Intent((new StringBuilder()).append(s).append(".action.NEXT").toString());
                pendingintent = PendingIntent.getBroadcast(context, 100, intent1, 0);
                intent1.setPackage(s);
                remoteviews.setOnClickPendingIntent(R.id.btn_next, pendingintent);
                intent2 = new Intent((new StringBuilder()).append(s).append(".action.PREVIOUS").toString());
                intent2.setPackage(s);
                remoteviews.setOnClickPendingIntent(R.id.btn_prev, PendingIntent.getBroadcast(context, 100, intent2, 0));
                intent3 = new Intent(context, com.mzdevelopment.freemusicplayer.MainActivity.class);
                intent3.setPackage(s);
                if(trackobject != null)
                {
                    intent3.putExtra("songId", trackobject.getId());
                    intent3.addFlags(0x10000000);
                }
                remoteviews.setOnClickPendingIntent(R.id.img_play, PendingIntent.getActivity(context, 1000, intent3, 0x10000000));
                remoteviews.setImageViewBitmap(R.id.img_play, bitmap);
                if(!flag1)
                {
                    i1 = 0;
                } else
                {
                    i1 = 4;
                }
                remoteviews.setViewVisibility(R.id.btn_play, i1);
                if(!flag1)
                {
                    j1 = 0;
                } else
                {
                    j1 = 4;
                }
                remoteviews.setViewVisibility(R.id.btn_next, j1);
                if(!flag1)
                {
                    k1 = 0;
                } else
                {
                    k1 = 4;
                }
                remoteviews.setViewVisibility(R.id.btn_prev, k1);
                if(flag1)
                {
                    l1 = 0;
                } else
                {
                    l1 = 4;
                }
                remoteviews.setViewVisibility(R.id.progressBar1, l1);
                appwidgetmanager.updateAppWidget(k, remoteviews);
                j++;
            }
        }
    }

    public void onDeleted(Context context, int ai[])
    {
        super.onDeleted(context, ai);
    }

    public void onDisabled(Context context)
    {
        super.onDisabled(context);
    }

    public void onEnabled(Context context)
    {
        super.onEnabled(context);
    }

    public void onUpdate(Context context, AppWidgetManager appwidgetmanager, int ai[])
    {
        super.onUpdate(context, appwidgetmanager, ai);
        updateWidget(context, appwidgetmanager, ai, false, false, R.drawable.ic_music_default);
    }

}
