package com.example.administrator.myzzhihuday.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.myzzhihuday.R;
import com.example.administrator.myzzhihuday.db.WebCacheDatabase;
import com.example.administrator.myzzhihuday.model.Content;
import com.example.administrator.myzzhihuday.model.StoriesEntity;
import com.example.administrator.myzzhihuday.util.Constant;
import com.example.administrator.myzzhihuday.util.HttpUtils;
import com.example.administrator.myzzhihuday.view.RevealBackgroundView;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import cz.msebera.android.httpclient.Header;



public class NewsContentActivity extends AppCompatActivity implements RevealBackgroundView.OnStateChangeListener{
    private ImageLoader mImageLoader;
    private StoriesEntity entity;
    private WebView webView;
    private ImageView iv;
    private Toolbar toolbar;
    private RevealBackgroundView mRevealBackground;
    private Content content;
    private CoordinatorLayout mcoordinatorLayout;
    private WebCacheDatabase dbHelper;
    private boolean isLighnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content_layout);
        isLighnt=getIntent().getBooleanExtra("isLight",true);
        dbHelper=new WebCacheDatabase(this,1);
        entity=(StoriesEntity)getIntent().getSerializableExtra("entity");
        mRevealBackground=(RevealBackgroundView)findViewById(R.id.revealBackgroundView);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(entity.getTitle());
        toolbar.setBackgroundColor(getResources().getColor(isLighnt?R.color.light_toolbar:R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mcoordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        mcoordinatorLayout.setVisibility(View.VISIBLE);
        webView=(WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        if(HttpUtils.isNetWorkConnected(this)){
            HttpUtils.get(Constant.CONTENT + entity.getId(), new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
                    responseString=responseString.replace("'","''");
                    db.execSQL("replace into WebCache(newsId,json) values("+entity.getId()+",'"+responseString+"')");
                    db.close();
                    parseJson(responseString);

                }
            });

        }else{
            SQLiteDatabase db=dbHelper.getReadableDatabase();
            Cursor cursor=db.rawQuery("select * from WebCache where newsId="+entity.getId(),null);
            if(cursor.moveToFirst()){
                String response=cursor.getString(cursor.getColumnIndex("json"));
                parseJson(response);
            }else{
                Toast.makeText(NewsContentActivity.this, "您没有连接上网络", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            db.close();
        }
        setUpRevealBackground(savedInstanceState);

    }
    public void parseJson(String responseString){
        Gson gson = new Gson();
        content = gson.fromJson(responseString, Content.class);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        Log.d("23", "<body)"+content.getBody());
        Log.d("23", "html"+html);

        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
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

            mcoordinatorLayout.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_to_left);
    }




}
