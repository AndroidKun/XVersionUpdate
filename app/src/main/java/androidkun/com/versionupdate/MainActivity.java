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
        VersionUpdateConfig.getInstance().setContext(MainActivity.this).setDownLoadURL(url)
                .setNotificationIconRes(R.mipmap.app_icon)
                .setNotificationSmallIconRes(R.mipmap.app_icon_small)
                .setNotificationTitle("版本升级Demo")
                .startDownLoad();
    }
}
