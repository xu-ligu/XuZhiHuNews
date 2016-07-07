package com.example.administrator.myzzhihuday.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myzzhihuday.R;

/**
 * Created by Administrator on 2016/7/3.
 */
public class textActivity extends AppCompatActivity {
    private String[] text={"1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3","1","2","3"};
   private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_content_layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        madapter madapter=new madapter();
     //   ListView listView=(ListView)findViewById(R.id.list_item);
       // listView.setAdapter(madapter);
    }
    public class  madapter extends BaseAdapter{

        @Override
        public int getCount() {
            return text.length;
        }

        @Override
        public Object getItem(int position) {
            return text[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(textActivity.this).inflate(R.layout.menu_item,null);
            TextView textView=(TextView)view.findViewById(R.id.tv_item);
            textView.setText(text[position]);
            return view;
        }
    }
}
