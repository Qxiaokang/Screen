package com.example.lockscreen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/5/8.
 * 监听屏幕变亮的广播
 */
public class ScreenOnReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        String action=intent.getAction();
        //变亮屏蔽系统锁屏
        if(action.equals("android.intent.action.SCREEN_ON")){
            /*
                 * 此方式已经过时，在activtiy中编写
                 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                 * 两句可以代替此方式
                 */
            LogUtils.i("android.intent.action.SCREEN_ON");
        }
    }
}

