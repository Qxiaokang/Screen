package com.example.lockscreen.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by Admin on 2017/6/14.
 */
public class HomeApplicaiton extends Application{
    public static Context context=null;
    @Override
    public void onCreate(){
        context=this;
        super.onCreate();

    }

}
