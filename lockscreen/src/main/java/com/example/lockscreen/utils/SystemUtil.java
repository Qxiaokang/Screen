package com.example.lockscreen.utils;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by Admin on 2017/5/9.
 *
 */
public class SystemUtil{
    private Context context;
    private static SystemUtil systemUtil;
    private  boolean isLock=false;
    public static  String shareDB="lockdb";
    public   static SharedPreferences sp;
    private String pwdString="201706";
    private Toast toast;
    public String getPwdString(){
        return pwdString;
    }

    public void setPwdString(String pwdString){
        this.pwdString = pwdString;
    }

    public  boolean isLock(){
        return isLock;
    }

    public  void setIsLock(boolean isLock){
        this.isLock = isLock;
    }


    private SystemUtil(Context context){
        this.context=context;
    }
    public static synchronized SystemUtil getInstance(Context context){
        if(systemUtil==null){
            systemUtil=new SystemUtil(context);
            sp=context.getSharedPreferences(shareDB,Context.MODE_PRIVATE);
        }
        return systemUtil;
    }

    //关闭系统锁
    public void closeSystemLock(){
        boolean systemLockOpen=false;
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        systemLockOpen=(km.inKeyguardRestrictedInputMode()?true:false);
        LogUtils.d("systemlockopen:"+systemLockOpen);
        if(systemLockOpen){
            KeyguardManager.KeyguardLock keyLock=km.newKeyguardLock("");
            keyLock.disableKeyguard();
        }
    }

    //判断系统锁屏是否开启
    public boolean getSystemLock(){
        boolean systemLockOpen=false;
        //api<=15
        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            try {
                Class<?> clazz = Class.forName("com.android.internal.widget.LockPatternUtils");
                Constructor<?> constructor = clazz.getConstructor(android.content.Context.class);
                constructor.setAccessible(true);
                Object utils = constructor.newInstance(context);
                Method method = clazz.getMethod("isSecure");
                systemLockOpen= (Boolean) method.invoke(utils);
                LogUtils.d("systemlockopen15:"+systemLockOpen);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        systemLockOpen=(km.inKeyguardRestrictedInputMode()?true:false);
        LogUtils.d("systemlockopen:"+systemLockOpen);
        return systemLockOpen;
    }

    //注册广播
    public void registerReceiver(String action, BroadcastReceiver broadcastReceiver){
        IntentFilter intentFilter=new IntentFilter(action);
        context.registerReceiver(broadcastReceiver,intentFilter);
    }
    //关闭系统锁屏
    public void closeLock(){
        KeyguardManager mKeyguardManager = (KeyguardManager)context.getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");
        mKeyguardLock.disableKeyguard();
        LogUtils.d("关闭系统锁屏。。");
    }

    //
    public void showToast(String text){
        if(toast==null){
            toast= Toast.makeText(context,text,Toast.LENGTH_LONG);
        }else {
            toast.setText(text);
        }
        toast.show();
    }
}
