package com.mzdevelopment.freemusicplayer.soundclound.object;

import android.content.ContentUris;
import android.net.Uri;
import com.ypyproductions.utils.DateTimeUtils;
import com.ypyproductions.utils.StringUtils;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class TrackObject
{

    private String artworkUrl;
    private String avatarUrl;
    private long commentCount;
    private Date createdDate;
    private String description;
    private boolean downloadable;
    private long duration;
    private long favoriteCount;
    private String genre;
    private long id;
    private boolean isLocalMusic;
    private boolean isStreamable;
    private String linkStream;
    private String path;
    private String permalinkUrl;
    private long playbackCount;
    private String sharing;
    private String tagList;
    private String title;
    private long userId;
    private String username;
    private String waveForm;

    public TrackObject()
    {
    }

    public TrackObject(long l, String s, Date date, long l1, String s1, 
            String s2, String s3, String s4, long l2, String s5)
    {
        id = l;
        title = s;
        createdDate = date;
        duration = l1;
        username = s1;
        avatarUrl = s2;
        permalinkUrl = s3;
        artworkUrl = s4;
        playbackCount = l2;
        path = s5;
    }

    public TrackObject(long l, Date date, long l1, String s, String s1, 
            String s2, String s3, String s4, String s5, long l2)
    {
        id = l;
        createdDate = date;
        duration = l1;
        title = s;
        description = s1;
        username = s2;
        avatarUrl = s3;
        permalinkUrl = s4;
        artworkUrl = s5;
        playbackCount = l2;
    }

    public TrackObject clone()
    {
        TrackObject trackobject = new TrackObject(id, title, createdDate, duration, username, avatarUrl, permalinkUrl, artworkUrl, playbackCount, path);
        trackobject.setCreatedDate(new Date());
        return trackobject;
    }

    public String getArtworkUrl()
    {
        return artworkUrl;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public long getCommentCount()
    {
        return commentCount;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public String getDescription()
    {
        return description;
    }

    public long getDuration()
    {
        return duration;
    }

    public long getFavoriteCount()
    {
        return favoriteCount;
    }

    public String getGenre()
    {
        return genre;
    }

    public long getId()
    {
        return id;
    }

    public String getLinkStream()
    {
        return linkStream;
    }

    public String getPath()
    {
        return path;
    }

    public String getPermalinkUrl()
    {
        return permalinkUrl;
    }

    public long getPlaybackCount()
    {
        return playbackCount;
    }

    public String getSharing()
    {
        return sharing;
    }

    public String getTagList()
    {
        return tagList;
    }

    public String getTitle()
    {
        return title;
    }

    public Uri getURI()
    {
        return ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }

    public long getUserId()
    {
        return userId;
    }

    public String getUsername()
    {
        return username;
    }

    public String getWaveForm()
    {
        return waveForm;
    }

    public boolean isDownloadable()
    {
        return downloadable;
    }

    public boolean isLocalMusic()
    {
        return isLocalMusic;
    }

    public boolean isStreamable()
    {
        return isStreamable;
    }

    public void setArtworkUrl(String s)
    {
        artworkUrl = s;
    }

    public void setAvatarUrl(String s)
    {
        avatarUrl = s;
    }

    public void setCommentCount(long l)
    {
        commentCount = l;
    }

    public void setCreatedDate(Date date)
    {
        createdDate = date;
    }

    public void setDescription(String s)
    {
        description = s;
    }

    public void setDownloadable(boolean flag)
    {
        downloadable = flag;
    }

    public void setDuration(long l)
    {
        duration = l;
    }

    public void setFavoriteCount(long l)
    {
        favoriteCount = l;
    }

    public void setGenre(String s)
    {
        genre = s;
    }

    public void setId(long l)
    {
        id = l;
    }

    public void setLinkStream(String s)
    {
        linkStream = s;
    }

    public void setLocalMusic(boolean flag)
    {
        isLocalMusic = flag;
    }

    public void setPath(String s)
    {
        path = s;
    }

    public void setPermalinkUrl(String s)
    {
        permalinkUrl = s;
    }

    public void setPlaybackCount(long l)
    {
        playbackCount = l;
    }

    public void setSharing(String s)
    {
        sharing = s;
    }

    public void setStreamable(boolean flag)
    {
        isStreamable = flag;
    }

    public void setTagList(String s)
    {
        tagList = s;
    }

    public void setTitle(String s)
    {
        title = s;
    }

    public void setUserId(long l)
    {
        userId = l;
    }

    public void setUsername(String s)
    {
        username = s;
    }

    public void setWaveForm(String s)
    {
        waveForm = s;
    }

    public JSONObject toJsonObject()
    {
        JSONObject jsonobject = new JSONObject();
        try{
	        jsonobject.put("id", id);
	        jsonobject.put("title", title);
	        jsonobject.put("username", username);
	        jsonobject.put("created_at", DateTimeUtils.convertDateToString(createdDate, "yyyy/MM/dd HH:mm:ss"));
	        jsonobject.put("duration", duration);
	        String s;
	        if(artworkUrl == null)
	            s = "";
	        else
	            s = artworkUrl;
	        jsonobject.put("artwork_url", s);
	        if(!StringUtils.isEmptyString(path))
	        {
	        	jsonobject.put("path", path);
	            return jsonobject;
	        }
	        jsonobject.put("avatar_url", avatarUrl);
	        jsonobject.put("permalink_url", permalinkUrl);
	        jsonobject.put("playback_count", playbackCount);
	        return jsonobject;
        }catch(Exception e){
        	return null;
        }
    }
}
