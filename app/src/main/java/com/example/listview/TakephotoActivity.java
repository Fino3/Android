package com.example.listview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class TakephotoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton ima1;
    Button btnuplod;
    EditText item;
    EditText sub;
    TextView messagelocation;
    ImageButton btn_getlocation;
    String mFilePath;
    private FileInputStream is = null;

    private static final int TIMEOUT_IN_MILLIONS = 5000;
    private static final String CHARSET = "utf-8";
    private static final String BOUNDARY = UUID.randomUUID().toString();
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")

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
        Log.v("aaaaa", getExternalFilesDir(null) + "/" + "mytest.png");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                Intent intent;
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkPermission = ContextCompat.checkSelfPermission(TakephotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                        (new AlertDialog.Builder(TakephotoActivity.this)).setMessage("????????????????????????????????????????????????").setPositiveButton("??????", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(TakephotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                            }
                        }).setNegativeButton("??????", (android.content.DialogInterface.OnClickListener) null).create().show();
                        return;
                    }
                }
                mFilePath=getExternalFilesDir(null) + "/" + "mytest.png";
                File cameraPhoto = new File(mFilePath);
                /*takePhotoBiggerThan7((new File(mFilePath)).getAbsolutePath());*/
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);;
                //MediaStore.ACTION_IMAGE_CAPTURE  ????????????????????????
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider",
                        cameraPhoto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent, 0x3);
                break;
            case R.id.btn_upload:
                Log.v("aaaaa","upload");
                mFilePath=getExternalFilesDir(null) + "/" + "mytest.png";
                File file=new File(mFilePath);
                try {
                    Result result = uploadFile(file,"http://49.235.134.191:8080/file/image/upload");
                    Log.v("aaaaa", result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                            Toast.makeText(TakephotoActivity.this,"????????????",Toast.LENGTH_SHORT).show();
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

        if (requestCode == 0x3) {
            try {
                mFilePath=getExternalFilesDir(null) + "/" + "mytest.png";
                // ???????????????
                is = new FileInputStream(mFilePath);
                // ???????????????bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                // ????????????
                ima1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                // ?????????
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        if (requestCode == 0x4) {
            Log.v("aaaaa", String.valueOf(data.getExtras()));
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //?????????????????????
                    String str= bundle.getString("aaa");
                    messagelocation.setText(str);
                    //feedBack.setAddress(str);
                }
            }
        }
    }
    public static Result uploadFile(File file, String RequestURL) throws Exception{
        String CONTENT_TYPE = "multipart/form-data";
        URL url = new URL(RequestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
        conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
        conn.setDoInput(true); //???????????????
        conn.setDoOutput(true); //???????????????
        conn.setUseCaches(false); //?????????????????????
        conn.setRequestMethod("POST"); //????????????
        conn.setRequestProperty("Charset", "utf-8");
        //????????????
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
        if (file != null) {
            Log.v("aaaaa","file != null");
            /** * ???????????????????????????????????????????????? */
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
            Log.v("aaaaa",file.getName());
            sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);
            dos.write(sb.toString().getBytes());
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            /**
             * ??????????????? 200=??????
             * ????????????????????????????????????
             */
            int res = conn.getResponseCode();
            if (res == 200) {
                Log.v("aaaaa","success");
                InputStream inputStream = conn.getInputStream();

                String input;
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                while ((input = reader.readLine()) != null) {
                    stringBuffer.append(input);
                }
                return JSONObject.parseObject(stringBuffer.toString(), Result.class);
            } else {
                Log.v("aaaaa", conn.getResponseMessage());
            }
        }
        throw new RuntimeException("???????????????");
    }

}
