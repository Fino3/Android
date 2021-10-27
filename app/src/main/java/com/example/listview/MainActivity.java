package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

    }

    private void initUI() {
        ImmersionBar.with(this).statusBarColor(R.color.transparent)
                .fitsSystemWindows(true).init();
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                Intent intent1=new Intent();
                intent1.setClass(getApplicationContext(),Newsactivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_register:
                Intent intent2=new Intent();
                intent2.setClass(getApplicationContext(),registeractivity.class);
                startActivity(intent2);
                break;
        }
    }
}