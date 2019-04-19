package androidkun.com.versionupdatelibrary.entity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidkun.com.versionupdatelibrary.Activity.TranslucentActivity;
import androidkun.com.versionupdatelibrary.service.VersionUpdateService;
import androidkun.com.versionupdatelibrary.utils.MD5Util;


/**
 * Created by Kun on 2017/5/22.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:
 */

public class VersionUpdateConfig {

    private static VersionUpdateConfig config = new VersionUpdateConfig();
    private FileBean fileBean;
    private Context context;

    private VersionUpdateConfig() {

    }

    public static VersionUpdateConfig getInstance() {
        return config;
    }

    /**
     * 设置上下文
     *
     * @param context
     * @return
     */
    public VersionUpdateConfig setContext(Context context) {
        this.context = context;
        return config;
    }

    /**
     * 设置文件保存路径
     *
     * @param path
     * @return
     */
    public VersionUpdateConfig setFileSavePath(String path) {
        Config.downLoadPath = path;
        return config;
    }
    /**
     * 设置强制更新
     * @param strongUpdate 1.是，0,否
     * @return
     */
    public VersionUpdateConfig setStrongUpdate(int strongUpdate) {
        Config.strongUpdate = strongUpdate;
        return config;
    }

    public int getStrongUpdate(){
        return Config.strongUpdate;
    }

    /**
     * 设置通知标题
     */
    public VersionUpdateConfig setNotificationTitle(String title) {
        Config.notificationTitle = title;
        return config;
    }

    /**
     * 设置通知图标
     */
    public VersionUpdateConfig setNotificationIconRes(int resId) {
        Config.notificaionIconResId = resId;
        return config;
    }

    /**
     * 设置通知小图标
     */
    public VersionUpdateConfig setNotificationSmallIconRes(int resId) {
        Config.notificaionSmallIconResId = resId;
        return config;
    }

    /**
     * 设置下载链接
     *
     * @param url
     * @return
     */
    public VersionUpdateConfig setDownLoadURL(String url) {
        fileBean = new FileBean(0, MD5Util.MD5(url) + ".apk", url, 0, 0);
        return config;
    }

    /**
     * 设置新包的版本号 用于区分是否下载过此包
     *
     * @param version 新包的版本号
     * @return
     */
    public VersionUpdateConfig setNewVersion(String version) {
        if (fileBean == null) {
            throw new NullPointerException("url cannot be null, you must call setDownLoadURL() before setNewVersion().");
        }
        fileBean.setVersion(version);
        return config;
    }



    /**
     * 开始下载
     */
    public void startDownLoad() {
        if (context == null) {
            throw new NullPointerException("context cannot be null, you must first call setContext().");
        }
        if (fileBean == null) {
            throw new NullPointerException("url cannot be null, you must first call setDownLoadURL().");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Activity) context).startActivity(new Intent(context, TranslucentActivity.class));
        } else {
            passCheck();
        }
    }

    public void passCheck() {
        Intent startIntent = new Intent(context, VersionUpdateService.class);
        startIntent.setAction(VersionUpdateService.ACTION_START);
        startIntent.putExtra("FileBean", fileBean);
        context.startService(startIntent);
    }


}
