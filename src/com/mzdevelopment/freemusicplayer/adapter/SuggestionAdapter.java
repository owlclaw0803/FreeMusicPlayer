package com.mzdevelopment.freemusicplayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.*;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class SuggestionAdapter extends CursorAdapter
    implements ICloudMusicPlayerConstants
{

    public static final String TAG = "SuggestionAdapter";
    private ArrayList mListItems;

    public SuggestionAdapter(Context context, Cursor cursor, ArrayList arraylist)
    {
        super(context, cursor, false);
        mListItems = arraylist;
    }

    public void bindView(View view, Context context, Cursor cursor)
    {
        ((TextView)view.findViewById(R.id.tv_name_options)).setText((CharSequence)mListItems.get(cursor.getPosition()));
    }

    public View newView(Context context, Cursor cursor, ViewGroup viewgroup)
    {
        if(cursor.getPosition() >= 0 && cursor.getPosition() < mListItems.size())
        {
            View view = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(R.layout.item_suggestion, viewgroup, false);
            ((TextView)view.findViewById(R.id.tv_name_options)).setText((CharSequence)mListItems.get(cursor.getPosition()));
            return view;
        } else
        {
            return null;
        }
    }

}
