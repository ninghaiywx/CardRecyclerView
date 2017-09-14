package com.example.yue.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.cardrecyclerview.CardLayoutManager;
import com.example.cardrecyclerview.CardRecyclerView;
import com.example.cardrecyclerview.OnRemoveListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CardRecyclerView recyclerView;
    Adapter adapter;
    List<String> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(CardRecyclerView)findViewById(R.id.recyclerview);
        adapter=new Adapter(this,list);
        CardLayoutManager manager=new CardLayoutManager();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        init();

        recyclerView.setOnRemoveListener(new OnRemoveListener() {
            @Override
            public void onLeftRemove() {
                Toast.makeText(MainActivity.this,"左侧删除"+list.get(list.size()-1),Toast.LENGTH_LONG).show();
                list.remove(list.size()-1);
                adapter.notifyDataSetChanged();
                Log.d("size",list.size()+"");
            }

            @Override
            public void onRightRemove() {
                Toast.makeText(MainActivity.this,"右侧删除"+list.get(list.size()-1),Toast.LENGTH_LONG).show();
                list.remove(list.size()-1);
                adapter.notifyDataSetChanged();
                Log.d("size",list.size()+"");
            }
        });
    }

    private void init() {
        for(int i=0;i<20;i++){
            list.add(i+"");
        }
        adapter.notifyDataSetChanged();
    }
}
