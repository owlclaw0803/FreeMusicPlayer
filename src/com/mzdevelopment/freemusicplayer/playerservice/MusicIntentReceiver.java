package com.mzdevelopment.freemusicplayer.playerservice;

import android.content.*;
import android.os.Bundle;
import android.view.KeyEvent;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.mzdevelopment.freemusicplayer.soundclound.SoundCloundDataMng;
import com.ypyproductions.utils.StringUtils;
import java.util.ArrayList;

public class MusicIntentReceiver extends BroadcastReceiver
    implements IMusicConstant
{

    public static final String TAG = "MusicIntentReceiver";
    private ArrayList mListTrack;

    public MusicIntentReceiver()
    {
    }

    public void onReceive(Context context, Intent intent)
    {
        String s;
        if(intent != null)
        {
            if(!StringUtils.isEmptyString(s = intent.getAction()))
            {
                mListTrack = SoundCloundDataMng.getInstance().getListPlayingTrackObjects();
                String s1 = context.getPackageName();
                if(s.equals("android.media.AUDIO_BECOMING_NOISY"))
                {
                    Intent intent1 = new Intent((new StringBuilder()).append(s1).append(".action.PAUSE").toString());
                    intent1.setPackage(context.getPackageName());
                    context.startService(intent1);
                    return;
                }
                if(s.equals((new StringBuilder()).append(s1).append(".action.NEXT").toString()))
                {
                    Intent intent2 = new Intent((new StringBuilder()).append(s1).append(".action.NEXT").toString());
                    intent2.setPackage(context.getPackageName());
                    context.startService(intent2);
                    return;
                }
                if(s.equals((new StringBuilder()).append(s1).append(".action.TOGGLE_PLAYBACK").toString()))
                {
                    Intent intent3 = new Intent((new StringBuilder()).append(s1).append(".action.TOGGLE_PLAYBACK").toString());
                    intent3.setPackage(context.getPackageName());
                    context.startService(intent3);
                    return;
                }
                if(s.equals((new StringBuilder()).append(s1).append(".action.PREVIOUS").toString()))
                {
                    Intent intent4 = new Intent((new StringBuilder()).append(s1).append(".action.PREVIOUS").toString());
                    intent4.setPackage(context.getPackageName());
                    context.startService(intent4);
                    return;
                }
                if(s.equals((new StringBuilder()).append(s1).append(".action.STOP").toString()))
                {
                    Intent intent5 = new Intent((new StringBuilder()).append(s1).append(".action.STOP").toString());
                    intent5.setPackage(context.getPackageName());
                    context.startService(intent5);
                    return;
                }
                if(s.equals("android.intent.action.MEDIA_BUTTON"))
                {
                    if(mListTrack == null || mListTrack.size() == 0)
                    {
                        SoundCloundDataMng.getInstance().onDestroy();
                        Intent intent6 = new Intent((new StringBuilder()).append(s1).append(".action.STOP").toString());
                        intent6.setPackage(context.getPackageName());
                        context.startService(intent6);
                        return;
                    }
                    KeyEvent keyevent = (KeyEvent)intent.getExtras().get("android.intent.extra.KEY_EVENT");
                    if(keyevent.getAction() == 0)
                    {
                        switch(keyevent.getKeyCode())
                        {
                        default:
                            return;

                        case 79: // 'O'
                        case 85: // 'U'
                            if(SettingManager.getOnline(context))
                            {
                                Intent intent13 = new Intent((new StringBuilder()).append(s1).append(".action.TOGGLE_PLAYBACK").toString());
                                intent13.setPackage(context.getPackageName());
                                context.startService(intent13);
                                return;
                            } else
                            {
                                Intent intent14 = new Intent((new StringBuilder()).append(s1).append(".action.STOP").toString());
                                intent14.setPackage(context.getPackageName());
                                context.startService(intent14);
                                return;
                            }

                        case 126: // '~'
                            Intent intent12 = new Intent((new StringBuilder()).append(s1).append(".action.PLAY").toString());
                            intent12.setPackage(context.getPackageName());
                            context.startService(intent12);
                            return;

                        case 127: // '\177'
                            if(SettingManager.getOnline(context))
                            {
                                Intent intent10 = new Intent((new StringBuilder()).append(s1).append(".action.PAUSE").toString());
                                intent10.setPackage(context.getPackageName());
                                context.startService(intent10);
                                return;
                            } else
                            {
                                Intent intent11 = new Intent((new StringBuilder()).append(s1).append(".action.STOP").toString());
                                intent11.setPackage(context.getPackageName());
                                context.startService(intent11);
                                return;
                            }

                        case 86: // 'V'
                            Intent intent9 = new Intent((new StringBuilder()).append(s1).append(".action.STOP").toString());
                            intent9.setPackage(context.getPackageName());
                            context.startService(intent9);
                            return;

                        case 87: // 'W'
                            Intent intent8 = new Intent((new StringBuilder()).append(s1).append(".action.NEXT").toString());
                            intent8.setPackage(context.getPackageName());
                            context.startService(intent8);
                            return;

                        case 88: // 'X'
                            Intent intent7 = new Intent((new StringBuilder()).append(s1).append(".action.PREVIOUS").toString());
                            intent7.setPackage(context.getPackageName());
                            context.startService(intent7);
                            return;
                        }
                    }
                }
            }
        }
    }

}
