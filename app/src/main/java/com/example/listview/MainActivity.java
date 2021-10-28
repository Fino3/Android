package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;


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
                        if(cb_remeberpwd.isChecked()){
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
                        startActivity(intent1);

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

}