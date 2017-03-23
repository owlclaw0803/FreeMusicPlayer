package com.mzdevelopment.freemusicplayer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.object.PlaylistObject;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import java.util.ArrayList;
import com.mzdevelopment.freemusicplayer.R;

public class PlaylistAdapter extends DBBaseAdapter
    implements ICloudMusicPlayerConstants
{
    public static interface OnPlaylistListener
    {

        public abstract void onDeletePlaylist(PlaylistObject playlistobject);

        public abstract void onGoToDetail(PlaylistObject playlistobject);

        public abstract void onPlayAllMusic(PlaylistObject playlistobject);

        public abstract void onRenamePlaylist(PlaylistObject playlistobject);
    }

    private static class ViewHolder
    {

        public ImageView mBtnMenu;
        public RelativeLayout mLayoutRoot;
        public TextView mTvNumberMusic;
        public TextView mTvOrder;
        public TextView mTvPlaylistName;

        private ViewHolder()
        {
        }

    }


    public static final String TAG = "PlaylistAdapter";
    private Typeface mTypefaceBold;
    private Typeface mTypefaceLight;
    private OnPlaylistListener onPlaylistListener;

    public PlaylistAdapter(Activity activity, ArrayList arraylist, Typeface typeface, Typeface typeface1)
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
        final PlaylistObject mPlaylistObject;
        int j;
        String s1;
        if(view == null)
        {
            viewholder = new ViewHolder();
            view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(R.layout.item_playlist, null);
            view.setTag(viewholder);
            viewholder.mTvPlaylistName = (TextView)view.findViewById(R.id.tv_playlist_name);
            viewholder.mTvPlaylistName.setTypeface(mTypefaceBold);
            viewholder.mTvNumberMusic = (TextView)view.findViewById(R.id.tv_number_music);
            viewholder.mTvNumberMusic.setTypeface(mTypefaceLight);
            viewholder.mTvOrder = (TextView)view.findViewById(R.id.tv_number);
            viewholder.mTvOrder.setTypeface(mTypefaceLight);
            viewholder.mBtnMenu = (ImageView)view.findViewById(R.id.img_menu);
            viewholder.mLayoutRoot = (RelativeLayout)view.findViewById(R.id.layout_root);
        } else
        {
            viewholder = (ViewHolder)view.getTag();
        }
        mPlaylistObject = (PlaylistObject)mListObjects.get(i);
        viewholder.mTvPlaylistName.setText(mPlaylistObject.getName());
        viewholder.mTvOrder.setText((new StringBuilder()).append(String.valueOf(i + 1)).append(".").toString());
        if(mPlaylistObject.getListTrackObjects() != null)
        {
            j = mPlaylistObject.getListTrackObjects().size();
        } else
        {
            j = 0;
        }
        if(j <= 1)
        {
            String s2 = mContext.getString(R.string.format_number_music);
            Object aobj1[] = new Object[1];
            aobj1[0] = Integer.valueOf(j);
            s1 = String.format(s2, aobj1);
        } else
        {
            String s = mContext.getString(R.string.format_number_musics);
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(j);
            s1 = String.format(s, aobj);
        }
        viewholder.mTvNumberMusic.setText(s1);
        viewholder.mLayoutRoot.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                if(onPlaylistListener != null)
                {
                    onPlaylistListener.onGoToDetail(mPlaylistObject);
                }
            }
        });
        viewholder.mBtnMenu.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view1)
            {
                showDiaglogOptions(mPlaylistObject);
            }
        });
        return view;
    }

    public void seOnPlaylistListener(OnPlaylistListener onplaylistlistener)
    {
        onPlaylistListener = onplaylistlistener;
    }

    public void showDiaglogOptions(final PlaylistObject mPlaylistObject)
    {
        (new android.app.AlertDialog.Builder(mContext)).setTitle(R.string.title_options).setItems(R.array.list_options_playlist, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                if(i != 0){
                	if(i != 1)
                    {
                    	if(i == 2 && onPlaylistListener != null)
                        	onPlaylistListener.onDeletePlaylist(mPlaylistObject);
                        return;
                    }
                    if(onPlaylistListener != null)
                    	onPlaylistListener.onRenamePlaylist(mPlaylistObject);
                }else if(onPlaylistListener != null)
                    onPlaylistListener.onPlayAllMusic(mPlaylistObject);
            }
        }).setPositiveButton(R.string.title_cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
            }
        }).create().show();
    }
}
