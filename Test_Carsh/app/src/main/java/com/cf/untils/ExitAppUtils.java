package com.cf.untils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

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
public class ExitAppUtils {

    private List<Activity> mActivityList=new LinkedList<Activity>();

    private static  ExitAppUtils instance=new ExitAppUtils();

    private ExitAppUtils(){};

    public static ExitAppUtils getInstance(){
        return instance;
    }

    /**
     * 添加Activity实例到mActivityList中，在onCreate()中调用
     * @param activity
     */
    public void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    /**
     * 从容器中删除多余的Activity实例，在onDestroy()中调用
     * @param activity
     */
    public void delActivity(Activity activity){
        mActivityList.remove(activity);
    }

    /**
     * 退出程序的方法
     */
    public void exit(){
        for(Activity activity : mActivityList){
            activity.finish();
        }
        System.exit(0);
    }

}
