package com.example.root.objectfromphone;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MagicActivity extends AppCompatActivity {

    private final String TAG = "quarter";
    private ImageView quarterImageView;
    private RelativeLayout mainLayout;
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


    }
}
