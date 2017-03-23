package com.mzdevelopment.freemusicplayer.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;
import com.mzdevelopment.freemusicplayer.MainActivity;
import com.mzdevelopment.freemusicplayer.adapter.LibraryAdapter;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.dataMng.TotalDataManager;
import com.mzdevelopment.freemusicplayer.soundclound.ISoundCloundConstants;
import com.mzdevelopment.freemusicplayer.soundclound.SoundCloundDataMng;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.*;
import com.ypyproductions.utils.DBListExcuteAction;
import com.ypyproductions.utils.StringUtils;
import java.io.File;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class FragmentLibrary extends DBFragment
    implements ICloudMusicPlayerConstants, ISoundCloundConstants
{

    public static final String TAG = "FragmentLibrary";
    private LibraryAdapter mAdapter;
    private MainActivity mContext;
    private DBTask mDBTask;
    private ArrayList mListTrackObjects;
    private ListView mListView;
    private TextView mTvNoResult;

    public FragmentLibrary()
    {
    }

    private String getIdFromContentUri(String s)
    {
        if(s == null)
        	return null;
        String as[];
        Cursor cursor;
        as = (new String[] {
            "_id"
        });
        String as1[] = {
            s
        };
        try{
	        cursor = mContext.getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, as, "_data = ?", as1, null);
	        if(cursor == null)
	        	return null;
	        String s1;
	        if(!cursor.moveToFirst())
	        	return null;
	        s1 = cursor.getString(cursor.getColumnIndex(as[0]));
	        cursor.close();
	        return s1;
        }catch(Exception e){
        	return null;
        }
    }

    private void saveAsNotification(TrackObject trackobject)
    {
        File file = new File(trackobject.getPath());
        if(file != null && file.isFile())
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("_data", file.getAbsolutePath());
            contentvalues.put("title", trackobject.getTitle());
            contentvalues.put("mime_type", "audio/*");
            contentvalues.put("is_notification", Boolean.valueOf(true));
            String s = getIdFromContentUri(file.getAbsolutePath());
            Uri uri;
            if(StringUtils.isEmptyString(s))
            {
                uri = mContext.getContentResolver().insert(android.provider.MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), contentvalues);
            } else
            {
                mContext.getContentResolver().update(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentvalues, "_id = ?", new String[] {
                    s
                });
                uri = Uri.parse(String.format("content://media/external/audio/media/%1$s", new Object[] {
                    s
                }));
            }
            RingtoneManager.setActualDefaultRingtoneUri(mContext, 2, uri);
            mContext.showToast(R.string.info_set_notification_successfully);
        }
    }

    private void saveAsRingtone(TrackObject trackobject)
    {
        File file = new File(trackobject.getPath());
        if(file != null && file.isFile())
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("_data", file.getAbsolutePath());
            contentvalues.put("title", trackobject.getTitle());
            contentvalues.put("mime_type", "audio/*");
            contentvalues.put("is_ringtone", Boolean.valueOf(true));
            String s = getIdFromContentUri(file.getAbsolutePath());
            Uri uri;
            if(StringUtils.isEmptyString(s))
            {
                uri = mContext.getContentResolver().insert(android.provider.MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), contentvalues);
            } else
            {
                mContext.getContentResolver().update(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentvalues, "_id = ?", new String[] {
                    s
                });
                uri = Uri.parse(String.format("content://media/external/audio/media/%1$s", new Object[] {
                    s
                }));
            }
            RingtoneManager.setActualDefaultRingtoneUri(mContext, 1, uri);
            mContext.showToast(R.string.info_set_ringtone_successfully);
        }
    }

    private void setUpInfo(final ArrayList mListNewTrackObjects)
    {
        mListTrackObjects = mListNewTrackObjects;
        if(mListNewTrackObjects != null)
        {
            TextView textview = mTvNoResult;
            byte byte0;
            if(mListNewTrackObjects.size() > 0)
            {
                byte0 = 8;
            } else
            {
                byte0 = 0;
            }
            textview.setVisibility(byte0);
            mListView.setVisibility(0);
            mAdapter = new LibraryAdapter(mContext, mListNewTrackObjects, mContext.mTypefaceBold, mContext.mTypefaceLight);
            mListView.setAdapter(mAdapter);
            mAdapter.setOnDownloadedListener(new com.mzdevelopment.freemusicplayer.adapter.LibraryAdapter.OnDownloadedListener() {
                public void onAddToPlaylist(TrackObject trackobject)
                {
                    mContext.showDialogPlaylist(trackobject);
                }

                public void onDeleteItem(final TrackObject trackobject)
                {
                    mContext.showProgressDialog();
                    final File mOutPutFile = new File(trackobject.getPath());
                    if(mOutPutFile.exists() && mOutPutFile.isFile())
                    {
                        DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                            public void onAction()
                            {
                                if(mOutPutFile.delete())
                                {
                                    mContext.deleteSongFromMediaStore(trackobject.getId());
                                    mListTrackObjects.remove(trackobject);
                                }
                                mContext.runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        mContext.dimissProgressDialog();
                                        notifyData();
                                    }
                                });
                            }
                        });
                    }
                }

                public void onPlayItem(TrackObject trackobject)
                {
                    SoundCloundDataMng.getInstance().setListPlayingTrackObjects(mListNewTrackObjects);
                    mContext.setInfoForPlayingTrack(trackobject, true, true);
                }

                public void onSetAsNotification(TrackObject trackobject)
                {
                    saveAsNotification(trackobject);
                }

                public void onSetAsRingtone(TrackObject trackobject)
                {
                    saveAsRingtone(trackobject);
                }
            });
            return;
        } else
        {
            mTvNoResult.setVisibility(0);
            return;
        }
    }

    public void findView()
    {
        setAllowFindViewContinous(true);
        mContext = (MainActivity)getActivity();
        mListView = (ListView)mRootView.findViewById(R.id.list_tracks);
        mTvNoResult = (TextView)mRootView.findViewById(R.id.tv_no_result);
        mTvNoResult.setTypeface(mContext.mTypefaceNormal);
        ArrayList arraylist = TotalDataManager.getInstance().getListLibraryTrackObjects();
        if(arraylist == null)
        {
            startLoadFromLibrary();
            return;
        } else
        {
            setUpInfo(arraylist);
            return;
        }
    }

    public void notifyData()
    {
        if(mAdapter != null)
        {
            mAdapter.notifyDataSetChanged();
        }
        TextView textview = mTvNoResult;
        byte byte0;
        if(mListTrackObjects != null && mListTrackObjects.size() > 0)
        {
            byte0 = 8;
        } else
        {
            byte0 = 0;
        }
        textview.setVisibility(byte0);
    }

    public View onInflateLayout(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.fragment_downloaded, viewgroup, false);
    }

    public void startLoadFromLibrary()
    {
        mDBTask = new DBTask(new IDBTaskListener() {
            public void onDoInBackground()
            {
                TotalDataManager.getInstance().readLibraryTrack(mContext);
            }

            public void onPostExcute()
            {
                mContext.dimissProgressDialog();
                ArrayList arraylist = TotalDataManager.getInstance().getListLibraryTrackObjects();
                setUpInfo(arraylist);
            }

            public void onPreExcute()
            {
                mContext.showProgressDialog();
            }
        });
        mDBTask.execute(new Void[0]);
    }
}
