package com.mzdevelopment.freemusicplayer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class LibraryAdapter extends DBBaseAdapter
    implements ICloudMusicPlayerConstants
{
    public static interface OnDownloadedListener
    {

        public abstract void onAddToPlaylist(TrackObject trackobject);

        public abstract void onDeleteItem(TrackObject trackobject);

        public abstract void onPlayItem(TrackObject trackobject);

        public abstract void onSetAsNotification(TrackObject trackobject);

        public abstract void onSetAsRingtone(TrackObject trackobject);
    }

    private static class ViewHolder
    {

        public ImageView mImgMenu;
        public RelativeLayout mLayoutRoot;
        public TextView mTvDuration;
        public TextView mTvNumber;
        public TextView mTvSongName;

        private ViewHolder()
        {
        }

    }


    public static final String TAG = "LibraryAdapter";
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private OnDownloadedListener onTrackListener;

    public LibraryAdapter(Activity activity, ArrayList arraylist, Typeface typeface, Typeface typeface1)
    {
        super(activity, arraylist);
        mTypefaceBold = typeface;
        mTypefaceLight = typeface1;
    }

    public View getAnimatedView(int i, View view, ViewGroup viewgroup)
    {
        return null;
    }

    public View getNormalView(int i, View view, ViewGroup viewgroup)
    {
        ViewHolder viewholder;
        final TrackObject mTrackObject;
        long l;
        String s;
        String s1;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(R.layout.item_local_track, null);
            view.setTag(viewholder);
            viewholder.mImgMenu = (ImageView)view.findViewById(R.id.img_menu);
            viewholder.mTvSongName = (TextView)view.findViewById(R.id.tv_song);
            viewholder.mTvDuration = (TextView)view.findViewById(R.id.tv_duration);
            viewholder.mTvNumber = (TextView)view.findViewById(R.id.tv_number);
            viewholder.mLayoutRoot = (RelativeLayout)view.findViewById(R.id.layout_root);
            viewholder.mTvSongName.setTypeface(mTypefaceBold);
            viewholder.mTvDuration.setTypeface(mTypefaceLight);
            viewholder.mTvNumber.setTypeface(mTypefaceLight);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        mTrackObject = (TrackObject)mListObjects.get(i);
        viewholder.mTvSongName.setText(mTrackObject.getTitle());
        viewholder.mTvNumber.setText((new StringBuilder()).append(String.valueOf(i + 1)).append(".").toString());
        l = mTrackObject.getDuration() / 1000L;
        s = String.valueOf((int)(l / 60L));
        s1 = String.valueOf((int)(l % 60L));
        if(s.length() < 2)
        {
            s = (new StringBuilder()).append("0").append(s).toString();
        }
        if(s1.length() < 2)
        {
            s1 = (new StringBuilder()).append("0").append(s1).toString();
        }
        viewholder.mTvDuration.setText((new StringBuilder()).append(s).append(":").append(s1).toString());
        viewholder.mLayoutRoot.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                if(onTrackListener != null)
                {
                    onTrackListener.onPlayItem(mTrackObject);
                }
            }
        });
        viewholder.mImgMenu.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                showDialogOptions(mTrackObject);
            }
        });
        return view;
    }

    public void setOnDownloadedListener(OnDownloadedListener ondownloadedlistener)
    {
        onTrackListener = ondownloadedlistener;
    }

    public void showDialogOptions(final TrackObject mTrackObject)
    {
        (new android.app.AlertDialog.Builder(mContext)).setTitle(R.string.title_options).setItems(R.array.list_options, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                if(i == 0){
	                if(onTrackListener != null)
	                    onTrackListener.onAddToPlaylist(mTrackObject);
	                return;
                }
                if(i == 1)
                {
                	if(onTrackListener != null)
                        onTrackListener.onDeleteItem(mTrackObject);
                    return;
                }
                if(i == 2)
                {
                	if(onTrackListener != null)
                    	onTrackListener.onSetAsRingtone(mTrackObject);
                    return;
                }
                if(i == 3 && onTrackListener != null)
                	onTrackListener.onSetAsNotification(mTrackObject);
            }
        }).setPositiveButton(R.string.title_cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
            }
        }).create().show();
    }
}
