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
    private Context context1=null;
    private Context context2=null;
    private int flag;
    private MyBinder myBinder;
    public MyConnection(Context context, int flag,MyBinder myBinder){
        this.flag = flag;
        this.myBinder=myBinder;
        LogUtils.i("---flag:"+flag);
        if(flag==LockService.LOCK_FLAG){
            this.context1=context;
        }
        if(flag==BindService.BIND_FAG){
            this.context2=context;
        }
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
                break;
            case LockService.LOCK_FLAG:
                LogUtils.d("---context1:"+context1+"  context2:"+context2+"   mybinder:"+myBinder);
                ComponentName name=context2.startService(new Intent(context2,LockService.class));
                LogUtils.d("---componentName:"+name.getClassName());
                context2.bindService(new Intent(context2,LockService.class),this,Context.BIND_AUTO_CREATE);
                break;
            default:
                break;
        }
    }
}
