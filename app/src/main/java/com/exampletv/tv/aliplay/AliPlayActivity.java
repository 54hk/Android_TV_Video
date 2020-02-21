package com.exampletv.tv.aliplay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.AliVcMediaPlayer;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.exampletv.tv.BorderView;
import com.exampletv.tv.R;

import static java.lang.System.currentTimeMillis;

public class AliPlayActivity extends Activity {

    private SurfaceView mSurfaceView;

    private AliyunVodPlayer aliyunVodPlayer;
    //伙ing
    private String url = "http://v.artfirecn.com/f366ec86328d47e09b32ff3959e0d0f2/0973bea330974871ad1abcc13f42f41a-aabbfa49cca45f35df36ce5903dd3858-ld.m3u8";
    //    private String url = "http://artfire-live.oss-cn-beijing.aliyuncs.com/artfire-test/c3689/2019-12-17-18-31-02_2019-12-17-18-32-11.m3u8";
    private SeekBar seekBar;
    private TextView tvFastShow;
    private RelativeLayout layoutFastShow;

    private int longRight = 0;
    private int currentPosition = 0;
    private boolean isFirst = true;
    protected static final int MESSAGE_SHOW_PROGRESS = 1;
    protected boolean isShowing;
    protected boolean isDragging; // 改变进度
    private Handler progressUpdateTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showVideoProgressInfo();
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == MESSAGE_SHOW_PROGRESS) {
            }

            //  单击处理
            if (null!=msg.obj && ((int) msg.obj * 20 / 1000) <= 0) {
               /* if (msg.what == 1001) {
                    layoutFastShow.setVisibility(View.VISIBLE);
                    tvFastShow.setText("快退" + 10 + "s");
                    setNProgress(2,(int) msg.obj * 20);
//                aliyunVodPlayer.seekTo((int) (aliyunVodPlayer.getCurrentPosition() - (int) msg.obj * 20));
//                alreadyPlayerTime.setText(TimeUtils.secToTime((int) (player.getCurrentPosition()-(long)msg.obj*20)/1000));
                } else if (msg.what == 1002) {
                    layoutFastShow.setVisibility(View.VISIBLE);
                    tvFastShow.setText("快进" + ((int) msg.obj * 20 / 1000) + "s");
                    setNProgress(1,(int) msg.obj * 20);
//                aliyunVodPlayer.seekTo((int) (aliyunVodPlayer.getCurrentPosition() + (int) msg.obj * 20));
//                alreadyPlayerTime.setText(TimeUtils.secToTime((int) (player.getCurrentPosition()+(long)msg.obj*20 )/1000));
                }*/
                //长按点击处理
            } else {
                if (msg.what == 1001) {
                    layoutFastShow.setVisibility(View.VISIBLE);
                    tvFastShow.setText("快退" + ((int) msg.obj * 20 / 1000) + "s");
                    setNProgress(2,(int) msg.obj * 20);
//                aliyunVodPlayer.seekTo((int) (aliyunVodPlayer.getCurrentPosition() - (int) msg.obj * 20));
//                alreadyPlayerTime.setText(TimeUtils.secToTime((int) (player.getCurrentPosition()-(long)msg.obj*20)/1000));
                } else if (msg.what == 1002) {
                    layoutFastShow.setVisibility(View.VISIBLE);
                    tvFastShow.setText("快进" + ((int) msg.obj * 20 / 1000) + "s");
                    setNProgress(1,(int) msg.obj * 20);
//                aliyunVodPlayer.seekTo((int) (aliyunVodPlayer.getCurrentPosition() + (int) msg.obj * 20));
//                alreadyPlayerTime.setText(TimeUtils.secToTime((int) (player.getCurrentPosition()+(long)msg.obj*20 )/1000));
                }
            }


        }
    };
    private TextView tTime;

    //长按跟踪seek指变换
    public void setNProgress(int type, int newPosition) {
        //设置时长显示
        int duration = (int) aliyunVodPlayer.getDuration();
        if (type == 1) { //前进
            tTime.setText(getTimeStr((int) (Math.ceil((double) (currentPosition + newPosition) / 1000))) + "/" + getTimeStr((int) Math.ceil(duration / 1000)));
            //设置seekBar进度
            seekBar.setProgress(currentPosition + newPosition);
        } else { //后退
            if ((currentPosition - newPosition) > 0) {
                tTime.setText(getTimeStr((int) (Math.ceil((double) (currentPosition - newPosition) / 1000))) + "/" + getTimeStr((int) Math.ceil(duration / 1000)));
                //设置seekBar进度
                seekBar.setProgress(currentPosition - newPosition);
            } else {
                tTime.setText(getTimeStr(0) + "/" + getTimeStr((int) Math.ceil(duration / 1000)));
                //设置seekBar进度
                seekBar.setProgress(currentPosition - newPosition);
            }
        }
        int percent = aliyunVodPlayer.getBufferingPosition();
        seekBar.setSecondaryProgress(percent * 10);
    }

    //长按松开按键后:设置seekBar时长
    public long setProgress() {
        int curPosition = (int) aliyunVodPlayer.getCurrentPosition();
        int duration = (int) aliyunVodPlayer.getDuration();
        int bufferPosition = aliyunVodPlayer.getBufferingPosition();
        seekBar.setSecondaryProgress(bufferPosition);
        seekBar.setMax(duration);
        //设置seekBar进度
        if (duration - curPosition < 1000) {
            seekBar.setProgress(duration);
        } else {
            seekBar.setProgress(curPosition);
        }
        //设置时长显示
        tTime.setText(getTimeStr((int) (Math.ceil((double) curPosition / 1000))) + "/" + getTimeStr((int) Math.ceil(duration / 1000)));
        return duration;
    }

    protected final SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { //暂无用,没有触发
            isDragging = true;
            isShowing = true;
            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {//暂无用,没有触发
            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
            isShowing = true;
            isDragging = false;
            handler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
        }
    };


    // 进度条的改变
    private void showVideoProgressInfo() {
        setProgress();
        progressUpdateTimer.removeMessages(0);
        progressUpdateTimer.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_play);

        keepScreenLongLight(this, true);
        BorderView border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);

        RelativeLayout relativeLayout = findViewById(R.id.main);
        border.attachTo(relativeLayout);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aliyunVodPlayer.stop();
        aliyunVodPlayer.release();
        progressUpdateTimer.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
    }

    private void initView() {
        mSurfaceView = findViewById(R.id.m_surface_view);
        seekBar = (SeekBar) findViewById(R.id.sb);
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(seekListener);
        tTime = findViewById(R.id.tv_time);

        tvFastShow = findViewById(R.id.tv_fast_show);
        layoutFastShow = findViewById(R.id.layout_fast_show);

        aliyunVodPlayer = new AliyunVodPlayer(this);
        initVideo();
        initSurface();
        initListener();
    }

    private void initListener() {

    }

    //准备视频
    private void preparePlay() {
        if (!TextUtils.isEmpty(url)) {
            AliyunLocalSource.AliyunLocalSourceBuilder asb
                    = new AliyunLocalSource.AliyunLocalSourceBuilder();
            asb.setSource(url);
            aliyunVodPlayer.prepareAsync(asb.build());
        }
    }

    //初始化SurfaceView
    private void initSurface() {

        mSurfaceView.setZOrderOnTop(false);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(holder);
                preparePlay();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliyunVodPlayer.surfaceChanged();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }


    // 初始化  aliyunVodPlayer 信息
    private void initVideo() {
        aliyunVodPlayer.setReferer(Globals.LIVE_SECURITY_CHAIN);
        aliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {

                aliyunVodPlayer.start();
            }
        });
        aliyunVodPlayer.setOnFirstFrameStartListener(new IAliyunVodPlayer.OnFirstFrameStartListener() {
            @Override
            public void onFirstFrameStart() {
                showVideoProgressInfo();
                handler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
            }
        });
        aliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
            @Override
            public void onError(int arg0, int arg1, String msg) {
                //出错时处理，查看接口文档中的错误码和错误消息
                //打点
                Log.e("TTTT", "setOnErrorListener" + msg + "argo === " + arg0 + "-----------arg1===========" + arg1);
            }
        });
        aliyunVodPlayer.setOnLoadingListener(new IAliyunVodPlayer.OnLoadingListener() {
            @Override
            public void onLoadStart() {
                Log.e("TTTT", "onLoadStart");
            }

            @Override
            public void onLoadEnd() {

            }

            @Override
            public void onLoadProgress(int i) {

            }
        });
        aliyunVodPlayer.setOnBufferingUpdateListener(new IAliyunVodPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(int i) {
            }
        });
        aliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                //播放正常完成时触发
//                imgPlay.setImageResource(R.mipmap.vod_play_start);
//                stopUpdateTimer();
//                LogUtil.e("playsss", "onCOmpletion");
            }
        });
        aliyunVodPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {
                //seek完成时触发
                Log.e("TTTT", "onSeekComplete");

            }
        });
        aliyunVodPlayer.setOnStoppedListner(new IAliyunVodPlayer.OnStoppedListener() {
            @Override
            public void onStopped() {
                //使用stop功能时触发
//                LogUtil.e("playsss", "onStopped");
            }
        });
        aliyunVodPlayer.setOnChangeQualityListener(new IAliyunVodPlayer.OnChangeQualityListener() {
            @Override
            public void onChangeQualitySuccess(String finalQuality) {
                //视频清晰度切换成功后触发
                Log.e("playsss", "onChangeQualitySuccess");
            }

            @Override
            public void onChangeQualityFail(int code, String msg) {
                //视频清晰度切换失败时触发
            }
        });
    }

    //    https://blog.csdn.net/jun5753/article/details/78665886
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_ENTER:
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (null != aliyunVodPlayer && aliyunVodPlayer.getPlayerState() == IAliyunVodPlayer.PlayerState.Paused) {
                    aliyunVodPlayer.resume();
//                    Toast("你按下中间键 == 暂停");
                } else {
                    aliyunVodPlayer.pause();
//                    Toast("你按下中间键 == 播放");
                }
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
//                Toast("你按下下方向键");
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:

//                Log.e("TTTT", "你按下左方向键");
                progressUpdateTimer.removeMessages(0);
                if (isFirst) {
                    aliyunVodPlayer.pause();
                    longRight = (int) currentTimeMillis();
                    currentPosition = (int) aliyunVodPlayer.getCurrentPosition();
                    isFirst = false;
                }
                int timeLeft = Long.valueOf(System.currentTimeMillis() - longRight).intValue();
                Message messageLeft = Message.obtain();
                messageLeft.what = 1001;
                messageLeft.obj = timeLeft;
                handler.sendMessage(messageLeft);
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                Toast("你按下右方向键==="+sb.getProgress());
//                Log.e("TTTT", "你按下右方向键");
                progressUpdateTimer.removeMessages(0);
                if (isFirst) {
                    aliyunVodPlayer.pause();
                    longRight = (int) currentTimeMillis();
                    currentPosition = (int) aliyunVodPlayer.getCurrentPosition();
                    isFirst = false;
                }
                int timeRight = Long.valueOf(System.currentTimeMillis() - longRight).intValue();
                Message messageRight = Message.obtain();
                messageRight.what = 1002;
                messageRight.obj = timeRight;
                handler.sendMessage(messageRight);
                break;
            //上
            case KeyEvent.KEYCODE_DPAD_UP:
//                Toast("你按下上方向键");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_ENTER:
                Log.e("TTTT", "你松开了Enter/OK键");
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Log.e("TTTT", "你松开了中间键");
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.e("TTTT", "你松开了下方向键");
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.e("TTTT", "你松开了左方向键");
                progressUpdateTimer.sendEmptyMessageDelayed(0, 1000);
                layoutFastShow.setVisibility(View.GONE);
                if (!isFirst) {
                    int time = Long.valueOf(System.currentTimeMillis() - longRight).intValue();
                    aliyunVodPlayer.seekTo((currentPosition - time * 20) >= 0 ? currentPosition - time * 20 : 0);
                    aliyunVodPlayer.start();
                    isFirst = true;
                }

                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.e("TTTT", "你松开了右方向键");
                progressUpdateTimer.sendEmptyMessageDelayed(0, 1000);
                layoutFastShow.setVisibility(View.GONE);
                if (!isFirst) {
                    int time = Long.valueOf(System.currentTimeMillis() - longRight).intValue();
                    aliyunVodPlayer.seekTo((currentPosition + time * 20) <= aliyunVodPlayer.getDuration()
                            ? currentPosition + time * 20 : (int) aliyunVodPlayer.getDuration());
                    aliyunVodPlayer.start();
                    isFirst = true;
                }
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                break;
        }

        return super.onKeyUp(keyCode, event);
    }


    public void Toast(String mgs) {
        Toast.makeText(this, mgs, Toast.LENGTH_SHORT).show();
    }

    /**
     * 将int型的时间转成##:##格式的时间
     */
    public static String getTimeStr(int timeInt) {
        int mi = 1 * 60;
        int hh = mi * 60;

        long hour = (timeInt) / hh;
        long minute = (timeInt - hour * hh) / mi;
        long second = timeInt - hour * hh - minute * mi;

        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if (hour > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return strMinute + ":" + strSecond;
        }
    }

    /**
     * 是否使屏幕常亮
     *
     * @param activity
     */
    public void keepScreenLongLight(Activity activity, boolean isOpenLight) {

        Window window = activity.getWindow();
        if (isOpenLight) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }
}
