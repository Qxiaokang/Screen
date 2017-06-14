package com.example.lockscreen.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.lockscreen.activity.HomeApplicaiton;
import com.example.lockscreen.broadcast.HomeReceiver;
import com.example.lockscreen.broadcast.ScreenOffReceiver;
import com.example.lockscreen.broadcast.ScreenOnReceiver;
import com.example.lockscreen.utils.LogUtils;
import com.example.lockscreen.utils.SystemUtil;

/**
 * Created by Admin on 2017/5/8.
 */
public class LockService extends Service{
    private ScreenOffReceiver screenOffReceiver = null;
    private ScreenOnReceiver screenOnReceiver = null;
    private HomeReceiver homeReceiver = null;
    public static final int LOCK_FLAG = 1;
    private MyConnection myConnection;
    private MyBinder myBinder;
    private Context context = null;
    private Intent startIntent = null;
    public static boolean isForce = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        LogUtils.e("---LockService---onBind---");
        return myBinder;
    }

    @Override
    public void onCreate(){
        LogUtils.e("---Lockservice---create");
        myBinder = myBinder == null ? new MyBinder(LOCK_FLAG) : myBinder;
        myConnection = new MyConnection();
        screenOffReceiver = new ScreenOffReceiver();
        screenOnReceiver = new ScreenOnReceiver();
        //homeReceiver=new HomeReceiver();
        //SystemUtil.getInstance(this).registerReceiver(Intent.ACTION_CLOSE_SYSTEM_DIALOGS,homeReceiver);
        SystemUtil.getInstance(getApplicationContext()).registerReceiver("android.intent.action.SCREEN_OFF", screenOffReceiver);
        SystemUtil.getInstance(getApplicationContext()).registerReceiver("android.intent.action.SCREEN_ON", screenOnReceiver);
        SystemUtil.getInstance(getApplicationContext()).getSp().edit().putInt("isfirst", 1).commit();
        SystemUtil.getInstance(getApplicationContext()).closeLock();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        LogUtils.e("---LockService--Start");
        context = LockService.this.getApplicationContext();
        boolean isbind = this.getApplicationContext().bindService(new Intent(LockService.this.getApplicationContext(), BindService.class), myConnection, Service.BIND_IMPORTANT);
        LogUtils.e("---bind--BindService:" + isbind);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        LogUtils.e("---LockService---destroy");
        if(screenOnReceiver != null){
            getApplicationContext().unregisterReceiver(screenOnReceiver);
        }
        if(screenOffReceiver != null){
            getApplicationContext().unregisterReceiver(screenOffReceiver);
        }
        if(homeReceiver != null){
            getApplicationContext().unregisterReceiver(homeReceiver);
        }
        if(!isForce){
            LogUtils.d("---isforce:"+isForce);
            SystemUtil.getInstance(getApplicationContext()).resetLock();
            Intent intent = new Intent(context, LockService.class);
            startService(intent);
        }
        super.onDestroy();
    }

    class MyConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder){
            LogUtils.i("---LockService---connect---success");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName){
            LogUtils.i("---LockService---connect---faild");
            LogUtils.i("context:" + HomeApplicaiton.context);
            isForce=true;
            startIntent = new Intent();
            startIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            startIntent.setAction("android.forcestop.action");
            HomeApplicaiton.context.sendBroadcast(startIntent);
        }
    }
}
