package com.cf.untils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * //          佛曰:
 * //                  写字楼里写字间，写字间里程序员；
 * //                  程序人员写程序，又拿程序换酒钱。
 * //                  酒醒只在网上坐，酒醉还来网下眠；
 * //                  酒醉酒醒日复日，网上网下年复年。
 * //                  但愿老死电脑间，不愿鞠躬老板前；
 * //                  奔驰宝马贵者趣，公交自行程序员。
 * //                  别人笑我忒疯癫，我笑自己命太贱；
 * //                  不见满街漂亮妹，哪个归得程序员？
 * <p/>
 * //             佛祖保佑             永无BUG
 * //* Created by CZ on 2016/8/1 0001.
 */
public class CustomCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "Activity";
    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().toString();
    private static CustomCrashHandler mInstance = new CustomCrashHandler();
    private Context mContext;
    private CustomCrashHandler(){}

    /**
     * 单例模式，保证只有一个CustomCrashHandler实例存在
     * @return
     */
    public static CustomCrashHandler getInstance(){
        return mInstance;
    }


    /**
     * 为我们的应用程序设置自定义Crash处理
     */
    public void setCustomCrashHanler(Context context){
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /***
     *
     * 当程序 发生 异常的 时候 调用 此方法
     * */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        // 保存 信息
        saveinfo(mContext,throwable);

        showToast(mContext,"程序异常");

        //  上传文件

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ExitAppUtils.getInstance().exit();
    }



    /**
     * 显示提示信息，需要在线程中显示Toast
     * @param context
     * @param msg
     */
    private void showToast(final Context context, final String msg){
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context){
        HashMap<String, String> map = new HashMap<String, String>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo =mPackageManager.getPackageInfo(context.getPackageName(),mPackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);//手机型号
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" +  Build.PRODUCT);
        return map;
    }


    // 异常信息
    private String ExceptionInfo(Throwable th){
        StringWriter stringbuffer=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringbuffer);
        th.printStackTrace(printWriter);
        printWriter.close();
        Log.i("log",stringbuffer.toString());
        return stringbuffer.toString();
    }

    private String saveinfo(Context mContext, Throwable throwable) {
        String filename=null;
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,String> entry: obtainSimpleInfo(mContext).entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        sb.append(ExceptionInfo(throwable));

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            // 如果存在sd 卡
             File file=new File(SDCARD_ROOT,"CXC_Crash");
          // File file = new File(SDCARD_ROOT + File.separator + "CXC" + File.separator);
            if(!file.exists()){
                file.mkdir();  //如果 不存在 就创建 文件 目录
            }
            try {
                filename=file.toString()+File.separator+paserTime(System.currentTimeMillis())+".log";//文件完整路径 和名称
                FileOutputStream fos = new FileOutputStream(filename);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filename;
    }


    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     * @param milliseconds
     * @return
     */
    private String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String times = format.format(new Date(milliseconds));
        return times;
    }
}
