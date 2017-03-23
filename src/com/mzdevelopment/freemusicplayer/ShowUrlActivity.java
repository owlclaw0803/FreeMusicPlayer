package com.mzdevelopment.freemusicplayer;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.*;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mzdevelopment.freemusicplayer.constants.ICloudMusicPlayerConstants;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mzdevelopment.freemusicplayer.R;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.utils.*;

public class ShowUrlActivity extends DBFragmentActivity
    implements ICloudMusicPlayerConstants, IDBFragmentConstants
{

    public static final String TAG = "ShowUrlActivity";
    private AdView adView;
    private String mNameHeader;
    private ProgressBar mProgressBar;
    private String mUrl;
    private WebView mWebViewShowPage;

    public ShowUrlActivity()
    {
    }

    private void backToHome()
    {
        if(SettingManager.getOnline(this))
        {
            finish();
            return;
        } else
        {
            startActivity(new Intent(this, com.mzdevelopment.freemusicplayer.SplashActivity.class));
            finish();
            return;
        }
    }

    private void setUpLayoutAdmob()
    {
        RelativeLayout relativelayout = (RelativeLayout)findViewById(R.id.layout_ad);
        if(ApplicationUtils.isOnline(this))
        {
            adView = new AdView(this);
            adView.setAdUnitId(ICloudMusicPlayerConstants.ADMOB_ID_BANNER);
            adView.setAdSize(AdSize.BANNER);
            relativelayout.addView(adView);
            com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice("0B8C2CBE428E35B4864AA5EFB41DB402").build();
            adView.loadAd(adrequest);
            return;
        } else {
            relativelayout.setVisibility(8);
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.layout_show_url);
        Intent intent = getIntent();
        if(intent != null)
        {
            mUrl = intent.getStringExtra("url");
            mNameHeader = intent.getStringExtra("KEY_HEADER");
            DBLog.d(TAG, (new StringBuilder()).append("===========>url=").append(mUrl).toString());
        }
        if(!StringUtils.isEmptyString(mNameHeader))
        {
            setTitle(mNameHeader);
        }
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
        mProgressBar.setVisibility(0);
        mWebViewShowPage = (WebView)findViewById(R.id.webview);
        mWebViewShowPage.getSettings().setJavaScriptEnabled(true);
        mWebViewShowPage.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webview, String s)
            {
                super.onPageFinished(webview, s);
                mProgressBar.setVisibility(8);
            }
        });
        mWebViewShowPage.loadUrl(mUrl);
        setUpLayoutAdmob();
    }

    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(mWebViewShowPage != null)
        {
            mWebViewShowPage.destroy();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(i == 4)
        {
            if(mWebViewShowPage.canGoBack())
            {
                mWebViewShowPage.goBack();
            } else
            {
                backToHome();
            }
            return true;
        } else
        {
            return super.onKeyDown(i, keyevent);
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId()){
        default:
        	break;
        case 16908332:
        	backToHome();
        	break;
        }
        return super.onOptionsItemSelected(menuitem);
    }
}
