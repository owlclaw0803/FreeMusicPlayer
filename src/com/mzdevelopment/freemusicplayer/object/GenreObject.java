package com.mzdevelopment.freemusicplayer.object;


public class GenreObject
{

    private String id;
    private String keyword;
    private String name;

    public GenreObject(String s, String s1, String s2)
    {
        id = s;
        name = s1;
        keyword = s2;
    }

    public String getId()
    {
        return id;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public String getName()
    {
        return name;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setKeyword(String s)
    {
        keyword = s;
    }

    public void setName(String s)
    {
        name = s;
    }
}
