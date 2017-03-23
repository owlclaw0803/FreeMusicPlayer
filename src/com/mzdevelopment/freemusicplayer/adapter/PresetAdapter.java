package com.mzdevelopment.freemusicplayer.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mzdevelopment.freemusicplayer.R;

public class PresetAdapter extends ArrayAdapter
{
    private static class ViewHolder
    {

        public TextView mTvName;

        private ViewHolder()
        {
        }

    }


    private Context mContext;
    private String mListString[];
    private Typeface mTypeFace;

    public PresetAdapter(Context context, int i, String as[], Typeface typeface)
    {
        super(context, i, as);
        mContext = context;
        mListString = as;
        mTypeFace = typeface;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        ViewHolder viewholder;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(R.layout.item_preset_name, null);
            view.setTag(viewholder);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        viewholder.mTvName = (TextView)view.findViewById(R.id.tv_name);
        viewholder.mTvName.setText(mListString[i]);
        viewholder.mTvName.setTypeface(mTypeFace);
        return view;
    }
}
