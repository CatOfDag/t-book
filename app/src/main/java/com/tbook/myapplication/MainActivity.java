package com.tbook.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbook.Utils.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements C {

    private Intent intent = new Intent();
    private Tips tips;
    private Context context = this;
    private Gson gson = new Gson();
    private RelativeLayout show_status_view;
    private NetCom netCom;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show_status_view = findViewById(R.id.show_status_view);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false,false);

        SharedPreferences sdp = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        token = sdp.getString("token","null");
        netCom = new NetCom("get/data",null,token);
        new Thread(){
            @Override
            public void run(){
                show_status_view.setVisibility(View.VISIBLE);
                tips = netCom.CommunicationFun();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show_status_view.setVisibility(View.GONE);
                        if(CODE_GET_DATA_SUCCESS.equals(tips.getCode())){ //code ==> 201 get Data success
                            findViewById(R.id.net_404).setVisibility(View.GONE);
                            findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                            initView();
                        } else if (CODE_NET_CONTACT_FALSE.equals(tips.getCode())){
                            findViewById(R.id.net_404).setVisibility(View.VISIBLE);
                            findViewById(R.id.scrollView).setVisibility(View.GONE);
                        } else if(CODE_VERIFICATION_FALSE.equals(tips.getCode())){//code ==> 505 token验证失败
                            intent = new Intent(context,LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        }.start();

        findViewById(R.id.search_igView).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1) {
            this.onCreate(null);
        }
    }

    /**
     * 双击返回键退出
     */
    private boolean isExit;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                this.finish();

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit= false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private List<String> connectNames = new ArrayList<>();
    private List<String> connectAddress = new ArrayList<>();
    private List<String> connectDuty = new ArrayList<>();
    private List<String> connectDepart = new ArrayList<>();
    private List<List<String>> connectTelephone = new ArrayList<>();
    private LinearLayout item_liner;

    public void initView() {
        List<Contacts> contactsList = gson.fromJson(tips.getInfo(), new TypeToken<List<Contacts>>(){}.getType());
        for (Contacts contact : contactsList) {
            connectNames.add(contact.getName());
            connectDepart.add(contact.getDepartment());
            connectAddress.add(contact.getOfficeadress());
            connectTelephone.add(contact.getTelephonenum());
            connectDuty.add(contact.getDuty());
            System.out.println(contact.toString());
        }
        //要添加view的容器
        item_liner = findViewById(R.id.scroll_linear);
        addView();
    }

    /**
     * 动态添加的具体实现
     */
    private void addView() {
        //ivList集合有几个元素就添加几个
        for (int i = 0; i < connectNames.size(); i++) {
            //首先引入要添加的View
            View body_view = View.inflate(this, R.layout.subgroup_item_list, null);

            //找到里面需要动态改变的控件
            TextView name= body_view.findViewById(R.id.item_name);
            //给控件赋值
            name.setText(connectNames.get(i));
            final int finalI = i;//由于OnClick里面拿不到i,所以需要重新赋值给一个final对象
            body_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,ContactActivity.class);
                    int telephone_n = 0;
                    intent.putExtra("name", connectNames.get(finalI));
                    switch (connectTelephone.get(finalI).size()){
                        case 2:intent.putExtra("telephone_2",connectTelephone.get(finalI).get(1));
                                intent.putExtra("telephone_n",++telephone_n);
                        case 1:intent.putExtra("telephone_1",connectTelephone.get(finalI).get(0));
                                intent.putExtra("telephone_n",++telephone_n);
                    }
                    intent.putExtra("depart",connectDepart.get(finalI));
                    intent.putExtra("address",connectAddress.get(finalI));
                    intent.putExtra("duty",connectDuty.get(finalI));
                    startActivity(intent);
                }
            });
            //把所有动态创建的view都添加到容器里面
            item_liner.addView(body_view);
        }

    }
}
