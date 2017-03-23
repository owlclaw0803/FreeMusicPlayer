package com.mzdevelopment.freemusicplayer.dataMng;

import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XMLParsingData
{

    public static final String TAG = "XMLParsingData";
    public static final String TAG_COMPLETE_SUGGESTION = "CompleteSuggestion";
    public static final String TAG_SUGGESTION = "suggestion";
    public static final String TAG_TOP_LEVEL = "toplevel";

    public XMLParsingData()
    {
    }

    public static ArrayList parsingSuggestion(InputStream inputstream)
    {
        if(inputstream == null)
        	return null;
        ArrayList arraylist = null;
        XmlPullParser xmlpullparser;
        int i;
        try{
	        xmlpullparser = Xml.newPullParser();
	        xmlpullparser.setInput(inputstream, "ISO-8859-1");
	        i = xmlpullparser.getEventType();
	        arraylist = new ArrayList();
	        while(i != 1){
		        switch(i){
		        default:case 0:case 1:
		        	break;
		        case 2:
		        	if(xmlpullparser.getName().equals("suggestion"))
		            	arraylist.add(xmlpullparser.getAttributeValue(0));
		        	break;
		        }
		        i = xmlpullparser.next();
	        }
	        if(inputstream != null)
	        {
	            try
	            {
	                inputstream.close();
	            }
	            catch(IOException ioexception4)
	            {
	                ioexception4.printStackTrace();
	            }
	        }
	        return arraylist;
        }catch(Exception e){
        	return null;
        }
    }
}
