package com.mzdevelopment.freemusicplayer.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import com.mzdevelopment.freemusicplayer.R;

public class SlidingTabLayout extends HorizontalScrollView
{
    private class InternalViewPagerListener
        implements android.support.v4.view.ViewPager.OnPageChangeListener
    {

        private int mScrollState;

        public void onPageScrollStateChanged(int i)
        {
            mScrollState = i;
            if(mViewPagerPageChangeListener != null)
            {
                mViewPagerPageChangeListener.onPageScrollStateChanged(i);
            }
        }

        public void onPageScrolled(int i, float f, int j)
        {
            int k = mTabStrip.getChildCount();
            if(k != 0 && i >= 0 && i < k)
            {
                mTabStrip.onViewPagerPageChanged(i, f);
                View view = mTabStrip.getChildAt(i);
                int l;
                if(view != null)
                {
                    l = (int)(f * (float)view.getWidth());
                } else
                {
                    l = 0;
                }
                scrollToTab(i, l);
                if(mViewPagerPageChangeListener != null)
                {
                    mViewPagerPageChangeListener.onPageScrolled(i, f, j);
                    return;
                }
            }
        }

        public void onPageSelected(int i)
        {
            if(mScrollState == 0)
            {
                mTabStrip.onViewPagerPageChanged(i, 0.0F);
                scrollToTab(i, 0);
            }
            if(mViewPagerPageChangeListener != null)
            {
                mViewPagerPageChangeListener.onPageSelected(i);
            }
        }

        private InternalViewPagerListener()
        {
            super();
        }

    }

    private class TabClickListener
        implements android.view.View.OnClickListener
    {
        public void onClick(View view)
        {
            int i = 0;
            do
            {
label0:
                {
                    if(i < mTabStrip.getChildCount())
                    {
                        if(view != mTabStrip.getChildAt(i))
                        {
                            break label0;
                        }
                        mViewPager.setCurrentItem(i);
                    }
                    return;
                }
                i++;
            } while(true);
        }

        private TabClickListener()
        {
            super();
        }

    }

    public static interface TabColorizer
    {

        public abstract int getDividerColor(int i);

        public abstract int getIndicatorColor(int i);
    }


    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private static final int TITLE_OFFSET_DIPS = 24;
    private final SlidingTabStrip mTabStrip;
    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private int mTitleOffset;
    private ViewPager mViewPager;
    private android.support.v4.view.ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    public SlidingTabLayout(Context context)
    {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);
        mTitleOffset = (int)(24F * getResources().getDisplayMetrics().density);
        mTabStrip = new SlidingTabStrip(context);
        addView(mTabStrip, -1, -2);
    }

    private void populateTabStrip()
    {
        PagerAdapter pageradapter = mViewPager.getAdapter();
        TabClickListener tabclicklistener = new TabClickListener();
        for(int i = 0; i < pageradapter.getCount(); i++)
        {
            int j = mTabViewLayoutId;
            TextView textview = null;
            Object obj = null;
            if(j != 0)
            {
                obj = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);
                textview = (TextView)((View) (obj)).findViewById(mTabViewTextViewId);
            }
            if(obj == null)
            {
                obj = createDefaultTabView(getContext());
            }
            if(textview == null && android.widget.TextView.class.isInstance(obj))
            {
                textview = (TextView)obj;
            }
            textview.setText(pageradapter.getPageTitle(i));
            ((View) (obj)).setOnClickListener(tabclicklistener);
            mTabStrip.addView(((View) (obj)));
        }

    }

    private void scrollToTab(int i, int j)
    {
        int k = mTabStrip.getChildCount();
        View view;
        if(k != 0 && i >= 0 && i < k)
        {
            if((view = mTabStrip.getChildAt(i)) != null)
            {
                int l = j + view.getLeft();
                if(i > 0 || j > 0)
                {
                    l -= mTitleOffset;
                }
                scrollTo(l, 0);
                return;
            }
        }
    }

    protected TextView createDefaultTabView(Context context)
    {
        TextView textview = new TextView(context);
        textview.setGravity(17);
        textview.setTextSize(2, 12F);
        textview.setTypeface(Typeface.DEFAULT_BOLD);
        if(android.os.Build.VERSION.SDK_INT >= 11)
        {
            TypedValue typedvalue = new TypedValue();
            getContext().getTheme().resolveAttribute(0x101030e, typedvalue, true);
            textview.setBackgroundResource(typedvalue.resourceId);
        }
        if(android.os.Build.VERSION.SDK_INT >= 14)
        {
            textview.setAllCaps(true);
        }
        int i = (int)(16F * getResources().getDisplayMetrics().density);
        textview.setPadding(i, i, i, i);
        return textview;
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        if(mViewPager != null)
        {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    public void setCustomTabColorizer(TabColorizer tabcolorizer)
    {
        mTabStrip.setCustomTabColorizer(tabcolorizer);
    }

    public void setCustomTabView(int i, int j)
    {
        mTabViewLayoutId = i;
        mTabViewTextViewId = j;
    }

    public void setDividerColors(int ai[])
    {
        mTabStrip.setDividerColors(ai);
    }

    public void setOnPageChangeListener(android.support.v4.view.ViewPager.OnPageChangeListener onpagechangelistener)
    {
        mViewPagerPageChangeListener = onpagechangelistener;
    }

    public void setSelectedIndicatorColors(int ai[])
    {
        mTabStrip.setSelectedIndicatorColors(ai);
    }

    public void setViewPager(ViewPager viewpager)
    {
        mTabStrip.removeAllViews();
        mViewPager = viewpager;
        if(viewpager != null)
        {
            viewpager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }
}
