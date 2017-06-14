package com.example.lockscreen.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.lockscreen.activity.HomeApplicaiton;
import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/6/12.
 */
public class BindService extends Service{
    public static final int BIND_FAG=2;
    private MyConnection myConnection;
    private MyBinder myBinder;
    private Context context;
    private Intent startIntent;
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        LogUtils.e("---BindServic---onBind---");
        return myBinder;
    }

    @Override
    public void onCreate(){
        LogUtils.e("---BindService--create");
        myBinder=myBinder==null?new MyBinder(BIND_FAG):myBinder;
        myConnection=new MyConnection();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        LogUtils.e("---BindService---start");
        this.context=BindService.this.getApplicationContext();
        boolean isbind=this.getApplicationContext().bindService(new Intent(BindService.this.getApplicationContext(),LockService.class),myConnection,Service.BIND_IMPORTANT);
        LogUtils.e("---bind--LockService:"+isbind);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        LogUtils.e("---BindService---destroy");
        super.onDestroy();
    }
    class MyConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder){
            LogUtils.i("---BinService---connect---success");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName){
            LogUtils.i("---BinService---connect---faild");
            LogUtils.i("context:"+ HomeApplicaiton.context);
            LockService.isForce=true;
                startIntent=new Intent();
                startIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                startIntent.setAction("android.forcestop.action");
                HomeApplicaiton.context.sendBroadcast(startIntent);
        }
    }

}
