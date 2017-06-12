package com.example.lockscreen.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lockscreen.R;
import com.example.lockscreen.ui.MainApplication;
import com.example.lockscreen.utils.LogUtils;
import com.example.lockscreen.utils.SystemUtil;
import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.PasswordType;

/**
 * 透明的activity
 */

public class LucencyActivity extends Activity implements GridPasswordView.OnPasswordChangedListener, View.OnClickListener{
    private int flag = -1;
    private Intent intent = null;
    private GridPasswordView gridPasswordView;
    private String pwdString;
    private TextView setView,tv_cancle;
    private Button enterbt;
    private EditText etpwd, newpwd1, newpwd2;
    private Dialog pwdDialog;
    private Button btSubmit, btCancel;
    private SystemUtil systemUtil;
    private TextView tv_message;
    private View inflateview;
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);//锁屏时显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);//去除系统锁屏界面窗口
        getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,FLAG_HOMEKEY_DISPATCHED);
        setContentView(R.layout.activity_lucency);
        MainApplication.getInstance().addActivity(this);
        initViews();
    }

    private void initViews(){
        gridPasswordView = (GridPasswordView) findViewById(R.id.pwdv_id);
        gridPasswordView.setOnPasswordChangedListener(this);
        gridPasswordView.setPasswordType(PasswordType.TEXT);
        setView = (TextView) findViewById(R.id.text_set);
        tv_cancle= (TextView) findViewById(R.id.text_cancle);
        tv_cancle.setOnClickListener(this);
        setView.setOnClickListener(this);
        enterbt = (Button) findViewById(R.id.enter_id);
        enterbt.setOnClickListener(this);
        systemUtil = SystemUtil.getInstance(getApplicationContext());
    }

    private void initDialog(){
        String digts="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.+,;!-=__~`':<>。*&%￥#@$^)(（）_/?？[]{}|\\“”、";
        inflateview = View.inflate(this, R.layout.setting_window, null);
        pwdDialog = new Dialog(this, android.R.style.Theme_Dialog);
        pwdDialog.setContentView(inflateview);
        etpwd = (EditText) inflateview.findViewById(R.id.yuan_et);
        newpwd1 = (EditText) inflateview.findViewById(R.id.new_et1);
        newpwd2 = (EditText) inflateview.findViewById(R.id.new_et2);
        etpwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        newpwd1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        newpwd2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        etpwd.setKeyListener(DigitsKeyListener.getInstance(digts));
        newpwd1.setKeyListener(DigitsKeyListener.getInstance(digts));
        newpwd2.setKeyListener(DigitsKeyListener.getInstance(digts));
        btSubmit = (Button) inflateview.findViewById(R.id.bt_yes);
        btCancel = (Button) inflateview.findViewById(R.id.bt_no);
        tv_message = (TextView) inflateview.findViewById(R.id.message_tv);
        pwdDialog.setTitle("修改密码");
        pwdDialog.setCancelable(false);
        btSubmit.setOnClickListener(this);
        btCancel.setOnClickListener(this);
    }

    @Override
    public void onTextChanged(String psw){
        this.pwdString=psw;
    }

    @Override
    public void onInputFinish(String psw){
        pwdString = psw;
    }

    @Override
    public void onClick(View view){
        InputMethodManager inputMethodManager1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager1.showSoftInput(view,InputMethodManager.SHOW_FORCED);
        inputMethodManager1.hideSoftInputFromWindow(view.getWindowToken(),0);
        switch(view.getId()){
            case R.id.text_set:
                initDialog();
                if(pwdDialog != null && !pwdDialog.isShowing()){
                    pwdDialog.show();
                }
                break;
            case R.id.enter_id:
                if(pwdString == null || pwdString.length() < 8){
                    systemUtil.showToast("请输入");
                }else if(pwdString.equals(systemUtil.getPwdString())){
                    finish();
                    MainApplication.getInstance().finishAllActivity();
                }else{
                    systemUtil.showToast("错误");
                }
                break;
            case R.id.bt_no:
                LogUtils.i("pwddialog---cancle");
                if(pwdDialog != null && pwdDialog.isShowing()){
                    pwdDialog.dismiss();
                }
//                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.bt_yes:
                LogUtils.i("pwddialog--yes");
                if(etpwd.getText().toString() != null && newpwd1.getText().toString() != null && newpwd2.getText().toString() != null&&!("").equals(etpwd.getText().toString())&&!("").equals(newpwd1.getText().toString())&&!("").equals(newpwd2.getText().toString())){
                    if(etpwd.getText().toString().length() < 8 || newpwd1.getText().toString().length() < 6 || newpwd2.getText().toString().length() < 6){
                        tv_message.setText("输入的密码长度必须为8位");
                    }else{
                        if(!systemUtil.getPwdString().equals(etpwd.getText().toString())){
                            tv_message.setText("输入的密码有误");
                        }else{
                            if(!newpwd1.getText().toString().equals(newpwd2.getText().toString())){
                                tv_message.setText("输入新密码不一致");
                            }else if(newpwd1.getText().toString().equals(systemUtil.getPwdString())){
                                tv_message.setText("新密码与旧密码一致");
                            }else{
                                tv_message.setText("密码修改成功");
                                tv_message.setTextColor(Color.GREEN);
                                systemUtil.setPwdString(newpwd1.getText().toString());
                                pwdDialog.dismiss();
                                systemUtil.showToast("密码修改成功！请使用新密码！");
                            }
                        }
                    }
                }else{
                    tv_message.setText("密码不能为空");
                }
//                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                break;

            case R.id.text_cancle:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy(){
        LogUtils.i("---lucencyActivity-destroy---");
        if(pwdDialog != null && pwdDialog.isShowing()){
            pwdDialog.cancel();
            pwdDialog = null;
        }
        super.onDestroy();
}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        LogUtils.e("keyCode:"+keyCode);
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
        }
        else {
            return false;
        }
    }

    @Override
    protected void onResume(){
        LogUtils.i("---lucencyActivity-resume---");
        super.onResume();
    }

    @Override
    protected void onStop(){
        LogUtils.i("---lucencyActivity-stop---");
        super.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        LogUtils.i("---LucencyActivity---onTouchEvent---");
        return true;
    }
}

