package com.mzdevelopment.freemusicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.TextView;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.object.GenreObject;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class GenreAdapter extends DBBaseAdapter
    implements ICloudMusicPlayerConstants
{
    private static class ViewHolder
    {

        public TextView mTvGenreName;

        private ViewHolder()
        {
        }

    }


    public static final String TAG = "GenreAdapter";
    private Typeface mTypeface;
    protected ArrayList resultList;

    public GenreAdapter(Activity activity, ArrayList arraylist, Typeface typeface)
    {
        super(activity, arraylist);
        mTypeface = typeface;
    }

    public View getAnimatedView(int i, View view, ViewGroup viewgroup)
    {
        return null;
    }

    public View getNormalView(int i, View view, ViewGroup viewgroup)
    {
        ViewHolder viewholder;
        GenreObject genreobject;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(R.layout.item_genre, null);
            view.setTag(viewholder);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        viewholder.mTvGenreName = (TextView)view.findViewById(R.id.tv_genre_name);
        genreobject = (GenreObject)mListObjects.get(i);
        viewholder.mTvGenreName.setText(genreobject.getName());
        viewholder.mTvGenreName.setTypeface(mTypeface);
        return view;
    }

}
