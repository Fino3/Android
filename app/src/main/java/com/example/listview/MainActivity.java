package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity  {
    SharedPreferences sp;
    private EditText et_name;
    private EditText et_pwd;
    private CheckBox cb_remeberpwd;
    private CheckBox cb_autologin;
    private Button bt_login;
    private Button bt_register;
    private String rpd="remeberpwd";
    private String alg="autologin";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        sp=getSharedPreferences("config", Context.MODE_PRIVATE);
        initUI();
        boolean remeberpwd=sp.getBoolean(rpd,false);
        boolean autologin=sp.getBoolean(alg,false);
        if(remeberpwd){
            String name=sp.getString("name",null);
            String pwd=sp.getString("pwd",null);
            et_name.setText(name);
            et_pwd.setText(pwd);
            cb_remeberpwd.setChecked(true);
        }
        if(autologin){
            cb_autologin.setChecked(true);
            Toast.makeText(MainActivity.this,"用户名",Toast.LENGTH_SHORT).show();
            Intent intent1=new Intent();
            intent1.setClass(getApplicationContext(), Mainfaceactivity.class);
            startActivity(intent1);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void initUI() {
        ImmersionBar.with(this).statusBarColor(R.color.transparent)
                .fitsSystemWindows(true).init();
        et_name=findViewById(R.id.ett_account);
        et_pwd=findViewById(R.id.ett_password);
        cb_remeberpwd=findViewById(R.id.cb_remeberpwd);
        cb_autologin=findViewById(R.id.cb_autologin);
        bt_register=findViewById(R.id.btn_register);
        bt_login=findViewById(R.id.btn_login);

        MyOnClickListener l=new MyOnClickListener();
        bt_login.setOnClickListener(l);
        bt_register.setOnClickListener(l);

    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_login:
                    String name=et_name.getText().toString().trim();
                    String pwd=et_pwd.getText().toString().trim();
                    if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)){
                        Toast.makeText(MainActivity.this,"用户名或密码为空",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            String path = "http://49.235.134.191:8080/user/login?account="+name+"&password="+pwd;
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int responseCode = conn.getResponseCode();
                            if (responseCode == 200) {
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
                                Log.v("debug",result.toString());
                                if (result.getCode()==200) {
                                    if (cb_remeberpwd.isChecked()) {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("name", name);
                                        editor.putString("pwd", pwd);
                                        editor.putBoolean(rpd, true);
                                        editor.apply();
                                    }
                                    if (cb_autologin.isChecked()) {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putBoolean(alg, true);
                                        editor.apply();
                                    }
                                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent();
                                    intent1.setClass(getApplicationContext(), Mainfaceactivity.class);
                                    startActivity(intent1);
                                    conn.disconnect();
                                }
                                else if (result.getCode()==400) {
                                    Toast.makeText(MainActivity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        /*if(cb_remeberpwd.isChecked()){
                            SharedPreferences.Editor editor =sp.edit();
                            editor.putString("name",name);
                            editor.putString("pwd",pwd);
                            editor.putBoolean(rpd,true);
                            editor.apply();
                        }
                        if(cb_autologin.isChecked()){
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putBoolean(alg,true);
                            editor.apply();

                        }
                        Intent intent1=new Intent();
                        intent1.setClass(getApplicationContext(), Mainfaceactivity.class);
                        startActivity(intent1);*/

                    }
                    break;
                case R.id.btn_register:
                    Intent intent2=new Intent();
                    intent2.setClass(getApplicationContext(),registeractivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    }
    private void requestPermissions() {
        int write=checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read=checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera=checkSelfPermission(Manifest.permission.CAMERA);
        if (write!= PackageManager.PERMISSION_GRANTED||camera!=PackageManager.PERMISSION_GRANTED||
                read!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},300);
        }
    }

}