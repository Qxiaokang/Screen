package com.example.lockscreen.service;

import android.os.RemoteException;

import com.example.lockscreen.IMyAidlInterface;

/**
 * Created by Admin on 2017/6/12.
 */
public class MyBinder extends IMyAidlInterface.Stub{
    private int flag;

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
