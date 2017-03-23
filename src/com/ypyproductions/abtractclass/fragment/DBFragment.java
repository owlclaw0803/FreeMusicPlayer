package com.ypyproductions.abtractclass.fragment;

import android.os.Bundle;
import android.support.v4.app.*;
import android.view.*;
import com.ypyproductions.utils.StringUtils;

public abstract class DBFragment extends Fragment
    implements IDBFragmentConstants
{

    public static final String TAG = "DBFragment";
    private boolean isAllowFindViewContinous;
    private boolean isExtractData;
    public int mIdFragment;
    public String mNameFragment;
    public View mRootView;

    public DBFragment()
    {
    }

    public void backToHome(FragmentActivity fragmentactivity)
    {
        FragmentManager fragmentmanager;
        FragmentTransaction fragmenttransaction;
        fragmentmanager = fragmentactivity.getSupportFragmentManager();
        fragmenttransaction = fragmentmanager.beginTransaction();
        fragmenttransaction.remove(this);
        Fragment fragment;
        if(mIdFragment <= 0){
        	boolean flag = StringUtils.isEmptyString(mNameFragment);
            fragment = null;
            if(!flag)
            {
                fragment = fragmentmanager.findFragmentByTag(mNameFragment);
            }
        }else
        	fragment = fragmentmanager.findFragmentById(mIdFragment);
        if(fragment != null)
        {
            fragmenttransaction.show(fragment);
        }
        fragmenttransaction.commit();
        return;
    }

    public abstract void findView();

    public Fragment getFragmentHome(FragmentActivity fragmentactivity)
    {
        Fragment fragment;
        if(mIdFragment > 0)
        {
            fragment = fragmentactivity.getSupportFragmentManager().findFragmentById(mIdFragment);
        } else
        {
            boolean flag = StringUtils.isEmptyString(mNameFragment);
            fragment = null;
            if(!flag)
            {
                return fragmentactivity.getSupportFragmentManager().findFragmentByTag(mNameFragment);
            }
        }
        return fragment;
    }

    public boolean isAllowFindViewContinous()
    {
        return isAllowFindViewContinous;
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        mRootView = onInflateLayout(layoutinflater, viewgroup, bundle);
        return mRootView;
    }

    public void onExtractData()
    {
        Bundle bundle = getArguments();
        if(bundle != null)
        {
            mNameFragment = bundle.getString("name_fragment");
            mIdFragment = bundle.getInt("id_fragment");
        }
    }

    public abstract View onInflateLayout(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle);

    public void onStart()
    {
        super.onStart();
        if(!isExtractData)
        {
            isExtractData = true;
            onExtractData();
            findView();
        } else
        if(isAllowFindViewContinous)
        {
            findView();
            return;
        }
    }

    public void setAllowFindViewContinous(boolean flag)
    {
        isAllowFindViewContinous = flag;
    }

}
