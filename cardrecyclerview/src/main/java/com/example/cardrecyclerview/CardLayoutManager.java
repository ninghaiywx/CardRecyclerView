package com.example.cardrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yue on 2017/8/21.
 */

public class CardLayoutManager extends RecyclerView.LayoutManager{
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int childCount=getItemCount();
        for(int i=0;i<childCount;i++){
            View view=recycler.getViewForPosition(i);
            measureChild(view,0,0);
            addView(view);
            int width=getDecoratedMeasuredWidth(view);
            int height=getDecoratedMeasuredHeight(view);
            layoutDecorated(view,0,0,width,height);
            //强制view回归原位，会有个别view总是不听话
            view.setX(0);
            view.setY(0);
            if(i<childCount-1){
                view.setScaleX(0.8f);
                view.setScaleY(0.8f);
            }
        }
    }
}
