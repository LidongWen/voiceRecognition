package com.wenld.birdcage.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.seven.birdcage.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/1/12.
 */

public class LoadingView extends View {

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
    private int[] doughnutColors = new int[]{0xFFDAF6FE, 0xFF45C3E5, 0xFF45C3E5, 0xFF45C3E5, 0xFFDAF6FE, 0xFF45C3E5, 0xFF45C3E5, 0xFF45C3E5};


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

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
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
        drawRing(canvas);
        drawText(canvas);
        drawProgress(canvas);
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
        mPaint.setStrokeWidth(dip2px(mContext, widthing));
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
        mPaint.setStrokeWidth(dip2px(mContext, widthing));
//        mPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, doughnutColors, null));
        RectF oval = new RectF(dip2px(mContext, pandding)
                , dip2px(mContext, pandding)
                , getWidth() - dip2px(mContext, pandding)
                , getHeight() - dip2px(mContext, pandding));
        canvas.drawArc(oval, progress, 90, false, mPaint);    //绘制圆弧
        mPaint.reset();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void start() {
        cancel();
        progressTimer.schedule(progressTask = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
            }
        }, 0, 5);
    }

    public void cancel() {
        try {
            postInvalidate();
            progressTask.cancel();
        }catch (Exception e){

        }
    }
}
