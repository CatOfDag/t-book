package com.tbook.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.tbook.Utils.*;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private Intent intent;
    private EditText register_telephone;
    private EditText register_password;
    private EditText register_password_once_again;
    private EditText register_email;
    private TextView verificationCodeBtn;
    private NetCom netCom;
    private Tips tips;
    private Toast toast;
    private Context context = this;
    private String vcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false,false);
        initView();

        findViewById(R.id.register_back_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.register_confirm_register).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String text_telephone = register_telephone.getText().toString();
                final String text_password = register_password.getText().toString();
                String text_password_again = register_password_once_again.getText().toString();
                final String text_email = register_email.getText().toString();

                if(!TNV.tnv(text_telephone)){
                    toastController("电话号码输入错误");
                } else if (text_password != null && !text_password.equals(text_password_again)) {
                    toastController("两次密码不一致");
                } else if (vcode != null && !vcode.equals(user_enter_code)){
                    toastController("验证码输入错误");
                } else {
                    new Thread(){
                        @Override
                        public void run() {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("account",text_telephone);
                                jsonObject.put("password",text_password);
                                jsonObject.put("email",text_email);
                                netCom = new NetCom("basic/register",String.valueOf(jsonObject),null);
                                tips = netCom.CommunicationFun();
                                if (C.CODE_Register_SUCCESS.equals(tips.getCode())){
                                    toastController("注册成功");
                                } else if (C.CODE_Register_FALSE.equals(tips.getCode())){
                                    toastController("注册失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } ;
                        }
                    }.start();
                }
            }
        });

        verificationCodeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                verificationCodeEditView();
                new Thread(){
                    @Override
                    public void run(){
                        JSONObject jsonObject = new JSONObject();
                        String telephone = register_telephone.getText().toString();
                        if (TNV.tnv(telephone)){
                            try {
                                jsonObject.put("telephone",register_telephone.getText().toString());
                                jsonObject.put("email",register_email.getText().toString());
                                netCom = new NetCom("basic/verification_code",String.valueOf(jsonObject),null);
                                tips = netCom.CommunicationFun();
                                vcode = tips.getInfo();
                                if (C.CODE_MAIL_SUCCESS.equals(tips.getCode())){
                                    toastController("邮件发送成功，请查收");
                                } else if (C.CODE_MAIL_FALSE.equals(tips.getCode())){
                                    toastController(tips.getInfo());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            toastController("电话号码输入错误");
                        }
                    }
                }.start();
            }
        });
    }

    private String user_enter_code;

    private void verificationCodeEditView(){
        final View view = View.inflate(this,R.layout.alertdialog_editview,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.myDialogTheme)
                .setView(view,0,0,0,0)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText e = view.findViewById(R.id.editText);
                        user_enter_code = e.getText().toString();
                    }
                }) .setNegativeButton("取消",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
        layoutParams.width = 600;
        alertDialog.getWindow().setAttributes(layoutParams);
    }

    private void toastController(final String massage){
        new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        toast = Toast.makeText(context,massage,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }.start();
    }

    private void initView(){
        register_telephone = findViewById(R.id.register_telephone);
        register_password = findViewById(R.id.register_password);
        register_password_once_again = findViewById(R.id.register_password_once_again);
        register_email = findViewById(R.id.register_email);
        verificationCodeBtn = findViewById(R.id.register_get_verification_code);
    }

}
