package com.example.administrator.myzzhihuday.adapter;

import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.myzzhihuday.R;
import com.example.administrator.myzzhihuday.activity.MainActivity;
import com.example.administrator.myzzhihuday.model.StoriesEntity;
import com.example.administrator.myzzhihuday.util.PreUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class NewsItemAdapter extends BaseAdapter{
    private Context context;
    private List<StoriesEntity> entities;
    private ImageLoader mimageLoder;
    private DisplayImageOptions options;
    private ViewHolder viewHolder;
    private boolean isLight;
    public NewsItemAdapter(Context context,List<StoriesEntity> entitys){
        this.context=context;
        this.entities=entitys;
        isLight=((MainActivity)context).isLight();
        mimageLoder=ImageLoader.getInstance();
        options=new DisplayImageOptions.Builder().
                cacheInMemory(true)
                .showImageOnFail(R.drawable.ic_data_error)
                .cacheOnDisk(true).build();
    }
    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int i) {
        return entities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.layout,viewGroup,false);
            viewHolder.iv_title=(ImageView)view.findViewById(R.id.iv_title);
            viewHolder.tv_title=(TextView)view.findViewById(R.id.tv_title);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }
        String ReadSee= PreUtils.getStringFromDefault(context,"read","");
        if(ReadSee.contains(entities.get(i).getId()+"")){
            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.clicked_tv_textcolor));

        }else{
            viewHolder.tv_title.setTextColor(context.getResources().getColor(isLight?android.R.color.black:android.R.color.white));
        }
        ((LinearLayout)viewHolder.iv_title.getParent().getParent().getParent()).setBackgroundColor(context.getResources().getColor(isLight?R.color.light_news_item:R.color.dark_news_item));
        ((FrameLayout)viewHolder.tv_title.getParent().getParent()).setBackgroundResource(isLight?R.drawable.item_background_selector_light:R.drawable.item_background_selector_dark);


        StoriesEntity entity=entities.get(i);
        viewHolder.tv_title.setText(entity.getTitle());
        if(entity.getImages()!=null){
            viewHolder.iv_title.setVisibility(View.VISIBLE);
            mimageLoder.displayImage(entity.getImages().get(0),viewHolder.iv_title,options);
        }else{
            viewHolder.iv_title.setVisibility(View.GONE);
        }


        return view;
    }
    public   static class  ViewHolder{
        TextView tv_title;
        ImageView iv_title;
    }
    public void updateTheme() {
        isLight = ((MainActivity) context).isLight();
        notifyDataSetChanged();
    }
}
