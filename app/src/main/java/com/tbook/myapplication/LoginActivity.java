package com.tbook.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.tbook.Utils.C;
import com.tbook.Utils.NetCom;
import com.tbook.Utils.StatusBarUtil;
import com.tbook.Utils.Tips;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements C {

    private Button loginBut;
    private Button registerBut;
    private EditText accountEdt;
    private EditText passwordEdt;

    private Tips tips;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false,false);

        //初始化组件
        AssemblyInit();

        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //参数put到json串里
                new Thread() {
                    @Override
                    public void run() {
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("account",accountEdt.getText().toString());
                            jsonObject.put("password",passwordEdt.getText().toString());
                            tips = new NetCom("basic/login", String.valueOf(jsonObject),null).CommunicationFun();
                            if (CODE_LOGIN_SUCCESS.equals(tips.getCode())){ // code ==> 510 Login success
                                SharedPreferences preferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token",tips.getInfo());
                                editor.apply();
                                Intent intent  = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("flash","true");
                                startActivityForResult(intent,1);
                            }
                            else if (CODE_LOGIN_FALSE.equals(tips.getCode())){
                                Looper.prepare();
                                Toast toast=Toast.makeText(LoginActivity.this,tips.getInfo(),Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Looper.loop();
                            }
                        } catch (JSONException e) {
                            System.out.println(e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AssemblyInit(){
        loginBut = findViewById(R.id.loginBut);
        registerBut = findViewById(R.id.registerBut);
        accountEdt = findViewById(R.id.accountEdt);
        passwordEdt = findViewById(R.id.passwordEtd);
    }


}
