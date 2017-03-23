package com.mzdevelopment.freemusicplayer.object;


public class TopMusicObject
{

    private String artist;
    private String artwork;
    private String name;

    public TopMusicObject()
    {
    }

    public TopMusicObject(String s, String s1, String s2)
    {
        name = s;
        artist = s1;
        artwork = s2;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getArtwork()
    {
        return artwork;
    }

    public String getName()
    {
        return name;
    }

    public void setArtist(String s)
    {
        artist = s;
    }

    public void setArtwork(String s)
    {
        artwork = s;
    }

    public void setName(String s)
    {
        name = s;
    }
}
