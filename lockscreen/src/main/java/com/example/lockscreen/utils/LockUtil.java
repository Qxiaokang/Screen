package com.example.lockscreen.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Admin on 2017/5/8.
 */
public class LockUtil{
    private static LockUtil mLockUtil;
    private WindowManager windowManager;
    private boolean isLocked;
    private WindowManager.LayoutParams layoutParams;
    private  Context context;
    private View mLockView;
    public static synchronized LockUtil getInstance(Context context){
        if(mLockUtil==null){
            mLockUtil=new LockUtil(context);
        }
        return mLockUtil;
    }
    private LockUtil(Context context){
        this.context=context;
        init();
    }

    private void init(){
        windowManager= (WindowManager) context.getApplicationContext().getSystemService("window");
        layoutParams=new WindowManager.LayoutParams();
        layoutParams.type= WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;//内部错误，显示所有内容之上
        layoutParams.format= PixelFormat.RGBA_8888;//设置图片格式，背景透明
        mLockView.setLayoutParams(layoutParams);
    }


    //设置视图
    public void setmLockView(View lockView){
        this.mLockView=lockView;
    }

    /**
     * 锁屏
     */
    public synchronized void lock() {
        if (mLockView != null && !isLocked) {
            windowManager.addView(mLockView, layoutParams);
        }
        isLocked = true;
    }
    /**
     * 解锁
     */
    public synchronized void unlock() {
        if (windowManager != null && isLocked) {
            windowManager.removeView(mLockView);
        }
        isLocked = false;
    }
    /**
     * 更新
     */
    public synchronized void update() {
        if (mLockView != null && !isLocked) {
            windowManager.updateViewLayout(mLockView, layoutParams);
        }
    }
}
