package com.example.listview;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mainfaceactivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    protected static  Bitmap bitmap;
    byte[] result;//将bitmap转化的byte数组
    protected static String ite;
    protected static String sub;

    public  class Data implements Serializable{
        private static final long serialVersionUID =1L;
        public Bitmap bitmap1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_face);
        Toast.makeText(Mainfaceactivity.this,ite+"++++",Toast.LENGTH_SHORT).show();
        /*Intent intent=new Intent();
        intent.setClass(Mainfaceactivity.this,TakephotoActivity.class);
        Bundle bundle=new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent,5);

        Toast.makeText(Mainfaceactivity.this,ite+"++++",Toast.LENGTH_SHORT).show();*/
        bottomNavigationView =findViewById(R.id.bottomNav);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new pho2fragment()).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 步骤1：获取FragmentManager

                Fragment fragment=null;
                switch (item.getItemId()){
                    case R.id.xinwen:
                        fragment=new NewsFragment();


                        Bundle bundle=new Bundle();

                        //ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
                        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
                        //result = output.toByteArray();//转换成功了  result就是一个bit的资源数组
                        bundle.putParcelable("图片",bitmap);
                        bundle.putString("标题",ite);
                        bundle.putString("内容",sub);
                        Date date = new Date();
                        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = format.format(date);
                        bundle.putString("时间",str);
                        fragment.setArguments(bundle);

                        break;
                    case R.id.mface:
                        fragment=new Mainfacefragment();
                        break;
                    case R.id.pho1:
                        fragment=new pho1fragment();
                        break;
                    case R.id.pho2:
                        fragment=new pho2fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                return true;

            }
        });
    }

    //@Override
    /*protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5) {
            Bundle bundle=data.getExtras();
            bitmap=bundle.getParcelable("图片");
            ite=bundle.getString("title");
            sub=bundle.getString("内容");
        }
    }*/
}
