package com.wenld.birdcage.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.seven.birdcage.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.translateX;
import static com.iflytek.cloud.VerifierResult.TAG;

/**
 * Created by Administrator on 2017/1/12.
 */

public class RecordView2 extends View {
    //View默认最小宽度
    private static final int DEFAULT_MIN_WIDTH = 500;

    private final TypedArray typedArray;
    private Context mContext;
    private Paint mPaint;

    /**
     * 默认是录制模式
     */
    private int model = RecordView.MODEL_RECORD;

    /**
     * 灵敏度
     */
    private int sensibility = 4;
    private boolean canSetVolume = true;


    /**
     * 振幅
     */
    private float amplitude = 1;
    /**
     * 音量
     */
    private float volume = 10;
    private int fineness = 1;
    private float targetVolume = 1;
    private float maxVolume = 100;
    private boolean isSet = false;

    private Timer progressTimer = new Timer(true);

    private int voiceLineColor; //
    private ArrayList<Path> paths;

    //    private long lastTime = 0;
//    private int lineSpeed = 100;
//    private float translateX = 0;
    private TimerTask progressTask;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                countdownTime2--;
//                if(countdownTime2 == 0){
//                    listener.onCountDown();
//                    canSetVolume = false;
//                    timeTask.stop();
//                    postInvalidate();
//                }
            } else if (msg.what == 2) {
//                progress += 360.00/(countdownTime*950.00/5.00);
////                Log.d(TAG,"progress:"+progress);
//                if(progress >360){
//                    targetVolume = 1;
//                    postInvalidate();
//                    progressTask.stop();
//                }else
                postInvalidate();
            }
        }
    };

    public RecordView2(Context context) {
        this(context, null);
    }

    public RecordView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.recordView);
        initAtts();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void initAtts() {
        model = typedArray.getInt(R.styleable.recordView_model, RecordView.MODEL_RECORD);
//        hintText = typedArray.getString(R.styleable.recordView_hintText);
//        progressDrawable = typedArray.getDrawable(R.styleable.recordView_progressSrc) == null?
//                getResources().getDrawable(R.mipmap.light_blue):typedArray.getDrawable(R.styleable.recordView_progressSrc);
//        textHintSize = typedArray.getDimension(R.styleable.recordView_hintTextSize,15);
        voiceLineColor = typedArray.getColor(R.styleable.recordView_middleLineColor, getResources().getColor(R.color.RoundFillColor));
        paths = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            paths.add(new Path());
        }
    }

    /**
     * 当布局为wrap_content时设置默认长宽
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int origin) {
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDefaultForPlay(canvas);
        drawVoiceLine2(canvas);
    }

    private void drawDefaultForPlay(Canvas canvas) {
    }

    /**
     * 画声纹（播放）
     */
    private void drawVoiceLine2(Canvas canvas) {
        mPaint.setColor(voiceLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        canvas.save();
        int moveY = getHeight() / 2;
        int pandY = getWidth() / 12;
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).reset();
            paths.get(i).moveTo(getWidth() - pandY, getHeight() / 2);
        }
        for (float j = getWidth() * 11 / 12 - 1; j >= getWidth() / 12; j -= fineness) {
            float i = j - getWidth() / 12;
            //这边必须保证起始点和终点的时候amplitude = 0;
            amplitude = 4 * volume * i / getWidth() - 4 * volume * i / getWidth() * i / getWidth() * 12 / 10;
            for (int n = 1; n <= paths.size(); n++) {
                float sin = amplitude * (float) Math.sin((i - Math.pow(1.22, n)) * Math.PI / 180 - translateX);
                paths.get(n - 1).lineTo(j, (2 * n * sin / paths.size() - 15 * sin / paths.size() + moveY));
            }
        }
        for (int n = 0; n < paths.size(); n++) {
            if (n == paths.size() - 1) {
                mPaint.setAlpha(255);
            } else {
                mPaint.setAlpha(n * 130 / paths.size());
            }
            if (mPaint.getAlpha() > 0) {
                canvas.drawPath(paths.get(n), mPaint);
            }
        }
        canvas.restore();
    }

    public void start() {
        //重置计时器显示的时间
        canSetVolume = true;
        progressTimer.schedule(progressTask = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
            }
        }, 0, 5);
    }

    private void lineChange() {
    }

    public void setVolume(int volume) {
        if (volume > 100)
            this.volume = volume / 100;
        this.volume = volume * 2 / 5;
        if (!canSetVolume)
            return;
        if (this.volume > maxVolume * sensibility / 30) {
            isSet = true;
            this.targetVolume = getHeight() * this.volume / 3 / maxVolume;
            Log.d(TAG, "targetVolume: " + targetVolume);
        }
    }
    public void cancel(){
        postInvalidate();
        progressTask.cancel();
    }
}
