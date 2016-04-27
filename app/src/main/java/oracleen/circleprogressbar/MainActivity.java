package oracleen.circleprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.coder.circlebar.CircleBar;


public class MainActivity extends Activity {

    private CircleBar progress;
    private TextView lastTime;
    private TextView lastTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initProgress(300);
    }

    private void initProgress(int min) {

        progress = (CircleBar) findViewById(R.id.progress);
        lastTime = (TextView) findViewById(R.id.day);
        lastTag = (TextView) findViewById(R.id.tag);

        int hour = min / 60;

        //初始化显示时间
        if (hour < 24) {
            lastTime.setText(hour + "");
            lastTag.setText("小时");
        } else {
            int day = hour / 24;
            lastTime.setText(day + "");
            lastTag.setText("天");
        }

        //初始化进度条
        progress.update(hour, 3000);
    }
}
