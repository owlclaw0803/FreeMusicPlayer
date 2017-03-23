package com.mzdevelopment.freemusicplayer.adapter;

import android.support.v4.app.FragmentManager;
import com.ypyproductions.abtractclass.fragment.DBFragmentAdapter;
import java.util.ArrayList;

public class DBSlidingTripAdapter extends DBFragmentAdapter
{

    private ArrayList mListTitles;

    public DBSlidingTripAdapter(FragmentManager fragmentmanager, ArrayList arraylist, ArrayList arraylist1)
    {
        super(fragmentmanager, arraylist);
        mListTitles = arraylist1;
    }

    public CharSequence getPageTitle(int i)
    {
        if(mListTitles != null && mListTitles.size() > 0 && i < mListTitles.size() && i >= 0)
        {
            return (CharSequence)mListTitles.get(i);
        } else
        {
            return super.getPageTitle(i);
        }
    }
}
