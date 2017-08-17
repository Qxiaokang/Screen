package com.example.lockscreen.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.example.lockscreen.R;
import com.example.lockscreen.ui.MainApplication;
import com.example.lockscreen.utils.SystemUtil;

public class FistActivity extends AppCompatActivity implements View.OnClickListener{
    private Button oneTime,everTime,close;
    private SystemUtil systemUtil;
    private TranslateAnimation t1,t2,t3;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fist);
        init();
    }

    private void init(){
        oneTime= (Button) findViewById(R.id.bt_one);
        everTime= (Button) findViewById(R.id.bt_ever);
        close= (Button) findViewById(R.id.bt_close);
        systemUtil=SystemUtil.getInstance(getApplicationContext());
        t1=new TranslateAnimation((HomeApplicaiton.screenW-oneTime.getWidth()/2)*-1,0,0,0);
        t1.setDuration(2000);
        t2=new TranslateAnimation(HomeApplicaiton.screenW,0,0,0);
        t2.setDuration(3000);
        t3=new TranslateAnimation((HomeApplicaiton.screenW-close.getWidth()/2)*-1,0,0,0);
        t3.setDuration(4000);
        oneTime.startAnimation(t1);
        everTime.startAnimation(t2);
        close.startAnimation(t3);
        oneTime.setOnClickListener(this);
        everTime.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    // hide   StateBar
    private void hideStateBar(){
        View decorView = getWindow().getDecorView();
        int option=View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setVisibility(option);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.bt_one:
                systemUtil.getSp().edit().putInt("LiveWallpaper",1).commit();
                systemUtil.showToast("设置成功，设置壁纸后生效！");
                break;
            case R.id.bt_ever:
                systemUtil.getSp().edit().putInt("LiveWallpaper",999).commit();
                systemUtil.showToast("设置成功，设置壁纸后生效！");
                break;
            case R.id.bt_close:
                break;
            default:

                break;
        }
        MainApplication.getInstance().finishAllActivity();
        finish();
    }

    class animThread extends Thread{
        @Override
        public void run(){
            super.run();
            oneTime.startAnimation(t1);
            SystemClock.sleep(2000);
            everTime.startAnimation(t2);
            SystemClock.sleep(2000);
            close.startAnimation(t3);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        MainApplication.getInstance().finishAllActivity();
        finish();
    }
}
