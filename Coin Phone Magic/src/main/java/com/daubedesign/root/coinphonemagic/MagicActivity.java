package com.daubedesign.root.coinphonemagic;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.root.objectfromphone.R;

import static android.util.DisplayMetrics.DENSITY_280;
import static android.util.DisplayMetrics.DENSITY_360;
import static android.util.DisplayMetrics.DENSITY_400;
import static android.util.DisplayMetrics.DENSITY_420;
import static android.util.DisplayMetrics.DENSITY_560;
import static android.util.DisplayMetrics.DENSITY_DEFAULT;
import static android.util.DisplayMetrics.DENSITY_HIGH;
import static android.util.DisplayMetrics.DENSITY_LOW;
import static android.util.DisplayMetrics.DENSITY_MEDIUM;
import static android.util.DisplayMetrics.DENSITY_TV;
import static android.util.DisplayMetrics.DENSITY_XHIGH;
import static android.util.DisplayMetrics.DENSITY_XXHIGH;
import static android.util.DisplayMetrics.DENSITY_XXXHIGH;
import static java.lang.String.valueOf;

public class MagicActivity extends AppCompatActivity implements SensorEventListener {

    private final String TAG = "quarter";
    private ImageView objImageView;
    private RelativeLayout mainLayout;
    private int xDelta;
    private int yDelta;
    private int screenWidth;
    private int screenHeight;
    private int objWidth;
    private int objHeight;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (PreferenceManager.getDefaultSharedPreferences(this).getString("pref_darkness","entryValues").equals("Dark")) {
            setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic);
        Toolbar magicToolbar = (Toolbar) findViewById(R.id.magic_toolbar);
        setSupportActionBar(magicToolbar);
        getSupportActionBar().setTitle(R.string.magic_activity_title);
        mainLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        objImageView = (ImageView) findViewById(R.id.obj_image_view);
        mp = MediaPlayer.create(this, R.raw.coins);
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("US Half Dollar")){
            objImageView.setImageResource(R.drawable.us_half_dollar);
            objImageView.getLayoutParams().width = getPixelsFromDPs(193);
            objImageView.getLayoutParams().height = getPixelsFromDPs(193);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("US Quarter")){
            objImageView.setImageResource(R.drawable.quarter_new_heads_matte);
            objImageView.getLayoutParams().width = getPixelsFromDPs(153);
            objImageView.getLayoutParams().height = getPixelsFromDPs(153);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("US Penny")){
            objImageView.setImageResource(R.drawable.penny_tails_matte);
            objImageView.getLayoutParams().width = getPixelsFromDPs(120);
            objImageView.getLayoutParams().height = getPixelsFromDPs(120);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Paperclip")){
            objImageView.setImageResource(R.drawable.paperclip);
            objImageView.getLayoutParams().width = getPixelsFromDPs(170);
            objImageView.getLayoutParams().height = getPixelsFromDPs(170);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("US Dime")){
            objImageView.setImageResource(R.drawable.dime_tails);
            objImageView.getLayoutParams().width = getPixelsFromDPs(113);
            objImageView.getLayoutParams().height = getPixelsFromDPs(113);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Euro 1 Cent")){
            objImageView.setImageResource(R.drawable.euro_1_cent);
            objImageView.getLayoutParams().width = getPixelsFromDPs(103);
            objImageView.getLayoutParams().height = getPixelsFromDPs(103);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Euro 2 Cent")){
            objImageView.setImageResource(R.drawable.euro_2_cent);
            objImageView.getLayoutParams().width = getPixelsFromDPs(118);
            objImageView.getLayoutParams().height = getPixelsFromDPs(118);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Euro 5 Cent")){
            objImageView.setImageResource(R.drawable.euro_5_cent);
            objImageView.getLayoutParams().width = getPixelsFromDPs(140);
            objImageView.getLayoutParams().height = getPixelsFromDPs(140);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Euro 10 Cent")){
            objImageView.setImageResource(R.drawable.euro_10_cent);
            objImageView.getLayoutParams().width = getPixelsFromDPs(124);
            objImageView.getLayoutParams().height = getPixelsFromDPs(124);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Euro 20 Cent")){
            objImageView.setImageResource(R.drawable.euro_20_cent);
            objImageView.getLayoutParams().width = getPixelsFromDPs(140);
            objImageView.getLayoutParams().height = getPixelsFromDPs(140);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Euro 50 Cent")){
            objImageView.setImageResource(R.drawable.euro_50_cent);
            objImageView.getLayoutParams().width = getPixelsFromDPs(165);
            objImageView.getLayoutParams().height = getPixelsFromDPs(165);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Euro 1 Euro")){
            objImageView.setImageResource(R.drawable.euro_1_euro);
            objImageView.getLayoutParams().width = getPixelsFromDPs(160);
            objImageView.getLayoutParams().height = getPixelsFromDPs(160);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("Euro 2 Euro")){
            objImageView.setImageResource(R.drawable.euro_1_euro);
            objImageView.getLayoutParams().width = getPixelsFromDPs(168);
            objImageView.getLayoutParams().height = getPixelsFromDPs(168);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("IN 50 Paise")){
            objImageView.setImageResource(R.drawable.in_50_paise);
            objImageView.getLayoutParams().width = getPixelsFromDPs(120);
            objImageView.getLayoutParams().height = getPixelsFromDPs(120);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("IN 1 Rupee")){
            objImageView.setImageResource(R.drawable.in_1_rupee);
            objImageView.getLayoutParams().width = getPixelsFromDPs(139);
            objImageView.getLayoutParams().height = getPixelsFromDPs(139);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("IN 2 Rupee")){
            objImageView.setImageResource(R.drawable.in_2_rupee);
            objImageView.getLayoutParams().width = getPixelsFromDPs(157);
            objImageView.getLayoutParams().height = getPixelsFromDPs(157);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("IN 5 Rupee")){
            objImageView.setImageResource(R.drawable.in_5_rupee);
            objImageView.getLayoutParams().width = getPixelsFromDPs(145);
            objImageView.getLayoutParams().height = getPixelsFromDPs(145);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_objType","entryValues").equals("IN 10 Rupee")){
            objImageView.setImageResource(R.drawable.in_10_rupee);
            objImageView.getLayoutParams().width = getPixelsFromDPs(170);
            objImageView.getLayoutParams().height = getPixelsFromDPs(170);
        }

        if (PreferenceManager.getDefaultSharedPreferences(this).getString("pref_sound", "entryValues").equals("Coins")) {
            mp = MediaPlayer.create(this, R.raw.coins);
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("pref_sound", "entryValues").equals("Big Click")) {
            mp = MediaPlayer.create(this, R.raw.bigclick);
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("pref_sound", "entryValues").equals("Clack")) {
            mp = MediaPlayer.create(this, R.raw.clack);
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("pref_sound", "entryValues").equals("Click")) {
            mp = MediaPlayer.create(this, R.raw.click);
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("pref_sound", "entryValues").equals("Coin Drop")) {
            mp = MediaPlayer.create(this, R.raw.coin_drop);
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("pref_sound", "entryValues").equals("Tick")) {
            mp = MediaPlayer.create(this, R.raw.schtick);
        }
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                screenWidth = mainLayout.getMeasuredWidth();
                screenHeight = mainLayout.getMeasuredHeight();
                objWidth = objImageView.getMeasuredWidth();
                objHeight = objImageView.getMeasuredHeight();
                Log.d(TAG, Integer.toString(screenWidth)+"-"+Integer.toString(screenHeight));
                Log.d(TAG, Integer.toString(objWidth)+"-"+Integer.toString(objHeight));
                RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(objWidth, objHeight);
                centerParams.leftMargin = (screenWidth - objWidth) / 2;
                centerParams.topMargin = (screenHeight - objHeight) / 2;
                objImageView.setLayoutParams(centerParams);
            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        objImageView.setOnTouchListener(onTouchListener());
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu magic_menu) {
        getMenuInflater().inflate(R.menu.magic_menu, magic_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_instructions:
                Intent instructionsIntent = new Intent(this, InstructionsActivity.class);
                this.startActivity(instructionsIntent);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                this.startActivity(settingsIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    protected void onResume() {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this, mSensor);
        super.onPause();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {

        if (objImageView.getVisibility() == View.INVISIBLE) {
            final String SHAKE = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_shake","entryValues");
            final float XNOISE = Float.parseFloat(SHAKE);
            final float YNOISE = Float.parseFloat(SHAKE);
            final float ZNOISE = Float.parseFloat(SHAKE);
            final String delayString = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_delay","entryValues");
            int delayInt = Integer.parseInt(delayString);

            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            double mLastX = 0.0;
            double mLastY = 0.0;
            double mLastZ = 0.0;


            final double ALPHA = 0.8;

            double[] gravity = {0, 0, 0};

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
            gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
            gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

            // Remove the gravity contribution with the high-pass filter.
            x = event.values[0] - gravity[0];
            y = event.values[1] - gravity[1];
            z = event.values[2] - gravity[2];

            if (!mInitialized) {
                // sensor is used for the first time, initialize the last read values
                mLastX = x;
                mLastY = y;
                mLastZ = z;
                mInitialized = true;
            } else {
                // sensor is already initialized, and we have previously read values.
                // take difference of past and current values and decide which
                // axis acceleration was detected by comparing values

                double deltaX = Math.abs(mLastX - x);
                double deltaY = Math.abs(mLastY - y);
                double deltaZ = Math.abs(mLastZ - z);
                if (deltaX < XNOISE)
                    deltaX = (float) 0.0;
                if (deltaY < YNOISE)
                    deltaY = (float) 0.0;
                if (deltaZ < ZNOISE)
                    deltaZ = (float) 0.0;

                mLastX = x;
                mLastY = y;
                mLastZ = z;


                if ((deltaX > deltaY) || (deltaY > deltaX) || ((deltaZ > deltaX) && (deltaZ > deltaY))) {


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mp.start();
                            objImageView.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(objWidth, objHeight);
                            centerParams.leftMargin = (screenWidth - objWidth) / 2;
                            centerParams.topMargin = (screenHeight - objHeight) / 2;
                            objImageView.setLayoutParams(centerParams);
                        }
                    }, delayInt);

                }

            }
        }
    }
    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams dragParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();
                        xDelta = x - dragParams.leftMargin;
                        yDelta = y - dragParams.topMargin;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = screenWidth - (x- xDelta) - objWidth;
                        layoutParams.bottomMargin = screenHeight - (y - yDelta) - objHeight;
                        view.setLayoutParams(layoutParams);
                        if ((x < 50 || x > screenWidth - 50) || (y < 50 || y > screenHeight - 50)) {
                            objImageView.setVisibility(View.INVISIBLE);
                        } else {
                            objImageView.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return true;
            }
        };
    }

    public int getPixelsFromDPs(int dps) {
        int rP = 0;
        int rDpi = getResources().getDisplayMetrics().densityDpi;
        Log.d(TAG, "DPI " + Integer.toString(rDpi));
        if (rDpi >= 120 && rDpi < 130) { rP = (int) ((int) dps * .781); }
        else if (rDpi >= 130 && rDpi < 140) { rP = (int) ((int) dps * .844); }
        else if (rDpi >= 140 && rDpi < 150) { rP = (int) ((int) dps * .906); }
        else if (rDpi >= 150 && rDpi < 160) { rP = (int) ((int) dps * .969); }
        else if (rDpi >= 160 && rDpi < 170) { rP = (int) ((int) dps * 1.031); }
        else if (rDpi >= 170 && rDpi < 180) { rP = (int) ((int) dps * 1.094); }
        else if (rDpi >= 180 && rDpi < 190) { rP = (int) ((int) dps * 1.156); }
        else if (rDpi >= 190 && rDpi < 200) { rP = (int) ((int) dps * 1.219); }
        else if (rDpi >= 200 && rDpi < 210) { rP = (int) ((int) dps * 1.281); }
        else if (rDpi >= 210 && rDpi < 220) { rP = (int) ((int) dps * 1.344); }
        else if (rDpi >= 220 && rDpi < 230) { rP = (int) ((int) dps * 1.406); }
        else if (rDpi >= 230 && rDpi < 240) { rP = (int) ((int) dps * 1.469); }
        else if (rDpi >= 240 && rDpi < 250) { rP = (int) ((int) dps * 1.531); }
        else if (rDpi >= 250 && rDpi < 260) { rP = (int) ((int) dps * 1.594); }
        else if (rDpi >= 260 && rDpi < 270) { rP = (int) ((int) dps * 1.656); }
        else if (rDpi >= 270 && rDpi < 280) { rP = (int) ((int) dps * 1.712); }
        else if (rDpi >= 280 && rDpi < 290) { rP = (int) ((int) dps * 1.774); }
        else if (rDpi >= 290 && rDpi < 300) { rP = (int) ((int) dps * 1.836); }
        else if (rDpi >= 300 && rDpi < 310) { rP = (int) ((int) dps * 1.898); }
        else if (rDpi >= 310 && rDpi < 320) { rP = (int) ((int) dps * 1.96); }
        else if (rDpi >= 320 && rDpi < 330) { rP = (int) ((int) dps * 2.022); }
        else if (rDpi >= 330 && rDpi < 340) { rP = (int) ((int) dps * 2.084); }
        else if (rDpi >= 340 && rDpi < 350) { rP = (int) ((int) dps * 2.146); }
        else if (rDpi >= 350 && rDpi < 360) { rP = (int) ((int) dps * 2.208); }
        else if (rDpi >= 360 && rDpi < 370) { rP = (int) ((int) dps * 2.27); }
        else if (rDpi >= 370 && rDpi < 380) { rP = (int) ((int) dps * 2.332); }
        else if (rDpi >= 380 && rDpi < 390) { rP = (int) ((int) dps * 2.394); }
        else if (rDpi >= 390 && rDpi < 400) { rP = (int) ((int) dps * 2.456); }
        else if (rDpi >= 400 && rDpi < 410) { rP = (int) ((int) dps * 2.518); }
        else if (rDpi >= 410 && rDpi < 420) { rP = (int) ((int) dps * 2.58); }
        else if (rDpi >= 420 && rDpi < 430) { rP = (int) ((int) dps * 2.642); }
        else if (rDpi >= 430 && rDpi < 440) { rP = (int) ((int) dps * 2.704); }
        else if (rDpi >= 440 && rDpi < 450) { rP = (int) ((int) dps * 2.766); }
        else if (rDpi >= 450 && rDpi < 460) { rP = (int) ((int) dps * 2.828); }
        else if (rDpi >= 460 && rDpi < 470) { rP = (int) ((int) dps * 2.89); }
        else if (rDpi >= 470 && rDpi < 480) { rP = (int) ((int) dps * 2.952); }
        else if (rDpi >= 480 && rDpi < 490) { rP = (int) ((int) dps * 3.014); }
        else if (rDpi >= 490 && rDpi < 500) { rP = (int) ((int) dps * 3.076); }
        else if (rDpi >= 500 && rDpi < 510) { rP = (int) ((int) dps * 3.138); }
        else if (rDpi >= 510 && rDpi < 520) { rP = (int) ((int) dps * 3.2); }
        else if (rDpi >= 520 && rDpi < 530) { rP = (int) ((int) dps * 3.262); }
        else if (rDpi >= 530 && rDpi < 540) { rP = (int) ((int) dps * 3.344); }
        else if (rDpi >= 540 && rDpi < 550) { rP = (int) ((int) dps * 3.406); }
        else if (rDpi >= 550 && rDpi < 560) { rP = (int) ((int) dps * 3.468); }
        else if (rDpi >= 560 && rDpi < 570) { rP = (int) ((int) dps * 3.53); }
        else if (rDpi >= 570 && rDpi < 580) { rP = (int) ((int) dps * 3.592); }
        else if (rDpi >= 580 && rDpi < 590) { rP = (int) ((int) dps * 3.654); }
        else if (rDpi >= 590 && rDpi < 600) { rP = (int) ((int) dps * 3.716); }
        else if (rDpi >= 600 && rDpi < 610) { rP = (int) ((int) dps * 3.778); }
        else if (rDpi >= 610 && rDpi < 620) { rP = (int) ((int) dps * 3.84); }
        else if (rDpi >= 620 && rDpi < 630) { rP = (int) ((int) dps * 3.902); }
        else if (rDpi >= 630 && rDpi < 640) { rP = (int) ((int) dps * 3.968); }
        else if (rDpi >= 640 && rDpi < 650) { rP = (int) ((int) dps * 4.03); }
        else if (rDpi >= 650 && rDpi < 660) { rP = (int) ((int) dps * 4.092); }
        else if (rDpi >= 660 && rDpi < 670) { rP = (int) ((int) dps * 4.154); }
        else if (rDpi >= 670 && rDpi < 680) { rP = (int) ((int) dps * 4.216); }
        else if (rDpi >= 680 && rDpi < 690) { rP = (int) ((int) dps * 4.278); }
        else if (rDpi >= 690 && rDpi < 700) { rP = (int) ((int) dps * 4.34); }
        else if (rDpi >= 700) { rP = (int) ((int) dps * 4.5); }
        return rP;
        }


    }


