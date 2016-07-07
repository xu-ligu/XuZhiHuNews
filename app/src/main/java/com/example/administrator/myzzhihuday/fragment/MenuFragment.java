package com.example.administrator.myzzhihuday.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myzzhihuday.R;
import com.example.administrator.myzzhihuday.activity.MainActivity;
import com.example.administrator.myzzhihuday.adapter.NewsItemAdapter;
import com.example.administrator.myzzhihuday.model.NewsListItem;
import com.example.administrator.myzzhihuday.util.Constant;
import com.example.administrator.myzzhihuday.util.HttpUtils;
import com.example.administrator.myzzhihuday.util.PreUtils;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MenuFragment extends BaseFragment implements View.OnClickListener {
    private ListView lv_item;
    private List<NewsListItem> items;
    private TextView tv_main,tv_download,tv_login,tv_backup;
    private NewsTypeAdapter madapter;
    private boolean isLight;
    private LinearLayout ll_menu;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.menu_layout,container,false);
        ll_menu=(LinearLayout)view.findViewById(R.id.ll_menu);
        tv_login=(TextView)view.findViewById(R.id.tv_login);
        tv_backup=(TextView)view.findViewById(R.id.tv_backup);
        tv_download=(TextView)view.findViewById(R.id.tv_download);
        tv_download.setOnClickListener(this);

        lv_item=(ListView)view.findViewById(R.id.lv_item);
        tv_main=(TextView)view.findViewById(R.id.tv_main);
        tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mActivity).loadLatest();
                ((MainActivity) mActivity).closeMenu();
            }
        });
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                        replace(R.id.fl_content, new NewsFragment(items.get(position).getId(), items.get(position).getTitle()),"news").commit();*/
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right,R.anim.slide_out_to_left).add(R.id.fl_content,
                        new NewsFragment(items.get(position).getId(), items.get(position).getTitle()), "news").addToBackStack("0").commit();

                ((MainActivity) mActivity).setCurId(items.get(position).getId());
                ((MainActivity) mActivity).closeMenu();
            }
        });

        return view;
    }
    @Override
    protected void initData() {
        super.initData();
        isLight=((MainActivity)mActivity).isLight();
        items=new ArrayList<NewsListItem>();
        if(HttpUtils.isNetWorkConnected(mActivity)){
        HttpUtils.get(Constant.THEMES, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String json=response.toString();
                PreUtils.putStringToDefault(mActivity,Constant.THEMES,json);
                parseJson(response);
            }
        });
        }else{
            String json=PreUtils.getStringFromDefault(mActivity,Constant.THEMES,"");
            try{
                JSONObject jsonObject=new JSONObject(json);
                parseJson(jsonObject);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }






    }
    public void parseJson(JSONObject jsonObject){
        try{
            JSONArray jsonArray=jsonObject.getJSONArray("others");
            for (int i=0;i<jsonArray.length();i++){
                NewsListItem item=new NewsListItem();
                JSONObject object=jsonArray.getJSONObject(i);
                item.setTitle(object.getString("name"));
                item.setId(object.getString("id"));
                items.add(item);
            }
            madapter=new NewsTypeAdapter();
            lv_item.setAdapter(madapter);
            updateTheme();

        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

    }

    public class NewsTypeAdapter extends BaseAdapter{
        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=LayoutInflater.from(mActivity).inflate(R.layout.menu_item,parent,false);

            }  TextView tv_item=(TextView)convertView.findViewById(R.id.tv_item);
            tv_item.setTextColor(getResources().getColor(isLight?R.color.light_menu_listview_textcolor:R.color.dark_menu_listview_textcolor));
            tv_item.setText(items.get(position).getTitle());
            return convertView;

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }
    }
    public void updateTheme(){
        isLight = ((MainActivity) mActivity).isLight();
        ll_menu.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_header : R.color.dark_menu_header));
        tv_login.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_backup.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_download.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tv_main.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_index_background : R.color.dark_menu_index_background));
        lv_item.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_listview_background : R.color.dark_menu_listview_background));
        madapter.notifyDataSetChanged();

    }

}
