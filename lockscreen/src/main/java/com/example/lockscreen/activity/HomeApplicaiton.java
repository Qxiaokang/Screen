package com.example.lockscreen.activity;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/6/14.
 */
public class HomeApplicaiton extends Application{
    public static Context context=null;
    public static int screenW,screenH;
    private WindowManager windowManager=null;
    @Override
    public void onCreate(){
        context=this;
        super.onCreate();
        DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
        //windowManager= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //screenW=windowManager.getDefaultDisplay().getWidth();
        //screenH=windowManager.getDefaultDisplay().getHeight();
        screenW=displayMetrics.widthPixels;
        screenH=displayMetrics.heightPixels;
        LogUtils.i("screenW:"+screenW+"   screenH:"+screenH);
    }

}
