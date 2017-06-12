package com.example.lockscreen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lockscreen.activity.LockActivity;
import com.example.lockscreen.service.LockService;
import com.example.lockscreen.utils.LogUtils;
import com.example.lockscreen.utils.SystemUtil;

/**
 * Created by Admin on 2017/6/5.
 */
public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        String actionString=intent.getAction();
        LogUtils.d("---action:"+intent.getAction());
        if(("android.intent.action.BOOT_COMPLETED").equals(actionString)){
            String action="android.intent.action.MAIN";
            String categary="android.intent.category.DEFAULT";
            SystemUtil.getInstance(context.getApplicationContext()).getSp().edit().putInt("isfirst",0).commit();
            Intent startIntent=new Intent(context, LockActivity.class);
            startIntent.setAction(action);
            startIntent.addCategory(categary);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
        }
        if(("android.intent.action.PACKAGE_RESTARTED").equals(actionString)){
            context.startService(new Intent(context, LockService.class));
        }

    }
}
