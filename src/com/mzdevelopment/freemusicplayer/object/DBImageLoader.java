package com.mzdevelopment.freemusicplayer.object;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.ypyproductions.utils.StringUtils;
import java.io.*;

public class DBImageLoader extends BaseImageDownloader
{

    public static final String TAG = "DBImageLoader";

    public DBImageLoader(Context context)
    {
        super(context);
    }

    protected InputStream getStreamFromContent(String s, Object obj)
        throws FileNotFoundException
    {
        Uri uri;
        if(StringUtils.isEmptyString(s))
        	return super.getStreamFromContent(s, obj);
        uri = Uri.parse(s);
        if(uri == null)
        	return super.getStreamFromContent(s, obj);
        MediaMetadataRetriever mediametadataretriever;
        byte abyte0[];
        mediametadataretriever = new MediaMetadataRetriever();
        mediametadataretriever.setDataSource(context, uri);
        abyte0 = mediametadataretriever.getEmbeddedPicture();
        if(abyte0 == null)
        {
        	try
            {
                mediametadataretriever.release();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            return super.getStreamFromContent(s, obj);
        }
        ByteArrayInputStream bytearrayinputstream;
        if(abyte0.length <= 0)
        {
        	try
            {
                mediametadataretriever.release();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            return super.getStreamFromContent(s, obj);
        }
        bytearrayinputstream = new ByteArrayInputStream(abyte0);
        mediametadataretriever.release();
        return bytearrayinputstream;
    }
}
