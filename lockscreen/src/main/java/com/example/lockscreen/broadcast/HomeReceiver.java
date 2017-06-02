package com.example.lockscreen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lockscreen.utils.LogUtils;
import com.example.lockscreen.utils.SystemUtil;

/**
 * Created by Admin on 2017/5/8.
 */
public class HomeReceiver extends BroadcastReceiver{
    private static final String LOG_TAG = "HomeReceiver";
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent){
        String action=intent.getAction();
        this.context=context;
        if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
            String reason=intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                // 短按Home键
                LogUtils.i(LOG_TAG+"   homekey"+  "   islock:"+SystemUtil.getInstance(context).isLock());
//                if(SystemUtil.getInstance(context).isLock()){
//                    Intent in=new Intent(context, LockActivity.class);
//                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(in);
//                }
            }
            else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                // 长按Home键 或者 activity切换键
                LogUtils.i(LOG_TAG+"   long press home key or activity switch");

            }
            else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                // 锁屏
                LogUtils.i(LOG_TAG+"   lock");
            }
            else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                // samsung 长按Home键
                LogUtils.i(LOG_TAG+"  assist");
            }
        }
    }


}
