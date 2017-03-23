package com.mzdevelopment.freemusicplayer.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

import com.mzdevelopment.freemusicplayer.R;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.object.ItemDrawerObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DrawerAdapter extends BaseAdapter
    implements ICloudMusicPlayerConstants
{
    private static class ViewHolder
    {

        public ImageView mImgIcon;
        public RelativeLayout mLayoutRoot;
        public TextView mTvNameDrawer;

        private ViewHolder()
        {
        }

    }


    public static final String TAG = "DrawerAdapter";
    private ArrayList listDrawerObjects;
    private Context mContext;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;

    public DrawerAdapter(Context context, ArrayList arraylist, Typeface typeface, Typeface typeface1)
    {
        mContext = context;
        listDrawerObjects = arraylist;
        mTypefaceBold = typeface;
        mTypefaceLight = typeface1;
    }

    public int getCount()
    {
        if(listDrawerObjects != null)
        {
            return listDrawerObjects.size();
        } else
        {
            return 0;
        }
    }

    public Object getItem(int i)
    {
        if(listDrawerObjects != null)
        {
            return listDrawerObjects.get(i);
        } else
        {
            return null;
        }
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public ArrayList getListDrawerObjects()
    {
        return listDrawerObjects;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        ViewHolder viewholder;
        ItemDrawerObject itemdrawerobject;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(R.layout.item_drawer, null);
            view.setTag(viewholder);
            viewholder.mTvNameDrawer = (TextView)view.findViewById(R.id.tv_name_setting);
            viewholder.mLayoutRoot = (RelativeLayout)view.findViewById(R.id.layout_root);
            viewholder.mImgIcon = (ImageView)view.findViewById(R.id.img_icon);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        itemdrawerobject = (ItemDrawerObject)listDrawerObjects.get(i);
        viewholder.mImgIcon.setImageResource(itemdrawerobject.getIconRes());
        viewholder.mTvNameDrawer.setText(itemdrawerobject.getName());
        if(itemdrawerobject.isSelected())
        {
            viewholder.mTvNameDrawer.setTypeface(mTypefaceBold);
            viewholder.mLayoutRoot.setBackgroundColor(mContext.getResources().getColor(R.color.background_color_highlight));
            return view;
        } else
        {
            viewholder.mTvNameDrawer.setTypeface(mTypefaceLight);
            viewholder.mLayoutRoot.setBackgroundColor(0);
            return view;
        }
    }

    public void setSelectedDrawer(int i)
    {
        if(i < 0 || i >= listDrawerObjects.size())
        {
            return;
        }
        for(Iterator iterator = listDrawerObjects.iterator(); iterator.hasNext(); ((ItemDrawerObject)iterator.next()).setSelected(false)) { }
        ((ItemDrawerObject)listDrawerObjects.get(i)).setSelected(true);
        notifyDataSetChanged();
    }

}
