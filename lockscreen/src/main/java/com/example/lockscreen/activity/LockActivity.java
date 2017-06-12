package com.example.lockscreen.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.example.lockscreen.R;
import com.example.lockscreen.service.BindService;
import com.example.lockscreen.service.LockService;
import com.example.lockscreen.ui.MainApplication;
import com.example.lockscreen.utils.LogUtils;
import com.example.lockscreen.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class LockActivity extends AppCompatActivity{
    private SystemUtil systemUtil;
    private TextView tv;
    public static  final  int SELF_FLAG=0;
    public static final  int OTHER_FLAG=1;
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
    private LottieAnimationView animationView;
    private int isFouseInt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        LogUtils.d("---LockActivity创建");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);//锁屏时显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);//去除系统锁屏界面窗口
        getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,FLAG_HOMEKEY_DISPATCHED);
        setContentView(R.layout.activity_lock);
        MainApplication.getInstance().addActivity(this);
        systemUtil=SystemUtil.getInstance(getApplicationContext());
        init();
    }
    private void init(){
        int si=systemUtil.getSp().getInt("isfirst",0);
        if(si==0){
            LogUtils.i("---si==0");
            Intent intent=new Intent(this, LockService.class);
            startService(intent);
            startService(new Intent(this, BindService.class));
        }
        animationView= (LottieAnimationView) findViewById(R.id.ani_lock);
        animationView.setImageAssetsFolder("Images");
        playByNme(this,"TwitterHeart.json",animationView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        LogUtils.d("keCode:"+keyCode);
        if(keyCode==KeyEvent.KEYCODE_HOME){
            LogUtils.i("keyCode==event.KEYCODE_HOME");
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_BACK){
            LogUtils.i("keyCode==event.KEYCODE_BACK");
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_MENU){
            LogUtils.i("keyCode==event.KEYCODE_MENU");
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onStop(){
        LogUtils.d("---LockActivity---stop");
        isFouseInt=1;
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        LogUtils.d("---LockActivity---destroy");
        if(isFouseInt!=1){
            LogUtils.e("---ifFouseInt:"+isFouseInt);
            Intent intent=new Intent(this, LockService.class);
            startService(intent);
            startService(new Intent(this, BindService.class));
        }
        if(animationView!=null){
            animationView.cancelAnimation();
        }
        systemUtil.setIsLock(false);
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        LogUtils.d("---LockActivity---Resume");
        systemUtil.setIsLock(true);
        super.onResume();
    }
    public void startActivity(){
        List<String> pkgName=new ArrayList<>();
        List<String> actName=new ArrayList<>();
        Intent mainIntent=new Intent(Intent.ACTION_SEND,null);
        mainIntent.setType("text/plain");
        List<ResolveInfo> resolveInfos = getApplicationContext().getPackageManager().queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        LogUtils.d("resolveInfos.size:"+resolveInfos.size());
        for(int i = 0; i < resolveInfos.size(); i++){
            String name=resolveInfos.get(i).activityInfo.packageName;
            LogUtils.i("name:"+name);
            if(!name.equals(this.getPackageName())){//排除自己
                pkgName.add(name);
                name=resolveInfos.get(i).activityInfo.name;
                actName.add(name);
            }
        }
        LogUtils.d("pkgName.size:"+pkgName.size()+"   actName.size:"+actName.size());
        if(pkgName.size()>0&&actName.size()>0){
            LogUtils.d("pkgname.0:"+pkgName.get(0).toString());
            ComponentName componentName=new ComponentName(pkgName.get(0),actName.get(0));
            Intent intent=new Intent();
            intent.setComponent(componentName);
            intent.putExtra("intentflag",LockActivity.OTHER_FLAG);
            this.startActivity(intent);
        }else {
//            Intent intent=new Intent(this,LucencyActivity.class);
//            intent.putExtra("intentflag", LockActivity.SELF_FLAG);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.startActivity(intent);
        }
    }

    private void playByNme(Context context , String jsonName, final LottieAnimationView lav){
        LottieComposition.Factory.fromAssetFileName(context, jsonName, new OnCompositionLoadedListener(){
            @Override
            public void onCompositionLoaded(LottieComposition composition){
                lav.setComposition(composition);
                lav.resumeAnimation();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_UP){
            Intent intent=new Intent(this,LucencyActivity.class);
            startActivity(intent);
            LogUtils.i("---LockActivityOntouch---up");
            return true;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent){
        LogUtils.e("---Lockactivity---onNemIntent");
        super.onNewIntent(intent);
    }

    @Override
    protected void onRestart(){
        LogUtils.e("---LockActivity---onRestart---");
        super.onRestart();
    }
}

