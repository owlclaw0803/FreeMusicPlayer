package com.mzdevelopment.freemusicplayer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.ypyproductions.dialog.utils.AlertDialogUtils;

public class DBAlertFragment extends DialogFragment
{

    public static final String KEY_ICON = "icon";
    public static final String KEY_ID_DIALOG = "id";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_NEGATIVE = "negative";
    public static final String KEY_POSITIVE = "positive";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TYPE = "type";
    public static final String TAG = "DBAlertFragment";
    public static final int TYPE_DIALOG_FULL = 1;
    public static final int TYPE_DIALOG_INFO = 2;

    public DBAlertFragment()
    {
    }

    public static DBAlertFragment newInstance(int i, int j, int k, int l, int i1)
    {
        DBAlertFragment dbalertfragment = new DBAlertFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putInt("id", i);
        bundle.putInt("title", k);
        bundle.putInt("message", i1);
        bundle.putInt("icon", j);
        bundle.putInt("positive", l);
        dbalertfragment.setArguments(bundle);
        return dbalertfragment;
    }

    public static DBAlertFragment newInstance(int i, int j, int k, int l, int i1, int j1)
    {
        DBAlertFragment dbalertfragment = new DBAlertFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putInt("id", i);
        bundle.putInt("title", k);
        bundle.putInt("message", j1);
        bundle.putInt("icon", j);
        bundle.putInt("positive", l);
        bundle.putInt("negative", i1);
        dbalertfragment.setArguments(bundle);
        return dbalertfragment;
    }

    public Dialog onCreateDialog(Bundle bundle)
    {
        Bundle bundle1 = getArguments();
        int i = bundle1.getInt("type");
        int j = bundle1.getInt("title");
        int k = bundle1.getInt("message");
        int l = bundle1.getInt("icon");
        int i1 = bundle1.getInt("positive");
        final int idDialog = bundle1.getInt("id");
        final DBFragmentActivity mContext = (DBFragmentActivity)getActivity();
        switch(i)
        {
        default:
            return super.onCreateDialog(bundle);

        case 2: // '\002'
            return AlertDialogUtils.createInfoDialog(mContext, l, j, i1, k, new com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener() {
                public void onClickButtonNegative()
                {
                    mContext.doNegativeClick(idDialog);
                }

                public void onClickButtonPositive()
                {
                    mContext.doPositiveClick(idDialog);
                }
            });

        case 1: // '\001'
            return AlertDialogUtils.createFullDialog(mContext, l, j, i1, bundle1.getInt("negative"), k, new com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener() {
                public void onClickButtonNegative()
                {
                    mContext.doNegativeClick(idDialog);
                }

                public void onClickButtonPositive()
                {
                    mContext.doPositiveClick(idDialog);
                }
            });
        }
    }
}
