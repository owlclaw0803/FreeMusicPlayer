package com.mzdevelopment.freemusicplayer.playerservice;


public interface IMusicFocusableListener
{

    public abstract void onGainedAudioFocus();

    public abstract void onLostAudioFocus(boolean flag);
}
