package androidkun.com.versionupdatelibrary.download;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import androidkun.com.versionupdatelibrary.db.ThreadDao;
import androidkun.com.versionupdatelibrary.db.ThreadDaoImpl;
import androidkun.com.versionupdatelibrary.entity.Config;
import androidkun.com.versionupdatelibrary.entity.FileBean;
import androidkun.com.versionupdatelibrary.entity.ThreadBean;
import androidkun.com.versionupdatelibrary.service.VersionUpdateService;


/**
 * Created by Kun on 2017/5/22.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:
 */
public class DownloadTask implements DownloadCallBack {

    private Context context;
    private FileBean fileBean;
    private ThreadDao dao;
    private int downloadThreadCount;

    /**
     * 总下载完成进度
     */
    private int finishedProgress = 0;
    /**
     * 下载线程信息集合
     */
    private List<ThreadBean> threads;
    /**
     * 下载线程集合
     */
    private List<DownloadThread> downloadThreads = new ArrayList<>();

    public DownloadTask(Context context, FileBean fileBean, int downloadThreadCount) {
        this.context = context;
        this.fileBean = fileBean;
        dao = new ThreadDaoImpl(context);
        this.downloadThreadCount = downloadThreadCount;
        //初始化下载线程
        initDownThreads();
    }

    private void initDownThreads() {
        //查询数据库中的下载线程信息
        threads = dao.getThreads(fileBean.getUrl());
        if(threads.size()==0){//如果列表没有数据 则为第一次下载
            //根据下载的线程总数平分各自下载的文件长度
            int length = fileBean.getLength()/downloadThreadCount;
            for(int i = 0; i<downloadThreadCount; i++){
                ThreadBean thread = new ThreadBean(i,fileBean.getUrl(),i * length,
                        (i + 1) * length -1,0);
                if(i == downloadThreadCount-1){
                    thread.setEnd(fileBean.getLength());
                }
                //将下载线程保存到数据库
                dao.insertThread(thread);
                threads.add(thread);
            }
        }
        //创建下载线程开始下载
        for(ThreadBean thread : threads){
            finishedProgress+= thread.getFinished();
            DownloadThread downloadThread = new DownloadThread(fileBean, thread, this);
            VersionUpdateService.executorService.execute(downloadThread);
            downloadThreads.add(downloadThread);
        }
    }
    /**
     * 开始下载
     */
    public void startDownload(){
        downloadThreads.clear();
        threads.clear();
        finishedProgress = 0;
        initDownThreads();
    }

    /**
     * 暂停下载
     */
    public void pauseDownload(){
        for(DownloadThread downloadThread : downloadThreads){
            if (downloadThread!=null) {
                downloadThread.setPause(true);
            }
        }
    }

    @Override
    public void pauseCallBack(ThreadBean threadBean) {
        dao.updateThread(threadBean.getUrl(),threadBean.getId(),threadBean.getFinished());
        Intent intent = new Intent(Config.ACTION_PAUSE);
        intent.putExtra("FileBean",fileBean);
        context.sendBroadcast(intent);
    }

    private long curTime = 0;
    private int speed = 0;
    @Override
    public void progressCallBack(int length) {
        finishedProgress += length;
        speed += length;
        //每500毫秒发送刷新进度事件
        long time = System.currentTimeMillis() - curTime;
        if(time >500 || finishedProgress==fileBean.getLength()){
            fileBean.setFinished(finishedProgress);
          /*  EventMessage message = new EventMessage(3,fileBean);
            EventBus.getDefault().post(message);*/
            Intent intent = new Intent(Config.ACTION_REFRESH);
            speed = (int) (speed/(time*0.001));
            intent.putExtra("Speed",speed);
            intent.putExtra("FileBean",fileBean);
            context.sendBroadcast(intent);
            curTime  = System.currentTimeMillis();
            speed = 0;
        }
    }

    @Override
    public synchronized void threadDownLoadFinished(ThreadBean threadBean) {
        for(ThreadBean bean:threads){
            if(bean.getId() == threadBean.getId()){
                //从列表中将已下载完成的线程信息移除
                threads.remove(bean);
                break;
            }
        }
        if(threads.size()==0){//如果列表size为0 则所有线程已下载完成
            //删除数据库中的信息
            dao.deleteThread(fileBean.getUrl());
            //发送下载完成事件
           /* EventMessage message = new EventMessage(2,fileBean);
            EventBus.getDefault().post(message);*/
            Intent intent = new Intent(Config.ACTION_FININSHED);
            intent.putExtra("FileBean",fileBean);
            context.sendBroadcast(intent);
        }
    }

    public FileBean getFileBean() {
        return fileBean;
    }
}
