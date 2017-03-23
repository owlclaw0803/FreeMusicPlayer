package com.mzdevelopment.freemusicplayer.dataMng;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import com.mzdevelopment.freemusicplayer.DBFragmentActivity;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.object.PlaylistObject;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.utils.*;
import java.io.File;
import java.util.*;
import org.json.JSONArray;
import com.mzdevelopment.freemusicplayer.R;

public class TotalDataManager
    implements ICloudMusicPlayerConstants
{

    public static final String TAG = "TotalDataManager";
    private static TotalDataManager totalDataManager;
    private ArrayList listCurrrentTrackObjects;
    private ArrayList listGenreObjects;
    private ArrayList listLibraryTrackObjects;
    private ArrayList listPlaylistObjects;
    private ArrayList listSavedTrackObjects;
    private ArrayList listTopMusicObjects;

    private TotalDataManager()
    {
    }

    private void checkDestroy(ArrayList arraylist)
    {
        if(arraylist != null)
        {
            DBLog.d(TAG, "===========>da destroy");
            arraylist.clear();
        }
    }

    private void filterSongOfPlaylistId(PlaylistObject playlistobject)
    {
        if(listSavedTrackObjects != null && listSavedTrackObjects.size() > 0)
        {
            ArrayList arraylist = playlistobject.getListTrackIds();
            if(arraylist != null && arraylist.size() > 0)
            {
                Iterator iterator = arraylist.iterator();
label0:
                do
                {
                    if(!iterator.hasNext())
                    {
                        break;
                    }
                    Long long1 = (Long)iterator.next();
                    Iterator iterator1 = listSavedTrackObjects.iterator();
                    TrackObject trackobject;
                    do
                    {
                        if(!iterator1.hasNext())
                        {
                            continue label0;
                        }
                        trackobject = (TrackObject)iterator1.next();
                    } while(trackobject.getId() != long1.longValue());
                    playlistobject.addTrackObject(trackobject, false);
                } while(true);
            }
        }
    }

    public static TotalDataManager getInstance()
    {
        if(totalDataManager == null)
        {
            totalDataManager = new TotalDataManager();
        }
        return totalDataManager;
    }

    private ArrayList getListMusicsFromLibrary(Context context)
    {
        android.net.Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, "is_music = 1", null, null);
        String s = TAG;
        StringBuilder stringbuilder = (new StringBuilder()).append("Query finished. ");
        String s1;
        if(cursor == null)
        {
            s1 = "Returned NULL.";
        } else
        {
            s1 = "Returned a cursor.";
        }
        DBLog.d(s, stringbuilder.append(s1).toString());
        if(cursor == null)
        {
            DBLog.d(TAG, "Failed to retrieve music: cursor is null :-(");
            return null;
        }
        if(!cursor.moveToFirst())
        {
            DBLog.d(TAG, "Failed to move cursor to first row (no query results).");
            return null;
        }
        int i = cursor.getColumnIndex("artist");
        int j = cursor.getColumnIndex("title");
        int k = cursor.getColumnIndex("album");
        int l = cursor.getColumnIndex("duration");
        int i1 = cursor.getColumnIndex("_id");
        int j1 = cursor.getColumnIndex("_data");
        int k1 = cursor.getColumnIndex("date_modified");
        ArrayList arraylist = new ArrayList();
        do
        {
            DBLog.d(TAG, (new StringBuilder()).append("ID: ").append(cursor.getString(i1)).append(" Title: ").append(cursor.getString(j)).toString());
            long l1 = cursor.getLong(i1);
            String s2 = cursor.getString(i);
            String s3 = cursor.getString(j);
            long l2 = cursor.getLong(l);
            String s4 = cursor.getString(k);
            String s5 = cursor.getString(j1);
            Date date = new Date(1000L * cursor.getLong(k1));
            if(!StringUtils.isEmptyString(s5))
            {
                File file = new File(s5);
                if(file.exists() && file.isFile())
                {
                    TrackObject trackobject = new TrackObject(l1, s3, date, l2, s2, "", "", s4, -1L, s5);
                    trackobject.setStreamable(true);
                    arraylist.add(trackobject);
                }
            }
        } while(cursor.moveToNext());
        return arraylist;
    }

    public void addPlaylistObject(final Context mContext, PlaylistObject playlistobject)
    {
        if(listPlaylistObjects != null && playlistobject != null)
        {
            listPlaylistObjects.add(playlistobject);
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction()
                {
                    savePlaylistObjects(mContext);
                }
            });
        }
    }

    public void addTrackToPlaylist(final DBFragmentActivity mContext, TrackObject trackobject, PlaylistObject playlistobject, boolean flag, IDBCallback idbcallback)
    {
        synchronized(this){
        	if(trackobject == null || playlistobject == null)
            	return;
            if(playlistobject.isSongAlreadyExited(trackobject.getId())){
            	if(flag)
                {
                	mContext.runOnUiThread(new Runnable() {
                        public void run()
                        {
                            mContext.showToast(R.string.info_song_already_playlist);
                        }
                    });
                }
            	return;
            }
            TrackObject trackobject1;
            trackobject1 = trackobject.clone();
            playlistobject.addTrackObject(trackobject1, true);
            boolean flag1 = true;
            for(Iterator iterator = listSavedTrackObjects.iterator(); iterator.hasNext();)
            {
                if(((TrackObject)iterator.next()).getId() == trackobject1.getId())
                {
                    flag1 = false;
                }
            }
            if(flag1)
            	listSavedTrackObjects.add(trackobject1);
            if(idbcallback != null)
            	idbcallback.onAction();
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction()
                {
                    savePlaylistObjects(mContext);
                    saveTrackObjects(mContext);
                }
            });
        }
    }

    public void editPlaylistObject(final Context mContext, PlaylistObject playlistobject, String s)
    {
        if(listPlaylistObjects != null && playlistobject != null && !StringUtils.isEmptyString(s))
        {
            playlistobject.setName(s);
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction()
                {
                    savePlaylistObjects(mContext);
                }
            });
        }
    }

    public TrackObject getLibraryTrack(long l)
    {
label0:
        {
            if(listLibraryTrackObjects == null || listLibraryTrackObjects.size() <= 0)
            {
                break label0;
            }
            Iterator iterator = listLibraryTrackObjects.iterator();
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

    public ArrayList getListCurrrentTrackObjects()
    {
        return listCurrrentTrackObjects;
    }

    public ArrayList getListGenreObjects()
    {
        return listGenreObjects;
    }

    public ArrayList getListLibraryTrackObjects()
    {
        return listLibraryTrackObjects;
    }

    public ArrayList getListPlaylistObjects()
    {
        return listPlaylistObjects;
    }

    public ArrayList getListSavedTrackObjects()
    {
        return listSavedTrackObjects;
    }

    public ArrayList getListTopMusicObjects()
    {
        return listTopMusicObjects;
    }

    public void onDestroy()
    {
        checkDestroy(listLibraryTrackObjects);
        checkDestroy(listCurrrentTrackObjects);
        checkDestroy(listSavedTrackObjects);
        if(listTopMusicObjects != null)
        {
            listTopMusicObjects.clear();
            listTopMusicObjects = null;
        }
        if(listPlaylistObjects != null)
        {
            listPlaylistObjects.clear();
            listPlaylistObjects = null;
        }
        if(listGenreObjects != null)
        {
            listGenreObjects.clear();
            listGenreObjects = null;
        }
        totalDataManager = null;
    }

    public void readLibraryTrack(Context context)
    {
        File file = new File(Environment.getExternalStorageDirectory(), "player");
        if(!file.exists())
        {
            file.mkdirs();
        }
        ArrayList arraylist = getListMusicsFromLibrary(context);
        ArrayList arraylist1;
        String s;
        StringBuilder stringbuilder;
        int i;
        if(arraylist == null)
        {
            arraylist1 = new ArrayList();
        } else
        {
            arraylist1 = arraylist;
        }
        setListLibraryTrackObjects(arraylist1);
        s = TAG;
        stringbuilder = (new StringBuilder()).append("========>mListSavedTrackObject=");
        if(arraylist != null)
        {
            i = arraylist.size();
        } else
        {
            i = 0;
        }
        DBLog.d(s, stringbuilder.append(i).toString());
    }

    public void readPlaylistCached(Context context, File file)
    {
        ArrayList arraylist = JsonParsingUtils.parsingPlaylistObject(IOUtils.readString(context, file.getAbsolutePath(), "list_playlists.dat"));
        Iterator iterator;
        if(arraylist != null && arraylist.size() > 0)
        {
            setListPlaylistObjects(arraylist);
        } else
        {
            arraylist = new ArrayList();
            setListPlaylistObjects(arraylist);
        }
        for(iterator = arraylist.iterator(); iterator.hasNext(); filterSongOfPlaylistId((PlaylistObject)iterator.next())) { }
    }

    public void readSavedTrack(Context context, File file)
    {
        ArrayList arraylist = JsonParsingUtils.parsingListSavingTrackObject(IOUtils.readString(context, file.getAbsolutePath(), "list_tracks.dat"));
        TotalDataManager totaldatamanager = getInstance();
        ArrayList arraylist1;
        String s;
        StringBuilder stringbuilder;
        int i;
        if(arraylist == null)
        {
            arraylist1 = new ArrayList();
        } else
        {
            arraylist1 = arraylist;
        }
        totaldatamanager.setListSavedTrackObjects(arraylist1);
        s = TAG;
        stringbuilder = (new StringBuilder()).append("========>mListSavedTrackObject=");
        if(arraylist != null)
        {
            i = arraylist.size();
        } else
        {
            i = 0;
        }
        DBLog.d(s, stringbuilder.append(i).toString());
    }

    public void removePlaylistObject(final Context mContext, int i)
    {
        if(listPlaylistObjects != null && listPlaylistObjects.size() > 0)
        {
            Iterator iterator = listPlaylistObjects.iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                if(((PlaylistObject)iterator.next()).getId() != (long)i)
                {
                    continue;
                }
                iterator.remove();
                break;
            } while(true);
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction()
                {
                    savePlaylistObjects(mContext);
                }
            });
        }
    }

    public void removePlaylistObject(final Context mContext, PlaylistObject playlistobject)
    {
        if(listPlaylistObjects != null && listPlaylistObjects.size() > 0)
        {
            listPlaylistObjects.remove(playlistobject);
            ArrayList arraylist = playlistobject.getListTrackObjects();
            boolean flag = false;
            if(arraylist != null)
            {
                int i = arraylist.size();
                flag = false;
                if(i > 0)
                {
                    Iterator iterator = arraylist.iterator();
                    do
                    {
                        if(!iterator.hasNext())
                        {
                            break;
                        }
                        TrackObject trackobject = (TrackObject)iterator.next();
                        boolean flag1 = true;
                        Iterator iterator1 = listPlaylistObjects.iterator();
                        do
                        {
                            if(!iterator1.hasNext())
                            {
                                break;
                            }
                            if(!((PlaylistObject)iterator1.next()).isSongAlreadyExited(trackobject.getId()))
                            {
                                continue;
                            }
                            flag1 = false;
                            break;
                        } while(true);
                        if(flag1)
                        {
                            listSavedTrackObjects.remove(trackobject);
                            flag = true;
                        }
                    } while(true);
                    arraylist.clear();
                }
            }
            final boolean isGlobal = flag;
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction()
                {
                    savePlaylistObjects(mContext);
                    if(isGlobal)
                    {
                        saveTrackObjects(mContext);
                    }
                }
            });
        }
    }

    public void removeTrackToPlaylist(final DBFragmentActivity mContext, TrackObject trackobject, PlaylistObject playlistobject, IDBCallback idbcallback)
    {
        synchronized(this){
        	if(trackobject == null || playlistobject == null)
        		return;
            playlistobject.removeTrackObject(trackobject);
            boolean flag = true;
            Iterator iterator = listPlaylistObjects.iterator();
            while(iterator.hasNext()){
            	if(((PlaylistObject)iterator.next()).isSongAlreadyExited(trackobject.getId())){
            		flag = false;
            		break;
            	}
            }
            DBLog.d(TAG, (new StringBuilder()).append("============>removeTrackToPlaylist=").append(flag).toString());
            if(flag)
            	listSavedTrackObjects.remove(trackobject);
            if(idbcallback != null)
            	idbcallback.onAction();
            DBListExcuteAction.getInstance().queueAction(new IDBCallback() {
                public void onAction()
                {
                    savePlaylistObjects(mContext);
                    saveTrackObjects(mContext);
                }
            });
        }
    }

    public void savePlaylistObjects(Context context)
    {
        synchronized(this){
	        boolean flag = ApplicationUtils.hasSDcard();
	        if(flag){
	        	File file;
	            JSONArray jsonarray;
	            file = IOUtils.getDiskCacheDir(context, "player");
	            if(!file.exists())
	            {
	                file.mkdirs();
	            }
	            jsonarray = new JSONArray();
	            if(listPlaylistObjects != null)
	            {
	                for(Iterator iterator = listPlaylistObjects.iterator(); iterator.hasNext(); jsonarray.put(((PlaylistObject)iterator.next()).toJson())) { }
	            }
	            DBLog.d(TAG, (new StringBuilder()).append("=============>savePlaylistObjects=").append(jsonarray.toString()).toString());
	            IOUtils.writeString(file.getAbsolutePath(), "list_playlists.dat", jsonarray.toString());
	        }
        }
    }

    public void saveTrackObjects(Context context)
    {
        synchronized(this){
        	boolean flag = ApplicationUtils.hasSDcard();
            if(flag){
            	File file;
                JSONArray jsonarray;
                file = IOUtils.getDiskCacheDir(context, "player");
                if(!file.exists())
                {
                    file.mkdirs();
                }
                jsonarray = new JSONArray();
                if(listSavedTrackObjects != null && listSavedTrackObjects.size() > 0)
                {
                    for(Iterator iterator = listSavedTrackObjects.iterator(); iterator.hasNext(); jsonarray.put(((TrackObject)iterator.next()).toJsonObject())) { }
                }
                DBLog.d(TAG, (new StringBuilder()).append("=============>saveTrackObjects=").append(jsonarray.toString()).toString());
                IOUtils.writeString(file.getAbsolutePath(), "list_tracks.dat", jsonarray.toString());
            }
        }
    }

    public void setListCurrrentTrackObjects(ArrayList arraylist)
    {
        listCurrrentTrackObjects = arraylist;
    }

    public void setListGenreObjects(ArrayList arraylist)
    {
        listGenreObjects = arraylist;
    }

    public void setListLibraryTrackObjects(ArrayList arraylist)
    {
        listLibraryTrackObjects = arraylist;
    }

    public void setListPlaylistObjects(ArrayList arraylist)
    {
        listPlaylistObjects = arraylist;
    }

    public void setListSavedTrackObjects(ArrayList arraylist)
    {
        listSavedTrackObjects = arraylist;
    }

    public void setListTopMusicObjects(ArrayList arraylist)
    {
        listTopMusicObjects = arraylist;
    }

}
