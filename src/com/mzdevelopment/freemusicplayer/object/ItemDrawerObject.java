package com.mzdevelopment.freemusicplayer.object;


public class ItemDrawerObject
{

    private int iconRes;
    private boolean isSelected;
    private String name;

    public ItemDrawerObject(String s)
    {
        name = s;
    }

    public int getIconRes()
    {
        return iconRes;
    }

    public String getName()
    {
        return name;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setIconRes(int i)
    {
        iconRes = i;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setSelected(boolean flag)
    {
        isSelected = flag;
    }
}
