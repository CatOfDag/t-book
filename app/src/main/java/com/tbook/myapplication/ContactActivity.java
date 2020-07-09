package com.tbook.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.tbook.Utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private int telephone_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false,false);

        Intent intent = this.getIntent();
        telephone_n = intent.getIntExtra("telephone_n", 0);
        TextView contact_name = findViewById(R.id.contact_name);
        contact_name.setText(intent.getStringExtra("name"));
        switch (telephone_n) {
            case 2:
                list.add(intent.getStringExtra("telephone_2"));
            case 1:
                list.add(intent.getStringExtra("telephone_1"));
        }
        list.add(intent.getStringExtra("depart"));
        list.add(intent.getStringExtra("duty"));
        list.add(intent.getStringExtra("address"));
        new Thread() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addTelephone_TextView();
                    }
                });
            }
        }.start();

        findViewById(R.id.contact_back).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {/*自定义返回按钮*/
                Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.contact_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void addTelephone_TextView() {
        String[] image_resource = {"电话","部门", "职务", "地址"};
        final int[] ids = {R.id.contect_view_1, R.id.contect_view_2, R.id.contect_view_3,
                R.id.contect_view_4, R.id.contect_view_5, R.id.contect_view_6, R.id.contect_view_7};
        LinearLayout contact_body_liner = findViewById(R.id.contact_body);
        for (int i = 0; i < list.size(); i++) {
            View body_view = View.inflate(this, R.layout.subgroup_contact_detail_item, null);
            TextView imageView = body_view.findViewById(R.id.contact_image);
            TextView textView = body_view.findViewById(R.id.contact_text);

            if (i + 3 < list.size()) {
                imageView.setText(image_resource[0]);
                textView.setId(ids[i]);
            } else {
                imageView.setText(image_resource[i - telephone_n + 1]);
            }
            textView.setText(list.get(i));

            final int finalI = i;//由于OnClick里面拿不到i,所以需要重新赋值给一个final对象
            body_view.setOnClickListener(new View.OnClickListener() {

                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.
                            permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this,
                                Manifest.permission.CALL_PHONE)) {
                            // 调用ActivityCompat.requestPermissions() 方法，向用户申请授权
                            Toast.makeText(ContactActivity.this, "请授权！", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        } else {
                            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        }
                    } else {
                        // 已经授权，执行
                        if(finalI < telephone_n){
                            TextView textView1 = findViewById(ids[finalI]);
                            String telephone = (String) textView1.getText();
                            if(telephone != null){
                                try {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + telephone);
                                    intent.setData(data);
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e){
                                    Toast toast=Toast.makeText(ContactActivity.this,"不能访问电话服务！",Toast.LENGTH_SHORT    );
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }
                        }
                    }
                }
            });
            contact_body_liner.addView(body_view);
        }
    }
}
