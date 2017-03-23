package com.mzdevelopment.freemusicplayer.soundclound;

import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.utils.DBLog;
import com.ypyproductions.utils.StringUtils;
import com.ypyproductions.webservice.DownloadUtils;
import java.util.ArrayList;
import java.util.Locale;

public class SoundCloundAPI
    implements ISoundCloundConstants
{

    private static final String TAG = "SoundCloundAPI";
    private String clientId;
    private String clientSecret;
    private String mPrefixClientId;

    public SoundCloundAPI(String s, String s1)
    {
        clientId = s;
        clientSecret = s1;
        mPrefixClientId = String.format("?client_id=%1$s", new Object[] {
            s
        });
    }

    public String getClientId()
    {
        return clientId;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }

    public ArrayList getListCommentObject(long l)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("https://api.soundcloud.com/");
        stringbuilder.append("tracks/");
        stringbuilder.append((new StringBuilder()).append(String.valueOf(l)).append("/").toString());
        stringbuilder.append("comments");
        stringbuilder.append(".json");
        stringbuilder.append(mPrefixClientId);
        String s = stringbuilder.toString();
        DBLog.d(TAG, (new StringBuilder()).append("==============>getListCommentObject=").append(s).toString());
        java.io.InputStream inputstream = DownloadUtils.download(s);
        if(inputstream != null)
        {
            return SoundCloundJsonParsingUtils.parsingListCommentObject(inputstream);
        } else
        {
            return null;
        }
    }

    public ArrayList getListTopMusic(String s, int i)
    {
        StringBuilder stringbuilder = new StringBuilder();
        Object aobj[] = new Object[2];
        aobj[0] = s.toLowerCase(Locale.US);
        aobj[1] = String.valueOf(i);
        stringbuilder.append(String.format("https://itunes.apple.com/%1$s/rss/topsongs/limit=%2$s/json", aobj));
        String s1 = stringbuilder.toString();
        DBLog.d(TAG, (new StringBuilder()).append("==============>getListTopMusic=").append(s1).toString());
        java.io.InputStream inputstream = DownloadUtils.download(s1);
        if(inputstream != null)
        {
            return SoundCloundJsonParsingUtils.parsingListTopMusicObject(inputstream);
        } else
        {
            return null;
        }
    }

    public ArrayList getListTrackObjectsByGenre(String s, int i, int j)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("https://api.soundcloud.com/");
        stringbuilder.append("tracks");
        stringbuilder.append(".json");
        stringbuilder.append(mPrefixClientId);
        stringbuilder.append(String.format("&genres=%1$s", new Object[] {
            s
        }));
        Object aobj[] = new Object[2];
        aobj[0] = String.valueOf(i);
        aobj[1] = String.valueOf(j);
        stringbuilder.append(String.format("&offset=%1$s&limit=%2$s", aobj));
        String s1 = stringbuilder.toString();
        DBLog.d(TAG, (new StringBuilder()).append("==============>getListTrackObjectsByGenre=").append(s1).toString());
        java.io.InputStream inputstream = DownloadUtils.download(s1);
        if(inputstream != null)
        {
            return SoundCloundJsonParsingUtils.parsingListTrackObject(inputstream);
        } else
        {
            return null;
        }
    }

    public ArrayList getListTrackObjectsByQuery(String s, int i, int j)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("https://api.soundcloud.com/");
        stringbuilder.append("tracks");
        stringbuilder.append(".json");
        stringbuilder.append(mPrefixClientId);
        stringbuilder.append(String.format("&q=%1$s", new Object[] {
            s
        }));
        Object aobj[] = new Object[2];
        aobj[0] = String.valueOf(i);
        aobj[1] = String.valueOf(j);
        stringbuilder.append(String.format("&offset=%1$s&limit=%2$s", aobj));
        String s1 = stringbuilder.toString();
        DBLog.d(TAG, (new StringBuilder()).append("==============>getListTrackObjectsByQuery=").append(s1).toString());
        java.io.InputStream inputstream = DownloadUtils.download(s1);
        if(inputstream != null)
        {
            return SoundCloundJsonParsingUtils.parsingListTrackObject(inputstream);
        } else
        {
            return null;
        }
    }

    public ArrayList getListTrackObjectsByTypes(String s, int i, int j)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("https://api.soundcloud.com/");
        stringbuilder.append("tracks");
        stringbuilder.append(".json");
        stringbuilder.append(mPrefixClientId);
        stringbuilder.append(String.format("&types=%1$s", new Object[] {
            s
        }));
        Object aobj[] = new Object[2];
        aobj[0] = String.valueOf(i);
        aobj[1] = String.valueOf(j);
        stringbuilder.append(String.format("&offset=%1$s&limit=%2$s", aobj));
        String s1 = stringbuilder.toString();
        DBLog.d(TAG, (new StringBuilder()).append("==============>getListTrackObjectsByQuery=").append(s1).toString());
        java.io.InputStream inputstream = DownloadUtils.download(s1);
        if(inputstream != null)
        {
            return SoundCloundJsonParsingUtils.parsingListTrackObject(inputstream);
        } else
        {
            return null;
        }
    }

    public ArrayList getListTrackObjectsOfUser(long l)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("https://api.soundcloud.com/");
        stringbuilder.append("users/");
        stringbuilder.append((new StringBuilder()).append(String.valueOf(l)).append("/").toString());
        stringbuilder.append("tracks");
        stringbuilder.append(".json");
        stringbuilder.append(mPrefixClientId);
        String s = stringbuilder.toString();
        DBLog.d(TAG, (new StringBuilder()).append("==============>getListTrackObjectsOfUser=").append(s).toString());
        java.io.InputStream inputstream = DownloadUtils.download(s);
        if(inputstream != null)
        {
            return SoundCloundJsonParsingUtils.parsingListTrackObject(inputstream);
        } else
        {
            return null;
        }
    }

    public TrackObject getTrackObject(long l)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("https://api.soundcloud.com/");
        stringbuilder.append("tracks");
        stringbuilder.append("/");
        stringbuilder.append(String.valueOf(l));
        stringbuilder.append(".json");
        stringbuilder.append(mPrefixClientId);
        String s = stringbuilder.toString();
        DBLog.d(TAG, (new StringBuilder()).append("==============>getTrackObject=").append(s).toString());
        String s1 = DownloadUtils.downloadString(s);
        if(!StringUtils.isEmptyString(s1))
        {
            return SoundCloundJsonParsingUtils.parsingTrackObject(s1);
        } else
        {
            return null;
        }
    }

    public void setClientId(String s)
    {
        clientId = s;
    }

    public void setClientSecret(String s)
    {
        clientSecret = s;
    }

}
