package com.wenld.birdcage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.seven.birdcage.R;
import com.wenld.birdcage.ui.LoadingView;
import com.wenld.birdcage.ui.RecordView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/1/12.
 */

public class AnimTestActivity extends AppCompatActivity implements View.OnTouchListener,View.OnClickListener{

    private RecordView mRecorfView;
    private LoadingView loadingView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int db = (int) (Math.random()*100);
            mRecorfView.setVolume(db);
        }
    };
    private int nowModel = RecordView.MODEL_RECORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animest);
        findViewById(R.id.button).setOnTouchListener(this);
        mRecorfView = (RecordView) findViewById(R.id.recordView);
        loadingView= (LoadingView) findViewById(R.id.loadingView);
        mRecorfView.setCountdownTime(9);
        mRecorfView.setModel(RecordView.MODEL_RECORD);
        findViewById(R.id.button2).setOnClickListener(this);
    }
    private TimerTask timeTask;
    private Timer timeTimer = new Timer(true);

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mRecorfView.start();
            timeTimer.schedule(timeTask = new TimerTask() {
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }, 20, 20);
            mRecorfView.setOnCountDownListener(new RecordView.OnCountDownListener() {
                @Override
                public void onCountDown() {
                    Toast.makeText(AnimTestActivity.this,"计时结束啦~~",Toast.LENGTH_SHORT).show();
                }
            });
            loadingView.start();
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            mRecorfView.cancel();
            loadingView.cancel();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(nowModel == RecordView.MODEL_PLAY){
            mRecorfView.setModel(RecordView.MODEL_RECORD);
            nowModel = RecordView.MODEL_RECORD;
        }else{
            mRecorfView.setModel(RecordView.MODEL_PLAY);
            nowModel = RecordView.MODEL_PLAY;
        }
    }
}