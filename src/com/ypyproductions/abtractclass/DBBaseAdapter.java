package com.ypyproductions.abtractclass;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import com.ypyproductions.utils.ResolutionUtils;
import java.util.ArrayList;

public abstract class DBBaseAdapter extends BaseAdapter
{

    public static final long ANIM_DEFAULT_MIN_SPEED = 300L;
    public static final long ANIM_DEFAULT_SPEED = 800L;
    public static final String TAG = "DBBaseAdapter";
    private Interpolator interpolator;
    private boolean isAnimate;
    public Context mContext;
    public ArrayList mListObjects;
    private SparseBooleanArray mPositionsMapper;
    private int screenHeight;
    private int screenWidth;

    public DBBaseAdapter(Activity activity, ArrayList arraylist)
    {
        mContext = activity;
        mListObjects = arraylist;
        mPositionsMapper = new SparseBooleanArray(arraylist.size());
        int ai[] = ResolutionUtils.getDeviceResolution(activity);
        if(ai != null)
        {
            screenWidth = ai[0];
            screenHeight = ai[1];
        }
    }

    public void addPositionMapper(int i, boolean flag)
    {
        mPositionsMapper.put(i, flag);
    }

    public boolean checkPositionMapper(int i)
    {
        return mPositionsMapper.get(i);
    }

    public abstract View getAnimatedView(int i, View view, ViewGroup viewgroup);

    public int getCount()
    {
        if(mListObjects != null)
        {
            return mListObjects.size();
        } else
        {
            return 0;
        }
    }

    public Interpolator getInterpolator()
    {
        return interpolator;
    }

    public Object getItem(int i)
    {
        if(mListObjects != null && mListObjects.size() > 0)
        {
            int j = mListObjects.size();
            if(i >= 0 && i < j)
            {
                return mListObjects.get(i);
            }
        }
        return null;
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public ArrayList getListObjects()
    {
        return mListObjects;
    }

    public abstract View getNormalView(int i, View view, ViewGroup viewgroup);

    public int getScreenHeight()
    {
        return screenHeight;
    }

    public int getScreenWidth()
    {
        return screenWidth;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        if(isAnimate)
        {
            return getAnimatedView(i, view, viewgroup);
        } else
        {
            return getNormalView(i, view, viewgroup);
        }
    }

    public boolean isAnimate()
    {
        return isAnimate;
    }

    public void onDestroy(boolean flag)
    {
        if(mListObjects != null)
        {
            mListObjects.clear();
            if(flag)
            {
                mListObjects = null;
            }
        }
    }

    public void setAnimate(boolean flag)
    {
        isAnimate = flag;
        notifyDataSetChanged();
    }

    public void setInterpolator(Interpolator interpolator1)
    {
        interpolator = interpolator1;
    }

    public void setListObjects(ArrayList arraylist, boolean flag)
    {
        if(arraylist != null)
        {
            if(mListObjects != null && flag)
            {
                mListObjects.clear();
                mListObjects = null;
            }
            mPositionsMapper = null;
            mPositionsMapper = new SparseBooleanArray(arraylist.size());
            mListObjects = arraylist;
            notifyDataSetChanged();
        }
    }

}
