package androidkun.com.versionupdatelibrary.download;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import androidkun.com.versionupdatelibrary.R;
import androidkun.com.versionupdatelibrary.entity.Config;
import androidkun.com.versionupdatelibrary.entity.FileBean;
import androidkun.com.versionupdatelibrary.utils.MD5Util;


/**
 * Created by Kun on 2017/5/22.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:
 */

public class InitThread extends Thread {

    private Context context;
    private FileBean fileBean;

    public InitThread(Context context, FileBean fileBean) {
        this.context = context;
        this.fileBean = fileBean;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        RandomAccessFile randomAccessFile = null;
        try {
            URL url = new URL(fileBean.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            int fileLength = -1;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                fileLength = connection.getContentLength();
            }
            if (fileLength <= 0) {
                Intent intent = new Intent(Config.ACTION_ERROR);
                intent.putExtra("error", context.getString(R.string.error_url));
                context.sendBroadcast(intent);
                return;
            }
            //判断当前版本的APK是否下载过
            File downLoadedFile = new File(Config.downLoadPath, fileBean.getFileName());
            if (downLoadedFile.exists()) {
                String versionNameFromApk = MD5Util.getVersionNameFromApk(context, downLoadedFile.getPath());
                Log.w("AAA", "versionNameFromApk:" + versionNameFromApk + "curVersion:" + fileBean.getVersion());
                if (!TextUtils.isEmpty(versionNameFromApk) &&
                        !TextUtils.isEmpty(fileBean.getVersion()) &&
                        versionNameFromApk.equals(fileBean.getVersion())) {
                    Intent intent = new Intent(Config.ACTION_FININSHED);
                    intent.putExtra("FileBean", fileBean);
                    context.sendBroadcast(intent);
                    return;
                }
            }

            File dir = new File(Config.downLoadPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, fileBean.getFileName());
            randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.setLength(fileLength);
            fileBean.setLength(fileLength);

            Intent intent = new Intent(Config.ACTION_START);
            intent.putExtra("FileBean", fileBean);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
