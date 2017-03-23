package com.mzdevelopment.freemusicplayer;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;

import com.mzdevelopment.freemusicplayer.dataMng.JsonParsingUtils;
import com.mzdevelopment.freemusicplayer.dataMng.TotalDataManager;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.mzdevelopment.freemusicplayer.R;
import com.ypyproductions.task.*;
import com.ypyproductions.utils.*;

import java.io.File;
import java.util.Locale;

public class SplashActivity extends DBFragmentActivity
{

    public static final String TAG = "SplashActivity";
    private boolean isLoading;
    private boolean isPressBack;
    protected boolean isShowingDialog;
    private boolean isStartAnimation;
    private DBTask mDBTask;
    private Handler mHandler;
    private ImageView mImgLogo;
    private ProgressBar mProgressBar;
    private TextView mTvAppName;
    private TextView mTvCopyright;
    private TextView mTvVersion;

    public SplashActivity()
    {
        mHandler = new Handler();
    }

    private void startAnimationLogo(final IDBCallback mCallback)
    {
        if(!isStartAnimation)
        {
            isStartAnimation = true;
            mProgressBar.setVisibility(4);
            mImgLogo.setRotationY(-180F);
            AccelerateDecelerateInterpolator acceleratedecelerateinterpolator = new AccelerateDecelerateInterpolator();
            ViewPropertyAnimator viewpropertyanimator = mImgLogo.animate().rotationY(0.0F).setDuration(1000L).setInterpolator(acceleratedecelerateinterpolator);
            viewpropertyanimator.setListener(new android.animation.Animator.AnimatorListener() {
                public void onAnimationCancel(Animator animator)
                {
                    if(mCallback != null)
                    {
                        mCallback.onAction();
                    }
                }

                public void onAnimationEnd(Animator animator)
                {
                    if(mCallback != null)
                    {
                        mCallback.onAction();
                    }
                }

                public void onAnimationRepeat(Animator animator)
                {
                }

                public void onAnimationStart(Animator animator)
                {
                }
            });
            viewpropertyanimator.start();
        } else
        if(mCallback != null)
        {
            mCallback.onAction();
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        getWindow().requestFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.splash);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
        mTvCopyright = (TextView)findViewById(R.id.tv_copyright);
        mTvVersion = (TextView)findViewById(R.id.tv_version);
        mTvAppName = (TextView)findViewById(R.id.tv_app_name);
        mImgLogo = (ImageView)findViewById(R.id.img_logo);
        mTvCopyright.setTypeface(mTypefaceNormal);
        mTvVersion.setTypeface(mTypefaceNormal);
        mTvAppName.setTypeface(mTypefaceNormal);
        mProgressBar.setVisibility(4);
        mTvAppName.setVisibility(4);
        TextView textview = mTvVersion;
        String s = getString(R.string.info_version_format);
        Object aobj[] = new Object[1];
        aobj[0] = ApplicationUtils.getVersionName(this);
        textview.setText(String.format(s, aobj));
        DBLog.setDebug(true);
        SettingManager.setOnline(this, true);
    }

    protected void onDestroy()
    {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void onDestroyData()
    {
        super.onDestroyData();
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(i == 4)
        {
            if(isPressBack)
            {
                finish();
            }
            return true;
        } else
        {
            return super.onKeyDown(i, keyevent);
        }
    }

    protected void onResume()
    {
        super.onResume();
        if(!isLoading)
        {
            isLoading = true;
            startAnimationLogo(new IDBCallback() {
                public void onAction()
                {
                    mProgressBar.setVisibility(0);
                    mTvAppName.setVisibility(0);
                    mHandler.postDelayed(new Runnable() {
                        public void run()
                        {
                            startLoadFavorite();
                        }
                    }, 2000L);
                }
            });
        }
    }

    public void startLoadFavorite()
    {
        final File mFile = IOUtils.getDiskCacheDir(this, "player");
        if(!mFile.exists())
        {
            mFile.mkdirs();
        }
        mDBTask = new DBTask(new IDBTaskListener() {
            public void onDoInBackground()
            {
                TotalDataManager.getInstance().readLibraryTrack(SplashActivity.this);
                String s = Locale.getDefault().getCountry().toLowerCase(Locale.US);
                String s1;
                String s2;
                java.util.ArrayList arraylist;
                if(s.equalsIgnoreCase("VN"))
                {
                    SettingManager.setLanguage(SplashActivity.this, "VN");
                    s1 = String.format("genres/genre_%1$s.dat", new Object[] {
                        s
                    });
                } else
                {
                    SettingManager.setLanguage(SplashActivity.this, "US");
                    s1 = String.format("genres/genre_%1$s.dat", new Object[] {
                        "en"
                    });
                }
                s2 = IOUtils.readStringFromAssets(SplashActivity.this, s1);
                DBLog.d(SplashActivity.TAG, (new StringBuilder()).append("=========>data=").append(s2).toString());
                arraylist = JsonParsingUtils.parsingGenreObject(s2);
                if(arraylist != null)
                {
                    TotalDataManager.getInstance().setListGenreObjects(arraylist);
                }
                TotalDataManager.getInstance().readSavedTrack(SplashActivity.this, mFile);
                TotalDataManager.getInstance().readPlaylistCached(SplashActivity.this, mFile);
            }

            public void onPostExcute()
            {
                mProgressBar.setVisibility(4);
                Intent intent = new Intent(SplashActivity.this, com.mzdevelopment.freemusicplayer.MainActivity.class);
                startActivity(intent);
                finish();
            }

            public void onPreExcute()
            {
            }
        });
        mDBTask.execute(new Void[0]);
    }
}
