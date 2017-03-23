package com.mzdevelopment.freemusicplayer.soundclound;

import android.util.JsonReader;
import android.util.JsonToken;
import com.mzdevelopment.freemusicplayer.object.TopMusicObject;
import com.mzdevelopment.freemusicplayer.soundclound.object.CommentObject;
import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import com.ypyproductions.utils.*;
import java.io.*;
import java.util.ArrayList;
import org.json.*;

public class SoundCloundJsonParsingUtils
{

    public static final String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_PATTERN_ORI = "yyyy/MM/dd hh:mm:ss Z";
    public static final String TAG = "SoundCloundJsonParsingUtils";

    public SoundCloundJsonParsingUtils()
    {
    }

    private static CommentObject parsingCommentObject(JsonReader jsonreader)
    {
    	CommentObject commentobject1;
        if(jsonreader == null)
        	return null;
        CommentObject commentobject = null;
        try{
	        while(jsonreader.hasNext()){
		        String s = jsonreader.nextName();
		        if(s.equals("id")){
			        commentobject1 = new CommentObject();
			        commentobject1.setId(jsonreader.nextLong());
		        }else if(s.equals("created_at")){
			        if(commentobject == null)
			        	commentobject1 = commentobject;
			        else{
				        commentobject.setCreatedAt(jsonreader.nextString());
				        commentobject1 = commentobject;
			        }
		        }else if(s.equals("user_id")){
			        if(commentobject == null)
			        	commentobject1 = commentobject;
			        else{
				        commentobject.setUserId(jsonreader.nextLong());
				        commentobject1 = commentobject;
			        }
		        }else if(s.equals("track_id")){
			        if(commentobject == null)
			        	commentobject1 = commentobject;
			        else{
				        commentobject.setTrackid(jsonreader.nextLong());
				        commentobject1 = commentobject;
			        }
		        }else if(!s.equals("timestamp") || jsonreader.peek() == JsonToken.NULL){
		        	if(s.equals("body")){
		    	        if(commentobject == null)
		    	        	commentobject1 = commentobject;
		    	        else{
		    		        commentobject.setBody(jsonreader.nextString());
		    		        commentobject1 = commentobject;
		    	        }
		            }else if(!s.equals("user")){
		            	jsonreader.skipValue();
		                commentobject1 = commentobject;
		            }else{
		    	        jsonreader.beginObject();
		    	        while(jsonreader.hasNext()){
		    	        	String s1 = jsonreader.nextName();
		    	            if(!s1.equals("username")){
		    	            	if(!s1.equals("avatar_url") || jsonreader.peek() == JsonToken.NULL)
		    	            		jsonreader.skipValue();
		    	            	else if(commentobject != null)
		    	                	commentobject.setAvatarUrl(jsonreader.nextString());
		    	            }else if(commentobject != null)
		    	            	commentobject.setUsername(jsonreader.nextString());
		    	        }
		    	        jsonreader.endObject();
		    	        commentobject1 = commentobject;
		            }
		        }else if(commentobject == null)
		        	commentobject1 = commentobject;
		        else{
			        commentobject.setTimeStamp(jsonreader.nextInt());
			        commentobject1 = commentobject;
		        }
		        commentobject = commentobject1;
	        }
	        return commentobject;
        }catch(Exception e){
        	return null;
        }
    }

    public static ArrayList parsingListCommentObject(InputStream inputstream)
    {
    	JsonReader jsonreader;
    	CommentObject commentobject;
        if(inputstream == null){
        	(new Exception((new StringBuilder()).append(TAG).append(" data can not null").toString())).printStackTrace();
        	return null;
        }
        ArrayList arraylist;
        try{
	        jsonreader = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
	        arraylist = new ArrayList();
	        jsonreader.beginArray();
	        while(jsonreader.hasNext())
	        {
	        	jsonreader.beginObject();
	            commentobject = parsingCommentObject(jsonreader);
	            if(commentobject != null)
	            	arraylist.add(commentobject);
	            jsonreader.endObject();
	        }
	        jsonreader.endArray();
	        jsonreader.close();
	        DBLog.d(TAG, (new StringBuilder()).append("================>listCommentObjects size=").append(arraylist.size()).toString());
	        if(inputstream != null){
		        try{
		            inputstream.close();
		        }catch(IOException ioexception2){
		            ioexception2.printStackTrace();
		        }
	        }
	        return arraylist;
        }catch(Exception e){
        	return null;
        }
    }

    public static ArrayList parsingListTopMusicObject(InputStream inputstream)
    {
        if(inputstream == null){
        	(new Exception((new StringBuilder()).append(TAG).append(" data can not null").toString())).printStackTrace();
        	return null;
        }
        ArrayList arraylist;
        JsonReader jsonreader;
        try{
	        jsonreader = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
	        arraylist = new ArrayList();
	        jsonreader.beginObject();
	        while(jsonreader.hasNext())
	        {
	        	if(!jsonreader.nextName().equals("feed"))
	            {
	            	jsonreader.skipValue();
	            }else{
		            jsonreader.beginObject();
		            while(jsonreader.hasNext()){
		    	        if(!jsonreader.nextName().equals("entry"))
		    	        	jsonreader.skipValue();
		    	        else{
		    		        jsonreader.beginArray();
		    		        while(jsonreader.hasNext()){
		    			        TopMusicObject topmusicobject;
		    			        jsonreader.beginObject();
		    			        topmusicobject = parsingTopMusicObject(jsonreader);
		    			        if(topmusicobject != null)
		    			        	arraylist.add(topmusicobject);
		    			        jsonreader.endObject();
		    		        }
		    		        jsonreader.endArray();
		    	        }
		            }
		            jsonreader.endObject();
	            }
	        }
	        jsonreader.endObject();
	        jsonreader.close();
	        DBLog.d(TAG, (new StringBuilder()).append("================>listTopObjects size=").append(arraylist.size()).toString());
	        if(inputstream != null)
	        {
	            try
	            {
	                inputstream.close();
	            }
	            catch(IOException ioexception2)
	            {
	                ioexception2.printStackTrace();
	            }
	        }
	        return arraylist;
        }catch(Exception e){
        	return null;
        }
    }

    public static ArrayList parsingListTrackObject(InputStream inputstream)
    {
    	TrackObject trackobject;
        if(inputstream == null){
        	(new Exception((new StringBuilder()).append(TAG).append(" data can not null").toString())).printStackTrace();
        	return null;
    	}
        ArrayList arraylist;
        JsonReader jsonreader;
        try{
	        jsonreader = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
	        arraylist = new ArrayList();
	        jsonreader.beginArray();
	        while(jsonreader.hasNext())
	        {
	        	jsonreader.beginObject();
	            trackobject = parsingNewTrackObject(jsonreader);
	            if(trackobject != null)
	            {
	            	if(trackobject.isStreamable())
	                    arraylist.add(trackobject);
	            }
	            jsonreader.endObject();
	        }
	        jsonreader.endArray();
	        jsonreader.close();
	        DBLog.d(TAG, (new StringBuilder()).append("================>listTrackObjects size=").append(arraylist.size()).toString());
	        if(inputstream != null){
		        try{
		            inputstream.close();
		        }catch(IOException ioexception2){
		            ioexception2.printStackTrace();
		        }
	        }
	        return arraylist;
        }catch(Exception e){
        	return null;
        }
    }

    public static ArrayList parsingListTrackObject(String s)
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
	        while(j < i){
		        TrackObject trackobject = parsingTrackObject(jsonarray.getJSONObject(j));
		        if(trackobject != null)
		        	arraylist.add(trackobject);
		        j++;
	        }
	        DBLog.d(TAG, (new StringBuilder()).append("================>listTrackObjects size=").append(arraylist.size()).toString());
	        return arraylist;
        }catch(Exception e){
        	return null;
        }
    }

    private static TrackObject parsingNewTrackObject(JsonReader jsonreader)
    {
        if(jsonreader == null)
        	return null;
        TrackObject trackobject = new TrackObject();
        try{
	        while(jsonreader.hasNext()){
		        String s = jsonreader.nextName();
		        if(s.equals("id"))
		        	trackobject.setId(jsonreader.nextLong());
		        else if(s.equals("created_at"))
		        {
		        	if(trackobject != null)
		            	trackobject.setCreatedDate(DateTimeUtils.getDateFromString(jsonreader.nextString(), "yyyy/MM/dd hh:mm:ss Z"));
		        }else if(s.equals("user_id")){
		        	if(trackobject != null)
		            	trackobject.setUserId(jsonreader.nextLong());
		        }else if(s.equals("duration")){
		        	if(trackobject != null)
		            	trackobject.setDuration(jsonreader.nextLong());
		        }else if(s.equals("sharing")){
		        	if(trackobject != null)
		            	trackobject.setSharing(jsonreader.nextString());
		        }else if(s.equals("tag_list") && jsonreader.peek() != JsonToken.NULL){
		        	if(trackobject != null)
		            	trackobject.setTagList(jsonreader.nextString());
		        }else if(s.equals("streamable") && jsonreader.peek() != JsonToken.NULL){
		        	if(trackobject != null)
		            	trackobject.setStreamable(jsonreader.nextBoolean());
		        }else if(s.equals("downloadable") && jsonreader.peek() != JsonToken.NULL){
			        if(trackobject != null)
			        	trackobject.setDownloadable(jsonreader.nextBoolean());
		        }else if(s.equals("genre") && jsonreader.peek() != JsonToken.NULL){
		        	if(trackobject != null)
		            	trackobject.setGenre(jsonreader.nextString());
		        }else if(s.equals("title") && jsonreader.peek() != JsonToken.NULL){
		        	if(trackobject != null)
		            	trackobject.setTitle(jsonreader.nextString());
		        }else if(s.equals("description") && jsonreader.peek() != JsonToken.NULL){
		        	if(trackobject != null)
		            	trackobject.setDescription(jsonreader.nextString());
		        }else if(s.equals("user")){
		        	jsonreader.beginObject();
		            String s1;
		            while(jsonreader.hasNext())
		            {
		            	s1 = jsonreader.nextName();
		                if(s1.equals("username")){
		        	        if(trackobject != null)
		        	        	trackobject.setUsername(jsonreader.nextString());
		                }else if(!s1.equals("avatar_url") || jsonreader.peek() == JsonToken.NULL)
		                	jsonreader.skipValue();
		                else if(trackobject != null)
		                	trackobject.setAvatarUrl(jsonreader.nextString());
		            }
		            jsonreader.endObject();
		        }else if(s.equals("permalink_url")) {
		        	if(trackobject != null)
		            	trackobject.setPermalinkUrl(jsonreader.nextString());
		        }else if(!(!s.equals("artwork_url") || jsonreader.peek() == JsonToken.NULL)){
		        	if(trackobject != null)
		            	trackobject.setArtworkUrl(jsonreader.nextString());
		        } else if(s.equals("waveform_url")) {
		        	if(trackobject != null)
		            	trackobject.setWaveForm(jsonreader.nextString());
		        }else if(s.equals("playback_count")){
		        	if(trackobject != null)
		            	trackobject.setPlaybackCount(jsonreader.nextLong());
		        }else if(s.equals("favoritings_count")){
		        	if(trackobject != null)
		            	trackobject.setFavoriteCount(jsonreader.nextLong());
		        }else if(s.equals("comment_count")){
		        	if(trackobject != null)
		            	trackobject.setCommentCount(jsonreader.nextLong());
		        }else
		        	jsonreader.skipValue();
	        }
	        return trackobject;
        }catch(Exception e){
        	return null;
        }
    }

    private static TopMusicObject parsingTopMusicObject(JsonReader jsonreader)
    {
        if(jsonreader == null)
        	return null;
        TopMusicObject topmusicobject = null;
        TopMusicObject topmusicobject1;
        try{
	        while(jsonreader.hasNext()){
		        String s = jsonreader.nextName();
		        if(s.equals("im:name")){
			        topmusicobject1 = new TopMusicObject();
			        jsonreader.beginObject();
			        while(jsonreader.hasNext()){
				        if(!jsonreader.nextName().equals("label"))
				        	jsonreader.skipValue();
				        else
				        	topmusicobject1.setName(jsonreader.nextString());
			        }
			        jsonreader.endObject();
		        }else if(!s.equals("im:image"))
		        {
		        	if(!s.equals("im:artist"))
		            {
		            	jsonreader.skipValue();
		                topmusicobject1 = topmusicobject;
		            }else{
		    	        jsonreader.beginObject();
		    	        while(jsonreader.hasNext())
		    	        {
		    	        	if(jsonreader.nextName().equals("label"))
		    	            	topmusicobject.setArtist(jsonreader.nextString());
		    	            else
		    	            	jsonreader.skipValue();
		    	        }
		    	        jsonreader.endObject();
		    	        topmusicobject1 = topmusicobject;
		            }
		        }else{
			        jsonreader.beginArray();
			        while(jsonreader.hasNext())
			        {
			        	jsonreader.beginObject();
			            while(jsonreader.hasNext())
			            {
			            	if(!jsonreader.nextName().equals("label"))
			                	jsonreader.skipValue();
			                else
			                	topmusicobject.setArtwork(jsonreader.nextString());
			            	
			            }
			            jsonreader.endObject();
			        }
			        jsonreader.endArray();
			        topmusicobject1 = topmusicobject;
		        }
		        topmusicobject = topmusicobject1;
	        }
	        return topmusicobject;
        }catch(Exception e){
        	return null;
        }
    }

    public static TrackObject parsingTrackObject(String s)
    {
    	try{
	        if(StringUtils.isEmptyString(s))
	        	return null;
	        TrackObject trackobject = parsingTrackObject(new JSONObject(s));
	        return trackobject;
    	}catch(Exception e){
    		return null;
    	}
    }

    private static TrackObject parsingTrackObject(JSONObject jsonobject)
    {
        if(jsonobject == null)
        	return null;
        TrackObject trackobject;
        try{
	        long l = jsonobject.getLong("id");
	        String s = jsonobject.getString("title");
	        long l1 = jsonobject.getLong("duration");
	        trackobject = new TrackObject(l, s, DateTimeUtils.getDateFromString(jsonobject.getString("created_at"), "yyyy/MM/dd HH:mm:ss"), l1, jsonobject.getString("username"), jsonobject.getString("avatar_url"), jsonobject.getString("permalink_url"), jsonobject.getString("artwork_url"), jsonobject.getLong("playback_count"), jsonobject.getString("path"));
	        trackobject.setStreamable(true);
	        return trackobject;
        }catch(Exception e){
        	return null;
        }
    }
}
