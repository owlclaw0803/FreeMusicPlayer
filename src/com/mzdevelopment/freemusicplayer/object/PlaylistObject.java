package com.mzdevelopment.freemusicplayer.object;

import com.mzdevelopment.freemusicplayer.soundclound.object.TrackObject;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlaylistObject
{

    private long id;
    private ArrayList listTrackIds;
    private ArrayList listTrackObjects;
    private String name;

    public PlaylistObject(long l, String s)
    {
        id = l;
        name = s;
        listTrackObjects = new ArrayList();
    }

    public void addTrackObject(TrackObject trackobject, boolean flag)
    {
        if(listTrackObjects != null && trackobject != null)
        {
            listTrackObjects.add(trackobject);
            if(flag)
            {
                listTrackIds.add(Long.valueOf(trackobject.getId()));
            }
        }
    }

    public long getId()
    {
        return id;
    }

    public ArrayList getListTrackIds()
    {
        return listTrackIds;
    }

    public ArrayList getListTrackObjects()
    {
        return listTrackObjects;
    }

    public String getName()
    {
        return name;
    }

    public TrackObject getTrackObject(long l)
    {
label0:
        {
            if(listTrackObjects == null || listTrackObjects.size() <= 0)
            {
                break label0;
            }
            Iterator iterator = listTrackObjects.iterator();
            TrackObject trackobject;
            do
            {
                if(!iterator.hasNext())
                {
                    break label0;
                }
                trackobject = (TrackObject)iterator.next();
            } while(trackobject.getId() != l);
            return trackobject;
        }
        return null;
    }

    public boolean isSongAlreadyExited(long l)
    {
label0:
        {
            if(listTrackIds == null || listTrackIds.size() <= 0)
            {
                break label0;
            }
            Iterator iterator = listTrackIds.iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break label0;
                }
            } while(((Long)iterator.next()).longValue() != l);
            return true;
        }
        return false;
    }

    public void removeTrackObject(long l)
    {
label0:
        {
            if(listTrackObjects == null || listTrackObjects.size() <= 0)
            {
                break label0;
            }
            Iterator iterator = listTrackObjects.iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break label0;
                }
            } while(((TrackObject)iterator.next()).getId() != l);
            iterator.remove();
        }
    }

    public void removeTrackObject(TrackObject trackobject)
    {
label0:
        {
            if(listTrackObjects == null || trackobject == null)
            {
                break label0;
            }
            Iterator iterator = listTrackObjects.iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                if(((TrackObject)iterator.next()).getId() != trackobject.getId())
                {
                    continue;
                }
                iterator.remove();
                break;
            } while(true);
            Iterator iterator1 = listTrackIds.iterator();
            do
            {
                if(!iterator1.hasNext())
                {
                    break label0;
                }
            } while(((Long)iterator1.next()).longValue() != trackobject.getId());
            iterator1.remove();
        }
    }

    public void setId(long l)
    {
        id = l;
    }

    public void setListTrackIds(ArrayList arraylist)
    {
        listTrackIds = arraylist;
    }

    public void setListTrackObjects(ArrayList arraylist)
    {
        listTrackObjects = arraylist;
    }

    public void setName(String s)
    {
        name = s;
    }

    public JSONObject toJson()
    {
        JSONObject jsonobject;
        JSONArray jsonarray;
        try
        {
            jsonobject = new JSONObject();
            jsonobject.put("id", id);
            jsonobject.put("name", name);
            jsonarray = new JSONArray();
            if(listTrackObjects != null && listTrackObjects.size() > 0)
            {
                for(Iterator iterator = listTrackObjects.iterator(); iterator.hasNext(); jsonarray.put(((TrackObject)iterator.next()).getId())) { }
            }
            jsonobject.put("tracks", jsonarray);
            return jsonobject;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}
