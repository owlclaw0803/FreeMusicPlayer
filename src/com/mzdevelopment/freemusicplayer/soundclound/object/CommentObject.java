package com.mzdevelopment.freemusicplayer.soundclound.object;


public class CommentObject
{

    private String avatarUrl;
    private String body;
    private String createdAt;
    private long id;
    private int timeStamp;
    private long trackid;
    private long userId;
    private String username;

    public CommentObject()
    {
    }

    public CommentObject(long l, String s, long l1, long l2, 
            int i, String s1, String s2, String s3)
    {
        id = l;
        createdAt = s;
        userId = l1;
        trackid = l2;
        timeStamp = i;
        body = s1;
        username = s2;
        avatarUrl = s3;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public String getBody()
    {
        return body;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public long getId()
    {
        return id;
    }

    public int getTimeStamp()
    {
        return timeStamp;
    }

    public long getTrackid()
    {
        return trackid;
    }

    public long getUserId()
    {
        return userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setAvatarUrl(String s)
    {
        avatarUrl = s;
    }

    public void setBody(String s)
    {
        body = s;
    }

    public void setCreatedAt(String s)
    {
        createdAt = s;
    }

    public void setId(long l)
    {
        id = l;
    }

    public void setTimeStamp(int i)
    {
        timeStamp = i;
    }

    public void setTrackid(long l)
    {
        trackid = l;
    }

    public void setUserId(long l)
    {
        userId = l;
    }

    public void setUsername(String s)
    {
        username = s;
    }
}
