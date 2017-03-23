package com.mzdevelopment.freemusicplayer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.mzdevelopment.freemusicplayer.MainActivity;
import com.mzdevelopment.freemusicplayer.adapter.GenreAdapter;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.dataMng.JsonParsingUtils;
import com.mzdevelopment.freemusicplayer.dataMng.TotalDataManager;
import com.mzdevelopment.freemusicplayer.object.GenreObject;
import com.mzdevelopment.freemusicplayer.setting.ISettingConstants;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.task.DBTask;
import com.ypyproductions.task.IDBTaskListener;
import com.ypyproductions.utils.IOUtils;
import java.util.ArrayList;
import java.util.Locale;
import com.mzdevelopment.freemusicplayer.R;

public class FragmentMusicGenre extends DBFragment
    implements ICloudMusicPlayerConstants, ISettingConstants
{

    public static final String TAG = "FragmentMusicGenre";
    private MainActivity mContext;
    private DBTask mDBTask;
    private GenreAdapter mGenreAdapter;
    private Handler mHandler;
    private ArrayList mListGenreObjects;
    private ListView mListView;

    public FragmentMusicGenre()
    {
        mHandler = new Handler();
    }

    private void setUpInfo()
    {
        mListView.setAdapter(null);
        if(mGenreAdapter != null)
        {
            mGenreAdapter = null;
        }
        mGenreAdapter = new GenreAdapter(mContext, mListGenreObjects, mContext.mTypefaceNormal);
        mListView.setAdapter(mGenreAdapter);
        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                GenreObject genreobject = (GenreObject)mListGenreObjects.get(i);
                if(genreobject != null)
                {
                    mContext.processSearchData(genreobject.getKeyword(), true);
                }
            }
        });
    }

    private void startGetData()
    {
        mDBTask = new DBTask(new IDBTaskListener() {
            public void onDoInBackground()
            {
                String s = Locale.getDefault().getCountry().toLowerCase(Locale.US);
                String s1;
                ArrayList arraylist;
                if(s.equalsIgnoreCase("BR"))
                {
                    s1 = String.format("genres/genre_%1$s.dat", new Object[] {
                        s
                    });
                } else
                {
                    s1 = String.format("genres/genre_%1$s.dat", new Object[] {
                        "en"
                    });
                }
                arraylist = JsonParsingUtils.parsingGenreObject(IOUtils.readStringFromAssets(mContext, s1));
                if(arraylist != null)
                {
                    TotalDataManager.getInstance().setListGenreObjects(arraylist);
                    mListGenreObjects = arraylist;
                }
            }

            public void onPostExcute()
            {
                mContext.dimissProgressDialog();
                setUpInfo();
            }

            public void onPreExcute()
            {
                mContext.showProgressDialog();
            }
        });
        mDBTask.execute(new Void[0]);
    }

    private void startLoadGenre()
    {
        mListGenreObjects = TotalDataManager.getInstance().getListGenreObjects();
        if(mListGenreObjects != null && mListGenreObjects.size() >= 0)
        {
            setUpInfo();
            return;
        } else
        {
            startGetData();
            return;
        }
    }

    public void findView()
    {
        mContext = (MainActivity)getActivity();
        mListView = (ListView)mRootView.findViewById(R.id.list_genres);
        setAllowFindViewContinous(true);
        startLoadGenre();
    }

    public void onDestroy()
    {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if(mDBTask != null)
        {
            mDBTask.cancel(true);
            mDBTask = null;
        }
    }

    public View onInflateLayout(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return layoutinflater.inflate(R.layout.fragment_genre, viewgroup, false);
    }





/*
    static ArrayList access$102(FragmentMusicGenre fragmentmusicgenre, ArrayList arraylist)
    {
        fragmentmusicgenre.mListGenreObjects = arraylist;
        return arraylist;
    }

*/

}
