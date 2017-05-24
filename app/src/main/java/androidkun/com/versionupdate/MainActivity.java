package androidkun.com.versionupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import androidkun.com.versionupdatelibrary.entity.VersionUpdateConfig;

public class MainActivity extends AppCompatActivity{


    private String url = "http://www.workingdom.com/media/apk/wkd-android-1-0.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void update(View view){
        VersionUpdateConfig.getInstance()//获取配置实例
                .setContext(MainActivity.this)//设置上下文
                .setDownLoadURL(url)//设置文件下载链接
                .setNotificationIconRes(R.mipmap.app_icon)//设置通知大图标
                .setNotificationSmallIconRes(R.mipmap.app_icon_small)//设置通知小图标
                .setNotificationTitle("版本升级Demo")//设置通知标题
                .startDownLoad();//开始下载
    }
}
