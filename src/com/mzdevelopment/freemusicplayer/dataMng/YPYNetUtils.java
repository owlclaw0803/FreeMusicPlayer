package com.mzdevelopment.freemusicplayer.dataMng;

import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.soundclound.ISoundCloundConstants;
import com.ypyproductions.utils.DBLog;
import com.ypyproductions.utils.StringUtils;
import com.ypyproductions.webservice.DownloadUtils;
import org.json.JSONObject;

public class YPYNetUtils
    implements ISoundCloundConstants, ICloudMusicPlayerConstants
{

    private static final String TAG = "YPYNetUtils";

    public YPYNetUtils()
    {
    }

    public static String getLinkStreamFromSoundClound(long l)
    {
        Object aobj[] = new Object[2];
        aobj[0] = String.valueOf(l);
        aobj[1] = SOUNDCLOUND_CLIENT_ID;
        String s = String.format("http://api.soundcloud.com/tracks/%1$s/stream?client_id=%2$s", aobj);
        String s1 = DownloadUtils.downloadString(s);
        DBLog.d(TAG, (new StringBuilder()).append("=========>dataServer=").append(s1).toString());
        boolean flag = StringUtils.isEmptyString(s1);
        String s2 = null;
        if(!flag)
        {
            String s3;
            try
            {
                s3 = (new JSONObject(s1)).getString("http_mp3_128_url");
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
                return s;
            }
            s2 = s3;
        }
        return s2;
    }

}
