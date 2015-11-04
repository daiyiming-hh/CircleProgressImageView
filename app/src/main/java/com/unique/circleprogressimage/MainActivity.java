package com.unique.circleprogressimage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.unique.circleprogressimage.view.CircleProgressImage;

public class MainActivity extends AppCompatActivity {

    private final static int HANDLER_MESSAGE_UPDATE_PROGRESS = 0;

    private CircleProgressImage cpimg_progress = null;

    private int progress = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE_UPDATE_PROGRESS: {
                    progress ++;
                    if (progress > 100) {
                        progress = 0;
                    }
                    cpimg_progress.setProgress(progress);
                    handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_UPDATE_PROGRESS, 100);
                } break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        cpimg_progress = (CircleProgressImage) findViewById(R.id.cpimg_progress);

        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_UPDATE_PROGRESS, 100);
    }
}




























































