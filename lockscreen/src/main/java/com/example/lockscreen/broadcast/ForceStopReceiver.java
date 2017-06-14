package com.example.lockscreen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lockscreen.service.BindService;
import com.example.lockscreen.service.LockService;
import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/6/12.
 */
public class ForceStopReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        String action=intent.getAction();
        LogUtils.d("receive  ---- action:"+action);
        if("android.forcestop.action".equals(action)){
            context.startService(new Intent(context,LockService.class));
            context.startService(new Intent(context, BindService.class));
        }
    }
}
