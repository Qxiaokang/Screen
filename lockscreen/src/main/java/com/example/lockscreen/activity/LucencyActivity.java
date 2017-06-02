package com.example.lockscreen.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lockscreen.R;
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
    private TextView setView;
    private Button enterbt;
    private EditText etpwd, newpwd1, newpwd2;
    private Dialog pwdDialog;
    private Button btSubmit, btCancel;
    private SystemUtil systemUtil;
    private TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucency);
        initViews();
        initDialog();
    }

    private void initViews(){
        gridPasswordView = (GridPasswordView) findViewById(R.id.pwdv_id);
        gridPasswordView.setPasswordType(PasswordType.TEXT);
        gridPasswordView.setOnPasswordChangedListener(this);
        setView = (TextView) findViewById(R.id.text_set);
        setView.setOnClickListener(this);
        enterbt = (Button) findViewById(R.id.enter_id);
        enterbt.setOnClickListener(this);
        systemUtil = SystemUtil.getInstance(getApplicationContext());
    }

    private void initDialog(){
        View vi = View.inflate(this, R.layout.setting_window, null);
        pwdDialog = new Dialog(this, android.R.style.Theme_Dialog);
        pwdDialog.setContentView(vi);
        etpwd = (EditText) vi.findViewById(R.id.yuan_et);
        newpwd1 = (EditText) vi.findViewById(R.id.new_et1);
        newpwd2 = (EditText) vi.findViewById(R.id.new_et2);
        etpwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        newpwd1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        newpwd2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        btSubmit = (Button) vi.findViewById(R.id.bt_yes);
        btCancel = (Button) vi.findViewById(R.id.bt_no);
        tv_message = (TextView) vi.findViewById(R.id.message_tv);
        pwdDialog.setTitle("修改密码");
        pwdDialog.setCancelable(false);
        btSubmit.setOnClickListener(this);
        btCancel.setOnClickListener(this);
    }

    @Override
    public void onTextChanged(String psw){
    }

    @Override
    public void onInputFinish(String psw){
        pwdString = psw;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.text_set:
                if(pwdDialog != null && !pwdDialog.isShowing()){
                    pwdDialog.show();
                }
                break;
            case R.id.enter_id:
                if(pwdString == null || pwdString.length() < 6){
                    systemUtil.showToast("请输入");
                }else if(pwdString.equals(systemUtil.getPwdString())){
                    finish();
                }else{
                    systemUtil.showToast("错误");
                }
                break;
            case R.id.bt_no:
                LogUtils.i("pwddialog---cancle");
                if(pwdDialog != null && pwdDialog.isShowing()){
                    pwdDialog.dismiss();
                }
                break;
            case R.id.bt_yes:
                LogUtils.i("pwddialog--yes");
                if(etpwd.getText().toString() != null && newpwd1.getText().toString() != null && newpwd2.getText().toString() != null&&!("").equals(etpwd.getText().toString())&&!("").equals(newpwd1.getText().toString())&&!("").equals(newpwd2.getText().toString())){
                    if(etpwd.getText().toString().length() < 6 || newpwd1.getText().toString().length() < 6 || newpwd2.getText().toString().length() < 6){
                        tv_message.setText("输入的密码长度必须为6位");
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
                            }
                        }
                    }
                }else{
                    tv_message.setText("密码不能为空");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy(){
        if(pwdDialog != null && pwdDialog.isShowing()){
            pwdDialog.cancel();
            pwdDialog = null;
        }
        super.onDestroy();
    }
}

