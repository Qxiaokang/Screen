package com.example.lockscreen.service;

import android.app.Service;
import android.os.RemoteException;

import com.example.lockscreen.IMyAidlInterface;

/**
 * Created by Admin on 2017/6/12.
 */
public class MyBinder extends IMyAidlInterface.Stub{
    private int flag;
    private Service service=null;
    private Service bindService=null;

    public Service getBindService(){
        return bindService;
    }

    public void setBindService(Service bindService){
        this.bindService = bindService;
    }

    public Service getService(){
        return service;
    }

    public void setService(Service service){
        this.service = service;
    }

    public MyBinder(int flag){
        this.flag=flag;
    }

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException{
    }

    @Override
    public String getName() throws RemoteException{
        return "name:"+flag;
    }

}
