package com.example.lockscreen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lockscreen.activity.LockActivity;
import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/5/8.
 * 监听屏幕变暗的广播
 */
public class ScreenOffReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        String action=intent.getAction();
        //变暗就启动锁屏页面
        LogUtils.i("---screenOff---action:"+action);
        if(action.equals("android.intent.action.SCREEN_OFF")){
            Intent it=new Intent(context, LockActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
            //SystemUtil.getInstance(context).closeLock();
        }
    }
}
