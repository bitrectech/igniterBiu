package com.bitrefactor.igniterbiu.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bitrefactor.igniterbiu.adapter.DeviceAdapter;

public class DeviceRecyclerView extends RecyclerView {
    View view;
    private DeviceAdapter.ViewHolder viewHolder;
    private LinearLayout itemLayout;
    private int lastX;
    private int lastY;
    private int deleteWidth;
    private final Scroller scroller;
    private int state = 0;

    public DeviceRecyclerView(@NonNull Context context) {
        super(context);

        scroller = new Scroller(context,new LinearInterpolator());
    }

    public DeviceRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        scroller = new Scroller(context,new LinearInterpolator());
    }

    public DeviceRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        scroller = new Scroller(context,new LinearInterpolator());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                    onTouchEvent(e);
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int)e.getX();
        int y = (int)e.getY();
        try {


        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(state == 0){
                    view = findChildViewUnder(x,y);
                    viewHolder = (DeviceAdapter.ViewHolder) getChildViewHolder(view);
                    itemLayout = viewHolder.getLayout();
                    ImageView deleteIv = viewHolder.getDelDevice();
                    deleteWidth = deleteIv.getWidth();
                }else if(state == 1){
                    View view1 = findChildViewUnder(x,y);
                    DeviceAdapter.ViewHolder viewHolder1 = (DeviceAdapter.ViewHolder) getChildViewHolder(view1);
                    boolean bool = viewHolder.equals(viewHolder1);//判断当前用户指向的Item是否为之前打开的那个Item
                    if(bool){
                        break;
                    }else {
                        scroller.startScroll(itemLayout.getScrollX(), 0, -deleteWidth, 0, 30);
                        invalidate();
                        state = 0;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollX = itemLayout.getScrollX();
                int dx = lastX - x;
                int dy = lastY - y;
                if(Math.abs(dx)>Math.abs(dy)){
                    if(scrollX + dx >= deleteWidth){
                        itemLayout.scrollTo(deleteWidth,0);
                        state = 1;
                        return true;
                    }else if(scrollX + dx <= 0){
                        itemLayout.scrollTo(0,0);
                        state = 0;
                        return true;
                    }
                    itemLayout.scrollBy(dx,0);
                }
                break;
            case MotionEvent.ACTION_UP:
                int deltaX = 0;
                int upScrollX = itemLayout.getScrollX();
                if(upScrollX >= deleteWidth/2) {
                    deltaX = deleteWidth - upScrollX;
                    state = 1;
                }else if(upScrollX < deleteWidth/2){
                    deltaX = -upScrollX;
                    state = 0;
                }
                scroller.startScroll(upScrollX,0,deltaX,0,30);
                invalidate();
                break;

        }
        lastX = x;
        lastY = y;
        }catch (Exception ignored){
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            itemLayout.scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }
}
