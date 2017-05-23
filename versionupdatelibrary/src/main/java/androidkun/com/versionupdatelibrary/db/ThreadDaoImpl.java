package androidkun.com.versionupdatelibrary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import androidkun.com.versionupdatelibrary.entity.ThreadBean;

/**
 * Created by Kun on 2017/5/22.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:
 */

public class ThreadDaoImpl implements ThreadDao{
    private DBHelper dbHelper;

    public ThreadDaoImpl(Context context){
        dbHelper = DBHelper.getInstance(context);
    }

    @Override
    public synchronized void insertThread(ThreadBean threadBean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into thread_info ( thread_id, url, start ,end, finished) values (?,?,?,?,?)"
                ,new Object[]{threadBean.getId(),threadBean.getUrl(),threadBean.getStart(),threadBean.getEnd(),threadBean.getFinished()});
        db.close();
    }

    @Override
    public synchronized void updateThread(String url, int thread_id, int finished) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update thread_info set finished = ? where url = ? and thread_id = ?"
                ,new Object[]{finished,url,thread_id});
        db.close();
    }

    @Override
    public void deleteThread(String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where url = ?",new Object[]{url});
        db.close();
    }

    @Override
    public List<ThreadBean> getThreads(String url) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url = ?",new String[]{url});
        List<ThreadBean> threadBeanList = new ArrayList<>();
        while (cursor.moveToNext()){
            ThreadBean bean = new ThreadBean();
            bean.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            bean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            bean.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            bean.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            bean.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            threadBeanList.add(bean);
        }
        cursor.close();
        db.close();
        return threadBeanList;
    }

    @Override
    public boolean isExists(String url, int thread_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url = ? and thread_id = ?", new String[]{url,thread_id+""});
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }
}
