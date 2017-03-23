package com.mzdevelopment.freemusicplayer.fragment;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.avocarrot.androidsdk.AvocarrotInstream;
import com.mzdevelopment.freemusicplayer.MainActivity;
import com.mzdevelopment.freemusicplayer.adapter.DetailPlaylistAdapter;
import com.mzdevelopment.freemusicplayer.adapter.PlaylistAdapter;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.dataMng.TotalDataManager;
import com.mzdevelopment.freemusicplayer.object.PlaylistObject;
import com.mzdevelopment.freemusicplayer.soundclound.SoundCloundDataMng;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.*;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.IOUtils;
import java.io.File;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class FragmentPlaylist extends DBFragment
    implements ICloudMusicPlayerConstants
{

    public static final String TAG = "FragmentPlaylist";
    private Button mBtnBack;
    private MainActivity mContext;
    private DBTask mDBTask;
    private DetailPlaylistAdapter mDetailPlaylistAdapter;
    private View mHeaderDetailPlaylistView;
    private View mHeaderPlaylistView;
    private ArrayList mListPlaylistObjects;
    private ListView mListViewDetailPlaylist;
    private ListView mListViewPlaylist;
    private PlaylistAdapter mPlaylistAdapter;
    private TextView mTvNamePlaylist;

    public FragmentPlaylist()
    {
    }

    private void createDialogPlaylist(boolean flag, PlaylistObject playlistobject)
    {
        mContext.createDialogPlaylist(flag, playlistobject, new IDBCallback() {
            public void onAction()
            {
                notifyData();
            }
        });
    }

    private void setUpHeaderForDetailPlaylist()
    {
        mHeaderDetailPlaylistView = mRootView.findViewById(R.id.header_detail_playlist);
        mTvNamePlaylist = (TextView)mHeaderDetailPlaylistView.findViewById(R.id.tv_name_playlist);
        mTvNamePlaylist.setTypeface(mContext.mTypefaceBold);
        mBtnBack = (Button)mHeaderDetailPlaylistView.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                backToPlaylist();
            }
        });
    }

    private void setUpHeaderForPlaylist()
    {
        mHeaderPlaylistView = mRootView.findViewById(R.id.header_playlist);
        ((TextView)mHeaderPlaylistView.findViewById(R.id.tv_add_new_playlist)).setTypeface(mContext.mTypefaceBold);
        mHeaderPlaylistView.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                createDialogPlaylist(false, null);
            }
        });
    }

    private void setUpInfoDetailPlaylist(final PlaylistObject mPlaylistObject)
    {
        ArrayList arraylist;
label0:
        {
            arraylist = mPlaylistObject.getListTrackObjects();
            if(arraylist != null)
            {
                if(mDetailPlaylistAdapter != null)
                {
                    break label0;
                }
                mDetailPlaylistAdapter = new DetailPlaylistAdapter(mContext, arraylist, mContext.mTypefaceBold, mContext.mTypefaceLight, mContext.mImgTrackOptions);
                mListViewDetailPlaylist.setAdapter(mDetailPlaylistAdapter);
                mDetailPlaylistAdapter.setDetailPlaylistAdapterListener(new com.mzdevelopment.freemusicplayer.adapter.DetailPlaylistAdapter.IDetailPlaylistAdapterListener() {
                    public void onPlayingTrack(TrackObject trackobject)
                    {
                        SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mDetailPlaylistAdapter.getListObjects());
                        mContext.setInfoForPlayingTrack(trackobject, true, true);
                    }

                    public void onRemoveToPlaylist(TrackObject trackobject)
                    {
                        TotalDataManager.getInstance().removeTrackToPlaylist(mContext, trackobject, mPlaylistObject, new IDBCallback() {
                            public void onAction()
                            {
                                if(mDetailPlaylistAdapter != null)
                                {
                                    mDetailPlaylistAdapter.notifyDataSetChanged();
                                }
                                if(mPlaylistAdapter != null)
                                {
                                    mPlaylistAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                });
            }
            return;
        }
        mDetailPlaylistAdapter.setListObjects(arraylist, false);
    }

    private void setUpInfoListPlaylist(ArrayList arraylist)
    {
        mListPlaylistObjects = arraylist;
        if(mListPlaylistObjects != null)
        {
            mPlaylistAdapter = new PlaylistAdapter(mContext, arraylist, mContext.mTypefaceBold, mContext.mTypefaceLight);
            AvocarrotInstream avocarrotinstream = new AvocarrotInstream(mPlaylistAdapter, getActivity(), "38d3c24b761a2faaf3fc4870881c952af25c79a6", "f06bf0756855d2369498ef547c48d3ac21862415");
            avocarrotinstream.setSandbox(false);
            mListViewPlaylist.setAdapter(avocarrotinstream);
            mPlaylistAdapter.seOnPlaylistListener(new com.mzdevelopment.freemusicplayer.adapter.PlaylistAdapter.OnPlaylistListener() {
                public void onDeletePlaylist(PlaylistObject playlistobject)
                {
                    showDialogDelete(playlistobject);
                }

                public void onGoToDetail(PlaylistObject playlistobject)
                {
                    showDetailPlaylist(playlistobject);
                }

                public void onPlayAllMusic(PlaylistObject playlistobject)
                {
                    ArrayList arraylist1 = playlistobject.getListTrackObjects();
                    if(arraylist1 != null && arraylist1.size() > 0)
                    {
                        SoundCloundDataMng.getInstance().setListPlayingTrackObjects(arraylist1);
                        mContext.setInfoForPlayingTrack((TrackObject)arraylist1.get(0), true, true);
                        return;
                    } else
                    {
                        mContext.showToast(R.string.info_nosong_playlist);
                        return;
                    }
                }

                public void onRenamePlaylist(PlaylistObject playlistobject)
                {
                    createDialogPlaylist(true, playlistobject);
                }
            });
        }
    }

    private void showDetailPlaylist(PlaylistObject playlistobject)
    {
        mListViewDetailPlaylist.setVisibility(0);
        mListViewPlaylist.setVisibility(8);
        mHeaderDetailPlaylistView.setVisibility(0);
        mHeaderPlaylistView.setVisibility(8);
        mTvNamePlaylist.setText(playlistobject.getName());
        setUpInfoDetailPlaylist(playlistobject);
    }

    private void showDialogDelete(final PlaylistObject mPlaylistObject)
    {
        mContext.showFullDialog(R.string.title_confirm, R.string.info_delete_playlist, R.string.title_ok, R.string.title_cancel, new IDBCallback() {
            public void onAction()
            {
                TotalDataManager.getInstance().removePlaylistObject(mContext, mPlaylistObject);
                if(mPlaylistAdapter != null)
                {
                    mPlaylistAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void startGetPlaylist()
    {
        if(!ApplicationUtils.hasSDcard())
        {
            return;
        }
        ArrayList arraylist = TotalDataManager.getInstance().getListPlaylistObjects();
        if(arraylist != null)
        {
            setUpInfoListPlaylist(arraylist);
            return;
        }
        final File mFile = IOUtils.getDiskCacheDir(mContext, "player");
        if(!mFile.exists())
        {
            mFile.mkdirs();
        }
        mDBTask = new DBTask(new IDBTaskListener() {
            private ArrayList mListPlaylist;
            public void onDoInBackground()
            {
                TotalDataManager.getInstance().readSavedTrack(mContext, mFile);
                TotalDataManager.getInstance().readPlaylistCached(mContext, mFile);
                mListPlaylist = TotalDataManager.getInstance().getListPlaylistObjects();
            }

            public void onPostExcute()
            {
                mContext.dimissProgressDialog();
                setUpInfoListPlaylist(mListPlaylist);
            }

            public void onPreExcute()
            {
                mContext.showProgressDialog();
            }
        });
        mDBTask.execute(new Void[0]);
    }

    public boolean backToPlaylist()
    {
        int i = mListViewDetailPlaylist.getVisibility();
        boolean flag = false;
        if(i == 0)
        {
            mListViewDetailPlaylist.setVisibility(8);
            mListViewPlaylist.setVisibility(0);
            mHeaderDetailPlaylistView.setVisibility(8);
            mHeaderPlaylistView.setVisibility(0);
            flag = true;
        }
        return flag;
    }

    public void findView()
    {
        setAllowFindViewContinous(true);
        mContext = (MainActivity)getActivity();
        mListViewPlaylist = (ListView)mRootView.findViewById(R.id.list_playlist);
        mListViewDetailPlaylist = (ListView)mRootView.findViewById(R.id.list_detail_playlist);
        setUpHeaderForPlaylist();
        setUpHeaderForDetailPlaylist();
        startGetPlaylist();
    }

    public void notifyData()
    {
        if(mPlaylistAdapter != null)
        {
            mPlaylistAdapter.notifyDataSetChanged();
        }
    }

    public View onInflateLayout(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.fragment_playlist_home, viewgroup, false);
    }
}
