package com.wenld.birdcage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.seven.birdcage.R;
import com.wenld.birdcage.model.InFo;
import com.wenld.birdcage.setting.IatSettings;
import com.wenld.birdcage.ui.LoadingView;
import com.wenld.birdcage.ui.MicrophoneView;
import com.wenld.birdcage.ui.RecordView;
import com.wenld.birdcage.util.FucUtil;
import com.wenld.birdcage.util.JsonParser;
import com.wenld.birdcage.util.VoicePlayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.iflytek.cloud.VerifierResult.TAG;
import static com.wenld.birdcage.model.InFo.FLAG_PALY_VOICE;
import static com.wenld.birdcage.model.InFo.FLAG_PLAY_FILE;

/**
 * Created by Administrator on 2017/1/9.
 */

public class MainActivity extends Activity {

    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 语音听写对象
    private SpeechRecognizer mIat;

    ViewStub viewStub_Record;//录音时UI
    MicrophoneView microphoneView_layout_record;

    ViewStub viewStub_loading; //正在解析语音;
    LoadingView loadingView_layout_loading;//进度条

    private int nowModel = RecordView.MODEL_RECORD;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private Toast mToast;

    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语记安装助手类
    ApkInstaller mInstaller;


    TextView tv;
    Button btnStart;
    Button btnFlag;

    List<InFo> inFos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化基础数据 & UI
        initBase();
        //语音识别
        initIat();
        //语言合成
        initTts();


        initData();
//        gogogo();

        tv = (TextView) findViewById(R.id.tv_activity_main);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnFlag = (Button) findViewById(R.id.btn_flag);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showMicrophone();
                gogogo();
            }
        });
        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
            }
        });

        // TODO: 2017/1/12 测试用 后续 需要删除
//        showLoading();
    }

    private void initBase() {
        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mInstaller = new ApkInstaller(this);

//        mRecorfView = (RecordView2) findViewById(R.id.recordView);
////        mRecorfView.setCountdownTime(9);
////        mRecorfView.setModel(RecordView.MODEL_PLAY);
//
//        mRecorfView.start();
//        Timer timeTimer = new Timer(true);
//        timeTimer.schedule(new TimerTask() {
//            public void run() {
//                Message msg = new Message();
//                msg.what = 1;
//                handler.sendMessage(msg);
//            }
//        }, 20, 20);
//        mRecorfView.setOnCountDownListener(new RecordView.OnCountDownListener() {
//            @Override
//            public void onCountDown() {
////                Toast.makeText(this,"计时结束啦~~",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void initIat() {
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        // 设置参数
        setParam();
    }

    private void initData() {
        inFos.add(new InFo("唱歌", "我不会唱歌,嘿嘿嘿", FLAG_PALY_VOICE));
        inFos.add(new InFo("唱首歌", "我不会唱歌,嘿嘿嘿", FLAG_PALY_VOICE));

        inFos.add(new InFo("voice/buguniao.mp3", "布谷鸟"));
        inFos.add(new InFo("voice/muji.mp3", "母鸡"));
        inFos.add(new InFo("voice/bage.mp3", "八哥"));
        inFos.add(new InFo("voice/banma.mp3", "斑马"));
        inFos.add(new InFo("voice/baozi.mp3", "豹子"));
        inFos.add(new InFo("voice/gongji.mp3", "公鸡"));
        inFos.add(new InFo("voice/gongji.mp3", "打鸣"));
        inFos.add(new InFo("voice/huangli.WAV", "黄鹂"));
        inFos.add(new InFo("voice/zhegu.WAV", "鹧鸪"));

    }

    /**
     * 语音合成
     */
    private void initTts() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mInitListener);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoxin");
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
    }

    private int goPos = 0;

    private void gogogo() {
        goPos++;
        new Handler().postDelayed(new timer(goPos), 1000);
    }

    class timer implements Runnable {

        int pos = 0;

        public timer(int pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            if (pos == goPos) {
                // 不显示听写对话框
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    showTip("听写失败,错误码：" + ret);
                } else {
                    showTip(getString(R.string.text_begin));
                }
            }
        }

    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
            tv.setText("开始说话");

            showMicrophone();
            mIatResults.clear();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
            tv.setText(error.getPlainDescription(true));
            mIatResults.clear();
            showError();
            gogogo();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showLoading();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);        // TODO 最后的结果
            if (isLast) {
                showSuccess();

                StringBuffer resultBuffer = new StringBuffer();
                for (String key : mIatResults.keySet()) {
                    resultBuffer.append(mIatResults.get(key));
                }

                String str = resultBuffer.toString();
                tv.setText(str);

                for (InFo inFo : inFos) {
                    if (str.contains(inFo.getShibieString())) {
                        showTip(inFo.getShibieString());
                        switch (inFo.getType()) {
                            case FLAG_PALY_VOICE:
                                mTts.startSpeaking(inFo.getPalyString(), mTtsListener);
                                return;
                            case FLAG_PLAY_FILE:
                                VoicePlayUtils.ControlPlayVoice(inFo.getFileName(), MainActivity.this, new VoicePlayUtils.voiceListener() {
                                    @Override
                                    public void finish() {
                                        // TODO: 2017/1/12 结束后继续监听
//                        gogogo();
                                    }
                                });
                                return;
                        }
                    }
                }
                // TODO: 2017/1/12 继续监听
//        gogogo();
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
            tv.setText("当前正在说话，音量大小：" + volume + "\n返回音频数据：" + data.length);
            if (microphoneView_layout_record != null) {
                int v = (int) ((double) volume * 100 / 30);
                microphoneView_layout_record.setVolume(v);
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void showMicrophone() {
        if (viewStub_Record == null) {
            viewStub_Record = (ViewStub) findViewById(R.id.viewStub_record);
            viewStub_Record.setLayoutResource(R.layout.layout_record);
            View view = viewStub_Record.inflate();
            microphoneView_layout_record = (MicrophoneView) view.findViewById(R.id.microphoneView_layout_record);
        }
        microphoneView_layout_record.setModel(MicrophoneView.MODEL_RECORD);
        if (viewStub_loading != null)
            viewStub_loading.setVisibility(View.GONE);
        viewStub_Record.setVisibility(View.VISIBLE);
    }

    void showLoading() {
        if (viewStub_Record == null) {
            viewStub_Record = (ViewStub) findViewById(R.id.viewStub_record);
            viewStub_Record.setLayoutResource(R.layout.layout_record);
            View view = viewStub_Record.inflate();
            microphoneView_layout_record = (MicrophoneView) view.findViewById(R.id.microphoneView_layout_record);
        }
        microphoneView_layout_record.setModel(MicrophoneView.MODEL_LOADING);
        if (viewStub_loading != null)
            viewStub_loading.setVisibility(View.GONE);
        viewStub_Record.setVisibility(View.VISIBLE);
    }

    void showError() {
        if (viewStub_loading != null)
            viewStub_loading.setVisibility(View.GONE);
        if (viewStub_Record != null) {
            viewStub_Record.setVisibility(View.GONE);
        }
        if (microphoneView_layout_record != null) {
            microphoneView_layout_record.stop();
        }
    }

    void showSuccess() {
        if (viewStub_loading != null)
            viewStub_loading.setVisibility(View.GONE);
        if (viewStub_Record != null) {
            viewStub_Record.setVisibility(View.GONE);
        }
        if (microphoneView_layout_record != null) {
            microphoneView_layout_record.stop();
        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);
    }

    int ret = 0; // 函数调用返回值

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        //选择本地&在线混合 识别
        mEngineType = SpeechConstant.TYPE_MIX;
        /**
         * 选择本地听写 判断是否安装语记,未安装则跳转到提示安装页面
         */
        if (!SpeechUtility.getUtility().checkServiceInstalled()) {
            mInstaller.install();
        } else {
            String result = FucUtil.checkLocalResource();
            if (!TextUtils.isEmpty(result)) {
                showTip(result);
            }
        }

        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "40000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();

            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
            VoicePlayUtils.stopPlayVoice();
        } catch (Exception e) {

        }
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };


    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            startPalyAnim();
//            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            stopPalyAnim();
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            startPalyAnim();
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {

//                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
            gogogo();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
}
