package androidkun.com.versionupdatelibrary.download;

import androidkun.com.versionupdatelibrary.entity.ThreadBean;

/**
 * Created by Kun on 2017/5/22.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:下载进度回调
 */

public interface DownloadCallBack {
    /**
     * 暂停回调
     * @param threadBean
     */
    void pauseCallBack(ThreadBean threadBean);
    /**
     * 停止下载回调
     * @param threadBean
     */
    void closeCallBack(ThreadBean threadBean);
    /**
     * 下载进度
     * @param length
     */
    void progressCallBack(int length);

    /**
     * 线程下载完毕
     * @param threadBean
     */
    void threadDownLoadFinished(ThreadBean threadBean);
}
