package com.example.cardrecyclerview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by yue on 2017/8/21.
 */

public class CardRecyclerView extends RecyclerView {
    private final static int REMOVE_LEFT=0;
    private final static int REMOVE_RIGHT=1;
    private int removeDir;
    //顶部view的x,y坐标
    private float topChildX,topChildY;
    //记录每次滑动手指落下的x,y坐标
    private float touchDownX,touchDownY;
    //边界值
    private int border=dip2px(120);
    //x,y坐标的滑动动画
    private ValueAnimator moveAnimatorX,moveAnimatorY;
    //x,y轴缩放动画
    private ValueAnimator scaleAnimatorX,scaleAnimatorY;
    //view能否触摸
    private boolean canTouch=true;
    //移除监听
    private OnRemoveListener onRemoveListener;

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    public CardRecyclerView(Context context) {
        this(context,null);
    }

    public CardRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CardRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //如果当前不能触摸就不消费此触摸事件
        if(!canTouch){
            return false;
        }
        if(getChildCount()==0){
            return super.onTouchEvent(e);
        }
        //获取到最上面的view
        View topChild=getChildAt(getChildCount()-1);
        float touchX=e.getX();
        float touchY=e.getY();
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                topChildX=topChild.getX();
                topChildY=topChild.getY();
                //此次滑动操作手指落下的初始位置
                touchDownX=touchX;
                touchDownY=touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx=touchX-touchDownX;
                float dy=touchY-touchDownY;
                topChild.setX(topChildX+dx);
                topChild.setY(topChildY+dy);
                float translate=Math.abs(topChild.getX()-topChildX)>Math.abs(topChild.getY()-topChildY)?Math.abs(topChild.getX()-topChildX):Math.abs(topChild.getY()-topChildY);
                scaleNextItem((float) (translate*0.1/border+0.8));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("childPosition",getChildAdapterPosition(topChild)+"");
                touchUp(topChild);
                break;
        }
        return super.onTouchEvent(e);
    }


    /**
     * 一次滑动完成手指抬起后的操作
     * @param view 此次操作的view
     */
    private void touchUp(final View view) {
        //每次抬手先设置该view不能触摸，防止归位动画没结束前连续触摸发生错乱
        canTouch=false;

        //是否该移除
        boolean isRemove=false;

        final View nextView=getChildAt(getChildCount()-2);
        float targetX=0;
        float targetY=0;
        //如果横坐标偏移没超过边界或者纵坐标偏移不超过边界
        if(Math.abs(view.getX()-topChildX)<border){
            isRemove=false;
            targetX=topChildX;
            targetY=topChildY;
        }else if(Math.abs(view.getX()-topChildX)>border&&view.getX()-topChildX>0){
            //从右侧移除屏幕
            isRemove=true;
            targetX=getResources().getDisplayMetrics().widthPixels;
            targetY=(topChildY-view.getY())/(topChildX-view.getX())*targetX+(topChildX*view.getY()-view.getX()*topChildY)/(topChildX-view.getX());
            //移除方向设置为向右移除
            removeDir=REMOVE_RIGHT;
        }else if (Math.abs(view.getX()-topChildX)>border&&view.getX()-topChildX<0){
            //从左侧移除屏幕
            isRemove=true;
            targetX=0-getResources().getDisplayMetrics().widthPixels;
            targetY=(topChildY-view.getY())/(topChildX-view.getX())*targetX+(topChildX*view.getY()-view.getX()*topChildY)/(topChildX-view.getX());
            //移除方向设置为向左移除
            removeDir=REMOVE_RIGHT;
        }

        /**
         * 初始化移动动画以及缩放动画
         * 平移针对当前view
         * 缩放针对下一个view
         */
        moveAnimatorX=ValueAnimator.ofFloat(view.getX(),targetX);
        moveAnimatorY=ValueAnimator.ofFloat(view.getY(),targetY);
        if(nextView!=null) {
            if (!isRemove) {
                scaleAnimatorX = ValueAnimator.ofFloat(nextView.getScaleX(), 0.8f);
                scaleAnimatorY = ValueAnimator.ofFloat(nextView.getScaleY(), 0.8f);
            } else {
                scaleAnimatorX = ValueAnimator.ofFloat(nextView.getScaleX(), 1.0f);
                scaleAnimatorY = ValueAnimator.ofFloat(nextView.getScaleY(), 1.0f);
            }
        }
        moveAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setX((Float) valueAnimator.getAnimatedValue());
            }
        });
        moveAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        if(nextView!=null) {
            scaleAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    nextView.setScaleX((Float) valueAnimator.getAnimatedValue());
                }
            });
            scaleAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    nextView.setScaleY((Float) valueAnimator.getAnimatedValue());
                }
            });
        }

        //动画set，一起执行
        AnimatorSet set=new AnimatorSet();
        if(nextView!=null) {
            set.playTogether(moveAnimatorX, moveAnimatorY, scaleAnimatorX, scaleAnimatorY);
        }else {
            set.playTogether(moveAnimatorX, moveAnimatorY);
        }
        set.setDuration(400);
        set.setInterpolator(new OvershootInterpolator());
        final boolean finalIsRemove = isRemove;
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            /**
             * 动画结束执行
             * @param animator
             */
            @Override
            public void onAnimationEnd(Animator animator) {
                //如果此时该移除当前，就根据移除方向标记回调移除接口
                if(finalIsRemove &&onRemoveListener!=null){
                    if(removeDir==REMOVE_RIGHT){
                        onRemoveListener.onRightRemove();
                    }else if(removeDir==REMOVE_LEFT){
                        onRemoveListener.onLeftRemove();
                    }
                }
                //动画执行结束，将view再次设置为可触摸
                canTouch=true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        set.start();
    }

    /**
     * 缩放下一个item
     * @param factor 缩放因子
     */
    private void scaleNextItem(float factor){
        if(getChildCount()<2){
            return;
        }
        if(factor>1){
            factor=1;
        }
        View nextChild=getChildAt(getChildCount()-2);
        nextChild.setScaleX(factor);
        nextChild.setScaleY(factor);
    }

    private int dip2px(float dip) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics()
        );
    }
}
