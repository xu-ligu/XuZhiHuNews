package com.example.administrator.myzzhihuday.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.example.administrator.myzzhihuday.R;
import com.example.administrator.myzzhihuday.db.CacheDataBase;
import com.example.administrator.myzzhihuday.fragment.MainFragment;
import com.example.administrator.myzzhihuday.fragment.MenuFragment;
import com.example.administrator.myzzhihuday.fragment.NewsFragment;

import java.security.acl.Group;


public class MainActivity extends AppCompatActivity{

    private static String curId;
    public static String idString;
    private Toolbar toolbar;
    private MenuFragment menuFragment;
    private FrameLayout fl_content;
    private DrawerLayout mDrawerLayout;
    private boolean isLight;
    private SharedPreferences sp;
   private CacheDataBase dbHelper;

    private SwipeRefreshLayout sr;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper=new CacheDataBase(this,1);

        isLight=sp.getBoolean("isLight",true);

        initView();
        loadLatest();
    }

    public void loadLatest() {
    	/*getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    	.replace(R.id.fl_content, new MainFragment(),"latest").commit();*/



        getSupportFragmentManager().
                beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                replace(R.id.fl_content,new MainFragment(),"latest").
                commit();

        curId = "latest";
    }



    private void initView(){

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(isLight?R.color.light_toolbar:R.color.dark_toolbar));
        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);
        setStatusBarColor(getResources().getColor(isLight?R.color.light_toolbar:R.color.dark_toolbar));
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
        sr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                replaceFragment();
                sr.setRefreshing(false);
            }
        });
        fl_content=(FrameLayout)findViewById(R.id.fl_content);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        final ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,
                R.string.app_name,R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }



    public void replaceFragment() { if (curId.equals("latest")) {
        getSupportFragmentManager().
                beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                replace(R.id.fl_content,new MainFragment(),"latest").
                commit();
    } else {

    }
    }

    public void setSwipeRefreshEnable(Boolean enable) {

        sr.setEnabled(enable);
    }

    public boolean isLight() {
        // TODO Auto-generated method stub
        return isLight;
    }
    public void closeMenu(){
        mDrawerLayout.closeDrawers();

    }
    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }
    public String getCurId() {
        return curId;
    }

    public static void setCurId(String Id) {
        curId = Id;
    }

    public SwipeRefreshLayout getSr() {
        return sr;
    }
    public CacheDataBase getdbHelper(){
        return  dbHelper;
    }

    public void setSr(SwipeRefreshLayout sr) {
        this.sr = sr;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.getItem(0).setTitle(sp.getBoolean("isLight",true)?"夜间模式":"日间模式");

        return true;
    }

    @TargetApi(21)
    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            Window window = this.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_mode){
            isLight=!isLight;
            item.setTitle(isLight ? "夜间模式" : "日间模式");
            toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
            setStatusBarColor(getResources().getColor(isLight?R.color.light_toolbar:R.color.dark_toolbar));
            if(curId.equals("latest")){
                ((MainFragment)getSupportFragmentManager().findFragmentByTag("latest")).updateTheme();
            }else{
                ((NewsFragment)getSupportFragmentManager().findFragmentByTag("news")).updateTheme();
            }
            ((MenuFragment)getSupportFragmentManager().findFragmentById(R.id.menu_fragment)).updateTheme();
            sp.edit().putBoolean("isLight",isLight).apply();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setToolbarTitle("首页");
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
            return;
        }else{

        }
        super.onBackPressed();
    }
}
