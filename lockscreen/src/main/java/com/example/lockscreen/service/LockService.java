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
    private ScreenOffReceiver screenOffReceiver=null;
    private ScreenOnReceiver screenOnReceiver=null;
    private HomeReceiver homeReceiver=null;
    public static final int LOCK_FLAG=1;
    private MyConnection myConnection;
    private MyBinder myBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        LogUtils.e("---LockService---onBind---");
        return myBinder;
    }

    @Override
    public void onCreate(){
        LogUtils.e("---Lockservice---create");
        myBinder=myBinder==null?new MyBinder(LOCK_FLAG):myBinder;
        myBinder.setService(LockService.this);
        myConnection=new MyConnection(this,LOCK_FLAG,myBinder);
        screenOffReceiver=new ScreenOffReceiver();
        screenOnReceiver=new ScreenOnReceiver();
        //homeReceiver=new HomeReceiver();
        //SystemUtil.getInstance(this).registerReceiver(Intent.ACTION_CLOSE_SYSTEM_DIALOGS,homeReceiver);
        SystemUtil.getInstance(getApplicationContext()).registerReceiver("android.intent.action.SCREEN_OFF",screenOffReceiver);
        SystemUtil.getInstance(getApplicationContext()).registerReceiver("android.intent.action.SCREEN_ON",screenOnReceiver);
        SystemUtil.getInstance(getApplicationContext()).getSp().edit().putInt("isfirst",1).commit();
        SystemUtil.getInstance(getApplicationContext()).closeLock();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        LogUtils.e("---LockService--Start");
        boolean isbind=this.bindService(new Intent(LockService.this,BindService.class),myConnection,Service.BIND_AUTO_CREATE);
        LogUtils.e("---bind--BindService:"+isbind);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        LogUtils.e("---LockService---destroy");
        SystemUtil.getInstance(getApplicationContext()).resetLock();
        if(screenOnReceiver!=null){
            getApplicationContext().unregisterReceiver(screenOnReceiver);
        }
        if(screenOffReceiver!=null){
            getApplicationContext().unregisterReceiver(screenOffReceiver);
        }
        if(homeReceiver!=null){
            getApplicationContext().unregisterReceiver(homeReceiver);
        }
        Intent intent=new Intent(this,LockService.class);
        startService(intent);
        super.onDestroy();
    }
}
