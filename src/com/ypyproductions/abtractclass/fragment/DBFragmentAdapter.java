package com.ypyproductions.abtractclass.fragment;

import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.View;
import java.util.ArrayList;

public class DBFragmentAdapter extends FragmentPagerAdapter
{

    public static final String TAG = "DBFragmentAdapter";
    private ArrayList listFragments;

    public DBFragmentAdapter(FragmentManager fragmentmanager, ArrayList arraylist)
    {
        super(fragmentmanager);
        listFragments = arraylist;
    }

    public void destroyItem(View view, int i, Object obj)
    {
        try
        {
            ((ViewPager)view).removeView((View)obj);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public int getCount()
    {
        return listFragments.size();
    }

    public Fragment getItem(int i)
    {
        return (Fragment)listFragments.get(i);
    }

}
