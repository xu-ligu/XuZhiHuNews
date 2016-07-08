package com.example.administrator.myzzhihuday.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.myzzhihuday.R;
import com.example.administrator.myzzhihuday.util.Constant;
import com.example.administrator.myzzhihuday.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;
import  org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends Activity {
    private ImageView iv_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_layout);
        iv_start=(ImageView)findViewById(R.id.iv_start);
        initImage();


    }

    private void initImage(){
        File dir=getFilesDir();
        final File imgFile=new File(dir,"Start.jpg");
        if(imgFile.exists()){
            iv_start.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }else{
            iv_start.setImageResource(R.mipmap.start);
        }
        final ScaleAnimation scaleAnimation=new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(3000);


        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //动画结束，加载图片保存到start。jpg再跳转到mainactivity
                if (HttpUtils.isNetWorkConnected(SplashActivity.this)) {

                    HttpUtils.get(Constant.START, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                            try {

                                final String bytes = new String(responseBody);
                                JSONObject jsonObject = new JSONObject(bytes);
                                String url = jsonObject.getString("img");
                                HttpUtils.getImage(url, new BinaryHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                                          Log.d("23","success");
                                        saceImage(imgFile, binaryData);
                                        startAvtivity();


                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                                           Log.d("23","failure");
                                        startAvtivity();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                                startAvtivity();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            startAvtivity();
                        }
                    });
                } else {
                    startAvtivity();
                    Toast.makeText(SplashActivity.this, "请连接到网络！", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        iv_start.startAnimation(scaleAnimation);




    }


    public void startAvtivity(){
        Intent intent=new Intent(SplashActivity.this, MainActivity.class);
       startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();

    }

    public void saceImage(File file ,byte[] bytes){

        try{
            if(file.exists()){
                file.delete();
            }
            FileOutputStream outputStream=new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
