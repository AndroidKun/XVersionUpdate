package androidkun.com.versionupdatelibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kun on 2017/5/22.
 * GitHub: https://github.com/AndroidKun
 * CSDN: http://blog.csdn.net/a1533588867
 * Description:
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "db_download";
    private static DBHelper dbHelper = null;

    public static DBHelper getInstance(Context context){
        if(dbHelper == null) dbHelper = new DBHelper(context);
        return dbHelper;
    }

    private DBHelper(Context context){
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table thread_info (_id integer primary key autoincrement," +
                "thread_id integer,url text,start integer,end integer,finished integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
