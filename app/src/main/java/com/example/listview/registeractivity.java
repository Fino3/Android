package com.example.listview;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class registeractivity extends AppCompatActivity implements View.OnClickListener {
    EditText account;
    EditText password;
    EditText samepassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.btn_register1).setOnClickListener(this);
        account=findViewById(R.id.ett_account);
        password=findViewById(R.id.ett_password);
        samepassword=findViewById(R.id.ett_samepassword);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onClick(View view) {
        if (password.getText().toString().equals(samepassword.getText().toString())) {
            try {
                String path="http://49.235.134.191:8080/user/save?account="+account.getText().toString()+"&password="+password.getText().toString();
                URL url=new URL(path);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                if (conn.getResponseCode()==200) {
                    InputStream in = conn.getInputStream();
                    String jsonStr = "";
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    jsonStr = new String(out.toByteArray());
                    Result result = JSONObject.parseObject(jsonStr, Result.class);
                    if (result.getCode()==200) {
                        Toast.makeText(registeractivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        conn.disconnect();
                    }
                    else if (result.getCode()==400) {
                        Toast.makeText(registeractivity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(registeractivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
        }
    }
}
