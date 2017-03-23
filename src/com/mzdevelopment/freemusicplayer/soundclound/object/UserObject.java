package com.mzdevelopment.freemusicplayer.soundclound.object;


public class UserObject
{

    private String avatarUrl;
    private String city;
    private String country;
    private String description;
    private int followers;
    private int following;
    private String fullName;
    private long id;
    private String permalink;
    private String permalinkUrl;
    private int playlistCount;
    private int trackCount;
    private String username;

    public UserObject()
    {
    }

    public UserObject(long l, String s, String s1, String s2, String s3)
    {
        id = l;
        permalink = s;
        username = s1;
        permalinkUrl = s2;
        avatarUrl = s3;
    }

    public UserObject(long l, String s, String s1, String s2, String s3, String s4, 
            String s5, String s6, String s7, int i, int j, int k, int i1)
    {
        id = l;
        permalink = s;
        username = s1;
        permalinkUrl = s2;
        avatarUrl = s3;
        country = s4;
        fullName = s5;
        description = s6;
        city = s7;
        trackCount = i;
        playlistCount = j;
        followers = k;
        following = i1;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public String getCity()
    {
        return city;
    }

    public String getCountry()
    {
        return country;
    }

    public String getDescription()
    {
        return description;
    }

    public int getFollowers()
    {
        return followers;
    }

    public int getFollowing()
    {
        return following;
    }

    public String getFullName()
    {
        return fullName;
    }

    public long getId()
    {
        return id;
    }

    public String getPermalink()
    {
        return permalink;
    }

    public String getPermalinkUrl()
    {
        return permalinkUrl;
    }

    public int getPlaylistCount()
    {
        return playlistCount;
    }

    public int getTrackCount()
    {
        return trackCount;
    }

    public String getUsername()
    {
        return username;
    }

    public void setAvatarUrl(String s)
    {
        avatarUrl = s;
    }

    public void setCity(String s)
    {
        city = s;
    }

    public void setCountry(String s)
    {
        country = s;
    }

    public void setDescription(String s)
    {
        description = s;
    }

    public void setFollowers(int i)
    {
        followers = i;
    }

    public void setFollowing(int i)
    {
        following = i;
    }

    public void setFullName(String s)
    {
        fullName = s;
    }

    public void setId(long l)
    {
        id = l;
    }

    public void setPermalink(String s)
    {
        permalink = s;
    }

    public void setPermalinkUrl(String s)
    {
        permalinkUrl = s;
    }

    public void setPlaylistCount(int i)
    {
        playlistCount = i;
    }

    public void setTrackCount(int i)
    {
        trackCount = i;
    }

    public void setUsername(String s)
    {
        username = s;
    }
}
