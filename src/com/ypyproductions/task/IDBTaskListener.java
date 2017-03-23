package com.ypyproductions.task;


public interface IDBTaskListener
{

    public abstract void onDoInBackground();

    public abstract void onPostExcute();

    public abstract void onPreExcute();
}
