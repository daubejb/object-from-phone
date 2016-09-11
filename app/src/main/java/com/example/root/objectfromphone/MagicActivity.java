package com.example.root.objectfromphone;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MagicActivity extends AppCompatActivity implements SensorEventListener {

    private final String TAG = "quarter";
    private ImageView quarterImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        mainLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        quarterImageView = (ImageView) findViewById(R.id.quarter_image_view);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                screenWidth = mainLayout.getMeasuredWidth();
                screenHeight = mainLayout.getMeasuredHeight();
                objWidth = quarterImageView.getMeasuredWidth();
                objHeight = quarterImageView.getMeasuredHeight();
                Log.d(TAG, Integer.toString(screenWidth)+"-"+Integer.toString(screenHeight));
                Log.d(TAG, Integer.toString(objWidth)+"-"+Integer.toString(objHeight));
                RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(objWidth, objHeight);
                centerParams.leftMargin = (screenWidth - objWidth) / 2;
                centerParams.topMargin = (screenHeight - objHeight) / 2;
                quarterImageView.setLayoutParams(centerParams);
            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        quarterImageView.setOnTouchListener(onTouchListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_instructions:
                // User chose the "Instructions" item, show the instructions UI...
                return true;
            case R.id.action_demonstration:
                // User chose the "Demonstration" item, take the user to youtube...
                return true;
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
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

        if (quarterImageView.getVisibility() == View.INVISIBLE) {

            final float XNOISE = (float) 16.0;
            final float YNOISE = (float) 16.0;
            final float ZNOISE = (float) 16.0;
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            double mLastX = 0.0;
            double mLastY = 0.0;
            double mLastZ = 0.0;
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.metal);

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
                    mp.start();
                    quarterImageView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(objWidth, objHeight);
                    centerParams.leftMargin = (screenWidth - objWidth) / 2;
                    centerParams.topMargin = (screenHeight - objHeight) / 2;
                    quarterImageView.setLayoutParams(centerParams);
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
                            quarterImageView.setVisibility(View.INVISIBLE);
                        } else {
                            quarterImageView.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return true;
            }
        };
    }
}
