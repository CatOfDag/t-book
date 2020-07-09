package com.tbook.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbook.Utils.*;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements C {

    private Tips tips;
    private Context context = this;
    private LinearLayout show_linear;
    private List<Contacts> contactsList;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false,true);

        SharedPreferences sdp = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        final String token = sdp.getString("token","null");
        findViewById(R.id.iv_search_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run(){
                        TextView searchTv = findViewById(R.id.et_search_keyword);/*获取搜索关键词*/
                        NetCom netCom = new NetCom("get/search",searchTv.getText().toString(),token);/*上传到服务器端处理*/
                        tips = netCom.CommunicationFun();
                        contactsList = gson.fromJson(tips.getInfo(), new TypeToken<List<Contacts>>(){}.getType());/*解析服务器返回值*/

                        show_linear = findViewById(R.id.search_show_search);

                        if (CODE_GET_DATA_SUCCESS.equals(tips.getCode())){//code ==> 201 data search success
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    show_linear.removeAllViews();/*清除搜索数据展示区的view*/
                                    findViewById(R.id.search_show).setVisibility(View.VISIBLE);/*设置搜索历史隐藏*/
                                    findViewById(R.id.search_history).setVisibility(View.GONE);

                                    for (final Contacts contact : contactsList){/*为搜索数据展示区设置data——item*/
                                        View item = View.inflate(context,R.layout.subgroup_search_history_item,null);
                                        TextView textView = item.findViewById(R.id.tv_item_search_history);
                                        textView.setText(contact.getName());
                                        item.setOnClickListener(new View.OnClickListener() {/*设置条目的点击事件*/
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(context,ContactActivity.class);
                                                int telephone_n = 0;
                                                intent.putExtra("name", contact.getName());
                                                switch (contact.getTelephonenum().size()){
                                                    case 2:intent.putExtra("telephone_2",contact.getTelephonenum().get(1));
                                                        intent.putExtra("telephone_n",++telephone_n);
                                                    case 1:intent.putExtra("telephone_1",contact.getTelephonenum().get(0));
                                                        intent.putExtra("telephone_n",++telephone_n);
                                                }
                                                intent.putExtra("depart",contact.getDepartment());
                                                intent.putExtra("address",contact.getOfficeadress());
                                                intent.putExtra("duty",contact.getDuty());
                                                startActivity(intent);
                                            }
                                        });
                                        show_linear.addView(item);
                                    }
                                }
                            });
                        } else if (CODE_SEARCH_DATA_FALSE.equals(tips.getCode())){

                        }
                    }
                }.start();
            }
        });

        findViewById(R.id.search_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
