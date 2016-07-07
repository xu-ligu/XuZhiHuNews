package com.example.administrator.myzzhihuday.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myzzhihuday.R;
import com.example.administrator.myzzhihuday.activity.MainActivity;
import com.example.administrator.myzzhihuday.adapter.MainNewsItemAdapter;
import com.example.administrator.myzzhihuday.model.Before;
import com.example.administrator.myzzhihuday.model.Latest;
import com.example.administrator.myzzhihuday.model.StoriesEntity;
import com.example.administrator.myzzhihuday.util.Constant;
import com.example.administrator.myzzhihuday.util.HttpUtils;
import com.example.administrator.myzzhihuday.util.PreUtils;
import com.example.administrator.myzzhihuday.view.Kanner;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import  com.example.administrator.myzzhihuday.activity.latestContentActivity;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainFragment extends BaseFragment {
    private  ListView lv_news;



    private Kanner kanner;
    private Latest latest;
    private MainNewsItemAdapter mAdapter;
    private Handler handler=new Handler();
    private boolean isLoading=false;
    private Before before;
    private String date;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view =inflater.inflate(R.layout.main_news_layout,container,false);

        lv_news=(ListView)view.findViewById(R.id.lv_news);
        View header=inflater.inflate(R.layout.kanner,lv_news,false);

        kanner=(Kanner)header.findViewById(R.id.kanner);
        kanner.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void click(View v, Latest.TopStoriesEntity entity) {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                StoriesEntity storiesEntity = new StoriesEntity();
                storiesEntity.setId(entity.getId());
                storiesEntity.setTitle(entity.getTitle());
                Intent intent = new Intent(mActivity, latestContentActivity.class);
                intent.putExtra(Constant.START_LOCATION, startingLocation);
                intent.putExtra("entity", storiesEntity);
               intent.putExtra("isLight", ((MainActivity) mActivity).isLight());
                startActivity(intent);
                mActivity.overridePendingTransition(0, 0);

            }
        });
        lv_news.addHeaderView(header);
        mAdapter=new MainNewsItemAdapter(mActivity);
        lv_news.setAdapter(mAdapter);
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (lv_news != null && lv_news.getChildCount() > 0) {
                    boolean enable = (i == 0) && (absListView.getChildAt(i).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                    if (i + i1 == i2 && !isLoading) {

                        loadMore(Constant.BEFORE + date);
                    }
                }

            }
        });
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int[] startingLocation=new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0]+=view.getWidth()/2;
                StoriesEntity entity=(StoriesEntity)parent.getAdapter().getItem(position);
                Intent intent=new Intent(mActivity,latestContentActivity.class);
                intent.putExtra(Constant.START_LOCATION, startingLocation);
                intent.putExtra("entity", entity);
                intent.putExtra("isLight", ((MainActivity) mActivity).isLight());
                String readSee=PreUtils.getStringFromDefault(mActivity,"read","");
                String[] splits=readSee.split(",");
                StringBuffer sb=new StringBuffer();
                if(splits.length>=200){
                    for(int i=100;i<splits.length;i++){
                        sb.append(splits[i]+",");
                    }
                    readSee=sb.toString();
                }
                if(!readSee.contains(entity.getId()+"")){
                    readSee=readSee+entity.getId()+",";
                }
                PreUtils.putStringToDefault(mActivity, "read", readSee);
                TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
                tv_title.setTextColor(getResources().getColor(R.color.clicked_tv_textcolor));
                startActivity(intent);
                mActivity.overridePendingTransition(0,0);


            }
        });



        return view;
    }
    private void LoadFirst(){
        if(HttpUtils.isNetWorkConnected(mActivity)){
            HttpUtils.get(Constant.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db=((MainActivity)mActivity).getdbHelper().getWritableDatabase();
                    db.execSQL("replace into Cache(date,json) values("+Constant.LATEST_COLUMN+",'"+responseString+"')");
                    db.close();
                parseLatestJson(responseString);


                }
            });
        }else{
            SQLiteDatabase db=((MainActivity)mActivity).getdbHelper().getReadableDatabase();
            Cursor cursor=db.rawQuery("select * from Cache where date ="+Constant.LATEST_COLUMN,null);
            if(cursor.moveToFirst()){
                String json=cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            }

        }


    }

    private void parseLatestJson(String Jsonresponse){
        Gson gson=new Gson();
        latest=gson.fromJson(Jsonresponse, Latest.class);
        date=latest.getDate();
        kanner.setTopStoriesEntities(latest.getTop_stories());
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> storiesEntities = latest.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle("今日热闻");
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;

            }
        });

    }

    public void loadMore(final String url){
        isLoading=true;

        if(HttpUtils.isNetWorkConnected(mActivity)){
            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {



                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    SQLiteDatabase db = ((MainActivity) mActivity).getdbHelper().getWritableDatabase();
                    db.execSQL("replace into Cache(date,json) values(" + date + ",' " + responseString + "')");
                    db.close();
                    parseBeforeJson(responseString);

                }
            });
        }else{
            SQLiteDatabase db = ((MainActivity) mActivity).getdbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from Cache where date = " + date, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("Cache", "date < " + date, null);
                isLoading = false;
                Snackbar sb = Snackbar.make(lv_news, "没有更多的离线内容了~", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(((MainActivity) mActivity).isLight() ? android.R.color.holo_blue_dark : android.R.color.black));
                sb.show();
            }
            cursor.close();
            db.close();
        }
    }
    public void parseBeforeJson(String responseString){
        Gson gson=new Gson();
        before=gson.fromJson(responseString,Before.class);
        if(before==null){
            isLoading=false;
            return;
        }
        date=before.getDate();
        handler.post(new Runnable() {
            @Override
            public void run() {

           List<StoriesEntity> storiesEntities=before.getStories();
                StoriesEntity topic=new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle(converData(date));
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading=false;
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        LoadFirst();
    }
    private String converData(String date){
        String result=date.substring(0,4);
        result +="年";
        result+=date.substring(4,6);
        result+="月";
        result+=date.substring(6,8);
        result+="日";
        return result;
    }
    public void updateTheme(){
        mAdapter.updateTheme();
    }


}
