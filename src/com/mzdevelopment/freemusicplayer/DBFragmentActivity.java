package com.mzdevelopment.freemusicplayer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.R;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.dialog.utils.AlertDialogUtils;
import com.ypyproductions.dialog.utils.IDialogFragmentListener;
import com.ypyproductions.task.IDBCallback;
import com.ypyproductions.task.IDBConstantURL;
import com.ypyproductions.utils.ResolutionUtils;
import com.ypyproductions.utils.ShareActionUtils;

import java.util.ArrayList;
import java.util.Random;

public class DBFragmentActivity extends FragmentActivity
    implements IDBConstantURL, IDialogFragmentListener, ICloudMusicPlayerConstants
{

    public static final String TAG = "DBFragmentActivity";
    public ArrayList mListFragments;
    private ProgressDialog mProgressDialog;
    private Random mRando;
    public Typeface mTypefaceBold;
    public Typeface mTypefaceLight;
    public Typeface mTypefaceLogo;
    public Typeface mTypefaceNormal;
    private int screenHeight;
    private int screenWidth;

    public DBFragmentActivity()
    {
    }

    private void createProgressDialog()
    {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialoginterface, int i, KeyEvent keyevent)
            {
                return i == 4;
            }
        });
    }

    public void addFragment(Fragment fragment)
    {
        if(fragment != null && mListFragments != null)
        {
            synchronized(mListFragments)
            {
                mListFragments.add(fragment);
            }
        }
    }

    public boolean backStack(IDBCallback idbcallback)
    {
        int i;
        if(mListFragments == null || mListFragments.size() <= 0)
            return false;
        i = mListFragments.size();
        if(i <= 0)
        	return false;
        ArrayList arraylist = mListFragments;
        synchronized(arraylist){
        	Fragment fragment = (Fragment)mListFragments.remove(i - 1);
            if(fragment == null)
            {
                return false;
            }
            if(!(fragment instanceof DBFragment))
            {
            	return false;
            }
            ((DBFragment)fragment).backToHome(this);
        }
        return true;
    }

    public void createArrayFragment()
    {
        mListFragments = new ArrayList();
    }

    public DialogFragment createWarningDialog(int i, int j, int k)
    {
        return DBAlertFragment.newInstance(i, 0x1080027, j, 0x104000a, k);
    }

    public void deleteSongFromMediaStore(long l)
    {
        try
        {
            android.net.Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, l);
            getContentResolver().delete(uri, null, null);
            return;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void dimissProgressDialog()
    {
        if(mProgressDialog != null)
        {
            mProgressDialog.dismiss();
        }
    }

    public void doNegativeClick(int i)
    {
    }

    public void doPositiveClick(int i)
    {
        switch(i)
        {
        default:
            return;

        case 8: // '\b'
            onDestroyData();
            break;
        }
        finish();
    }

    public int getScreenHeight()
    {
        return screenHeight;
    }

    public int getScreenWidth()
    {
        return screenWidth;
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        getWindow().setFormat(1);
        getWindow().setSoftInputMode(3);
        createProgressDialog();
        mTypefaceNormal = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        mTypefaceLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        mTypefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        mTypefaceLogo = Typeface.createFromAsset(getAssets(), "fonts/Biko_Regular.otf");
        int ai[] = ResolutionUtils.getDeviceResolution(this);
        if(ai != null && ai.length == 2)
        {
            screenWidth = ai[0];
            screenHeight = ai[1];
        }
        mRando = new Random();
    }

    public void onDestroyData()
    {
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(i == 4)
        {
            showQuitDialog();
            return true;
        } else
        {
            return super.onKeyDown(i, keyevent);
        }
    }

    public void showDialogFragment(int i)
    {
        android.support.v4.app.FragmentManager fragmentmanager = getSupportFragmentManager();
        switch(i)
        {
        default:
            return;

        case 1: // '\001'
            createWarningDialog(1, R.string.title_warning, R.string.info_lose_internet).show(fragmentmanager, "DIALOG_LOSE_CONNECTION");
            return;

        case 2: // '\002'
            createWarningDialog(2, R.string.title_warning, R.string.info_empty).show(fragmentmanager, "DIALOG_EMPTY");
            return;

        case 19: // '\023'
            createWarningDialog(19, R.string.title_warning, R.string.info_server_error).show(fragmentmanager, "DIALOG_SEVER_ERROR");
            break;
        }
    }

    public void showDialogTurnOnInternetConnection(final IDBCallback mCallback)
    {
        AlertDialogUtils.createFullDialog(this, 0, R.string.title_warning, R.string.title_settings, R.string.title_cancel, R.string.info_lose_internet, new com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener() {
            public void onClickButtonNegative()
            {
            }

            public void onClickButtonPositive()
            {
                startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                if(mCallback != null)
                {
                    mCallback.onAction();
                }
            }
        }).show();
    }

    public void showFullDialog(int i, int j, int k, int l, final IDBCallback mDBCallback)
    {
        AlertDialogUtils.createFullDialog(this, -1, i, k, l, j, new com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener() {
            public void onClickButtonNegative()
            {
            }

            public void onClickButtonPositive()
            {
                if(mDBCallback != null)
                {
                    mDBCallback.onAction();
                }
            }
        }).show();
    }

    public void showFullDialog(int i, String s, int j, int k, final IDBCallback mDBCallback)
    {
        AlertDialogUtils.createFullDialog(this, -1, i, j, k, s, new com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener() {
            public void onClickButtonNegative()
            {
            }

            public void onClickButtonPositive()
            {
                if(mDBCallback != null)
                {
                    mDBCallback.onAction();
                }
            }
        }).show();
    }

    public void showInfoDialog(int i, int j, final IDBCallback mDBCallback)
    {
        AlertDialogUtils.createInfoDialog(this, 0, i, R.string.title_ok, j, new com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener() {
            public void onClickButtonNegative()
            {
                if(mDBCallback != null)
                {
                    mDBCallback.onAction();
                }
            }

            public void onClickButtonPositive()
            {
                if(mDBCallback != null)
                {
                    mDBCallback.onAction();
                }
            }
        }).show();
    }

    public void showInfoDialog(int i, String s)
    {
        AlertDialogUtils.createInfoDialog(this, 0, i, R.string.title_ok, s, null).show();
    }

    public void showInfoDialog(int i, String s, final IDBCallback mDBCallback)
    {
        AlertDialogUtils.createInfoDialog(this, 0, i, R.string.title_ok, s, new com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener() {
            public void onClickButtonNegative()
            {
                if(mDBCallback != null)
                {
                    mDBCallback.onAction();
                }
            }

            public void onClickButtonPositive()
            {
                if(mDBCallback != null)
                {
                    mDBCallback.onAction();
                }
            }
        }).show();
    }

    public void showIntertestialAds()
    {
    }

    public void showProgressDialog()
    {
        if(mProgressDialog != null)
        {
            mProgressDialog.setMessage(getString(R.string.loading));
            if(!mProgressDialog.isShowing())
            {
                mProgressDialog.show();
            }
        }
    }

    public void showProgressDialog(int i)
    {
        if(mProgressDialog != null)
        {
            mProgressDialog.setMessage(getString(i));
            if(!mProgressDialog.isShowing())
            {
                mProgressDialog.show();
            }
        }
    }

    public void showProgressDialog(String s)
    {
        if(mProgressDialog != null)
        {
            mProgressDialog.setMessage(s);
            if(!mProgressDialog.isShowing())
            {
                mProgressDialog.show();
            }
        }
    }

    public void showQuitDialog()
    {
        final int index = mRando.nextInt(5);
        int i = R.string.title_rate_us;
        if(index % 2 != 0)
        {
            i = R.string.title_more_apps;
        }
        Dialog dialog = AlertDialogUtils.createFullDialog(this, R.drawable.ic_launcher, R.string.title_confirm, R.string.title_yes, i, R.string.info_close_app, new com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener() {
            public void onClickButtonNegative()
            {
                if(index % 2 != 0)
                {
                    ShareActionUtils.goToUrl(DBFragmentActivity.this, "https://play.google.com/store/apps/details?id=kawkaw.callrecorder");
                    return;
                } else
                {
                    Object aobj[] = new Object[1];
                    aobj[0] = getPackageName();
                    String s = String.format("https://play.google.com/store/apps/details?id=%1$s", aobj);
                    ShareActionUtils.goToUrl(DBFragmentActivity.this, s);
                    return;
                }
            }

            public void onClickButtonPositive()
            {
                onDestroyData();
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialoginterface, int j, KeyEvent keyevent)
            {
                return false;
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void showToast(int i)
    {
        showToast(getString(i));
    }

    public void showToast(String s)
    {
        Toast toast = Toast.makeText(this, s, 0);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public void showToastWithLongTime(int i)
    {
        showToastWithLongTime(getString(i));
    }

    public void showToastWithLongTime(String s)
    {
        Toast toast = Toast.makeText(this, s, 1);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public void showWarningDialog(int i, int j)
    {
        AlertDialogUtils.createInfoDialog(this, 0, i, R.string.title_ok, j, null).show();
    }

    public void showWarningDialog(int i, String s)
    {
        AlertDialogUtils.createInfoDialog(this, 0, i, R.string.title_ok, s, null).show();
    }

}
