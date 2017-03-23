package com.mzdevelopment.freemusicplayer.dataMng;

import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.object.GenreObject;
import com.mzdevelopment.freemusicplayer.object.PlaylistObject;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.utils.DateTimeUtils;
import com.ypyproductions.utils.StringUtils;
import java.util.ArrayList;
import org.json.*;

public class JsonParsingUtils
    implements ICloudMusicPlayerConstants
{

    public static final String TAG = "JsonParsingUtils";
    public static final String TAG_IMAGE = "image";
    public static final String TAG_NAME = "name";
    public static final String TAG_STATUS = "status";

    public JsonParsingUtils()
    {
    }

    public static ArrayList parsingGenreObject(String s)
    {
        if(StringUtils.isEmptyString(s))
        	return null;
        JSONArray jsonarray;
        int i;
        try{
	        jsonarray = new JSONArray(s);
	        i = jsonarray.length();
	        if(i <= 0)
	        	return null;
	        ArrayList arraylist = new ArrayList();
	        int j = 0;
	        while(j < i)
	        {
	        	JSONObject jsonobject = jsonarray.getJSONObject(j);
	            arraylist.add(new GenreObject(jsonobject.getString("type"), jsonobject.getString("name"), jsonobject.getString("keyword")));
	            j++;
	        }
	        return arraylist;
        }catch(Exception e){
        	return null;
        }
    }

    public static ArrayList parsingListSavingTrackObject(String s)
    {
    	JSONObject jsonobject;
        long l;
        long l1;
        String s1;
        String s2;
        java.util.Date date;
        String s3;
        if(StringUtils.isEmptyString(s))
        	return null;
        JSONArray jsonarray;
        int i;
        try{
	        jsonarray = new JSONArray(s);
	        i = jsonarray.length();
	        if(i <= 0)
	        	return null;
	        ArrayList arraylist = new ArrayList();
	        int j = 0;
	        while(j < i){
		        jsonobject = jsonarray.getJSONObject(j);
		        l = jsonobject.getLong("id");
		        l1 = jsonobject.getLong("duration");
		        s1 = jsonobject.getString("title");
		        s2 = jsonobject.getString("username");
		        date = DateTimeUtils.getDateFromString(jsonobject.getString("created_at"), "yyyy/MM/dd HH:mm:ss");
		        s3 = jsonobject.getString("artwork_url");
		        if(jsonobject.opt("path") == null){
		        	String s4 = jsonobject.getString("avatar_url");
		            String s5 = jsonobject.getString("permalink_url");
		            long l2 = jsonobject.getLong("playback_count");
		            TrackObject trackobject1 = new TrackObject(l, date, l1, s1, "", s2, s4, s5, s3, l2);
		            trackobject1.setStreamable(true);
		            arraylist.add(trackobject1);
		        }else{
			        TrackObject trackobject = new TrackObject(l, s1, date, l1, s2, "", "", s3, -1L, jsonobject.getString("path"));
			        trackobject.setStreamable(true);
			        arraylist.add(trackobject);
		        }
		        j++;
	        }
	        return arraylist;
        }catch(Exception e){
        	return null;
        }
    }

    public static ArrayList parsingPlaylistObject(String s)
    {
    	PlaylistObject playlistobject;
        JSONArray jsonarray1;
        int k;
        ArrayList arraylist1;
        if(StringUtils.isEmptyString(s))
        	return null;
        JSONArray jsonarray;
        int i;
        try{
	        jsonarray = new JSONArray(s);
	        i = jsonarray.length();
	        if(i <= 0)
	        	return null;
	        ArrayList arraylist = new ArrayList();
	        int j = 0;
	        while(j < i)
	        {
	        	JSONObject jsonobject = jsonarray.getJSONObject(j);
	            playlistobject = new PlaylistObject(jsonobject.getLong("id"), jsonobject.getString("name"));
	            jsonarray1 = jsonobject.getJSONArray("tracks");
	            k = jsonarray1.length();
	            arraylist1 = new ArrayList();
	            if(k > 0){
	            	int l = 0;
	                while(l < k)
	                {
	                	arraylist1.add(Long.valueOf(jsonarray1.getLong(l)));
	                    l++;
	                }
	            }
	            playlistobject.setListTrackIds(arraylist1);
	            arraylist.add(playlistobject);
	            j++;
	        }
	        return arraylist;
        }catch(Exception e){
        	return null;
        }
    }
}
