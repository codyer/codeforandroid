package com.pao123.common;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class LoadImageThread extends Thread
{
    String url;
    int id;
    Handler handler;

    public LoadImageThread(Handler handler, int id, String url)
    {
        this.url = url;
        this.id = id;
        this.handler = handler;
    }

    public void run()
    {
        URL myFileURL;
        Bitmap bitmap = null;
        try
        {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            bitmap = BitmapFactory.decodeStream(is, null, opts);
            // 关闭数据流
            is.close();
            if (handler != null)
            {
                if (bitmap != null)
                {
                    Message msg = new Message();
                    msg.what = Constants.MSG_LOAD_IMAGE_SUCCESS;
                    msg.arg1 = id;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
                else
                {
                    Message msg = new Message();
                    msg.what = Constants.MSG_LOAD_IMAGE_FAILED;
                    msg.arg1 = id;
                    handler.sendMessage(msg);
                }
            }
            else
            {
            }

        }
        catch (Exception e)
        {
            Message msg = new Message();
            msg.what = Constants.MSG_LOAD_IMAGE_FAILED;
            msg.arg1 = id;
            handler.sendMessage(msg);

        }
    }
}