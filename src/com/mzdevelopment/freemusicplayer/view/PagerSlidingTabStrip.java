package com.mzdevelopment.freemusicplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.*;
import android.graphics.*;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import java.util.Locale;
import com.mzdevelopment.freemusicplayer.R;

public class PagerSlidingTabStrip extends HorizontalScrollView
{
    public static interface IconTabProvider
    {

        public abstract int getPageIconResId(int i);
    }

    private class PageListener
        implements android.support.v4.view.ViewPager.OnPageChangeListener
    {
        public void onPageScrollStateChanged(int i)
        {
            if(i == 0)
            {
                scrollToChild(pager.getCurrentItem(), 0);
            }
            if(delegatePageListener != null)
            {
                delegatePageListener.onPageScrollStateChanged(i);
            }
        }

        public void onPageScrolled(int i, float f, int j)
        {
            currentPosition = i;
            currentPositionOffset = f;
            scrollToChild(i, (int)(f * (float)tabsContainer.getChildAt(i).getWidth()));
            invalidate();
            if(delegatePageListener != null)
            {
                delegatePageListener.onPageScrolled(i, f, j);
            }
        }

        public void onPageSelected(int i)
        {
            if(delegatePageListener != null)
            {
                delegatePageListener.onPageSelected(i);
            }
        }

        private PageListener()
        {
            super();
        }

    }

    static class SavedState extends android.view.View.BaseSavedState
    {

        public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

            public SavedState createFromParcel(Parcel parcel)
            {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i)
            {
                return new SavedState[i];
            }
        };
        int currentPosition;

        public void writeToParcel(Parcel parcel, int i)
        {
            super.writeToParcel(parcel, i);
            parcel.writeInt(currentPosition);
        }


        private SavedState(Parcel parcel)
        {
            super(parcel);
            currentPosition = parcel.readInt();
        }


        public SavedState(Parcelable parcelable)
        {
            super(parcelable);
        }
    }


    private static final int ATTRS[] = {
        0x1010095, 0x1010098
    };
    private int currentPosition;
    private float currentPositionOffset;
    private android.widget.LinearLayout.LayoutParams defaultTabLayoutParams;
    public android.support.v4.view.ViewPager.OnPageChangeListener delegatePageListener;
    private int dividerColor;
    private int dividerPadding;
    private Paint dividerPaint;
    private int dividerWidth;
    private android.widget.LinearLayout.LayoutParams expandedTabLayoutParams;
    private int indicatorColor;
    private int indicatorHeight;
    private int lastScrollX;
    private Locale locale;
    private final PageListener pageListener;
    private ViewPager pager;
    private Paint rectPaint;
    private int scrollOffset;
    private boolean shouldExpand;
    private int tabBackgroundResId;
    private int tabCount;
    private int tabPadding;
    private int tabTextColor;
    private int tabTextSize;
    private Typeface tabTypeface;
    private int tabTypefaceStyle;
    private LinearLayout tabsContainer;
    private boolean textAllCaps;
    private int underlineColor;
    private int underlineHeight;

    public PagerSlidingTabStrip(Context context)
    {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        pageListener = new PageListener();
        currentPosition = 0;
        currentPositionOffset = 0.0F;
        indicatorColor = 0xff666666;
        underlineColor = 0x1a000000;
        dividerColor = 0x1a000000;
        shouldExpand = false;
        textAllCaps = true;
        scrollOffset = 52;
        indicatorHeight = 8;
        underlineHeight = 2;
        dividerPadding = 12;
        tabPadding = 24;
        dividerWidth = 1;
        tabTextSize = 12;
        tabTextColor = 0xff666666;
        tabTypeface = null;
        tabTypefaceStyle = 1;
        lastScrollX = 0;
        tabBackgroundResId = R.drawable.background_tab;
        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(0);
        tabsContainer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
        addView(tabsContainer);
        android.util.DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        scrollOffset = (int)TypedValue.applyDimension(1, scrollOffset, displaymetrics);
        indicatorHeight = (int)TypedValue.applyDimension(1, indicatorHeight, displaymetrics);
        underlineHeight = (int)TypedValue.applyDimension(1, underlineHeight, displaymetrics);
        dividerPadding = (int)TypedValue.applyDimension(1, dividerPadding, displaymetrics);
        tabPadding = (int)TypedValue.applyDimension(1, tabPadding, displaymetrics);
        dividerWidth = (int)TypedValue.applyDimension(1, dividerWidth, displaymetrics);
        tabTextSize = (int)TypedValue.applyDimension(2, tabTextSize, displaymetrics);
        TypedArray typedarray = context.obtainStyledAttributes(attributeset, ATTRS);
        tabTextSize = typedarray.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = typedarray.getColor(1, tabTextColor);
        typedarray.recycle();
        int style_PagerSlidingTabStrip[] = {
                R.attr.pstsIndicatorColor, R.attr.pstsUnderlineColor, R.attr.pstsDividerColor, R.attr.pstsIndicatorHeight, 
				R.attr.pstsUnderlineHeight, R.attr.pstsDividerPadding, R.attr.pstsTabPaddingLeftRight, R.attr.pstsScrollOffset,
				R.attr.pstsTabBackground, R.attr.pstsShouldExpand, R.attr.pstsTextAllCaps
            };
        TypedArray typedarray1 = context.obtainStyledAttributes(attributeset, style_PagerSlidingTabStrip);
        indicatorColor = typedarray1.getColor(0, indicatorColor);
        underlineColor = typedarray1.getColor(1, underlineColor);
        dividerColor = typedarray1.getColor(2, dividerColor);
        indicatorHeight = typedarray1.getDimensionPixelSize(3, indicatorHeight);
        underlineHeight = typedarray1.getDimensionPixelSize(4, underlineHeight);
        dividerPadding = typedarray1.getDimensionPixelSize(5, dividerPadding);
        tabPadding = typedarray1.getDimensionPixelSize(6, tabPadding);
        tabBackgroundResId = typedarray1.getResourceId(8, tabBackgroundResId);
        shouldExpand = typedarray1.getBoolean(9, shouldExpand);
        scrollOffset = typedarray1.getDimensionPixelSize(7, scrollOffset);
        textAllCaps = typedarray1.getBoolean(10, textAllCaps);
        typedarray1.recycle();
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(android.graphics.Paint.Style.FILL);
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);
        defaultTabLayoutParams = new android.widget.LinearLayout.LayoutParams(-2, -1);
        expandedTabLayoutParams = new android.widget.LinearLayout.LayoutParams(0, -1, 1.0F);
        if(locale == null)
        {
            locale = getResources().getConfiguration().locale;
        }
    }

    private void addIconTab(int i, int j)
    {
        ImageButton imagebutton = new ImageButton(getContext());
        imagebutton.setImageResource(j);
        addTab(i, imagebutton);
    }

    private void addTab(final int position, View view)
    {
        view.setFocusable(true);
        view.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                pager.setCurrentItem(position);
            }
        });
        view.setPadding(tabPadding, 0, tabPadding, 0);
        LinearLayout linearlayout = tabsContainer;
        android.widget.LinearLayout.LayoutParams layoutparams;
        if(shouldExpand)
        {
            layoutparams = expandedTabLayoutParams;
        } else
        {
            layoutparams = defaultTabLayoutParams;
        }
        linearlayout.addView(view, position, layoutparams);
    }

    private void addTextTab(int i, String s)
    {
        TextView textview = new TextView(getContext());
        textview.setText(s);
        textview.setGravity(17);
        textview.setSingleLine();
        addTab(i, textview);
    }

    private void scrollToChild(int i, int j)
    {
        if(tabCount != 0)
        {
            int k = j + tabsContainer.getChildAt(i).getLeft();
            if(i > 0 || j > 0)
            {
                k -= scrollOffset;
            }
            if(k != lastScrollX)
            {
                lastScrollX = k;
                scrollTo(k, 0);
                return;
            }
        }
    }

    private void updateTabStyles()
    {
        int i = 0;
        while(i < tabCount) 
        {
            View view = tabsContainer.getChildAt(i);
            view.setBackgroundResource(tabBackgroundResId);
            if(!(view instanceof TextView))
            {
                continue;
            }
            TextView textview = (TextView)view;
            textview.setTextSize(0, tabTextSize);
            textview.setTypeface(tabTypeface, tabTypefaceStyle);
            textview.setTextColor(tabTextColor);
            if(textAllCaps)
            {
                if(android.os.Build.VERSION.SDK_INT >= 14)
                {
                    textview.setAllCaps(true);
                } else
                {
                    textview.setText(textview.getText().toString().toUpperCase(locale));
                }
            }
            i++;
        }
    }

    public int getDividerColor()
    {
        return dividerColor;
    }

    public int getDividerPadding()
    {
        return dividerPadding;
    }

    public int getIndicatorColor()
    {
        return indicatorColor;
    }

    public int getIndicatorHeight()
    {
        return indicatorHeight;
    }

    public int getScrollOffset()
    {
        return scrollOffset;
    }

    public boolean getShouldExpand()
    {
        return shouldExpand;
    }

    public int getTabBackground()
    {
        return tabBackgroundResId;
    }

    public int getTabPaddingLeftRight()
    {
        return tabPadding;
    }

    public int getTextColor()
    {
        return tabTextColor;
    }

    public int getTextSize()
    {
        return tabTextSize;
    }

    public int getUnderlineColor()
    {
        return underlineColor;
    }

    public int getUnderlineHeight()
    {
        return underlineHeight;
    }

    public boolean isTextAllCaps()
    {
        return textAllCaps;
    }

    public void notifyDataSetChanged()
    {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        int i = 0;
        while(i < tabCount) 
        {
            if(pager.getAdapter() instanceof IconTabProvider)
            {
                addIconTab(i, ((IconTabProvider)pager.getAdapter()).getPageIconResId(i));
            } else
            {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
            i++;
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
			public void onGlobalLayout()
            {
                if(android.os.Build.VERSION.SDK_INT < 16)
                {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else
                {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(!isInEditMode() && tabCount != 0)
        {
            int i = getHeight();
            rectPaint.setColor(indicatorColor);
            View view = tabsContainer.getChildAt(currentPosition);
            float f = view.getLeft();
            float f1 = view.getRight();
            if(currentPositionOffset > 0.0F && currentPosition < -1 + tabCount)
            {
                View view2 = tabsContainer.getChildAt(1 + currentPosition);
                float f2 = view2.getLeft();
                float f3 = view2.getRight();
                f = f2 * currentPositionOffset + f * (1.0F - currentPositionOffset);
                f1 = f3 * currentPositionOffset + f1 * (1.0F - currentPositionOffset);
            }
            canvas.drawRect(f, i - indicatorHeight, f1, i, rectPaint);
            rectPaint.setColor(underlineColor);
            canvas.drawRect(0.0F, i - underlineHeight, tabsContainer.getWidth(), i, rectPaint);
            dividerPaint.setColor(dividerColor);
            int j = 0;
            while(j < -1 + tabCount) 
            {
                View view1 = tabsContainer.getChildAt(j);
                canvas.drawLine(view1.getRight(), dividerPadding, view1.getRight(), i - dividerPadding, dividerPaint);
                j++;
            }
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable)
    {
        SavedState savedstate = (SavedState)parcelable;
        super.onRestoreInstanceState(savedstate.getSuperState());
        currentPosition = savedstate.currentPosition;
        requestLayout();
    }

    public Parcelable onSaveInstanceState()
    {
        SavedState savedstate = new SavedState(super.onSaveInstanceState());
        savedstate.currentPosition = currentPosition;
        return savedstate;
    }

    public void setAllCaps(boolean flag)
    {
        textAllCaps = flag;
    }

    public void setDividerColor(int i)
    {
        dividerColor = i;
        invalidate();
    }

    public void setDividerColorResource(int i)
    {
        dividerColor = getResources().getColor(i);
        invalidate();
    }

    public void setDividerPadding(int i)
    {
        dividerPadding = i;
        invalidate();
    }

    public void setIndicatorColor(int i)
    {
        indicatorColor = i;
        invalidate();
    }

    public void setIndicatorColorResource(int i)
    {
        indicatorColor = getResources().getColor(i);
        invalidate();
    }

    public void setIndicatorHeight(int i)
    {
        indicatorHeight = i;
        invalidate();
    }

    public void setOnPageChangeListener(android.support.v4.view.ViewPager.OnPageChangeListener onpagechangelistener)
    {
        delegatePageListener = onpagechangelistener;
    }

    public void setScrollOffset(int i)
    {
        scrollOffset = i;
        invalidate();
    }

    public void setShouldExpand(boolean flag)
    {
        shouldExpand = flag;
        requestLayout();
    }

    public void setTabBackground(int i)
    {
        tabBackgroundResId = i;
    }

    public void setTabPaddingLeftRight(int i)
    {
        tabPadding = i;
        updateTabStyles();
    }

    public void setTextColor(int i)
    {
        tabTextColor = i;
        updateTabStyles();
    }

    public void setTextColorResource(int i)
    {
        tabTextColor = getResources().getColor(i);
        updateTabStyles();
    }

    public void setTextSize(int i)
    {
        tabTextSize = i;
        updateTabStyles();
    }

    public void setTypeface(Typeface typeface, int i)
    {
        tabTypeface = typeface;
        tabTypefaceStyle = i;
        updateTabStyles();
    }

    public void setUnderlineColor(int i)
    {
        underlineColor = i;
        invalidate();
    }

    public void setUnderlineColorResource(int i)
    {
        underlineColor = getResources().getColor(i);
        invalidate();
    }

    public void setUnderlineHeight(int i)
    {
        underlineHeight = i;
        invalidate();
    }

    public void setViewPager(ViewPager viewpager)
    {
        pager = viewpager;
        if(viewpager.getAdapter() == null)
        {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        } else
        {
            viewpager.setOnPageChangeListener(pageListener);
            notifyDataSetChanged();
            return;
        }
    }




/*
    static int access$102(PagerSlidingTabStrip pagerslidingtabstrip, int i)
    {
        pagerslidingtabstrip.currentPosition = i;
        return i;
    }

*/




/*
    static float access$402(PagerSlidingTabStrip pagerslidingtabstrip, float f)
    {
        pagerslidingtabstrip.currentPositionOffset = f;
        return f;
    }

*/

}
