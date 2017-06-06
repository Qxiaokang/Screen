package com.example.lockscreen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lockscreen.activity.LockActivity;
import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/6/5.
 */
public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        LogUtils.i("开 机");
        String action="android.intent.action.MAIN";
        String categary="android.intent.category.LAUNCHER";
        Intent startIntent=new Intent(context, LockActivity.class);
        startIntent.setAction(action);
        startIntent.addCategory(categary);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startIntent);
    }
}
