package com.example.lockscreen.broadcast;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;

import com.example.lockscreen.utils.LogUtils;

/**
 * Created by Admin on 2017/6/7.
 */
public class MydeviceReceiver extends DeviceAdminReceiver{
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent){
       /* Intent intent1=new Intent(context, LockActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
        context.stopService(intent);
        return "";*/
        LogUtils.d("---deviceReceiver---"+intent.getAction());
        //跳离当前询问是否取消激活的 dialog
        Intent outOfDialog = context.getPackageManager().getLaunchIntentForPackage("com.android.settings");
        outOfDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(outOfDialog);

        //调用设备管理器本身的功能，每 100ms 锁屏一次，用户即便解锁也会立即被锁，直至 7s 后
        final DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        dpm.lockNow();
        //dpm.resetPassword("123456",0);
        //dpm.wipeData(0);//恢复出厂设置
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 70) {
                    dpm.lockNow();
                    try {
                        Thread.sleep(100);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return "";
    }

    @Override
    public void onEnabled(Context context, Intent intent){
        super.onEnabled(context, intent);
        LogUtils.e("已注册");
    }

    @Override
    public void onDisabled(Context context, Intent intent){
        super.onDisabled(context, intent);
        LogUtils.e("已注销");
    }
}
