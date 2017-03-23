package com.ypyproductions.dialog.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogUtils
{
    public static interface IOnDialogListener
    {

        public abstract void onClickButtonNegative();

        public abstract void onClickButtonPositive();
    }


    public AlertDialogUtils()
    {
    }

    public static Dialog createFullDialog(Context context, int i, int j, int k, int l, int i1, final IOnDialogListener mOnDialogListener)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        if(i != 0)
        {
            builder.setIcon(i);
        }
        builder.setTitle(j).setMessage(i1).setCancelable(false).setPositiveButton(k, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int j1)
            {
                if(mOnDialogListener != null)
                {
                    mOnDialogListener.onClickButtonPositive();
                }
            }
        }).setNegativeButton(l, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int j1)
            {
                if(mOnDialogListener != null)
                {
                    mOnDialogListener.onClickButtonNegative();
                }
            }
        });
        return builder.create();
    }

    public static Dialog createFullDialog(Context context, int i, int j, int k, int l, String s, final IOnDialogListener mOnDialogListener)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        if(i != 0)
        {
            builder.setIcon(i);
        }
        builder.setTitle(j).setMessage(s).setCancelable(false).setPositiveButton(k, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i1)
            {
                if(mOnDialogListener != null)
                {
                    mOnDialogListener.onClickButtonPositive();
                }
            }
        }).setNegativeButton(l, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i1)
            {
                if(mOnDialogListener != null)
                {
                    mOnDialogListener.onClickButtonNegative();
                }
            }
        });
        return builder.create();
    }

    public static Dialog createInfoDialog(Context context, int i, int j, int k, int l, final IOnDialogListener mOnDialogListener)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        if(i != 0)
        {
            builder.setIcon(i);
        }
        builder.setTitle(j).setMessage(l).setCancelable(false).setPositiveButton(k, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i1)
            {
                if(mOnDialogListener != null)
                {
                    mOnDialogListener.onClickButtonPositive();
                }
            }
        });
        return builder.create();
    }

    public static Dialog createInfoDialog(Context context, int i, int j, int k, String s, final IOnDialogListener mOnDialogListener)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        if(i != 0)
        {
            builder.setIcon(i);
        }
        builder.setTitle(j).setMessage(s).setCancelable(false).setPositiveButton(k, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int l)
            {
                if(mOnDialogListener != null)
                {
                    mOnDialogListener.onClickButtonPositive();
                }
            }
        });
        return builder.create();
    }
}
