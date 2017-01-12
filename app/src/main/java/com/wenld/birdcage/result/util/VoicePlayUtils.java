
package com.wenld.birdcage.result.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;


//声音播放
public class VoicePlayUtils {
    private static MediaPlayer mediaPlayer = null;
    private static boolean isPlaying = false;
    private static Context mcontext;
    private static AnimationDrawable voiceAnimation = null;

    private VoicePlayUtils() {
        throw new AssertionError();
    }

    public static void stopPlayVoice() { // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (voiceAnimation != null) {
//            if (AnimType) {
//                voiceIconView.setImageResource(R.drawable.chat_sender_voice_playing_no);
//            } else {
//                voiceIconView.setImageResource(R.drawable.chat_receiver_voice_playing_no);
//            }
            voiceAnimation.stop();
        }
        isPlaying = false;
    }

    public static void playVoice(String filePath, Context context) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        // if (true) {//判断播放模式
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        // }
        // else {
        // audioManager.setSpeakerphoneOn(false);// 关闭扬声器
        // audioManager.setMode(AudioManager.MODE_IN_CALL); // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
        // mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        // }
        try {
            if (new File(filePath).exists()) {
                mediaPlayer.setDataSource(filePath);
            } else {
                AssetFileDescriptor fileDescriptor = null;
                try {
                    fileDescriptor = context.getAssets().openFd(filePath);
                    mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
                } catch (Exception e) {

                }
            }

            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice();
                    if (listener != null) {
                        listener.finish();
                    }
                }
            });
            isPlaying = true;
            mediaPlayer.start();
        } catch (Exception e) {
            if (listener != null) {
                listener.finish();
            }
        }
    }

    // 显示播放动画
    private static void showAnimation() {
//        if (AnimType) {
//            voiceIconView.setImageResource(+R.anim.voice_to_icon);
//        } else {
//            voiceIconView.setImageResource(+R.anim.voice_from_icon);
//        }
//        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
//        voiceAnimation.start();
    }


    // type:朝向//view:放动画的View
    public static void ControlPlayVoice(String filePath, Context context) {
        ControlPlayVoice(filePath, context, null);
    }


    public static void ControlPlayVoice(String filePath, Context context, voiceListener listene) {
        listener = listene;
        if (isPlaying) {
            stopPlayVoice();
        } else {
            isPlaying = !isPlaying;
            playVoice(filePath, context);
            showAnimation();
        }
    }

    private static voiceListener listener;

    public interface voiceListener {
        void finish();
    }
}
