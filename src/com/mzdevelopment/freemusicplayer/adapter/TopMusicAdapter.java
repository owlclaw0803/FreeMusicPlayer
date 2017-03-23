package com.mzdevelopment.freemusicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.object.TopMusicObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.ypyproductions.utils.StringUtils;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class TopMusicAdapter extends DBBaseAdapter
    implements ICloudMusicPlayerConstants
{
    public static interface OnTopMusicListener
    {

        public abstract void onSearchDetail(TopMusicObject topmusicobject);
    }

    private static class ViewHolder
    {

        public ImageView mImgTrack;
        public RelativeLayout mLayoutRoot;
        public TextView mTvArtist;
        public TextView mTvSongName;

        private ViewHolder()
        {
        }

    }


    public static final String TAG = "TopMusicAdapter";
    private DisplayImageOptions mOptions;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private OnTopMusicListener onTrackListener;

    public TopMusicAdapter(Activity activity, ArrayList arraylist, Typeface typeface, Typeface typeface1, DisplayImageOptions displayimageoptions)
    {
        super(activity, arraylist);
        mTypefaceBold = typeface;
        mTypefaceLight = typeface1;
        mOptions = displayimageoptions;
    }

    public View getAnimatedView(int i, View view, ViewGroup viewgroup)
    {
        return null;
    }

    public View getNormalView(int i, View view, ViewGroup viewgroup)
    {
        ViewHolder viewholder;
        final TopMusicObject mTrackObject;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(R.layout.item_hot_music, null);
            view.setTag(viewholder);
            viewholder.mTvSongName = (TextView)view.findViewById(R.id.tv_song);
            viewholder.mTvArtist = (TextView)view.findViewById(R.id.tv_artist);
            viewholder.mImgTrack = (ImageView)view.findViewById(R.id.img_songs);
            viewholder.mLayoutRoot = (RelativeLayout)view.findViewById(R.id.layout_root);
            viewholder.mTvSongName.setTypeface(mTypefaceBold);
            viewholder.mTvArtist.setTypeface(mTypefaceLight);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        mTrackObject = (TopMusicObject)mListObjects.get(i);
        viewholder.mTvSongName.setText(mTrackObject.getName());
        viewholder.mTvArtist.setText(mTrackObject.getArtist());
        if(!StringUtils.isEmptyString(mTrackObject.getArtwork()))
        {
            ImageLoader.getInstance().displayImage(mTrackObject.getArtwork(), viewholder.mImgTrack, mOptions);
        } else
        {
            viewholder.mImgTrack.setImageResource(R.drawable.ic_music_default);
        }
        viewholder.mLayoutRoot.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                if(onTrackListener != null)
                {
                    onTrackListener.onSearchDetail(mTrackObject);
                }
            }
        });
        return view;
    }

    public void setOnTopMusicListener(OnTopMusicListener ontopmusiclistener)
    {
        onTrackListener = ontopmusiclistener;
    }


}
