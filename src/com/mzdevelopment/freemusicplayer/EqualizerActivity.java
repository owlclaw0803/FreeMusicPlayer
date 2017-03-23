package com.mzdevelopment.freemusicplayer;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import com.mzdevelopment.freemusicplayer.adapter.PresetAdapter;
import com.mzdevelopment.freemusicplayer.setting.SettingManager;
import com.mzdevelopment.freemusicplayer.soundclound.SoundCloundDataMng;
import com.mzdevelopment.freemusicplayer.R;
import com.ypyproductions.utils.DBLog;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

public class EqualizerActivity extends DBFragmentActivity
{

    public static final String TAG = "EqualizerActivity";
    private short bands;
    private boolean isCreateLocal;
    private ArrayList listSeekBars;
    private Equalizer mEqualizer;
    private String mEqualizerParams[];
    private LinearLayout mLayoutBands;
    private String mLists[];
    private MediaPlayer mMediaPlayer;
    private Spinner mSpinnerPresents;
    private Switch mSwitchBtn;
    private short maxEQLevel;
    private short minEQLevel;

    public EqualizerActivity()
    {
        listSeekBars = new ArrayList();
    }

    private void saveEqualizerParams()
    {
        if(mEqualizer != null && bands > 0)
        {
            String s = "";
            for(short word0 = 0; word0 < bands; word0++)
            {
                if(word0 < -1 + bands)
                {
                    s = (new StringBuilder()).append(s).append(mEqualizer.getBandLevel(word0)).append(":").toString();
                }
            }

            DBLog.d(TAG, (new StringBuilder()).append("================>dataSave=").append(s).toString());
            SettingManager.setEqualizerPreset(this, String.valueOf(-1 + mLists.length));
            SettingManager.setEqualizerParams(this, s);
        }
    }

    private void setUpEqualizerCustom()
    {
        if(mEqualizer != null)
        {
            String s = SettingManager.getEqualizerParams(this);
            if(!StringUtils.isEmptyString(s))
            {
                mEqualizerParams = s.split(":");
                if(mEqualizerParams != null && mEqualizerParams.length > 0)
                {
                    int i = mEqualizerParams.length;
                    for(int j = 0; j < i; j++)
                    {
                        mEqualizer.setBandLevel((short)j, Short.parseShort(mEqualizerParams[j]));
                        ((SeekBar)listSeekBars.get(j)).setProgress(Short.parseShort(mEqualizerParams[j]) - minEQLevel);
                    }

                    mSpinnerPresents.setSelection(-1 + mLists.length);
                    SettingManager.setEqualizerPreset(this, String.valueOf(-1 + mLists.length));
                }
            }
        }
    }

    private void setUpParams()
    {
label0:
        {
            if(mEqualizer != null)
            {
                String s = SettingManager.getEqualizerPreset(this);
                if(StringUtils.isEmptyString(s) || !StringUtils.isNumber(s))
                {
                    break label0;
                }
                short word0 = Short.parseShort(s);
                short word1 = mEqualizer.getNumberOfPresets();
                if(word1 <= 0 || word0 >= word1 - 1 || word0 < 0)
                {
                    break label0;
                }
                mEqualizer.usePreset(word0);
                mSpinnerPresents.setSelection(word0);
            }
            return;
        }
        setUpEqualizerCustom();
    }

    private void setUpPresetName()
    {
        if(mEqualizer != null)
        {
            short word0 = mEqualizer.getNumberOfPresets();
            if(word0 > 0)
            {
                mLists = new String[word0 + 1];
                for(short word1 = 0; word1 < word0; word1++)
                {
                    mLists[word1] = mEqualizer.getPresetName(word1);
                }

                mLists[word0] = getString(R.string.title_custom);
                PresetAdapter presetadapter = new PresetAdapter(this, R.layout.item_preset_name, mLists, mTypefaceNormal);
                presetadapter.setDropDownViewResource(0x1090009);
                mSpinnerPresents.setAdapter(presetadapter);
                mSpinnerPresents.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView adapterview, View view, int i, long l)
                    {
                        SettingManager.setEqualizerPreset(EqualizerActivity.this, String.valueOf(i));
                        short word2;
                        if(i < -1 + mLists.length)
                        {
                            mEqualizer.usePreset((short)i);
                        } else
                        {
                            setUpEqualizerCustom();
                        }
                        for(word2 = 0; word2 < bands; word2++)
                        {
                            ((SeekBar)listSeekBars.get(word2)).setProgress(mEqualizer.getBandLevel(word2) - minEQLevel);
                        }

                    }

                    public void onNothingSelected(AdapterView adapterview)
                    {
                    }
                });
                return;
            } else
            {
                mSpinnerPresents.setVisibility(4);
                return;
            }
        } else
        {
            mSpinnerPresents.setVisibility(4);
            return;
        }
    }

    private void setupEqualizerFxAndUI()
    {
        mEqualizer = SoundCloundDataMng.getInstance().getEqualizer();
        if(mEqualizer == null)
        {
            mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
            mEqualizer.setEnabled(SettingManager.getEqualizer(this));
        }
        bands = mEqualizer.getNumberOfBands();
        short aword0[];
        if(bands != 0){
        	if((aword0 = mEqualizer.getBandLevelRange()) != null && aword0.length >= 2)
            {
                minEQLevel = aword0[0];
                maxEQLevel = aword0[1];
                short word0 = 0;
                while(word0 < bands) 
                {
                    final int band = word0;
                    TextView textview = new TextView(this);
                    textview.setLayoutParams(new android.view.ViewGroup.LayoutParams(-1, -2));
                    textview.setGravity(1);
                    textview.setText((new StringBuilder()).append(mEqualizer.getCenterFreq((short) band) / 1000).append(" Hz").toString());
                    textview.setTextColor(getResources().getColor(R.color.black));
                    textview.setTextSize(2, 16F);
                    mLayoutBands.addView(textview);
                    LinearLayout linearlayout = new LinearLayout(this);
                    linearlayout.setOrientation(0);
                    TextView textview1 = new TextView(this);
                    textview1.setLayoutParams(new android.view.ViewGroup.LayoutParams(-2, -2));
                    textview1.setText((new StringBuilder()).append(minEQLevel / 100).append(" dB").toString());
                    textview1.setTextColor(getResources().getColor(R.color.black));
                    textview1.setTextSize(2, 16F);
                    TextView textview2 = new TextView(this);
                    textview2.setLayoutParams(new android.view.ViewGroup.LayoutParams(-2, -2));
                    textview2.setText((new StringBuilder()).append(maxEQLevel / 100).append(" dB").toString());
                    textview2.setTextColor(getResources().getColor(R.color.black));
                    textview2.setTextSize(2, 16F);
                    android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.LinearLayout.LayoutParams(-1, -2);
                    layoutparams.weight = 1.0F;
                    SeekBar seekbar = new SeekBar(this);
                    seekbar.setLayoutParams(layoutparams);
                    seekbar.setMax(maxEQLevel - minEQLevel);
                    seekbar.setProgress(mEqualizer.getBandLevel((short) band) - minEQLevel);
                    seekbar.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
                        public void onProgressChanged(SeekBar seekbar1, int i, boolean flag)
                        {
                            if(flag)
                            {
                                mEqualizer.setBandLevel((short) band, (short)(i + minEQLevel));
                                saveEqualizerParams();
                                mSpinnerPresents.setSelection(-1 + mLists.length);
                            }
                        }

                        public void onStartTrackingTouch(SeekBar seekbar1)
                        {
                        }

                        public void onStopTrackingTouch(SeekBar seekbar1)
                        {
                        }
                    });
                    listSeekBars.add(seekbar);
                    linearlayout.addView(textview1);
                    linearlayout.addView(seekbar);
                    linearlayout.addView(textview2);
                    mLayoutBands.addView(linearlayout);
                    word0++;
                }
            }
        }
    }

    private void startCheckEqualizer()
    {
        boolean flag = SettingManager.getEqualizer(this);
        mSpinnerPresents.setEnabled(flag);
        if(mEqualizer != null)
        {
            mEqualizer.setEnabled(flag);
        }
        if(listSeekBars.size() > 0)
        {
            for(int i = 0; i < listSeekBars.size(); i++)
            {
                ((SeekBar)listSeekBars.get(i)).setEnabled(flag);
            }

        }
        mSwitchBtn.setChecked(flag);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_equalizer);
        setTitle(R.string.title_equalizer);
        mLayoutBands = (LinearLayout)findViewById(R.id.layout_bands);
        mSpinnerPresents = (Spinner)findViewById(R.id.list_preset);
        mSwitchBtn = (Switch)findViewById(R.id.switch1);
        mSwitchBtn.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                boolean flag = mSwitchBtn.isChecked();
                SettingManager.setEqualizer(EqualizerActivity.this, flag);
                startCheckEqualizer();
            }
        });
        mMediaPlayer = SoundCloundDataMng.getInstance().getPlayer();
        if(mMediaPlayer == null || !mMediaPlayer.isPlaying())
        {
            isCreateLocal = true;
            mMediaPlayer = new MediaPlayer();
        }
        setupEqualizerFxAndUI();
        setUpPresetName();
        startCheckEqualizer();
        setUpParams();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(listSeekBars != null)
        {
            listSeekBars.clear();
            listSeekBars = null;
        }
        if(isCreateLocal && mMediaPlayer != null)
        {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(i == 4)
        {
            finish();
            return true;
        } else
        {
            return super.onKeyDown(i, keyevent);
        }
    }











}
