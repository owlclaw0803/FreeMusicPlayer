package com.mzdevelopment.freemusicplayer.soundclound;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.utils.DBLog;
import java.util.*;

public class SoundCloundDataMng
{

    public static final String TAG = "SoundCloundDataMng";
    private static SoundCloundDataMng instance;
    private int currentIndex;
    private TrackObject currentTrackObject;
    private Equalizer equalizer;
    private ArrayList listTrackObjects;
    private Random mRandom;
    private MediaPlayer player;

    private SoundCloundDataMng()
    {
        currentIndex = -1;
        mRandom = new Random();
    }

    public static SoundCloundDataMng getInstance()
    {
        if(instance == null)
        {
            instance = new SoundCloundDataMng();
        }
        return instance;
    }

    public int getCurrentIndex()
    {
        return currentIndex;
    }

    public TrackObject getCurrentTrackObject()
    {
        return currentTrackObject;
    }

    public Equalizer getEqualizer()
    {
        return equalizer;
    }

    public ArrayList getListPlayingTrackObjects()
    {
        return listTrackObjects;
    }

    public TrackObject getNextTrackObject(Context context)
    {
        if(listTrackObjects != null)
        {
            int i = listTrackObjects.size();
            DBLog.d(TAG, (new StringBuilder()).append("==========>currentIndex=").append(currentIndex).toString());
            if(i > 0 && currentIndex >= 0 && currentIndex <= i)
            {
                if(SettingManager.getShuffle(context))
                {
                    currentIndex = mRandom.nextInt(i);
                    currentTrackObject = (TrackObject)listTrackObjects.get(currentIndex);
                    return currentTrackObject;
                }
                currentIndex = 1 + currentIndex;
                if(currentIndex >= i)
                {
                    currentIndex = 0;
                }
                currentTrackObject = (TrackObject)listTrackObjects.get(currentIndex);
                return currentTrackObject;
            }
        }
        return null;
    }

    public MediaPlayer getPlayer()
    {
        return player;
    }

    public TrackObject getPrevTrackObject(Context context)
    {
        if(listTrackObjects != null)
        {
            int i = listTrackObjects.size();
            if(i > 0 && currentIndex >= 0 && currentIndex <= i)
            {
                if(SettingManager.getShuffle(context))
                {
                    currentIndex = mRandom.nextInt(i);
                    currentTrackObject = (TrackObject)listTrackObjects.get(currentIndex);
                    return currentTrackObject;
                }
                currentIndex = -1 + currentIndex;
                if(currentIndex < 0)
                {
                    currentIndex = i - 1;
                }
                currentTrackObject = (TrackObject)listTrackObjects.get(currentIndex);
                return currentTrackObject;
            }
        }
        return null;
    }

    public TrackObject getTrackObject(long l)
    {
label0:
        {
            if(listTrackObjects == null || listTrackObjects.size() <= 0)
            {
                break label0;
            }
            Iterator iterator = listTrackObjects.iterator();
            TrackObject trackobject;
            do
            {
                if(!iterator.hasNext())
                {
                    break label0;
                }
                trackobject = (TrackObject)iterator.next();
            } while(trackobject.getId() != l);
            return trackobject;
        }
        return null;
    }

    public void onDestroy()
    {
        if(listTrackObjects != null)
        {
            listTrackObjects.clear();
            listTrackObjects = null;
        }
        mRandom = null;
        instance = null;
    }

    public void onResetMedia()
    {
        try
        {
            if(equalizer != null)
            {
                equalizer.release();
                equalizer = null;
            }
            if(player != null)
            {
                if(player.isPlaying())
                {
                    player.stop();
                }
                player.release();
                player = null;
            }
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void setCurrentIndex(int i)
    {
        currentIndex = i;
        if(listTrackObjects != null && listTrackObjects.size() > 0 && i >= 0 && i < listTrackObjects.size())
        {
            currentTrackObject = (TrackObject)listTrackObjects.get(i);
        }
    }

    public boolean setCurrentIndex(TrackObject trackobject)
    {
label0:
        {
            if(listTrackObjects != null && listTrackObjects.size() > 0 && trackobject != null)
            {
                currentTrackObject = trackobject;
                currentIndex = listTrackObjects.indexOf(trackobject);
                DBLog.d(TAG, (new StringBuilder()).append("===========>mTrackObject=").append(trackobject.getId()).append("===>currentIndex=").append(currentIndex).toString());
                if(currentIndex >= 0)
                {
                    break label0;
                }
                currentIndex = 0;
            }
            return false;
        }
        return true;
    }

    public void setEqualizer(Equalizer equalizer1)
    {
        equalizer = equalizer1;
    }

    public void setListPlayingTrackObjects(ArrayList arraylist)
    {
        if(arraylist == null){
        	listTrackObjects = arraylist;
            return;
        }
        if(arraylist.size() <= 0){
        	currentIndex = -1;
        	listTrackObjects = arraylist;
            return;
        }
        boolean flag;
        flag = true;
        if(currentTrackObject != null)
        {
        	Iterator iterator = arraylist.iterator();
            TrackObject trackobject;
            while(iterator.hasNext())
            {
            	trackobject = (TrackObject)iterator.next();
            	if(currentTrackObject.getId() == trackobject.getId()){
            		flag = false;
                    currentIndex = arraylist.indexOf(trackobject);
            	}
            }
        }
        if(flag)
        {
            currentIndex = 0;
        }
        listTrackObjects = arraylist;
        return;
    }

    public void setPlayer(MediaPlayer mediaplayer)
    {
        player = mediaplayer;
    }

}
