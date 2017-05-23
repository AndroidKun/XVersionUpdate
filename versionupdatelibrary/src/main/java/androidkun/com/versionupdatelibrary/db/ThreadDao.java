package androidkun.com.versionupdatelibrary.db;

import java.util.List;

import androidkun.com.versionupdatelibrary.entity.ThreadBean;

/**
 * Created by Kun on 2017/5/22.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:
 */

public interface ThreadDao {
    /**
     * 插入下载线程信息
     * @param threadBean
     */
    void insertThread(ThreadBean threadBean);

    /**
     * 更新下载线程信息
     * @param url
     * @param thread_id
     * @param finished
     */
    void updateThread(String url, int thread_id, int finished);

    /**
     * 删除下载线程
     * @param url
     */
    void deleteThread(String url);

    /**
     * 获取下载线程
     * @param url
     * @return
     */
    List<ThreadBean> getThreads(String url);

    /**
     * 判断下载线程是否存在
     * @param url
     * @param thread_id
     * @return
     */
    boolean isExists(String url, int thread_id);
}
