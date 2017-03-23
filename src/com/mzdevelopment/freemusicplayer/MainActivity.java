package com.mzdevelopment.freemusicplayer;

import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.database.MatrixCursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.mzdevelopment.freemusicplayer.adapter.DBSlidingTripAdapter;
import com.mzdevelopment.freemusicplayer.adapter.SuggestionAdapter;
import com.mzdevelopment.freemusicplayer.adapter.TrackAdapter;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.dataMng.TotalDataManager;
import com.mzdevelopment.freemusicplayer.dataMng.XMLParsingData;
import com.mzdevelopment.freemusicplayer.fragment.FragmentLibrary;
import com.mzdevelopment.freemusicplayer.fragment.FragmentMusicGenre;
import com.mzdevelopment.freemusicplayer.fragment.FragmentPlaylist;
import com.mzdevelopment.freemusicplayer.fragment.FragmentSearch;
import com.mzdevelopment.freemusicplayer.fragment.FragmentTopMusic;
import com.mzdevelopment.freemusicplayer.object.DBImageLoader;
import com.mzdevelopment.freemusicplayer.object.PlaylistObject;
import com.mzdevelopment.freemusicplayer.playerservice.IMusicConstant;
import com.mzdevelopment.freemusicplayer.setting.ISettingConstants;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.mzdevelopment.freemusicplayer.soundclound.ISoundCloundConstants;
import com.mzdevelopment.freemusicplayer.soundclound.SoundCloundAPI;
import com.mzdevelopment.freemusicplayer.soundclound.SoundCloundDataMng;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.mzdevelopment.freemusicplayer.view.PagerSlidingTabStrip;
import com.google.android.gms.ads.*;
import com.mzdevelopment.freemusicplayer.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.utils.*;
import com.ypyproductions.webservice.DownloadUtils;

import java.util.*;

public class MainActivity extends DBFragmentActivity implements IDBFragmentConstants, ISoundCloundConstants, ISettingConstants, IMusicConstant
{
    private class MusicPlayerBroadcast extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {
            if(intent == null)
            	return;
            String s1;
            String s2;
            int i;
            boolean flag;
            String s = intent.getAction();
            if(StringUtils.isEmptyString(s))
            	return;
            s1 = getPackageName();
            if(!s.equals((new StringBuilder()).append(s1).append(".action.ACTION_BROADCAST_PLAYER").toString()))
            	return;
            s2 = intent.getStringExtra("action");
            if(StringUtils.isEmptyString(s2))
            	return;
            if(s2.equals((new StringBuilder()).append(s1).append(".action.NEXT").toString()))
            {
                onUpdateStatePausePlay(false);
                return;
            }
            try
            {
                if(s2.equals((new StringBuilder()).append(s1).append(".action.LOADING").toString()))
                {
                    showProgressDialog();
                    return;
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
                return;
            }
            if(s2.equals((new StringBuilder()).append(s1).append(".action.DIMISS_LOADING").toString()))
            {
                dimissProgressDialog();
                return;
            }
            if(s2.equals((new StringBuilder()).append(s1).append(".action.PAUSE").toString()) || s2.equals((new StringBuilder()).append(s1).append(".action.STOP").toString()))
            {
                onUpdateStatePausePlay(false);
                return;
            }
            TrackObject trackobject;
            if(!s2.equals((new StringBuilder()).append(s1).append(".action.PLAY").toString()))
            {
            	if(!s2.equals((new StringBuilder()).append(s1).append(".action.UPDATE_POS").toString()))
                	return;
                i = intent.getIntExtra("pos", -1);
                if(i <= 0)
                	return;
                if(mCurrentTrack != null)
                {
                    long l = i / 1000;
                    String s3 = String.valueOf((int)(l / 60L));
                    String s4 = String.valueOf((int)(l % 60L));
                    if(s3.length() < 2)
                    {
                        s3 = (new StringBuilder()).append("0").append(s3).toString();
                    }
                    if(s4.length() < 2)
                    {
                        s4 = (new StringBuilder()).append("0").append(s4).toString();
                    }
                    mTvCurrentTime.setText((new StringBuilder()).append(s3).append(":").append(s4).toString());
                    int j = (int)(100F * ((float)i / (float)mCurrentTrack.getDuration()));
                    mSeekbar.setProgress(j);
                }
                return;
            }
            onUpdateStatePausePlay(true);
            trackobject = SoundCloundDataMng.getInstance().getCurrentTrackObject();
            if(trackobject == null)
            	return;
            MainActivity mainactivity = MainActivity.this;
            if(mLayoutPlayMusic.getVisibility() == 0)
            {
                flag = true;
            } else
            {
                flag = false;
            }
            mainactivity.setInfoForPlayingTrack(trackobject, flag, false);
            return;
        }

        private MusicPlayerBroadcast()
        {
            super();
        }

    }


    public static final String TAG = "MainActivity";
    private AdView adView;
    public boolean isFirstTime;
    public DisplayImageOptions mAvatarOptions;
    private android.widget.RelativeLayout.LayoutParams mBottomLayoutParams;
    private android.widget.RelativeLayout.LayoutParams mBottomSmallLayoutParams;
    private Button mBtnNext;
    private Button mBtnPlay;
    private Button mBtnPrev;
    private Button mBtnSmallNext;
    private Button mBtnSmallPlay;
    private CheckBox mCbRepeat;
    private CheckBox mCbShuffe;
    private String mColumns[];
    private TrackObject mCurrentTrack;
    private MatrixCursor mCursor;
    private Date mDate;
    private ImageView mImgAvatar;
    private ImageView mImgSmallSong;
    private ImageView mImgTrack;
    public DisplayImageOptions mImgTrackOptions;
    public InterstitialAd mInterstitialAd;
    private RelativeLayout mLayoutControl;
    private RelativeLayout mLayoutPlayMusic;
    private RelativeLayout mLayoutSmallMusic;
    private ArrayList mListFragments;
    private String mListStr[];
    private ArrayList mListSuggestionStr;
    private ArrayList mListTitle;
    private Menu mMenu;
    private PagerSlidingTabStrip mPagerTabStrip;
    private MusicPlayerBroadcast mPlayerBroadcast;
    private SeekBar mSeekbar;
    public SoundCloundAPI mSoundClound;
    private SuggestionAdapter mSuggestAdapter;
    private DBSlidingTripAdapter mTabAdapters;
    private Object mTempData[];
    private android.widget.RelativeLayout.LayoutParams mTopLayoutParams;
    private android.widget.RelativeLayout.LayoutParams mTopSmallLayoutParams;
    private TextView mTvCurrentTime;
    private TextView mTvDuration;
    private TextView mTvLink;
    private TextView mTvPlayCount;
    private TextView mTvSmallSong;
    private TextView mTvTime;
    private TextView mTvTitleSongs;
    private TextView mTvUserName;
    private ViewPager mViewPager;
    private int numPlays;
    private int numSearches;
    protected ProgressDialog progressDialog;
    private SearchView searchView;

    public MainActivity()
    {
        mSoundClound = new SoundCloundAPI(SOUNDCLOUND_CLIENT_ID, SOUNDCLOUND_CLIENT_SECRET);
        mListFragments = new ArrayList();
        mListTitle = new ArrayList();
        numSearches = 0;
        numPlays = 0;
    }

    private void createTab()
    {
        mPagerTabStrip.setVisibility(0);
        Fragment fragment = Fragment.instantiate(this, com.mzdevelopment.freemusicplayer.fragment.FragmentSearch.class.getName(), null);
        mListFragments.add(fragment);
        mListTitle.add(getString(R.string.title_home).toUpperCase(Locale.US));
        Fragment fragment1 = Fragment.instantiate(this, com.mzdevelopment.freemusicplayer.fragment.FragmentTopMusic.class.getName(), null);
        mListFragments.add(fragment1);
        mListTitle.add(getString(R.string.title_hot_music).toUpperCase(Locale.US));
        Fragment fragment2 = Fragment.instantiate(this, com.mzdevelopment.freemusicplayer.fragment.FragmentLibrary.class.getName(), null);
        mListFragments.add(fragment2);
        mListTitle.add(getString(R.string.title_download_songs).toUpperCase(Locale.US));
        Fragment fragment3 = Fragment.instantiate(this, com.mzdevelopment.freemusicplayer.fragment.FragmentPlaylist.class.getName(), null);
        mListFragments.add(fragment3);
        mListTitle.add(getString(R.string.title_playlist).toUpperCase(Locale.US));
        Fragment fragment4 = Fragment.instantiate(this, com.mzdevelopment.freemusicplayer.fragment.FragmentMusicGenre.class.getName(), null);
        mListFragments.add(fragment4);
        mListTitle.add(getString(R.string.title_genre).toUpperCase(Locale.US));
        mTabAdapters = new DBSlidingTripAdapter(getSupportFragmentManager(), mListFragments, mListTitle);
        mViewPager.setAdapter(mTabAdapters);
        mPagerTabStrip.setViewPager(mViewPager);
        mViewPager.setCurrentItem(1, true);
        mViewPager.setOffscreenPageLimit(5);
    }

    private FragmentMusicGenre getFragmentGenre()
    {
        if(mListFragments != null && mListFragments.size() > 0)
        {
            Fragment fragment = (Fragment)mListFragments.get(4);
            if(fragment instanceof FragmentMusicGenre)
            {
                return (FragmentMusicGenre)fragment;
            }
        }
        return null;
    }

    private FragmentLibrary getFragmentLibrary()
    {
        if(mListFragments != null && mListFragments.size() > 0)
        {
            Fragment fragment = (Fragment)mListFragments.get(2);
            if(fragment instanceof FragmentLibrary)
            {
                return (FragmentLibrary)fragment;
            }
        }
        return null;
    }

    private FragmentSearch getFragmentMainSearch()
    {
        if(mListFragments != null && mListFragments.size() > 0)
        {
            Fragment fragment = (Fragment)mListFragments.get(0);
            if(fragment instanceof FragmentSearch)
            {
                return (FragmentSearch)fragment;
            }
        }
        return null;
    }

    private FragmentPlaylist getFragmentPlaylist()
    {
        if(mListFragments != null && mListFragments.size() > 0)
        {
            Fragment fragment = (Fragment)mListFragments.get(3);
            if(fragment instanceof FragmentPlaylist)
            {
                return (FragmentPlaylist)fragment;
            }
        }
        return null;
    }

    private FragmentTopMusic getFragmentTop()
    {
        if(mListFragments != null && mListFragments.size() > 0)
        {
            Fragment fragment = (Fragment)mListFragments.get(1);
            if(fragment instanceof FragmentTopMusic)
            {
                return (FragmentTopMusic)fragment;
            }
        }
        return null;
    }

    private void handleIntent(Intent intent)
    {
        if(intent != null && "android.intent.action.SEARCH".equals(intent.getAction()))
        {
            processSearchData(intent.getStringExtra("query"), false);
        }
    }

    private void onHiddenPlay(boolean flag)
    {
        if(flag)
        {
            mBtnPlay.setVisibility(0);
            mBtnPlay.setBackgroundResource(R.drawable.ic_play);
            mLayoutSmallMusic.setBackgroundResource(R.drawable.ic_play_arrow_white_36dp);
            mLayoutSmallMusic.setVisibility(8);
            Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.STOP").toString());
            intent.setPackage(getPackageName());
            startService(intent);
        }
        mLayoutPlayMusic.setVisibility(8);
    }

    private void onProcessPausePlayAction()
    {
        if(SoundCloundDataMng.getInstance().setCurrentIndex(mCurrentTrack))
        {
            Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.TOGGLE_PLAYBACK").toString());
            intent.setPackage(getPackageName());
            startService(intent);
        }
    }

    private void onUpdateStatePausePlay(boolean flag)
    {
        Button button = mBtnPlay;
        int i;
        Button button1;
        int j;
        if(!flag)
        {
            i = R.drawable.ic_play;
        } else
        {
            i = R.drawable.ic_pause;
        }
        button.setBackgroundResource(i);
        button1 = mBtnSmallPlay;
        if(!flag)
        {
            j = R.drawable.ic_play_arrow_white_36dp;
        } else
        {
            j = R.drawable.ic_pause_white_36dp;
        }
        button1.setBackgroundResource(j);
        if(flag)
        {
            numPlays = 1 + numPlays;
            if(numPlays == 6)
            {
                numPlays = 1;
                if(mLayoutPlayMusic.getVisibility() == 0 && mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
            }
        }
    }

    private void requestNewInterstitial()
    {
        com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice("0B8C2CBE428E35B4864AA5EFB41DB402").build();
        mInterstitialAd.loadAd(adrequest);
    }

    private void seekAudio(int i)
    {
        Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.ACTION_SEEK").toString());
        intent.setPackage(getPackageName());
        intent.putExtra("pos", i);
        startService(intent);
    }

    private void setUpLayoutAdmob()
    {
        RelativeLayout relativelayout = (RelativeLayout)findViewById(R.id.layout_ad);
        if(ApplicationUtils.isOnline(this))
        {
            adView = new AdView(this);
            adView.setAdUnitId(ICloudMusicPlayerConstants.ADMOB_ID_BANNER);
            adView.setAdSize(AdSize.SMART_BANNER);
            relativelayout.addView(adView);
            com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice("0B8C2CBE428E35B4864AA5EFB41DB402").build();
            adView.loadAd(adrequest);
            mLayoutSmallMusic.setLayoutParams(mTopSmallLayoutParams);
            return;
        } else
        {
            relativelayout.setVisibility(8);
            mLayoutSmallMusic.setLayoutParams(mBottomSmallLayoutParams);
            return;
        }
    }

    private void setUpPlayMusicLayout()
    {
        mLayoutPlayMusic = (RelativeLayout)findViewById(R.id.layout_listen_music);
        mSeekbar = (SeekBar)findViewById(R.id.seekBar1);
        mSeekbar.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
            {
                if(flag && mCurrentTrack != null)
                {
                    int j = (int)((float)((long)i * mCurrentTrack.getDuration()) / 100F);
                    DBLog.d(MainActivity.TAG, (new StringBuilder()).append("=================>currentPos=").append(j).toString());
                    seekAudio(j);
                }
            }

            public void onStartTrackingTouch(SeekBar seekbar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekbar)
            {
            }
        });
        mCbShuffe = (CheckBox)findViewById(R.id.cb_shuffle);
        mCbShuffe.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                SettingManager.setShuffle(MainActivity.this, mCbShuffe.isChecked());
            }
        });
        mCbShuffe.setChecked(SettingManager.getShuffle(this));
        mCbRepeat = (CheckBox)findViewById(R.id.cb_repeat);
        mCbRepeat.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                SettingManager.setRepeat(MainActivity.this, mCbRepeat.isChecked());
            }
        });
        mCbRepeat.setChecked(SettingManager.getRepeat(this));
        mLayoutPlayMusic.findViewById(R.id.img_bg).setOnTouchListener(new android.view.View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionevent)
            {
                return true;
            }
        });
        findViewById(R.id.btn_add_playlist).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(mCurrentTrack != null)
                {
                    showDialogPlaylist(mCurrentTrack);
                }
            }
        });
        mLayoutControl = (RelativeLayout)findViewById(R.id.layout_control);
        mTopLayoutParams = (android.widget.RelativeLayout.LayoutParams)mLayoutControl.getLayoutParams();
        mBottomLayoutParams = new android.widget.RelativeLayout.LayoutParams(-1, (int)ResolutionUtils.convertDpToPixel(this, 60F));
        mBottomLayoutParams.addRule(12);
        mTvLink = (TextView)findViewById(R.id.tv_link);
        mTvLink.setTypeface(mTypefaceNormal);
        mTvLink.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(mCurrentTrack != null)
                {
                    Intent intent = new Intent(MainActivity.this, com.mzdevelopment.freemusicplayer.ShowUrlActivity.class);
                    intent.setPackage(getPackageName());
                    intent.putExtra("url", mCurrentTrack.getPermalinkUrl());
                    intent.putExtra("KEY_HEADER", mCurrentTrack.getTitle());
                    startActivity(intent);
                }
            }
        });
        mTvUserName = (TextView)findViewById(R.id.tv_username);
        mTvUserName.setTypeface(mTypefaceBold);
        mTvTime = (TextView)findViewById(R.id.tv_time);
        mTvTime.setTypeface(mTypefaceLight);
        mImgAvatar = (ImageView)findViewById(R.id.img_avatar1);
        mImgTrack = (ImageView)findViewById(R.id.img_track);
        mTvCurrentTime = (TextView)findViewById(R.id.tv_current_time);
        mTvCurrentTime.setTypeface(mTypefaceLight);
        mTvDuration = (TextView)findViewById(R.id.tv_duration);
        mTvDuration.setTypeface(mTypefaceLight);
        mTvTitleSongs = (TextView)findViewById(R.id.tv_song);
        mTvTitleSongs.setTypeface(mTypefaceBold);
        mTvPlayCount = (TextView)findViewById(R.id.tv_playcount);
        mTvPlayCount.setTypeface(mTypefaceLight);
        mBtnPlay = (Button)findViewById(R.id.btn_play);
        mBtnPrev = (Button)findViewById(R.id.btn_prev);
        mBtnNext = (Button)findViewById(R.id.btn_next);
        ((Button)findViewById(R.id.btn_equalizer)).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, com.mzdevelopment.freemusicplayer.EqualizerActivity.class);
                intent.setPackage(getPackageName());
                DirectionUtils.changeActivity(MainActivity.this, R.anim.slide_in_from_right, R.anim.slide_out_to_left, false, intent);
            }
        });
        mBtnNext.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                nextTrack();
            }
        });
        mBtnPrev.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                prevTrack();
            }
        });
        mBtnPlay.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                onProcessPausePlayAction();
            }
        });
    }

    private void setUpSmallMusicLayout()
    {
        mLayoutSmallMusic = (RelativeLayout)findViewById(R.id.layout_child_listen);
        mBtnSmallPlay = (Button)findViewById(R.id.btn_small_play);
        mBtnSmallNext = (Button)findViewById(R.id.btn_small_next);
        ((Button)findViewById(R.id.btn_small_close)).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                onHiddenPlay(true);
            }
        });
        mLayoutSmallMusic.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                if(mLayoutPlayMusic.getVisibility() != 0)
                {
                    mLayoutPlayMusic.setVisibility(0);
                }
            }
        });
        mBtnSmallPlay.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                onProcessPausePlayAction();
            }
        });
        mBtnSmallNext.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                nextTrack();
            }
        });
        mTvSmallSong = (TextView)findViewById(R.id.tv_small_song);
        mImgSmallSong = (ImageView)findViewById(R.id.img_small_track);
        mTopSmallLayoutParams = (android.widget.RelativeLayout.LayoutParams)mLayoutSmallMusic.getLayoutParams();
        mBottomSmallLayoutParams = new android.widget.RelativeLayout.LayoutParams(-1, (int)ResolutionUtils.convertDpToPixel(this, 70F));
        mBottomSmallLayoutParams.addRule(12);
    }

    private void showDiaglogAboutUs()
    {
        (new android.app.AlertDialog.Builder(this)).setTitle(R.string.title_about_us).setItems(R.array.list_share, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                if(i == 0)
                {
                    ShareActionUtils.shareViaEmail(MainActivity.this, ICloudMusicPlayerConstants.YOUR_EMAIL_CONTACT, "", "");
                } else
                {
                    if(i == 1)
                    {
                        Object aobj[] = new Object[1];
                        aobj[0] = getPackageName();
                        String s = String.format("https://play.google.com/store/apps/details?id=%1$s", aobj);
                        ShareActionUtils.goToUrl(MainActivity.this, s);
                        return;
                    }
                    if(i == 2)
                    {
                        ShareActionUtils.goToUrl(MainActivity.this, "https://play.google.com/store/apps/details?id=kawkaw.callrecorder");
                        return;
                    }
                }
            }
        }).setPositiveButton(getString(R.string.title_cancel), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
            }
        }).create().show();
    }

    private void startSuggestion(final String search)
    {
        if(!StringUtils.isEmptyString(search))
        {
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction()
                {
                    Object aobj[] = new Object[1];
                    aobj[0] = StringUtils.urlEncodeString(search);
                    String s = String.format("http://suggestqueries.google.com/complete/search?ds=yt&output=toolbar&hl=en-US&q=%1$s", aobj);
                    DBLog.d(MainActivity.TAG, (new StringBuilder()).append("===============>url suggest=").append(s).toString());
                    java.io.InputStream inputstream = DownloadUtils.download(s);
                    if(inputstream != null)
                    {
                        final ArrayList arraylist = XMLParsingData.parsingSuggestion(inputstream);
                        if(arraylist != null)
                        {
                            runOnUiThread(new Runnable() {
                                public void run()
                                {
                                    searchView.setSuggestionsAdapter(null);
                                    mSuggestAdapter = null;
                                    if(MainActivity.this.mListSuggestionStr != null)
                                    {
                                    	MainActivity.this.mListSuggestionStr.clear();
                                    	MainActivity.this.mListSuggestionStr = null;
                                    }
                                    MainActivity.this.mListSuggestionStr = arraylist;
                                    mTempData = null;
                                    mColumns = null;
                                    if(mCursor != null)
                                    {
                                        mCursor.close();
                                        mCursor = null;
                                    }
                                    mColumns = (new String[] {
                                        "_id", "text"
                                    });
                                    MainActivity mainactivity = MainActivity.this;
                                    Object aobj[] = new Object[2];
                                    aobj[0] = Integer.valueOf(0);
                                    aobj[1] = "default";
                                    mainactivity.mTempData = aobj;
                                    mCursor = new MatrixCursor(mColumns);
                                    int i = mListSuggestionStr.size();
                                    mCursor.close();
                                    for(int j = 0; j < i; j++)
                                    {
                                        mTempData[0] = Integer.valueOf(j);
                                        mTempData[1] = mListSuggestionStr.get(j);
                                        mCursor.addRow(mTempData);
                                    }

                                    mSuggestAdapter = new SuggestionAdapter(MainActivity.this, mCursor, arraylist);
                                    searchView.setSuggestionsAdapter(mSuggestAdapter);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void createDialogPlaylist(final boolean isEdit, final PlaylistObject mPlaylistObject, final IDBCallback mCallback)
    {
        final EditText mEdPlaylistName = new EditText(this);
        if(isEdit)
        {
            mEdPlaylistName.setText(mPlaylistObject.getName());
        }
        android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(this)).setTitle(getString(R.string.title_playlist_name)).setView(mEdPlaylistName).setPositiveButton(getString(R.string.title_save), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                String s = mEdPlaylistName.getText().toString();
                if(StringUtils.isEmptyString(s))
                {
                    showToast(R.string.info_playlistname_error);
                } else
                {
                    if(!isEdit)
                    {
                        PlaylistObject playlistobject = new PlaylistObject(System.currentTimeMillis(), s);
                        playlistobject.setListTrackIds(new ArrayList());
                        TotalDataManager.getInstance().addPlaylistObject(MainActivity.this, playlistobject);
                    } else
                    {
                        TotalDataManager.getInstance().editPlaylistObject(MainActivity.this, mPlaylistObject, s);
                    }
                    if(mCallback != null)
                    {
                        mCallback.onAction();
                        return;
                    }
                }
            }
        }).setNegativeButton(getString(R.string.title_cancel), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
            }
        });
        builder.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialoginterface, int i, KeyEvent keyevent)
            {
                return i == 4;
            }
        });
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
        alertdialog.setCanceledOnTouchOutside(true);
        alertdialog.setCancelable(true);
    }

    public void hiddenVirtualKeyBoard()
    {
        if(searchView != null)
        {
            searchView.clearFocus();
            ApplicationUtils.hiddenVirtualKeyboard(this, searchView);
        }
    }

    public boolean hideLayoutPlay()
    {
        int i = mLayoutPlayMusic.getVisibility();
        boolean flag = false;
        if(i == 0)
        {
            onHiddenPlay(false);
            flag = true;
        }
        return flag;
    }

    protected void nextTrack()
    {
        Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.NEXT").toString());
        intent.setPackage(getPackageName());
        startService(intent);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        SettingManager.setFirstTime(this, true);
        com.nostra13.universalimageloader.core.ImageLoaderConfiguration imageloaderconfiguration = (new com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder(this)).memoryCacheExtraOptions(400, 400).diskCacheExtraOptions(400, 400, null).threadPriority(3).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(0x3200000).imageDownloader(new DBImageLoader(this)).tasksProcessingOrder(QueueProcessingType.FIFO).writeDebugLogs().build();
        ImageLoader.getInstance().init(imageloaderconfiguration);
        mImgTrackOptions = (new com.nostra13.universalimageloader.core.DisplayImageOptions.Builder()).showImageOnLoading(R.drawable.music_note).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        mAvatarOptions = (new com.nostra13.universalimageloader.core.DisplayImageOptions.Builder()).showImageOnLoading(R.drawable.ic_account_circle_grey).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mPagerTabStrip = (PagerSlidingTabStrip)findViewById(R.id.pagertabstrip);
        mPagerTabStrip.setVisibility(8);
        mPagerTabStrip.setIndicatorColor(getResources().getColor(R.color.tab_indicator_color));
        mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.main_color));
        mPagerTabStrip.setTextColor(getResources().getColor(R.color.white));
        setUpPlayMusicLayout();
        setUpSmallMusicLayout();
        setUpLayoutAdmob();
        mDate = new Date();
        registerPlayerBroadCastReceiver();
        createTab();
        Intent intent = getIntent();
        if(intent != null && intent.getLongExtra("songId", -1L) > 0L)
        {
            TrackObject trackobject = SoundCloundDataMng.getInstance().getCurrentTrackObject();
            if(trackobject != null)
            {
                setInfoForPlayingTrack(trackobject, true, true);
            }
        }
        handleIntent(getIntent());
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(ICloudMusicPlayerConstants.ADMOB_ID_INTERTESTIAL);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed()
            {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(Html.fromHtml((new StringBuilder()).append("<font color = #ffffff>").append(getResources().getString(R.string.title_search)).append("</font>").toString()));
        SearchManager searchmanager = (SearchManager)getSystemService("search");
        searchView.setSearchableInfo(searchmanager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String s)
            {
                startSuggestion(s);
                return false;
            }

            public boolean onQueryTextSubmit(String s)
            {
                processSearchData(s, false);
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean flag)
            {
                if(!flag)
                {
                    MenuItem menuitem = mMenu.findItem(R.id.action_search);
                    if(menuitem != null)
                    {
                        menuitem.collapseActionView();
                    }
                    searchView.setQuery("", false);
                }
            }
        });
        searchView.setOnSuggestionListener(new android.widget.SearchView.OnSuggestionListener() {
            public boolean onSuggestionClick(int i)
            {
                if(mListSuggestionStr != null && mListSuggestionStr.size() > 0)
                {
                    searchView.setQuery((CharSequence)mListSuggestionStr.get(i), false);
                    processSearchData((String)mListSuggestionStr.get(i), false);
                }
                return false;
            }

            public boolean onSuggestionSelect(int i)
            {
                return false;
            }
        });
        mMenu = menu;
        return true;
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(mPlayerBroadcast != null)
        {
            unregisterReceiver(mPlayerBroadcast);
            mPlayerBroadcast = null;
        }
        if(mListFragments != null)
        {
            mListFragments.clear();
            mListFragments = null;
        }
        mColumns = null;
        mTempData = null;
        try
        {
            if(mCursor != null)
            {
                mCursor.close();
                mCursor = null;
            }
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void onDestroyData()
    {
        super.onDestroyData();
        Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.STOP").toString());
        intent.setPackage(getPackageName());
        startService(intent);
        SoundCloundDataMng.getInstance().onDestroy();
        showIntertestialAds();
        TotalDataManager.getInstance().onDestroy();
        SettingManager.setOnline(this, false);
        ImageLoader.getInstance().stop();
        ImageLoader.getInstance().clearDiskCache();
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(i == 4)
        {
            boolean flag = hideLayoutPlay();
            if(flag)
            {
                return flag;
            }
            if(mViewPager.getCurrentItem() == 3)
            {
                FragmentPlaylist fragmentplaylist = getFragmentPlaylist();
                if(fragmentplaylist != null && fragmentplaylist.backToPlaylist())
                {
                    return true;
                }
            }
            if(mViewPager.getCurrentItem() != 0)
            {
                mViewPager.setCurrentItem(0, true);
                return true;
            }
        }
        return super.onKeyDown(i, keyevent);
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        default:
        	break;
        case R.id.action_info:
        	showDiaglogAboutUs();
        	break;
        }
        return super.onOptionsItemSelected(menuitem);
    }

    protected void prevTrack()
    {
        Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.PREVIOUS").toString());
        intent.setPackage(getPackageName());
        startService(intent);
    }

    public void processSearchData(String s, boolean flag)
    {
        if(!StringUtils.isEmptyString(s))
        {
            String s1;
            FragmentSearch fragmentsearch;
            if(flag)
            {
                SettingManager.setSearchType(this, 2);
            } else
            {
                SettingManager.setSearchType(this, 1);
            }
            s1 = StringUtils.urlEncodeString(s);
            onHiddenPlay(false);
            fragmentsearch = getFragmentMainSearch();
            if(fragmentsearch != null)
            {
                mViewPager.setCurrentItem(0, true);
                fragmentsearch.startGetData(false, s1, true);
            }
            numSearches = 1 + numSearches;
            if(numSearches == 3)
            {
                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
                numSearches = 1;
            }
        }
    }

    public void registerPlayerBroadCastReceiver()
    {
        if(mPlayerBroadcast != null)
        {
            return;
        } else
        {
            mPlayerBroadcast = new MusicPlayerBroadcast();
            IntentFilter intentfilter = new IntentFilter();
            intentfilter.addAction((new StringBuilder()).append(getPackageName()).append(".action.ACTION_BROADCAST_PLAYER").toString());
            registerReceiver(mPlayerBroadcast, intentfilter);
            return;
        }
    }

    public void setInfoForPlayingTrack(TrackObject trackobject, boolean flag, boolean flag1)
    {
        mCurrentTrack = trackobject;
        Log.d("trackobjectooo", trackobject.getTitle());
        Log.d("trackobjectooo", (new StringBuilder()).append("AutoPlay : ").append(flag1).toString());
        RelativeLayout relativelayout = mLayoutPlayMusic;
        int i;
        Date date;
        String s;
        long l;
        String s1;
        String s2;
        if(flag)
        {
            i = 0;
        } else
        {
            i = 8;
        }
        relativelayout.setVisibility(i);
        if(mLayoutSmallMusic.getVisibility() != 0)
        {
            mLayoutSmallMusic.setVisibility(0);
        }
        mLayoutSmallMusic.setBackgroundColor(getResources().getColor(R.color.main_color));
        mTvUserName.setText(trackobject.getUsername());
        date = trackobject.getCreatedDate();
        if(date != null)
        {
            String s7 = TrackAdapter.getStringTimeAgo(this, (mDate.getTime() - date.getTime()) / 1000L);
            mTvTime.setText(s7);
        }
        s = trackobject.getArtworkUrl();
        if(StringUtils.isEmptyString(s) || s.equals("null"))
        {
            s = trackobject.getAvatarUrl();
        }
        if(!StringUtils.isEmptyString(s) && s.startsWith("http"))
        {
            String s6 = s.replace("large", "crop");
            ImageLoader.getInstance().displayImage(s6, mImgTrack, mImgTrackOptions);
            ImageLoader.getInstance().displayImage(s6, mImgSmallSong, mImgTrackOptions);
        } else
        {
            Uri uri = trackobject.getURI();
            if(uri != null)
            {
                String s5 = uri.toString();
                ImageLoader.getInstance().displayImage(s5, mImgTrack, mImgTrackOptions);
                ImageLoader.getInstance().displayImage(s5, mImgSmallSong, mImgTrackOptions);
            } else
            {
                mImgTrack.setImageResource(R.drawable.music_note);
                mImgSmallSong.setImageResource(R.drawable.ic_music_default);
            }
        }
        if(!StringUtils.isEmptyString(trackobject.getAvatarUrl()) && trackobject.getAvatarUrl().startsWith("http"))
        {
            ImageLoader.getInstance().displayImage(trackobject.getAvatarUrl(), mImgAvatar, mAvatarOptions);
        } else
        {
            mImgAvatar.setImageResource(R.drawable.ic_account_circle_grey);
        }
        if(!StringUtils.isEmptyString(trackobject.getPermalinkUrl()))
        {
            TextView textview = mTvLink;
            String s4 = getString(R.string.format_soundcloud_url);
            Object aobj[] = new Object[1];
            aobj[0] = trackobject.getPermalinkUrl();
            textview.setText(String.format(s4, aobj));
        } else
        {
            mTvLink.setText("");
        }
        if(trackobject.getPlaybackCount() > 0L)
        {
            findViewById(R.id.layout_playcount).setVisibility(0);
            String s3 = TrackAdapter.formatVisualNumber(trackobject.getPlaybackCount(), ",");
            mTvPlayCount.setText(s3);
        } else
        {
            findViewById(R.id.layout_playcount).setVisibility(8);
        }
        mTvTitleSongs.setText(trackobject.getTitle());
        mTvSmallSong.setText(trackobject.getTitle());
        mTvCurrentTime.setText("00:00");
        mSeekbar.setProgress(0);
        l = trackobject.getDuration() / 1000L;
        s1 = String.valueOf((int)(l / 60L));
        s2 = String.valueOf((int)(l % 60L));
        if(s1.length() < 2)
        {
            s1 = (new StringBuilder()).append("0").append(s1).toString();
        }
        if(s2.length() < 2)
        {
            s2 = (new StringBuilder()).append("0").append(s2).toString();
        }
        mTvDuration.setText((new StringBuilder()).append(s1).append(":").append(s2).toString());
        if(StringUtils.isEmptyString(trackobject.getPath()))
        {
            mLayoutControl.setLayoutParams(mTopLayoutParams);
        } else
        {
            mLayoutControl.setLayoutParams(mBottomLayoutParams);
        }
        DBLog.d(TAG, (new StringBuilder()).append("=======================>isAutoPlay=").append(flag1).toString());
        if(flag1)
        {
            TrackObject trackobject1 = SoundCloundDataMng.getInstance().getCurrentTrackObject();
            boolean flag2;
            if(trackobject1 != null && trackobject1.getId() == trackobject.getId())
            {
                flag2 = true;
            } else
            {
                flag2 = false;
            }
            DBLog.d(TAG, (new StringBuilder()).append("=======================>isPlayingTrack=").append(flag2).toString());
            if(!flag2)
            {
                if(SoundCloundDataMng.getInstance().setCurrentIndex(trackobject))
                {
                    Intent intent1 = new Intent((new StringBuilder()).append(getPackageName()).append(".action.PLAY").toString());
                    intent1.setPackage(getPackageName());
                    startService(intent1);
                }
            } else
            {
                MediaPlayer mediaplayer = SoundCloundDataMng.getInstance().getPlayer();
                if(mediaplayer != null)
                {
                    onUpdateStatePausePlay(mediaplayer.isPlaying());
                    return;
                }
                onUpdateStatePausePlay(false);
                if(SoundCloundDataMng.getInstance().setCurrentIndex(trackobject))
                {
                    Intent intent = new Intent((new StringBuilder()).append(getPackageName()).append(".action.PLAY").toString());
                    intent.setPackage(getPackageName());
                    startService(intent);
                    return;
                }
            }
        }
    }

    public void setUpInfoForTop(ArrayList arraylist)
    {
        FragmentTopMusic fragmenttopmusic = getFragmentTop();
        if(fragmenttopmusic != null)
        {
            fragmenttopmusic.setUpInfo(true, arraylist);
        }
    }

    public void showDialogPlaylist(final TrackObject mTrackObject)
    {
        final ArrayList mListPlaylist = TotalDataManager.getInstance().getListPlaylistObjects();
        if(mListPlaylist != null && mListPlaylist.size() > 0)
        {
            int i = mListPlaylist.size();
            mListStr = new String[i];
            for(int j = 0; j < i; j++)
            {
                PlaylistObject playlistobject = (PlaylistObject)mListPlaylist.get(j);
                mListStr[j] = playlistobject.getName();
            }

            (new android.app.AlertDialog.Builder(this)).setTitle(R.string.title_select_playlist).setItems(mListStr, new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int k)
                {
                    TotalDataManager.getInstance().addTrackToPlaylist(MainActivity.this, mTrackObject, (PlaylistObject)mListPlaylist.get(k), true, new IDBCallback() {
                        public void onAction()
                        {
                            updateDataOfPlaylist();
                        }
                    });
                    mListStr = null;
                }
            }).setPositiveButton(R.string.title_cancel, new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int k)
                {
                    mListStr = null;
                }
            }).create().show();
            return;
        } else
        {
            mListStr = getResources().getStringArray(R.array.list_create_playlist);
            (new android.app.AlertDialog.Builder(this)).setTitle(R.string.title_select_playlist).setItems(mListStr, new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int k)
                {
                    createDialogPlaylist(false, null, new IDBCallback() {
                        public void onAction()
                        {
                            updateDataOfPlaylist();
                            ArrayList arraylist = TotalDataManager.getInstance().getListPlaylistObjects();
                            TotalDataManager.getInstance().addTrackToPlaylist(MainActivity.this, mTrackObject, (PlaylistObject)arraylist.get(0), true, new IDBCallback() {
                                public void onAction()
                                {
                                    updateDataOfPlaylist();
                                }
                            });
                        }
                    });
                    mListStr = null;
                }
            }).setPositiveButton(R.string.title_cancel, new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int k)
                {
                    mListStr = null;
                }
            }).create().show();
            return;
        }
    }

    public void updateDataOfPlaylist()
    {
        FragmentPlaylist fragmentplaylist = getFragmentPlaylist();
        if(fragmentplaylist != null)
        {
            fragmentplaylist.notifyData();
        }
    }







/*
    static ArrayList access$1102(MainActivity mainactivity, ArrayList arraylist)
    {
        mainactivity.mListSuggestionStr = arraylist;
        return arraylist;
    }

*/



/*
    static SuggestionAdapter access$1202(MainActivity mainactivity, SuggestionAdapter suggestionadapter)
    {
        mainactivity.mSuggestAdapter = suggestionadapter;
        return suggestionadapter;
    }

*/



/*
    static Object[] access$1302(MainActivity mainactivity, Object aobj[])
    {
        mainactivity.mTempData = aobj;
        return aobj;
    }

*/



/*
    static String[] access$1402(MainActivity mainactivity, String as[])
    {
        mainactivity.mColumns = as;
        return as;
    }

*/



/*
    static MatrixCursor access$1502(MainActivity mainactivity, MatrixCursor matrixcursor)
    {
        mainactivity.mCursor = matrixcursor;
        return matrixcursor;
    }

*/


/*
    static String[] access$1702(MainActivity mainactivity, String as[])
    {
        mainactivity.mListStr = as;
        return as;
    }

*/
}
