package com.wenld.birdcage.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/12.
 */

public class InFo implements Serializable {
    public static final String FLAG_PLAY_FILE = "FLAG_Play_File";//播放文件
    public static final String FLAG_PALY_VOICE = "FLAG_Paly_voice";//播放文字

    String fileName;    //播放文件的地址
    String shibieString;//识别的语音
    String palyString;//说的语音
    String type;// 类别  播放mp3 还是 合成声音

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPalyString() {
        return palyString;
    }

    public void setPalyString(String palyString) {
        this.palyString = palyString;
    }

    public InFo(String fileName, String bird) {
        this.fileName = fileName;
        this.shibieString = bird;
        type = FLAG_PLAY_FILE;
    }

    public InFo(String fileName, String shibieString, String palyString, String type) {
        this.fileName = fileName;
        this.shibieString = shibieString;
        this.palyString = palyString;
        this.type = type;
    }

    public InFo(String shibieString, String palyString, String type) {
        this.shibieString = shibieString;
        this.palyString = palyString;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getShibieString() {
        return shibieString;
    }

    public void setShibieString(String shibieString) {
        this.shibieString = shibieString;
    }
}