package com.example.listview;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class NewsFragment extends Fragment {
    public String[] titles={"福州大学签订生物医药领域重大专利转化项目","厦门工艺美术学院在中国大学生计算机设计大赛获两项全国二等奖",
            "机械学院科研成果在国际知名期刊以封面文章形式发表","福州大学5200余名新生开启军训之旅","福州大学第50届田径运动会组委会会议举行","福州大学获第44届ICPC全球总决赛(线上)第21名"};
    public String[] detailss={"福州大学与苏州维益生物科技有限公司签订了“基于硫醇交换递送系统的纳米药物”的技术（专利申请权）转让合同与技术开发（合作）合同，签约总金额达300万元。",
            "厦门工艺美术学院数字媒体艺术系同学在本次大赛成绩突出，在全国总决赛获得二等奖2项，在福建省赛获得一等奖3项、二等奖5项、三等奖7项",
            "机械学院尹超、魏发南副教授、姚立纲教授等人关于微型软体机器人的科研成果Visible Light-Driven Jellyfish-like Miniature Swimming Soft Robot被国际知名期刊ACS Applied Materials & Interfaces（美国化学学会应用材料与界面）以封面文章形式发表。",
            "金秋送爽,丹桂飘香。10月11日上午，福州大学2021级本科生军训动员大会在旗山校区第一田径运动场举行。",
            "10月12日上午，福州大学第50届田径运动会组织委员会会议（第二次筹备会）举行。会议讨论了运动会竞赛规程及有关问题，林键副校长出席会议并讲话。",
            "福州大学ACM代表队由吴媛媛、傅滨和李进三位同学组成，在教练团队的指导下，经与国际知名高校代表队的激烈角逐，最终在57支线上参赛的强劲竞争对手中位居并列第21名。"};
    public String[] dates={"2021-10-12","2021-10-12","2021-10-11","2021-10-11","2021-10-12","2021-10-11"};
    public int []icons={R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,R.drawable.a6};
    Bundle bundle;
    Bitmap bitmap;
    Bitmap []bitmaps={};


    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = View.inflate(getActivity(),R.layout.news,null);
        ListView listview=view.findViewById(R.id.lv);
        /*bundle=this.getArguments();
        if(bundle!=null){
            Toast.makeText(getActivity(),"fragment获得了信息",Toast.LENGTH_SHORT).show();
            detailss=insert(detailss,bundle.getString("内容"));
            titles=insert(titles,bundle.getString("标题"));
            bitmap=bundle.getParcelable("图片");
            dates=insert(dates,bundle.getString("时间"));
        }*/
        getNewsDetails();
        MyBaseAdapter adapter=new MyBaseAdapter();
        listview.setAdapter(adapter);
        return view;
    }
    public void getNewsDetails() {
        try{
            String path="http://49.235.134.191:8080/news/get";
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
                if (result.getCode() == 200) {
                    NewsDetails newsDetails;
                    JSONArray newsJsonArray=JSONArray.parseArray(result.getData().toString());
                    Log.v("mybug", newsJsonArray.toString());
                    for(int i=0;i<newsJsonArray.size();i++) {
                        JSONObject newsJsonObject=newsJsonArray.getJSONObject(i);
                        newsDetails = JSON.toJavaObject(newsJsonObject,NewsDetails.class);
                        Log.v("mybug",newsDetails.getTitle());
                        titles=insert(titles,newsDetails.getTitle());
                        detailss=insert(detailss,newsDetails.getDesc());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        dates=insert(dates,dateFormat.format(newsDetails.getPublishTime()));
                        Bitmap bitmap1 = getHttpBitmap(newsDetails.getImageUrl());
                        bitmaps=insert(bitmaps,bitmap1);
                    }
                }
                else {
                    Toast.makeText(getActivity(),result.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class MyBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int i) {
            return titles[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view=View.inflate(getActivity(),R.layout.list_item,null);
            TextView title=view.findViewById(R.id.title);
            TextView details=view.findViewById(R.id.details);
            TextView date1=view.findViewById(R.id.date1);
            ImageView iv=view.findViewById(R.id.iv);
            title.setText(titles[i]);
            details.setText(detailss[i]);
            date1.setText(dates[i]);
            if(i<icons.length){
                iv.setImageResource(icons[i]);
            }
            else {
                //iv.setImageBitmap(bitmap);
                Log.v("aaabbb", String.valueOf(bitmaps[i- icons.length].getHeight()));
                Matrix matrix = new Matrix();
                matrix.setScale(0.2f, 0.2f);
                bitmaps[i-icons.length] = Bitmap.createBitmap(bitmaps[i-icons.length], 0, 0, bitmaps[i-icons.length].getWidth(),
                        bitmaps[i-icons.length].getHeight(), matrix, true);
                iv.setImageBitmap(bitmaps[i- icons.length]);
                Log.v("aaabbb", String.valueOf(bitmaps[i- icons.length].getHeight()));
            }
            return view;
        }
    }
    private static String[] insert(String[] arr, String str) {
        int size = arr.length;  //获取数组长度
        String[] tmp = new String[size + 1];  //新建临时字符串数组，在原来基础上长度加一
        for (int i = 0; i < size; i++){  //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i];
        }
        tmp[size] = str;  //在最后添加上需要追加的数据
        return tmp;  //返回拼接完成的字符串数组
    }
    private  static Bitmap[] insert(Bitmap[] arr, Bitmap bitmap) {
        int size = arr.length;  //获取数组长度
        Bitmap[] tmp = new Bitmap[size + 1];  //新建临时字符串数组，在原来基础上长度加一
        for (int i = 0; i < size; i++){  //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i];
        }
        tmp[size] = bitmap;  //在最后添加上需要追加的数据
        return tmp;  //返回拼接完成的字符串数组
    }
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}

