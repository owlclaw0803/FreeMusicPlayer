package com.mzdevelopment.freemusicplayer.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import com.avocarrot.androidsdk.AvocarrotInstream;
import com.mzdevelopment.freemusicplayer.MainActivity;
import com.mzdevelopment.freemusicplayer.adapter.TopMusicAdapter;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.dataMng.TotalDataManager;
import com.mzdevelopment.freemusicplayer.object.TopMusicObject;
import com.mzdevelopment.freemusicplayer.setting.ISettingConstants;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.mzdevelopment.freemusicplayer.soundclound.ISoundCloundConstants;
import com.mzdevelopment.freemusicplayer.soundclound.SoundCloundAPI;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.StringUtils;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class FragmentTopMusic extends DBFragment
    implements ICloudMusicPlayerConstants, ISoundCloundConstants, ISettingConstants
{

    public static final String TAG = "FragmentTopMusic";
    private TopMusicAdapter mAdapter;
    private MainActivity mContext;
    private DBTask mDBTask;
    private ArrayList mListHotObjects;
    private PullToRefreshListView mListView;
    private TextView mTvNoResult;
    protected ProgressDialog progressDialog;

    public FragmentTopMusic()
    {
    }

    public void findView()
    {
        setAllowFindViewContinous(true);
        mContext = (MainActivity)getActivity();
        mListView = (PullToRefreshListView)mRootView.findViewById(R.id.list_hot_music);
        mTvNoResult = (TextView)mRootView.findViewById(R.id.tv_no_result);
        mTvNoResult.setTypeface(mContext.mTypefaceNormal);
        mListView.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener() {
            public void onRefresh(PullToRefreshBase pulltorefreshbase)
            {
                startGetData(true);
            }
        });
        startGetData(false);
    }

    public View onInflateLayout(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.fragment_hot, viewgroup, false);
    }

    public void setUpInfo(boolean flag, ArrayList arraylist)
    {
        mListView.setAdapter(null);
        if(flag && mListHotObjects != null)
        {
            mListHotObjects.clear();
            mListHotObjects = null;
        }
        mListHotObjects = arraylist;
        if(arraylist != null && arraylist.size() > 0)
        {
            mTvNoResult.setVisibility(8);
            mListView.setVisibility(0);
            mAdapter = new TopMusicAdapter(mContext, arraylist, mContext.mTypefaceBold, mContext.mTypefaceLight, mContext.mImgTrackOptions);
            AvocarrotInstream avocarrotinstream = new AvocarrotInstream(mAdapter, getActivity(), "38d3c24b761a2faaf3fc4870881c952af25c79a6", "f06bf0756855d2369498ef547c48d3ac21862415");
            avocarrotinstream.setSandbox(false);
            avocarrotinstream.setFrequency(2, 4);
            mListView.setAdapter(avocarrotinstream);
            mAdapter.setOnTopMusicListener(new com.mzdevelopment.freemusicplayer.adapter.TopMusicAdapter.OnTopMusicListener() {
                public void onSearchDetail(TopMusicObject topmusicobject)
                {
                    String s = (new StringBuilder()).append(topmusicobject.getName()).append(" - ").append(topmusicobject.getArtist()).toString();
                    if(!StringUtils.isEmptyString(s))
                    {
                        mContext.processSearchData(s, false);
                    }
                }
            });
            return;
        } else
        {
            mTvNoResult.setVisibility(0);
            return;
        }
    }

    public void startGetData(final boolean isRefresh)
    {
        if(!isRefresh){
	        ArrayList arraylist = TotalDataManager.getInstance().getListTopMusicObjects();
	        if(!(arraylist == null || arraylist.size() <= 0)){
	        	mListView.onRefreshComplete();
	        	setUpInfo(isRefresh, arraylist);
	        	return;
	        }
        }
        if(!ApplicationUtils.isOnline(mContext))
        {
            mListView.onRefreshComplete();
            mContext.showToast(R.string.info_lose_internet);
            if(mAdapter == null)
            {
                mTvNoResult.setVisibility(0);
                return;
            }
        } else
        {
            mDBTask = new DBTask(new IDBTaskListener() {

                private ArrayList mListNewTopObjects;
                public void onDoInBackground()
                {
                    mListNewTopObjects = mContext.mSoundClound.getListTopMusic(SettingManager.getLanguage(mContext), 80);
                    if(mListNewTopObjects != null && mListNewTopObjects.size() > 0)
                    {
                        TotalDataManager.getInstance().setListTopMusicObjects(mListNewTopObjects);
                    }
                }

                public void onPostExcute()
                {
                    mContext.dimissProgressDialog();
                    mListView.onRefreshComplete();
                    mContext.hiddenVirtualKeyBoard();
                    setUpInfo(true, mListNewTopObjects);
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
