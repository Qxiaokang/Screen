package com.example.lockscreen.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.lockscreen.broadcast.HomeReceiver;
import com.example.lockscreen.broadcast.ScreenOffReceiver;
import com.example.lockscreen.broadcast.ScreenOnReceiver;
import com.example.lockscreen.utils.LogUtils;
import com.example.lockscreen.utils.SystemUtil;

/**
 * Created by Admin on 2017/5/8.
 */
public class LockService extends Service{
    private ScreenOffReceiver screenOffReceiver;
    private ScreenOnReceiver screenOnReceiver;
    private HomeReceiver homeReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        LogUtils.d("---service创建");
        super.onCreate();
        screenOffReceiver=new ScreenOffReceiver();
        screenOnReceiver=new ScreenOnReceiver();
        homeReceiver=new HomeReceiver();
        SystemUtil.getInstance(getApplicationContext()).registerReceiver(Intent.ACTION_CLOSE_SYSTEM_DIALOGS,homeReceiver);
        SystemUtil.getInstance(getApplicationContext()).registerReceiver("android.intent.action.SCREEN_OFF",screenOffReceiver);
        SystemUtil.getInstance(getApplicationContext()).registerReceiver("android.intent.action.SCREEN_ON",screenOnReceiver);
        SystemUtil.sp.edit().putInt("si",1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        LogUtils.d("---service开启");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        LogUtils.d("---service销毁");
        super.onDestroy();
        unregisterReceiver(screenOffReceiver);
        unregisterReceiver(screenOnReceiver);
        unregisterReceiver(homeReceiver);
        Intent intent=new Intent(getApplicationContext(),LockService.class);
        startService(intent);
    }
}
