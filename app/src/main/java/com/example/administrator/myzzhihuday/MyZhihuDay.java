package com.example.administrator.myzzhihuday;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MyZhihuDay extends Application {
    public void onCreate(){
        super.onCreate();
        initImagerLoader(getApplicationContext());

    }
    private void initImagerLoader(Context context){
        File cacheDir= StorageUtils.getCacheDirectory(context);
       /* ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(
                context).threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY-2)
                .memoryCache(new LruMemoryCache(2*1024*1024)).
                        diskCacheFileNameGenerator(new Md5FileNameGenerator()).
                        tasksProcessingOrder(QueueProcessingType.LIFO).
                        diskCache(new UnlimitedDiskCache(cacheDir)).
                        writeDebugLogs().build();*/
        ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);


    }

}
