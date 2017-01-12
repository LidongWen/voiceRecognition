package com.wenld.birdcage.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.seven.birdcage.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wenld on 2017/1/12.
 */

public class MicrophoneView extends View {

    Context mContext;
    private final TypedArray typedArray;

    //View默认最小宽度
    private static final int DEFAULT_MIN_WIDTH = 400;
    //圆环的边距
    private int pandding = 10;
    //圆环的宽度
    private int widthing = 3;
    /**
     * 圆环渐变颜色
     */
    private int[] doughnutColors = new int[]{0x3045C3E5,  0x1245C3E5, 0x0545C3E5, 0x1245C3E5, 0x3045C3E5, 0xC545C3E5, 0xDD45C3E5, 0xFF45C3E5, 0xDD45C3E5, 0xC545C3E5, 0x3045C3E5};
    /**
     * 声波线宽度
     */
    private int widthVoice = 1;

    /**
     * 音量
     */
    private float volume = 10;
    /**
     * 音量等级
     */
    private int volumeLevel = 11;

    private String playHintText;    //文字
    private float progress = 0;

    private Paint mPaint;

    private Timer progressTimer = new Timer(true);
    private TimerTask progressTask;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            } else if (msg.what == 2) {
                progress += 360.00 / (1 * 950.00 / 5.00);
//                Log.d(TAG,"progress:"+progress);
                if (progress > 360) {
                    progress = progress - 360;
                    postInvalidate();
                } else
                    postInvalidate();
            }
        }
    };

    public MicrophoneView(Context context) {
        this(context, null);
    }

    public MicrophoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.recordView);
        initAtts();

        mPaint = new Paint();
    }

    private void initAtts() {
        playHintText = typedArray.getString(R.styleable.recordView_playHintText);
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

        onDrawMicrophone(canvas);
        drawArc(canvas);


//        drawRing(canvas);
//        drawText(canvas);
//        drawProgress(canvas);
    }


    /**
     * 绘制
     *
     * @param canvas
     */
    private void onDrawMicrophone(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_microphone);
        float w = getWidth() / 2 - dip2px(mContext, pandding + 5);
        float h = getHeight() / 2 - dip2px(mContext, pandding + 5);
        RectF rectF = new RectF(getWidth() / 2 - w / 2
                , getHeight() / 2
                , w / 2 + getWidth() / 2
                , getHeight() / 2 + h);
        canvas.drawBitmap(bitmap, null, rectF, null);
    }

    private void drawText(Canvas canvas) {
        /**
         * 画中间文字
         * */
        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setTextSize(dip2px(mContext, 14));
        paint2.setColor(mContext.getResources().getColor(R.color.RoundFillColor));
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        paint2.setTextAlign(Paint.Align.CENTER);
        if (playHintText == null) {
            playHintText = "正在播放录音.";
        }
        canvas.drawText(playHintText, getWidth() / 2, (getHeight() + fm.descent - fm.ascent) / 2, paint2);
    }

    private void drawRing(Canvas canvas) {
        /**
         * 外层 圆环
         */
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dip2px(mContext, widthVoice));
        mPaint.setColor(mContext.getResources().getColor(R.color.RoundColor));
        RectF oval = new RectF(dip2px(mContext, pandding)
                , dip2px(mContext, pandding)
                , getWidth() - dip2px(mContext, pandding)
                , getHeight() - dip2px(mContext, pandding));
        canvas.drawArc(oval, 0, 360, false, mPaint);    //绘制圆弧
    }

    /**
     * 绘制圆弧
     */
    private void drawProgress(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.RoundFillColor));
        mPaint.setStrokeWidth(dip2px(mContext, widthVoice));
//        mPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, doughnutColors, null));
        RectF oval = new RectF(dip2px(mContext, pandding)
                , dip2px(mContext, pandding)
                , getWidth() - dip2px(mContext, pandding)
                , getHeight() - dip2px(mContext, pandding));
        canvas.drawArc(oval, progress, 90, false, mPaint);    //绘制圆弧
        mPaint.reset();
    }

    private void drawArc(Canvas canvas) {
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(getResources().getColor(R.color.RoundFillColor));
        mPaint.setStrokeWidth(dip2px(mContext, widthVoice));
        mPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, doughnutColors, null));
        float r = getWidth() / 2f - dip2px(mContext, pandding);
        for (int i = 5; i > 0; i--) {
            drawArc(r, i, canvas);
        }
        drawLightArc(r, volumeLevel, canvas);
        mPaint.reset();
    }

    private void drawArc(float r, int i, Canvas canvas) {
        float currentR = r / 5 * (i);

        RectF oval = new RectF(getWidth() / 2 - currentR
                , getHeight() / 2 - currentR
                , getWidth() / 2 + currentR
                , getHeight() / 2 + currentR);
        canvas.drawArc(oval, 160, 360, false, mPaint);    //绘制圆弧
    }

    /**
     * 亮
     *
     * @param r
     * @param i
     * @param canvas
     */
    private void drawLightArc(float r, int i, Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, Color.parseColor("#2245C3E5"), Color.parseColor("#2245C3E5")));
        float currentR = r / 20 * (i);

        RectF oval = new RectF(getWidth() / 2 - currentR
                , getHeight() / 2 - currentR
                , getWidth() / 2 + currentR
                , getHeight() / 2 + currentR);
        canvas.drawArc(oval, 160, 360, true, mPaint);    //绘制圆弧
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void start() {
        stop();
        progressTimer.schedule(progressTask = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
            }
        }, 0, 5);
    }

    public void stop() {
        try {
            postInvalidate();
            progressTask.cancel();
        } catch (Exception e) {

        }
    }

    public void setVolume(int volume) {
        if (volume <= 0 && volume > 100) {
            throw new NullPointerException("数值在0-100, 请转换。");
        } else {
            this.volume = volume*20;
            if (this.volume < 20) {
                volumeLevel = 1;
                return;
            } else if (  this.volume == 200) {
                volumeLevel = 20;
                return;
            }
            volumeLevel = volume / 20;
        }

    }

    public void cancel() {
        try {
            progress = 0;
            postInvalidate();
            progressTask.cancel();
        } catch (Exception e) {

        }
    }
}
