package com.mzdevelopment.freemusicplayer.playerservice;

import android.app.*;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;
import com.mzdevelopment.freemusicplayer.MainActivity;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.dataMng.TotalDataManager;
import com.mzdevelopment.freemusicplayer.dataMng.YPYNetUtils;
import com.mzdevelopment.freemusicplayer.setting.ISettingConstants;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.mzdevelopment.freemusicplayer.soundclound.*;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.mzdevelopment.freemusicplayer.widget.MusicWidgetProvider;
import com.ypyproductions.task.*;
import com.ypyproductions.utils.*;
import com.ypyproductions.webservice.DownloadUtils;
import java.io.*;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class MusicService extends Service
    implements android.media.MediaPlayer.OnCompletionListener, android.media.MediaPlayer.OnPreparedListener, android.media.MediaPlayer.OnErrorListener, IMusicFocusableListener, IMusicConstant, ISoundCloundConstants, ISettingConstants, ICloudMusicPlayerConstants
{
    private enum AudioFocus
    {
    	NO_FOCUS_NO_DUCK("NO_FOCUS_NO_DUCK", 0),
        NO_FOCUS_CAN_DUCK("NO_FOCUS_CAN_DUCK", 1),
        FOCUSED("FOCUSED", 2);
        static 
        {
            AudioFocus aaudiofocus[] = new AudioFocus[3];
            aaudiofocus[0] = NO_FOCUS_NO_DUCK;
            aaudiofocus[1] = NO_FOCUS_CAN_DUCK;
            aaudiofocus[2] = FOCUSED;
        }

        private AudioFocus(String s, int i)
        {
        }
    }

    private enum State
    {
    	STOPPED("STOPPED", 0),
        PREPARING("PREPARING", 1),
        PLAYING("PLAYING", 2),
        PAUSED("PAUSED", 3);
        static 
        {
            State astate[] = new State[4];
            astate[0] = STOPPED;
            astate[1] = PREPARING;
            astate[2] = PLAYING;
            astate[3] = PAUSED;
        }

        private State(String s, int i)
        {
        }
    }


    public static final String TAG = "MusicService";
    private AudioFocus mAudioFocus;
    private AudioFocusHelper mAudioFocusHelper;
    private AudioManager mAudioManager;
    private Bitmap mBitmapTrack;
    private TrackObject mCurrentTrack;
    private Equalizer mEqualizer;
    private Handler mHandler;
    private ComponentName mMediaButtonReceiverComponent;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private MediaPlayer mPlayer;
    private RemoteControlClientCompat mRemoteControlClientCompat;
    private String mSongTitle;
    private State mState;
    private android.net.wifi.WifiManager.WifiLock mWifiLock;
    private RemoteViews notificationView;

    public MusicService()
    {
        mPlayer = null;
        mAudioFocusHelper = null;
        mState = State.STOPPED;
        mAudioFocus = AudioFocus.NO_FOCUS_NO_DUCK;
        mSongTitle = "";
        mNotification = null;
        mHandler = new Handler();
    }

    private void broadcastAction(String s)
    {
        Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.ACTION_BROADCAST_PLAYER").toString());
        intent.putExtra("action", s);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    private void configAndStartMediaPlayer()
    {
        if(mPlayer != null)
        {
            if(mAudioFocus == AudioFocus.NO_FOCUS_NO_DUCK)
            {
                if(mPlayer.isPlaying())
                {
                    mPlayer.pause();
                    SettingManager.setPlayingState(this, false);
                    mHandler.removeCallbacksAndMessages(null);
                    broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.PAUSE").toString());
                }
            } else
            {
                if(mAudioFocus == AudioFocus.NO_FOCUS_CAN_DUCK)
                {
                    mPlayer.setVolume(0.1F, 0.1F);
                } else
                {
                    mPlayer.setVolume(1.0F, 1.0F);
                }
                if(!mPlayer.isPlaying())
                {
                    mPlayer.start();
                    SettingManager.setPlayingState(this, true);
                    startUpdatePosition();
                    broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.PLAY").toString());
                    return;
                }
            }
        }
    }

    private void createMediaPlayerIfNeeded()
    {
        if(mPlayer == null)
        {
            mPlayer = new MediaPlayer();
            mPlayer.setWakeMode(getApplicationContext(), 1);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
            mEqualizer = new Equalizer(0, mPlayer.getAudioSessionId());
            mEqualizer.setEnabled(SettingManager.getEqualizer(this));
            setUpParams();
            SoundCloundDataMng.getInstance().setPlayer(mPlayer);
            SoundCloundDataMng.getInstance().setEqualizer(mEqualizer);
            return;
        }
        mPlayer.reset();
        if(mEqualizer != null)
        {
            mEqualizer.release();
            mEqualizer = null;
        }
        mEqualizer = new Equalizer(0, mPlayer.getAudioSessionId());
        mEqualizer.setEnabled(SettingManager.getEqualizer(this));
        setUpParams();
        SoundCloundDataMng.getInstance().setEqualizer(mEqualizer);
    }

    private void giveUpAudioFocus()
    {
        if(mAudioFocus != null && mAudioFocus == AudioFocus.FOCUSED && mAudioFocusHelper != null && mAudioFocusHelper.abandonFocus())
        {
            mAudioFocus = AudioFocus.NO_FOCUS_NO_DUCK;
        }
    }

    private void onDestroyBitmap()
    {
        if(mBitmapTrack != null)
        {
            mBitmapTrack.recycle();
            mBitmapTrack = null;
        }
    }

    private void playNextSong()
    {
        mState = State.STOPPED;
        relaxResources(false);
        mHandler.removeCallbacksAndMessages(null);
        if(mCurrentTrack == null)
        {
            mState = State.PAUSED;
            processStopRequest(true);
            updateWidget(true);
            return;
        } else
        {
            Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.COUNT_PLAY").toString());
            intent.setPackage(getPackageName());
            sendBroadcast(intent);
            startGetLinkStream();
            return;
        }
    }

    private void processNextRequest()
    {
label0:
        {
            if(mState == State.PLAYING || mState == State.PAUSED || mState == State.STOPPED)
            {
                mCurrentTrack = SoundCloundDataMng.getInstance().getNextTrackObject(this);
                DBLog.d(TAG, (new StringBuilder()).append("==========>mCurrentTrack=").append(mCurrentTrack).toString());
                if(mCurrentTrack == null)
                {
                    break label0;
                }
                tryToGetAudioFocus();
                playNextSong();
            }
            return;
        }
        mState = State.PAUSED;
        processStopRequest(true);
    }

    private void processPauseRequest()
    {
        if(mCurrentTrack == null)
        {
            mState = State.PAUSED;
            processStopRequest(true);
        } else
        {
            if(mState == State.PLAYING)
            {
                mState = State.PAUSED;
                mPlayer.pause();
                relaxResources(false);
                updateStatusPlayPause();
                SettingManager.setPlayingState(this, false);
                broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.PAUSE").toString());
            }
            if(mRemoteControlClientCompat != null)
            {
                mRemoteControlClientCompat.setPlaybackState(2);
                return;
            }
        }
    }

    private void processPlayRequest()
    {
        if(SoundCloundDataMng.getInstance().getListPlayingTrackObjects() == null)
        {
            startGetListData(new IDBCallback() {
                public void onAction()
                {
                    processPlayRequest();
                }
            });
        } else
        {
            long l = 0L;
            if(mCurrentTrack != null)
            {
                l = mCurrentTrack.getId();
            }
            mCurrentTrack = SoundCloundDataMng.getInstance().getCurrentTrackObject();
            if(mCurrentTrack == null)
            {
                mState = State.PAUSED;
                SoundCloundDataMng.getInstance().onDestroy();
                processStopRequest(true);
                onDestroyBitmap();
                updateWidget(false);
                return;
            }
            tryToGetAudioFocus();
            if(mState == State.STOPPED || mState == State.PLAYING)
            {
                playNextSong();
                broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.NEXT").toString());
            } else
            if(mState == State.PAUSED)
            {
                if(l != mCurrentTrack.getId())
                {
                    playNextSong();
                    broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.NEXT").toString());
                    return;
                }
                mState = State.PLAYING;
                configAndStartMediaPlayer();
                updateStatusPlayPause();
            }
            if(mRemoteControlClientCompat != null)
            {
                mRemoteControlClientCompat.setPlaybackState(3);
                return;
            }
        }
    }

    private void processPreviousRequest()
    {
label0:
        {
            if(mState == State.PLAYING || mState == State.PAUSED || mState == State.STOPPED)
            {
                mCurrentTrack = SoundCloundDataMng.getInstance().getPrevTrackObject(this);
                if(mCurrentTrack == null)
                {
                    break label0;
                }
                tryToGetAudioFocus();
                playNextSong();
            }
            return;
        }
        mState = State.PAUSED;
        processStopRequest(true);
    }

    private void processSeekBar(int i)
    {
        if((mState == State.PLAYING || mState == State.PAUSED) && i > 0 && mPlayer != null)
        {
            DBLog.d(TAG, (new StringBuilder()).append("================>currentPos=").append(i).toString());
            mPlayer.seekTo(i);
        }
    }

    private void processStopRequest()
    {
        processStopRequest(false);
    }

    private void processStopRequest(boolean flag)
    {
        if(mState == State.PLAYING || mState == State.PAUSED || flag)
        {
            SettingManager.setPlayingState(this, false);
            mHandler.removeCallbacksAndMessages(null);
            mState = State.STOPPED;
            relaxResources(true);
            giveUpAudioFocus();
            if(mRemoteControlClientCompat != null)
            {
                mRemoteControlClientCompat.setPlaybackState(1);
            }
            broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.STOP").toString());
            stopSelf();
        }
        updateWidget(false);
    }

    private void processTogglePlaybackRequest()
    {
        if(mState == State.PAUSED || mState == State.STOPPED)
        {
            processPlayRequest();
            return;
        } else
        {
            processPauseRequest();
            return;
        }
    }

    private void relaxResources(boolean flag)
    {
        if(flag && mPlayer != null)
        {
            stopForeground(true);
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
            if(mEqualizer != null)
            {
                mEqualizer.release();
                mEqualizer = null;
            }
            SoundCloundDataMng.getInstance().setEqualizer(null);
            SoundCloundDataMng.getInstance().setPlayer(null);
        }
        if(mWifiLock.isHeld())
        {
            mWifiLock.release();
        }
    }

    private void setUpAsForeground()
    {
        if(mCurrentTrack == null)
        {
            return;
        }
        Intent intent = new Intent(getApplicationContext(), com.mzdevelopment.freemusicplayer.MainActivity.class);
        intent.putExtra("songId", mCurrentTrack.getId());
        intent.addFlags(0x10000000);
        intent.setPackage(getPackageName());
        PendingIntent pendingintent = PendingIntent.getActivity(getApplicationContext(), 1000, intent, 0x10000000);
        mNotification = new Notification(R.drawable.ic_launcher, mCurrentTrack.getTitle(), System.currentTimeMillis());
        notificationView = new RemoteViews(getPackageName(), R.layout.item_small_notification_music);
        notificationView.setTextViewText(R.id.tv_song, mCurrentTrack.getTitle());
        RemoteViews remoteviews = notificationView;
        String s;
        String s1;
        Intent intent1;
        PendingIntent pendingintent1;
        Intent intent2;
        PendingIntent pendingintent2;
        Intent intent3;
        PendingIntent pendingintent3;
        Intent intent4;
        PendingIntent pendingintent4;
        Notification notification;
        if(StringUtils.isEmptyString(mCurrentTrack.getUsername()))
        {
            s = getString(R.string.title_unknown);
        } else
        {
            s = mCurrentTrack.getUsername();
        }
        remoteviews.setTextViewText(R.id.tv_singer, s);
        notificationView.setImageViewResource(R.id.btn_play, R.drawable.ic_pause_white_36dp);
        if(mBitmapTrack != null)
        {
            notificationView.setImageViewBitmap(R.id.img_play, mBitmapTrack);
        } else
        {
            notificationView.setImageViewResource(R.id.img_play, R.drawable.ic_music_default);
        }
        s1 = getPackageName();
        intent1 = new Intent((new StringBuilder()).append(s1).append(".action.TOGGLE_PLAYBACK").toString());
        intent1.setPackage(getPackageName());
        pendingintent1 = PendingIntent.getBroadcast(this, 100, intent1, 0);
        notificationView.setOnClickPendingIntent(R.id.btn_play, pendingintent1);
        intent2 = new Intent((new StringBuilder()).append(s1).append(".action.NEXT").toString());
        intent2.setPackage(getPackageName());
        pendingintent2 = PendingIntent.getBroadcast(this, 100, intent2, 0);
        notificationView.setOnClickPendingIntent(R.id.btn_next, pendingintent2);
        intent3 = new Intent((new StringBuilder()).append(s1).append(".action.PREVIOUS").toString());
        intent3.setPackage(getPackageName());
        pendingintent3 = PendingIntent.getBroadcast(this, 100, intent3, 0);
        notificationView.setOnClickPendingIntent(R.id.btn_prev, pendingintent3);
        intent4 = new Intent((new StringBuilder()).append(s1).append(".action.STOP").toString());
        intent4.setPackage(getPackageName());
        pendingintent4 = PendingIntent.getBroadcast(this, 100, intent4, 0);
        notificationView.setOnClickPendingIntent(R.id.btn_close, pendingintent4);
        mNotification.contentView = notificationView;
        mNotification.contentIntent = pendingintent;
        notification = mNotification;
        notification.flags = 0x20 | notification.flags;
        startForeground(1000, mNotification);
        updateWidget(true);
    }

    private void setUpEqualizerCustom()
    {
        if(mEqualizer != null)
        {
            String s = SettingManager.getEqualizerParams(this);
            if(!StringUtils.isEmptyString(s))
            {
                String as[] = s.split(":");
                if(as != null && as.length > 0)
                {
                    int i = as.length;
                    for(int j = 0; j < i; j++)
                    {
                        mEqualizer.setBandLevel((short)j, Short.parseShort(as[j]));
                    }

                    SettingManager.setEqualizerPreset(this, String.valueOf(mEqualizer.getNumberOfPresets()));
                }
            }
        }
    }

    private void setUpParams()
    {
label0:
        {
            if(mEqualizer != null)
            {
                String s = SettingManager.getEqualizerPreset(this);
                if(StringUtils.isEmptyString(s) || !StringUtils.isNumber(s))
                {
                    break label0;
                }
                short word0 = Short.parseShort(s);
                short word1 = mEqualizer.getNumberOfPresets();
                if(word1 <= 0 || word0 >= word1 - 1 || word0 < 0)
                {
                    break label0;
                }
                mEqualizer.usePreset(word0);
            }
            return;
        }
        setUpEqualizerCustom();
    }

    private void startDownloadBitmap()
    {
        if(mCurrentTrack != null){
	        String s = mCurrentTrack.getArtworkUrl();
	        if(!StringUtils.isEmptyString(s)){
		        if(!s.startsWith("http")){
		        	File file = new File(s);
		            if(file.exists() && file.isFile())
		            {
		                android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
		                options.inJustDecodeBounds = true;
		                BitmapFactory.decodeFile(s, options);
		                ImageProcessingUtils.calculateInSampleSize(options, 100, 100);
		                options.inJustDecodeBounds = false;
		                mBitmapTrack = BitmapFactory.decodeFile(s, options);
		                return;
		            }
		        }else{
			        InputStream inputstream = DownloadUtils.download(s);
			        if(inputstream != null)
			        {
			            try
			            {
			                mBitmapTrack = ImageProcessingUtils.decodePortraitBitmap(inputstream, 100, 100);
			                inputstream.close();
			                return;
			            }
			            catch(Exception exception)
			            {
			                exception.printStackTrace();
			            }
			        }
		        }
	        }
        }
        mBitmapTrack = BitmapFactory.decodeResource(getResources(), R.drawable.ic_music_default);
    }

    private void startGetLinkStream()
    {
        final String packageName;
label0:
        {
            if(mCurrentTrack != null)
            {
                packageName = getPackageName();
                onDestroyBitmap();
                if(StringUtils.isEmptyString(mCurrentTrack.getPath()))
                {
                    break label0;
                }
                broadcastAction((new StringBuilder()).append(packageName).append(".action.LOADING").toString());
                updateWidget(true);
                startStreamWithUrl(null);
            }
            return;
        }
        String s = mCurrentTrack.getLinkStream();
        if(!StringUtils.isEmptyString(s))
        {
            broadcastAction((new StringBuilder()).append(packageName).append(".action.LOADING").toString());
            updateWidget(true);
            startDownloadBitmap();
            startStreamWithUrl(s);
            return;
        } else
        {
            (new DBTask(new IDBTaskListener() {

                private String finalUrl;
                public void onDoInBackground()
                {
                    if(mCurrentTrack.isStreamable())
                    {
                        finalUrl = YPYNetUtils.getLinkStreamFromSoundClound(mCurrentTrack.getId());
                    }
                    if(StringUtils.isEmptyString(finalUrl))
                    {
                        Object aobj[] = new Object[2];
                        aobj[0] = Long.valueOf(mCurrentTrack.getId());
                        aobj[1] = ICloudMusicPlayerConstants.SOUNDCLOUND_CLIENT_ID;
                        finalUrl = String.format("http://api.soundcloud.com/tracks/%1$s/stream?client_id=%2$s", aobj);
                    }
                    startDownloadBitmap();
                }

                public void onPostExcute()
                {
                    DBLog.d(MusicService.TAG, (new StringBuilder()).append("========>final Url=").append(finalUrl).toString());
                    if(!StringUtils.isEmptyString(finalUrl))
                    {
                        startStreamWithUrl(finalUrl);
                        mCurrentTrack.setLinkStream(finalUrl);
                        return;
                    } else
                    {
                        broadcastAction((new StringBuilder()).append(packageName).append(".action.DIMISS_LOADING").toString());
                        return;
                    }
                }

                public void onPreExcute()
                {
                    broadcastAction((new StringBuilder()).append(packageName).append(".action.LOADING").toString());
                    updateWidget(true);
                }
            })).execute(new Void[0]);
            return;
        }
    }

    private void startGetListData(IDBCallback idbcallback)
    {
        final Context mContext = getApplicationContext();
        final SoundCloundAPI mSoundClound = new SoundCloundAPI(SOUNDCLOUND_CLIENT_ID, SOUNDCLOUND_CLIENT_SECRET);
        final IDBCallback mCallback = idbcallback;
    
        (new DBTask(new IDBTaskListener() {

            private ArrayList mListNewTrackObjects;

            public void onDoInBackground()
            {
                if(!ApplicationUtils.isOnline(mContext)){
                	TotalDataManager.getInstance().readLibraryTrack(mContext);
	                mListNewTrackObjects = TotalDataManager.getInstance().getListLibraryTrackObjects();
	                if(mListNewTrackObjects != null && mListNewTrackObjects.size() > 0)
	                {
	                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mListNewTrackObjects);
	                    SoundCloundDataMng.getInstance().setCurrentIndex(0);
	                }
	                return;
            	}
                mListNewTrackObjects = TotalDataManager.getInstance().getListCurrrentTrackObjects();
                if(mListNewTrackObjects != null && mListNewTrackObjects.size() != 0){
                	SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mListNewTrackObjects);
                    SoundCloundDataMng.getInstance().setCurrentIndex(0);
                    return;
                }
                if(SettingManager.getSearchType(mContext) == 2)
                {
                    mListNewTrackObjects = mSoundClound.getListTrackObjectsByGenre(SettingManager.getLastKeyword(mContext), 0, 10);
                } else
                {
                    mListNewTrackObjects = mSoundClound.getListTrackObjectsByQuery(SettingManager.getLastKeyword(mContext), 0, 10);
                }
                if(mListNewTrackObjects != null && mListNewTrackObjects.size() > 0)
                {
                    TotalDataManager.getInstance().setListCurrrentTrackObjects(mListNewTrackObjects);
                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mListNewTrackObjects);
                    SoundCloundDataMng.getInstance().setCurrentIndex(0);
                }
            }

            public void onPostExcute()
            {
                updateWidget(true);
                if(mCallback != null)
                {
                    mCallback.onAction();
                }
            }

            public void onPreExcute()
            {
                updateWidget(true);
            }
        })).execute(new Void[0]);
    }

    private void startStreamWithUrl(String s)
    {
    	boolean flag2;
    	boolean flag1;
        Intent intent;
        boolean flag = true;
        try{
	        mSongTitle = mCurrentTrack.getTitle();
	        if(StringUtils.isEmptyString(mCurrentTrack.getPath())){
	        	flag1 = StringUtils.isEmptyString(s);
	            flag2 = false;
	            if(!flag1){
	    	        createMediaPlayerIfNeeded();
	    	        mPlayer.setAudioStreamType(3);
	    	        mPlayer.setDataSource(s);
	    	        flag2 = true;
	            }
	        }else{
		        createMediaPlayerIfNeeded();
		        mPlayer.setDataSource(this, mCurrentTrack.getURI());
		        mPlayer.prepare();
		        flag2 = true;
		        flag = false;
	        }
	        if(!flag2)
	        	return;
	        mState = State.PREPARING;
	        setUpAsForeground();
	        MediaButtonHelper.registerMediaButtonEventReceiverCompat(mAudioManager, mMediaButtonReceiverComponent);
	        if(mRemoteControlClientCompat == null)
	        {
	            intent = new Intent("android.intent.action.MEDIA_BUTTON");
	            intent.setPackage(getPackageName());
	            intent.setComponent(mMediaButtonReceiverComponent);
	            mRemoteControlClientCompat = new RemoteControlClientCompat(PendingIntent.getBroadcast(this, 0, intent, 0));
	            RemoteControlHelper.registerRemoteControlClient(mAudioManager, mRemoteControlClientCompat);
	        }
	        mRemoteControlClientCompat.setPlaybackState(3);
	        mRemoteControlClientCompat.setTransportControlFlags(181);
	        mRemoteControlClientCompat.editMetadata(true).putString(2, mCurrentTrack.getUsername()).putString(3, mCurrentTrack.getUsername()).putString(7, mSongTitle).putLong(9, mCurrentTrack.getDuration()).apply();
	        if(flag)
	        	mPlayer.prepareAsync();
	        mWifiLock.acquire();
        }catch(Exception e){}
    }

    private void startUpdatePosition()
    {
        mHandler.postDelayed(new Runnable() {
            public void run()
            {
                if(mPlayer != null && mCurrentTrack != null)
                {
                    int i = mPlayer.getCurrentPosition();
                    Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.ACTION_BROADCAST_PLAYER").toString());
                    intent.putExtra("pos", i);
                    intent.putExtra("action", (new StringBuilder()).append(getPackageName()).append(".action.UPDATE_POS").toString());
                    intent.setPackage(getPackageName());
                    sendBroadcast(intent);
                    if((long)i < mCurrentTrack.getDuration())
                    {
                        startUpdatePosition();
                    }
                }
            }
        }, 1000L);
    }

    private void tryToGetAudioFocus()
    {
        if(mAudioFocus != null && mAudioFocus != AudioFocus.FOCUSED && mAudioFocusHelper != null && mAudioFocusHelper.requestFocus())
        {
            mAudioFocus = AudioFocus.FOCUSED;
        }
    }

    private void updateNotification(String s)
    {
        mNotificationManager.notify(1000, mNotification);
        updateWidget(false);
    }

    private void updateStatusPlayPause()
    {
        if(mPlayer != null && notificationView != null)
        {
            RemoteViews remoteviews;
            int i;
            if(mBitmapTrack != null)
            {
                notificationView.setImageViewBitmap(R.id.img_play, mBitmapTrack);
            } else
            {
                notificationView.setImageViewResource(R.id.img_play, R.drawable.ic_music_default);
            }
            remoteviews = notificationView;
            if(mPlayer.isPlaying())
            {
                i = R.drawable.ic_pause_white_36dp;
            } else
            {
                i = R.drawable.ic_play_arrow_white_36dp;
            }
            remoteviews.setImageViewResource(R.id.btn_play, i);
            mNotificationManager.notify(1000, mNotification);
            updateWidget(false);
        }
    }

    private void updateWidget(boolean flag)
    {
        AppWidgetManager appwidgetmanager = AppWidgetManager.getInstance(this);
        int ai[] = appwidgetmanager.getAppWidgetIds(new ComponentName(getApplicationContext(), com.mzdevelopment.freemusicplayer.widget.MusicWidgetProvider.class));
        if(mBitmapTrack != null)
        {
            MediaPlayer mediaplayer1 = mPlayer;
            boolean flag2 = false;
            if(mediaplayer1 != null)
            {
                flag2 = mPlayer.isPlaying();
            }
            MusicWidgetProvider.updateWidget(this, appwidgetmanager, ai, flag2, flag, mBitmapTrack);
            return;
        }
        MediaPlayer mediaplayer = mPlayer;
        boolean flag1 = false;
        if(mediaplayer != null)
        {
            flag1 = mPlayer.isPlaying();
        }
        MusicWidgetProvider.updateWidget(this, appwidgetmanager, ai, flag1, flag, R.drawable.ic_music_default);
    }

    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void onCompletion(MediaPlayer mediaplayer)
    {
        mState = State.STOPPED;
        SettingManager.setPlayingState(this, false);
        if(SettingManager.getRepeat(this))
        {
            playNextSong();
        } else
        {
            processNextRequest();
        }
        broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.NEXT").toString());
    }

    public void onCreate()
    {
        mWifiLock = ((WifiManager)getSystemService("wifi")).createWifiLock(1, "mylock");
        mNotificationManager = (NotificationManager)getSystemService("notification");
        mAudioManager = (AudioManager)getSystemService("audio");
        mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), this);
        mMediaButtonReceiverComponent = new ComponentName(this, com.mzdevelopment.freemusicplayer.playerservice.MusicIntentReceiver.class);
    }

    public void onDestroy()
    {
        onDestroyBitmap();
        mHandler.removeCallbacksAndMessages(null);
        mState = State.STOPPED;
        try
        {
            relaxResources(true);
            giveUpAudioFocus();
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean onError(MediaPlayer mediaplayer, int i, int j)
    {
        DBLog.e(TAG, (new StringBuilder()).append("Error: what=").append(String.valueOf(i)).append(", extra=").append(String.valueOf(j)).toString());
        broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.DIMISS_LOADING").toString());
        mState = State.PAUSED;
        processStopRequest(true);
        return true;
    }

    public void onGainedAudioFocus()
    {
        mAudioFocus = AudioFocus.FOCUSED;
        if(mState == State.PLAYING)
        {
            configAndStartMediaPlayer();
        }
    }

    public void onLostAudioFocus(boolean flag)
    {
        AudioFocus audiofocus;
        if(flag)
        {
            audiofocus = AudioFocus.NO_FOCUS_CAN_DUCK;
        } else
        {
            audiofocus = AudioFocus.NO_FOCUS_NO_DUCK;
        }
        mAudioFocus = audiofocus;
        if(mPlayer != null && mPlayer.isPlaying())
        {
            configAndStartMediaPlayer();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer)
    {
        broadcastAction((new StringBuilder()).append(getPackageName()).append(".action.DIMISS_LOADING").toString());
        mState = State.PLAYING;
        configAndStartMediaPlayer();
        updateNotification(mSongTitle);
    }

    public int onStartCommand(Intent intent, int i, int j)
    {
        String s = intent.getAction();
        String s1 = getPackageName();
        if(!StringUtils.isEmptyString(s))
        {
            if(s.equals((new StringBuilder()).append(s1).append(".action.TOGGLE_PLAYBACK").toString()))
            {
                processTogglePlaybackRequest();
                return 2;
            }
            if(s.equals((new StringBuilder()).append(s1).append(".action.PLAY").toString()))
            {
                processPlayRequest();
                return 2;
            }
            if(s.equals((new StringBuilder()).append(s1).append(".action.PAUSE").toString()))
            {
                processPauseRequest();
                return 2;
            }
            if(s.equals((new StringBuilder()).append(s1).append(".action.NEXT").toString()))
            {
                processNextRequest();
                return 2;
            }
            if(s.equals((new StringBuilder()).append(s1).append(".action.STOP").toString()))
            {
                processStopRequest();
                return 2;
            }
            if(s.equals((new StringBuilder()).append(s1).append(".action.PREVIOUS").toString()))
            {
                processPreviousRequest();
                return 2;
            }
            if(s.equals((new StringBuilder()).append(s1).append(".action.ACTION_SEEK").toString()))
            {
                processSeekBar(intent.getIntExtra("pos", -1));
                return 2;
            }
        }
        return 2;
    }









}
