package com.example.root.objectfromphone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MagicActivity extends AppCompatActivity {

    private final String TAG = "quarter";
    private ImageView quarterImageView;
    private RelativeLayout mainLayout;
    private int xDelta;
    private int yDelta;
    private int screenWidth;
    private int screenHeight;
    private int objWidth;
    private int objHeight;


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
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(objWidth, objHeight);
                params.leftMargin = (screenWidth - objWidth) / 2;
                params.topMargin = (screenHeight - objHeight) / 2;
                quarterImageView.setLayoutParams(params);
            }
        });

        quarterImageView.setOnTouchListener(onTouchListener());



    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();
                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = screenWidth - (x- xDelta) - quarterImageView.getWidth();
                        layoutParams.bottomMargin = screenHeight - (y - yDelta) - quarterImageView.getHeight();
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
