package com.mzdevelopment.freemusicplayer.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.*;
import android.view.View;
import android.widget.LinearLayout;

class SlidingTabStrip extends LinearLayout
{
    private static class SimpleTabColorizer
        implements SlidingTabLayout.TabColorizer
    {

        private int mDividerColors[];
        private int mIndicatorColors[];

        public final int getDividerColor(int i)
        {
            return mDividerColors[i % mDividerColors.length];
        }

        public final int getIndicatorColor(int i)
        {
            return mIndicatorColors[i % mIndicatorColors.length];
        }

        void setDividerColors(int ai[])
        {
            mDividerColors = ai;
        }

        void setIndicatorColors(int ai[])
        {
            mIndicatorColors = ai;
        }

        private SimpleTabColorizer()
        {
        }

    }


    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 38;
    private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 2;
    private static final byte DEFAULT_DIVIDER_COLOR_ALPHA = 32;
    private static final float DEFAULT_DIVIDER_HEIGHT = 0.5F;
    private static final int DEFAULT_DIVIDER_THICKNESS_DIPS = 1;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xff33b5e5;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 8;
    private final Paint mBottomBorderPaint;
    private final int mBottomBorderThickness;
    private SlidingTabLayout.TabColorizer mCustomTabColorizer;
    private final int mDefaultBottomBorderColor;
    private final SimpleTabColorizer mDefaultTabColorizer;
    private final float mDividerHeight;
    private final Paint mDividerPaint;
    private final Paint mSelectedIndicatorPaint;
    private final int mSelectedIndicatorThickness;
    private int mSelectedPosition;
    private float mSelectionOffset;

    SlidingTabStrip(Context context)
    {
        this(context, null);
    }

    SlidingTabStrip(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        setWillNotDraw(false);
        float f = getResources().getDisplayMetrics().density;
        TypedValue typedvalue = new TypedValue();
        context.getTheme().resolveAttribute(0x1010030, typedvalue, true);
        int i = typedvalue.data;
        mDefaultBottomBorderColor = setColorAlpha(i, (byte)38);
        mDefaultTabColorizer = new SimpleTabColorizer();
        mDefaultTabColorizer.setIndicatorColors(new int[] {
            0xff33b5e5
        });
        SimpleTabColorizer simpletabcolorizer = mDefaultTabColorizer;
        int ai[] = new int[1];
        ai[0] = setColorAlpha(i, (byte)32);
        simpletabcolorizer.setDividerColors(ai);
        mBottomBorderThickness = (int)(2.0F * f);
        mBottomBorderPaint = new Paint();
        mBottomBorderPaint.setColor(mDefaultBottomBorderColor);
        mSelectedIndicatorThickness = (int)(8F * f);
        mSelectedIndicatorPaint = new Paint();
        mDividerHeight = 0.5F;
        mDividerPaint = new Paint();
        mDividerPaint.setStrokeWidth((int)(1.0F * f));
    }

    private static int blendColors(int i, int j, float f)
    {
        float f1 = 1.0F - f;
        float f2 = f * (float)Color.red(i) + f1 * (float)Color.red(j);
        float f3 = f * (float)Color.green(i) + f1 * (float)Color.green(j);
        float f4 = f * (float)Color.blue(i) + f1 * (float)Color.blue(j);
        return Color.rgb((int)f2, (int)f3, (int)f4);
    }

    private static int setColorAlpha(int i, byte byte0)
    {
        return Color.argb(byte0, Color.red(i), Color.green(i), Color.blue(i));
    }

    protected void onDraw(Canvas canvas)
    {
        int i = getHeight();
        int j = getChildCount();
        int k = (int)(Math.min(Math.max(0.0F, mDividerHeight), 1.0F) * (float)i);
        Object obj;
        int l;
        if(mCustomTabColorizer != null)
        {
            obj = mCustomTabColorizer;
        } else
        {
            obj = mDefaultTabColorizer;
        }
        if(j > 0)
        {
            View view1 = getChildAt(mSelectedPosition);
            int j1 = view1.getLeft();
            int k1 = view1.getRight();
            int l1 = mSelectedPosition;
            int i2 = ((SlidingTabLayout.TabColorizer) (obj)).getIndicatorColor(l1);
            if(mSelectionOffset > 0.0F && mSelectedPosition < -1 + getChildCount())
            {
                int j2 = 1 + mSelectedPosition;
                int k2 = ((SlidingTabLayout.TabColorizer) (obj)).getIndicatorColor(j2);
                if(i2 != k2)
                {
                    i2 = blendColors(k2, i2, mSelectionOffset);
                }
                View view2 = getChildAt(1 + mSelectedPosition);
                j1 = (int)(mSelectionOffset * (float)view2.getLeft() + (1.0F - mSelectionOffset) * (float)j1);
                k1 = (int)(mSelectionOffset * (float)view2.getRight() + (1.0F - mSelectionOffset) * (float)k1);
            }
            mSelectedIndicatorPaint.setColor(i2);
            canvas.drawRect(j1, i - mSelectedIndicatorThickness, k1, i, mSelectedIndicatorPaint);
        }
        canvas.drawRect(0.0F, i - mBottomBorderThickness, getWidth(), i, mBottomBorderPaint);
        l = (i - k) / 2;
        for(int i1 = 0; i1 < j - 1; i1++)
        {
            View view = getChildAt(i1);
            mDividerPaint.setColor(((SlidingTabLayout.TabColorizer) (obj)).getDividerColor(i1));
            canvas.drawLine(view.getRight(), l, view.getRight(), l + k, mDividerPaint);
        }

    }

    void onViewPagerPageChanged(int i, float f)
    {
        mSelectedPosition = i;
        mSelectionOffset = f;
        invalidate();
    }

    void setCustomTabColorizer(SlidingTabLayout.TabColorizer tabcolorizer)
    {
        mCustomTabColorizer = tabcolorizer;
        invalidate();
    }

    void setDividerColors(int ai[])
    {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setDividerColors(ai);
        invalidate();
    }

    void setSelectedIndicatorColors(int ai[])
    {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setIndicatorColors(ai);
        invalidate();
    }
}
