package com.mzdevelopment.freemusicplayer.fragment;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.*;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.avocarrot.androidsdk.AvocarrotInstream;
import com.mzdevelopment.freemusicplayer.MainActivity;
import com.mzdevelopment.freemusicplayer.adapter.NewTrackAdapter;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.dataMng.TotalDataManager;
import com.mzdevelopment.freemusicplayer.setting.ISettingConstants;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.mzdevelopment.freemusicplayer.soundclound.*;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.*;
import java.util.ArrayList;
import java.util.Iterator;
import com.mzdevelopment.freemusicplayer.R;

public class FragmentSearch extends DBFragment
    implements ICloudMusicPlayerConstants, ISoundCloundConstants, ISettingConstants, com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener
{

    public static final String TAG = "FragmentSearch";
    private int currentPage;
    private boolean isAllowAddPage;
    private boolean isStartAddingPage;
    private MainActivity mContext;
    private DBTask mDBTask;
    private RelativeLayout mFooterView;
    private ArrayList mListTrackObjects;
    private PullToRefreshListView mListView;
    private NewTrackAdapter mTrackAdapter;
    private TextView mTvNoResult;
    protected ProgressDialog progressDialog;

    public FragmentSearch()
    {
        currentPage = 0;
        isAllowAddPage = false;
        isStartAddingPage = false;
    }

    private void hideFooterView()
    {
        if(mFooterView.getVisibility() == 0)
        {
            mFooterView.setVisibility(8);
        }
    }

    private void onLoadNextTrackObject()
    {
        if(!ApplicationUtils.isOnline(mContext))
        {
            hideFooterView();
            mContext.showToast(R.string.info_lose_internet);
            return;
        } else
        {
        	final int offset = mListTrackObjects.size() + 1;
            mDBTask = new DBTask(new IDBTaskListener() {

                private ArrayList mListNewTrackObjects;

                public void onDoInBackground()
                {
                    if(SettingManager.getSearchType(mContext) == 2)
                    {
                        mListNewTrackObjects = mContext.mSoundClound.getListTrackObjectsByGenre(SettingManager.getLastKeyword(mContext), offset, 10);
                        return;
                    } else
                    {
                        mListNewTrackObjects = mContext.mSoundClound.getListTrackObjectsByQuery(SettingManager.getLastKeyword(mContext), offset, 10);
                        return;
                    }
                }

                public void onPostExcute()
                {
                	PullToRefreshListView pulltorefreshlistview;
                	hideFooterView();
                	isStartAddingPage = false;
                    FragmentSearch fragmentsearch;
                    if(mListNewTrackObjects != null && mListNewTrackObjects.size() > 0)
                    {
                        TrackObject trackobject;
                        for(Iterator iterator = mListNewTrackObjects.iterator(); iterator.hasNext(); mListTrackObjects.add(trackobject))
                        {
                            trackobject = (TrackObject)iterator.next();
                        }

                        if(mTrackAdapter != null)
                        	mTrackAdapter.notifyDataSetChanged();
                        currentPage = currentPage + 1;
                        DBLog.d(FragmentSearch.TAG, (new StringBuilder()).append("=========>currentPage=").append(String.valueOf(currentPage)).toString());
                        if(currentPage < 14)
                        	isAllowAddPage = true;
                        else
                        	isAllowAddPage = false;
                    } else
                    	isAllowAddPage = false;
                    pulltorefreshlistview = mListView;
                    if(isAllowAddPage)
                        fragmentsearch = FragmentSearch.this;
                    else
                        fragmentsearch = null;
                    mListView.setOnLastItemVisibleListener(fragmentsearch);
                }

                public void onPreExcute()
                {
                }
            });
            mDBTask.execute(new Void[0]);
            return;
        }
    }

    private void setUpInfo(ArrayList arraylist, boolean flag)
    {
    	mListView.setAdapter(null);
        if(flag && mListTrackObjects != null)
        {
            mListTrackObjects.clear();
            mListTrackObjects = null;
        }
        mListTrackObjects = arraylist;
        if(arraylist == null || arraylist.size() <= 0)
        {
        	mTvNoResult.setVisibility(0);
            return;
        }
        mTvNoResult.setVisibility(8);
        mListView.setVisibility(0);
        mTrackAdapter = new NewTrackAdapter(mContext, arraylist, mContext.mTypefaceBold, mContext.mTypefaceLight, mContext.mImgTrackOptions, mContext.mAvatarOptions);
        AvocarrotInstream avocarrotinstream = new AvocarrotInstream(mTrackAdapter, getActivity(), "38d3c24b761a2faaf3fc4870881c952af25c79a6", "f06bf0756855d2369498ef547c48d3ac21862415");
        avocarrotinstream.setSandbox(false);
        avocarrotinstream.setFrequency(2, 4);
        mListView.setAdapter(avocarrotinstream);
        mTrackAdapter.setNewTrackAdapterListener(new com.mzdevelopment.freemusicplayer.adapter.NewTrackAdapter.INewTrackAdapterListener() {
            public void onAddToPlaylist(TrackObject trackobject)
            {
                mContext.showDialogPlaylist(trackobject);
            }

            public void onDownload(TrackObject trackobject)
            {
            }

            public void onListenDemo(TrackObject trackobject)
            {
                if(!ApplicationUtils.isOnline(mContext))
                {
                    mContext.showToast(R.string.info_server_error);
                    return;
                } else
                {
                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mListTrackObjects);
                    mContext.setInfoForPlayingTrack(trackobject, true, true);
                    return;
                }
            }
        });
        currentPage = mListTrackObjects.size() / 10;
        PullToRefreshListView pulltorefreshlistview;
        FragmentSearch fragmentsearch;
        MediaPlayer mediaplayer;
        ArrayList arraylist1;
        if(currentPage >= 14)
        {
            isAllowAddPage = false;
        } else
        {
            isAllowAddPage = true;
        }
        pulltorefreshlistview = mListView;
        if(isAllowAddPage)
        {
            fragmentsearch = this;
        } else
        {
            fragmentsearch = null;
        }
        pulltorefreshlistview.setOnLastItemVisibleListener(fragmentsearch);
        mediaplayer = SoundCloundDataMng.getInstance().getPlayer();
        if(mediaplayer == null)
        	return;
        try
        {
            if(!mediaplayer.isPlaying())
            	return;
            arraylist1 = SoundCloundDataMng.getInstance().getListPlayingTrackObjects();
            if(arraylist1 != null)
            {
            	if(arraylist1.equals(TotalDataManager.getInstance().getListLibraryTrackObjects()))
                	return;
            }
            SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mListTrackObjects);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return;
        }
    }

    private void showFooterView()
    {
        if(mFooterView.getVisibility() != 0)
        {
            mFooterView.setVisibility(0);
        }
    }

    public void findView()
    {
        setAllowFindViewContinous(true);
        mContext = (MainActivity)getActivity();
        mListView = (PullToRefreshListView)mRootView.findViewById(R.id.list_tracks);
        mTvNoResult = (TextView)mRootView.findViewById(R.id.tv_no_result);
        mTvNoResult.setTypeface(mContext.mTypefaceNormal);
        mFooterView = (RelativeLayout)mRootView.findViewById(R.id.layout_footer);
        ((TextView)mFooterView.findViewById(R.id.tv_message)).setTypeface(mContext.mTypefaceNormal);
        mListView.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener() {
            public void onRefresh(PullToRefreshBase pulltorefreshbase)
            {
                startGetData(true, SettingManager.getLastKeyword(mContext), true);
            }
        });
        startGetData(false, SettingManager.getLastKeyword(mContext), false);
    }

    public View onInflateLayout(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.fragment_search, viewgroup, false);
    }

    public void onLastItemVisible()
    {
label0:
        {
            if(mTrackAdapter != null)
            {
                DBLog.d(TAG, (new StringBuilder()).append("==============>isAllowAddPage=").append(isAllowAddPage).append("===>isRefreshing=").append(mListView.isRefreshing()).toString());
                if(!isAllowAddPage || mListView.isRefreshing())
                {
                    break label0;
                }
                if(ApplicationUtils.isOnline(mContext))
                {
                    showFooterView();
                    if(!isStartAddingPage)
                    {
                        isStartAddingPage = true;
                        onLoadNextTrackObject();
                    }
                }
            }
            return;
        }
        hideFooterView();
    }

    public void startGetData(final boolean isRefresh, final String keyword, boolean flag)
    {
        if(!(isRefresh || flag)){
	        ArrayList arraylist = TotalDataManager.getInstance().getListCurrrentTrackObjects();
	        if(arraylist != null){
		        mListView.onRefreshComplete();
		        setUpInfo(arraylist, isRefresh);
		        return;
	        }
        }
        if(!ApplicationUtils.isOnline(mContext))
        {
            mListView.onRefreshComplete();
            mContext.showToast(R.string.info_lose_internet);
            if(mTrackAdapter == null)
            {
                mTvNoResult.setVisibility(0);
                return;
            }
        } else
        {
            if(isStartAddingPage)
            {
                mListView.onRefreshComplete();
                return;
            }
            isStartAddingPage = false;
            isAllowAddPage = false;
            currentPage = 0;
            if(StringUtils.isEmptyString(keyword))
            {
                setUpInfo(new ArrayList(), true);
                return;
            } else {
                mDBTask = new DBTask(new IDBTaskListener() {

                    private ArrayList mListNewTopObjects;
                    private ArrayList mListNewTrackObjects;

                    public void onDoInBackground()
                    {
                        if(SettingManager.getSearchType(mContext) == 2)
                        {
                            mListNewTrackObjects = mContext.mSoundClound.getListTrackObjectsByGenre(keyword, 0, 10);
                        } else
                        {
                            mListNewTrackObjects = mContext.mSoundClound.getListTrackObjectsByQuery(keyword, 0, 10);
                        }
                        if(mListNewTrackObjects == null || mListNewTrackObjects.size() <= 0){
                        	if(SettingManager.getSearchType(mContext) == 2)
                            {
                                SettingManager.setSearchType(mContext, 1);
                            }
                        }else{
	                        TotalDataManager.getInstance().setListCurrrentTrackObjects(mListNewTrackObjects);
	                        SettingManager.setLastKeyword(mContext, keyword);
                        }
                        if(!mContext.isFirstTime)
                        {
                            mContext.isFirstTime = true;
                            if(TotalDataManager.getInstance().getListTopMusicObjects() == null || TotalDataManager.getInstance().getListTopMusicObjects().size() == 0)
                            {
                                mListNewTopObjects = mContext.mSoundClound.getListTopMusic(SettingManager.getLanguage(mContext), 80);
                                if(mListNewTopObjects != null && mListNewTopObjects.size() > 0)
                                {
                                    TotalDataManager.getInstance().setListTopMusicObjects(mListNewTopObjects);
                                }
                                mContext.runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        mContext.setUpInfoForTop(mListNewTopObjects);
                                    }
                                });
                            }
                        }
                        return;
                    }

                    public void onPostExcute()
                    {
                        mContext.dimissProgressDialog();
                        mListView.onRefreshComplete();
                        mContext.hiddenVirtualKeyBoard();
                        setUpInfo(mListNewTrackObjects, true);
                    }

                    public void onPreExcute()
                    {
                        mTvNoResult.setVisibility(8);
                        if(!isRefresh)
                        {
                            mContext.showProgressDialog();
                        }
                    }
                });
                mDBTask.execute(new Void[0]);
                return;
            }
        }
    }
}
