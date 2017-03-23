package com.mzdevelopment.freemusicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.ypyproductions.utils.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import com.mzdevelopment.freemusicplayer.R;

public class NewTrackAdapter extends DBBaseAdapter
    implements ICloudMusicPlayerConstants
{
    public static interface INewTrackAdapterListener
    {

        public abstract void onAddToPlaylist(TrackObject trackobject);

        public abstract void onDownload(TrackObject trackobject);

        public abstract void onListenDemo(TrackObject trackobject);
    }

    private static class ViewHolder
    {

        public ImageView mImgAdd;
        public ImageView mImgSongs;
        public RelativeLayout mRootLayout;
        public TextView mTvDuration;
        public TextView mTvPlayCount;
        public TextView mTvSongName;

        private ViewHolder()
        {
        }

    }


    public static final String TAG = "NewTrackAdapter";
    private DisplayImageOptions mAvatarOptions;
    private Date mDate;
    private DisplayImageOptions mImgOptions;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private INewTrackAdapterListener trackAdapter;

    public NewTrackAdapter(Activity activity, ArrayList arraylist, Typeface typeface, Typeface typeface1, DisplayImageOptions displayimageoptions, DisplayImageOptions displayimageoptions1)
    {
        super(activity, arraylist);
        mTypefaceBold = typeface;
        mTypefaceLight = typeface1;
        mImgOptions = displayimageoptions;
        mAvatarOptions = displayimageoptions1;
        mDate = new Date();
    }

    public static String formatVisualNumber(long l, String s)
    {
    	int i;
        int j;
        String s2;
        int k;
        String s3;
        int i1;
        int j1;
        String s1 = String.valueOf(l);
        if(s1.length() <= 3)
        	return String.valueOf(s1);
        i = (int)Math.floor(s1.length() / 3);
        j = s1.length();
        s2 = "";
        k = 0;
        while(k < i){
        	i1 = 0;
            while(i1 < 3){
    	        j1 = j - 1 - (i1 + k * 3);
    	        s2 = (new StringBuilder()).append(s1.charAt(j1)).append(s2).toString();
    	        i1++;
            }
            if(k == i - 1){
            	if(j - i * 3 > 0)
            		s2 = (new StringBuilder()).append(s).append(s2).toString();
            }else
                s2 = (new StringBuilder()).append(s).append(s2).toString();
        	k++;
        }
        s3 = (new StringBuilder()).append(s1.substring(0, j - i * 3)).append(s2).toString();
        return s3;
    }

    public static String getStringTimeAgo(long l)
    {
        double d = (float)l / 60F;
        if(l < 5L)
        {
            return "Just now";
        }
        if(l < 60L)
        {
            return (new StringBuilder()).append(String.valueOf(l)).append(" seconds ago").toString();
        }
        if(l < 120L)
        {
            return "A minute ago";
        }
        if(d < 60D)
        {
            return (new StringBuilder()).append(String.valueOf((int)d)).append(" minutes ago").toString();
        }
        if(d < 120D)
        {
            return "A hour ago";
        }
        if(d < 1440D)
        {
            double d5 = Math.floor(d / 60D);
            return (new StringBuilder()).append(String.valueOf((int)d5)).append(" hours ago").toString();
        }
        if(d < 2880D)
        {
            return "Yesterday";
        }
        if(d < 10080D)
        {
            double d4 = Math.floor(d / 1440D);
            return (new StringBuilder()).append(String.valueOf((int)d4)).append(" days ago").toString();
        }
        if(d < 20160D)
        {
            return "Last week";
        }
        if(d < 44640D)
        {
            double d3 = Math.floor(d / 10080D);
            return (new StringBuilder()).append(String.valueOf((int)d3)).append(" weeks ago").toString();
        }
        if(d < 87840D)
        {
            return "Last Month";
        }
        if(d < 525960D)
        {
            double d2 = Math.floor(d / 43200D);
            return (new StringBuilder()).append(String.valueOf((int)d2)).append(" months ago").toString();
        }
        if(d < 1052640D)
        {
            return "Last year";
        }
        if(d > 1052640D)
        {
            double d1 = Math.floor(d / 525600D);
            return (new StringBuilder()).append(String.valueOf((int)d1)).append(" years ago").toString();
        } else
        {
            return "Unknown";
        }
    }

    public View getAnimatedView(int i, View view, ViewGroup viewgroup)
    {
        return null;
    }

    public View getNormalView(int i, View view, ViewGroup viewgroup)
    {
        ViewHolder viewholder;
        final TrackObject mTrackObject;
        String s;
        long l;
        String s1;
        String s2;
        String s3;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(R.layout.item_new_track, null);
            view.setTag(viewholder);
            viewholder.mImgSongs = (ImageView)view.findViewById(R.id.img_songs);
            viewholder.mImgAdd = (ImageView)view.findViewById(R.id.img_add_playlist);
            viewholder.mTvSongName = (TextView)view.findViewById(R.id.tv_song);
            viewholder.mTvDuration = (TextView)view.findViewById(R.id.tv_duration);
            viewholder.mTvPlayCount = (TextView)view.findViewById(R.id.tv_playcount);
            viewholder.mRootLayout = (RelativeLayout)view.findViewById(R.id.layout_root);
            viewholder.mTvSongName.setTypeface(mTypefaceBold);
            viewholder.mTvDuration.setTypeface(mTypefaceLight);
            viewholder.mTvPlayCount.setTypeface(mTypefaceLight);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        mTrackObject = (TrackObject)mListObjects.get(i);
        viewholder.mTvSongName.setText(mTrackObject.getTitle());
        s = formatVisualNumber(mTrackObject.getPlaybackCount(), ",");
        viewholder.mTvPlayCount.setText(s);
        l = mTrackObject.getDuration() / 1000L;
        s1 = String.valueOf((int)(l / 60L));
        s2 = String.valueOf((int)(l % 60L));
        if(s1.length() < 2)
        {
            s1 = (new StringBuilder()).append("0").append(s1).toString();
        }
        if(s2.length() < 2)
        {
            s2 = (new StringBuilder()).append("0").append(s2).toString();
        }
        viewholder.mTvDuration.setText((new StringBuilder()).append(s1).append(":").append(s2).toString());
        s3 = mTrackObject.getArtworkUrl();
        if(StringUtils.isEmptyString(s3) || s3.equals("null"))
        {
            s3 = mTrackObject.getAvatarUrl();
        }
        if(!StringUtils.isEmptyString(s3) && s3.startsWith("http"))
        {
            String s4 = s3.replace("large", "crop");
            ImageLoader.getInstance().displayImage(s4, viewholder.mImgSongs, mImgOptions);
        } else
        {
            viewholder.mImgSongs.setImageResource(R.drawable.music_note);
        }
        viewholder.mRootLayout.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                if(trackAdapter != null)
                {
                    trackAdapter.onListenDemo(mTrackObject);
                }
            }
        });
        viewholder.mImgAdd.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                if(trackAdapter != null)
                {
                    trackAdapter.onAddToPlaylist(mTrackObject);
                }
            }
        });
        return view;
    }

    public void setNewTrackAdapterListener(INewTrackAdapterListener inewtrackadapterlistener)
    {
        trackAdapter = inewtrackadapterlistener;
    }


}
