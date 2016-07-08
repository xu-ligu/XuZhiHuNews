package com.example.administrator.myzzhihuday.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myzzhihuday.R;
import com.example.administrator.myzzhihuday.util.Constant;
import com.example.administrator.myzzhihuday.view.RevealBackgroundView;

/**
 * Created by Administrator on 2016/7/3.
 */
public class textActivity extends AppCompatActivity implements RevealBackgroundView.OnStateChangeListener{

    private RevealBackgroundView mRevealBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        mRevealBackground=(RevealBackgroundView)findViewById(R.id.revealBackgroundView);

    }

    public void setUpRevealBackground(Bundle savedInstanceState){
        mRevealBackground.setOnStateChangeListener(this);
        if(savedInstanceState==null){
            final int[] startingLocation=getIntent().getIntArrayExtra(Constant.START_LOCATION);
            mRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    mRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        }else{
            mRevealBackground.setToFinishedFrame();
        }
    }

    @Override
    public void onStateChange(int state) {
        if(RevealBackgroundView.STATE_FINISHED==state){
            /*mAppBarLayout.setVisibility(View.VISIBLE);
            setStatusBarColor(Color.TRANSPARENT);*/

        }
    }
}
