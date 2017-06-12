package com.example.lockscreen.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/6/12.
 */
public class BindService extends Service{
    public static final int BIND_FAG=2;
    private MyConnection myConnection;
    private MyBinder myBinder;
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
        myBinder.setBindService(BindService.this);
        myConnection=new MyConnection(this,BIND_FAG,myBinder);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        LogUtils.e("---BindService---start");
        boolean isbind=this.bindService(new Intent(BindService.this,LockService.class),myConnection,Service.BIND_AUTO_CREATE);

        LogUtils.e("---bind--LockService:"+isbind);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        LogUtils.e("---BindService---destroy");
        super.onDestroy();
    }

}
