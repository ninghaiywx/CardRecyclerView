package com.example.yue.demo;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yue on 2017/8/21.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<String>list;
    private Context mContext;

    public Adapter(Context context,List<String>list){
        this.list=list;
        mContext=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text=list.get(position);
        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.recycler_item);
        }
    }
}
