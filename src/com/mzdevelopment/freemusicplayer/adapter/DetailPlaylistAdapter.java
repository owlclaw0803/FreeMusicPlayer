package com.mzdevelopment.freemusicplayer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.*;
import android.widget.*;

import com.mzdevelopment.freemusicplayer.R;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

public class DetailPlaylistAdapter extends DBBaseAdapter
    implements ICloudMusicPlayerConstants
{
    public static interface IDetailPlaylistAdapterListener
    {

        public abstract void onPlayingTrack(TrackObject trackobject);

        public abstract void onRemoveToPlaylist(TrackObject trackobject);
    }

    private static class ViewHolder
    {

        public ImageView mImgMenu;
        public ImageView mImgSongs;
        public RelativeLayout mRootLayout;
        public TextView mTvSinger;
        public TextView mTvSongName;

        private ViewHolder()
        {
        }

    }


    public static final String TAG = "DetailPlaylistAdapter";
    private DisplayImageOptions mImgOptions;
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private IDetailPlaylistAdapterListener trackAdapter;

    public DetailPlaylistAdapter(Activity activity, ArrayList arraylist, Typeface typeface, Typeface typeface1, DisplayImageOptions displayimageoptions)
    {
        super(activity, arraylist);
        mTypefaceBold = typeface;
        mTypefaceLight = typeface1;
        mImgOptions = displayimageoptions;
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
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(R.layout.item_detail_playlist, null);
            view.setTag(viewholder);
            viewholder.mImgSongs = (ImageView)view.findViewById(R.id.img_songs);
            viewholder.mImgMenu = (ImageView)view.findViewById(R.id.img_menu);
            viewholder.mTvSongName = (TextView)view.findViewById(R.id.tv_song);
            viewholder.mTvSinger = (TextView)view.findViewById(R.id.tv_singer);
            viewholder.mRootLayout = (RelativeLayout)view.findViewById(R.id.layout_root);
            viewholder.mTvSongName.setTypeface(mTypefaceBold);
            viewholder.mTvSinger.setTypeface(mTypefaceLight);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        mTrackObject = (TrackObject)mListObjects.get(i);
        viewholder.mTvSongName.setText(mTrackObject.getTitle());
        viewholder.mTvSinger.setText(mTrackObject.getUsername());
        s = mTrackObject.getArtworkUrl();
        if(StringUtils.isEmptyString(s) || s.equals("null"))
        {
            s = mTrackObject.getAvatarUrl();
        }
        if(StringUtils.isEmptyString(mTrackObject.getPath()))
        {
            if(!StringUtils.isEmptyString(s) && s.startsWith("http"))
            {
                String s2 = s.replace("large", "crop");
                ImageLoader.getInstance().displayImage(s2, viewholder.mImgSongs, mImgOptions);
            } else
            {
                viewholder.mImgSongs.setImageResource(R.drawable.music_note);
            }
        } else
        {
            Uri uri = mTrackObject.getURI();
            if(uri != null)
            {
                String s1 = uri.toString();
                ImageLoader.getInstance().displayImage(s1, viewholder.mImgSongs, mImgOptions);
            } else
            {
                viewholder.mImgSongs.setImageResource(R.drawable.music_note);
            }
        }
        viewholder.mRootLayout.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                if(trackAdapter != null)
                {
                    trackAdapter.onPlayingTrack(mTrackObject);
                }
            }
        });
        viewholder.mImgMenu.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                showDiaglogOptions(mTrackObject);
            }
        });
        return view;
    }

    public void setDetailPlaylistAdapterListener(IDetailPlaylistAdapterListener idetailplaylistadapterlistener)
    {
        trackAdapter = idetailplaylistadapterlistener;
    }

    public void showDiaglogOptions(final TrackObject mTrackObject)
    {
        (new android.app.AlertDialog.Builder(mContext)).setTitle(R.string.title_options).setItems(R.array.list_track_playlist, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                if(i == 0 && trackAdapter != null)
                {
                    trackAdapter.onRemoveToPlaylist(mTrackObject);
                }
            }
        }).setPositiveButton(R.string.title_cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
            }
        }).create().show();
    }
}
