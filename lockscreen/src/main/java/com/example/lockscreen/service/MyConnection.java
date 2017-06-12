package com.example.lockscreen.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/6/12.
 */
public class MyConnection implements ServiceConnection{
    private Context context;
    private int flag;
    private MyBinder myBinder;
    public MyConnection(Context context, int flag,MyBinder myBinder){
        this.context = context;
        this.flag = flag;
        this.myBinder=myBinder;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder){
        LogUtils.e("---service---connect---success---flagInt:"+flag);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName){
        LogUtils.e("---service---conncect---faild---flagInt:"+flag);
        switch(flag){
            case BindService.BIND_FAG:
                myBinder.getService().startService(new Intent(myBinder.getService(),BindService.class));
                myBinder.getService().bindService(new Intent(myBinder.getService(),BindService.class),MyConnection.this,Context.BIND_AUTO_CREATE);
                break;
            case LockService.LOCK_FLAG:
                LogUtils.d("---context:"+context+"   mybinder:"+myBinder);
                ComponentName name=myBinder.getBindService().startService(new Intent(myBinder.getBindService(),com.example.lockscreen.service.LockService.class));
                LogUtils.d("---componentName:"+name.getClassName());
                myBinder.getBindService().bindService(new Intent(myBinder.getBindService(),com.example.lockscreen.service.LockService.class),this,Context.BIND_AUTO_CREATE);
                break;
            default:
                break;
        }
    }
}
