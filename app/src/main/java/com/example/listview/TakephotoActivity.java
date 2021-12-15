package com.example.listview;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TakephotoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton ima1;
    Button btnuplod;
    public final File SDPATH = Environment.getExternalStorageDirectory() ;
    EditText item;
    EditText sub;
    Bitmap bitmap;
    TextView messagelocation;
    ImageButton btn_getlocation;
    FeedBack feedBack;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photos);
        ima1=findViewById(R.id.img_photo);
        ima1.setOnClickListener(this);
        messagelocation=findViewById(R.id.message_location);
        btnuplod=findViewById(R.id.btn_upload);
        btnuplod.setOnClickListener(this);
        item=findViewById(R.id.ett_item);
        sub=findViewById(R.id.ett_sub);
        btn_getlocation=findViewById(R.id.get_location);
        btn_getlocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                Intent intent;
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkPermission = ContextCompat.checkSelfPermission(TakephotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                        (new AlertDialog.Builder(TakephotoActivity.this)).setMessage("您需要在设置里打开存储空间权限。").setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(TakephotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                            }
                        }).setNegativeButton("取消", (android.content.DialogInterface.OnClickListener) null).create().show();
                        return;
                    }
                }
                intent = new Intent();
                //MediaStore.ACTION_IMAGE_CAPTURE  调用系统的照相机
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0x3);
                break;
            case R.id.btn_upload:
                /*Intent intent1 = new Intent(TakephotoActivity.this, Mainfaceactivity.class);
                Mainfaceactivity.bitmap = bitmap;
                Mainfaceactivity.ite = item.getText().toString().trim();
                Mainfaceactivity.sub = item.getText().toString().trim();
                startActivity(intent1);*/
                /*try {
                    String path="http://49.235.134.191:8080/feedback/save";
                    URL url=new URL(path);
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10 * 1000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setInstanceFollowRedirects(true);


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
                            Toast.makeText(TakephotoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                            conn.disconnect();
                        }
                        else if (result.getCode()==400) {
                            Toast.makeText(TakephotoActivity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                break;
            case R.id.get_location:
                Intent intent2 = new Intent();
                intent2.setClass(TakephotoActivity.this,Location.class);
                Bundle bundle = new Bundle();
                intent2.putExtras(bundle);
                startActivityForResult(intent2, 0x4);
                break;

        };

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1) {
            if (data != null) {
                Uri uri = data.getData();
                //裁剪图片
                getImg(uri);
            } else {
                return;
            }
        }
        if (requestCode == 0x2) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                //得到图片
                Bitmap bitmap = bundle.getParcelable("data");
                //上传图片获得Url


//              //保存到图片到本地
                saveBitmap(bitmap, "abc");
                //设置图片
                ima1.setImageBitmap(bitmap);
            } else {
                return;
            }
        }
        if (requestCode == 0x3) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String str = format.format(date);
                saveBitmap(bitmap,str);
                ima1.setImageBitmap(bitmap);
            } else {
                return;
            }
        }
        if (requestCode == 0x4) {
            Log.v("aaaaa", String.valueOf(data.getExtras()));
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //处理代码在此地
                    String str= bundle.getString("aaa");
                    messagelocation.setText(str);
                    //feedBack.setAddress(str);
                }
            }
        }
    }

    private void getImg(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            //从输入流中解码位图
            // Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            //保存位图
            // img.setImageBitmap(bitmap);
            cutImg(uri);
            //关闭流
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //裁剪图片
    private void cutImg(Uri uri) {
        if (uri != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            //true:出现裁剪的框
            intent.putExtra("crop", "true");
            //裁剪宽高时的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //裁剪后的图片的大小
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);  // 返回数据
            intent.putExtra("output", uri);
            intent.putExtra("scale", true);
            startActivityForResult(intent, 0x2);
        } else {
            return;
        }
    }

    public void saveBitmap(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        Log.v("aaaaa", String.valueOf(bm.getHeight()));
        try {
            File file = new File(getExternalFilesDir(null), picName + ".JPEG");
            Log.v("aaaaa", String.valueOf(getExternalFilesDir(null)));
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * 通过URI获取文件的路径
     * @param uri
     * @param activity
     */
    public static String getFilePathWithUri(Uri uri, Activity activity) throws Exception {
        if (uri == null) {
            return "";
        }
        String picturePath = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri,
                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);  //获取照片路径
            cursor.close();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return picturePath;
    }
}
