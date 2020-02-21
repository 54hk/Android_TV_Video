package com.exampletv.tv;

import com.alivc.player.AliVcMediaPlayer;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化播放器
        AliVcMediaPlayer.init(getApplicationContext());
    }
}
