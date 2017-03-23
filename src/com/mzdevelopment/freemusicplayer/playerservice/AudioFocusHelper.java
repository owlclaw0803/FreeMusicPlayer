package com.mzdevelopment.freemusicplayer.playerservice;

import android.content.Context;
import android.media.AudioManager;

public class AudioFocusHelper
    implements android.media.AudioManager.OnAudioFocusChangeListener
{

    private AudioManager mAM;
    private IMusicFocusableListener mFocusable;

    public AudioFocusHelper(Context context, IMusicFocusableListener imusicfocusablelistener)
    {
        mAM = (AudioManager)context.getSystemService("audio");
        mFocusable = imusicfocusablelistener;
    }

    public boolean abandonFocus()
    {
        return 1 == mAM.abandonAudioFocus(this);
    }

    public void onAudioFocusChange(int i)
    {
        if(mFocusable == null)
        {
            return;
        }
        switch(i)
        {
        case 0: // '\0'
        default:
            return;

        case -3: 
            mFocusable.onLostAudioFocus(true);
            return;

        case 1: // '\001'
            mFocusable.onGainedAudioFocus();
            return;

        case -2: 
        case -1: 
            mFocusable.onLostAudioFocus(false);
            return;
        }
    }

    public boolean requestFocus()
    {
        return 1 == mAM.requestAudioFocus(this, 3, 1);
    }
}
