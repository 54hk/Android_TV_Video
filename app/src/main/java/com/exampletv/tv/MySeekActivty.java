package com.exampletv.tv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

import java.util.Formatter;
import java.util.Locale;

public class MySeekActivty extends AppCompatActivity {
    /**
     * 进度拖动条监听器
     */
    private SeekBar.OnSeekBarChangeListener mSeekBarListener = new SeekBar.OnSeekBarChangeListener() {

        private boolean isTouchSeeked;

        // 通知进度已经被修改
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            Log.e("TTTT", "onProgressChanged------isTouchSeeked=" +isTouchSeeked);
            if (isTouchSeeked) {
                mSeekBar.showSeekDialog(makeTimeString(progress));//动态展示当前播放时间
            } else {
                mSeekBar.hideSeekDialog();
            }
        }

        // 通知用户已经开始一个触摸拖动手势
        public void onStartTrackingTouch(SeekBar seekBar) {
//            showControlView(3600000);

            Log.e("TTTT", "onStartTrackingTouch------");
            isTouchSeeked = true;
        }

        // 通知用户触摸手势已经结束
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.e("TTTT", "onStopTrackingTouch------");
            Message msg = Message.obtain();
            msg.what = PROGRESS_SEEKTO;
            msg.arg1 = seekBar.getProgress();
            mHandler.removeMessages(PROGRESS_SEEKTO);
            mHandler.sendMessageAtTime(msg, 1000);// 1秒之后开始发送更新进度的消息
            isTouchSeeked = false;
//            showControlView(sDefaultTimeout);
        }
    };
    private final int PROGRESS_SEEKTO = 1;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == PROGRESS_SEEKTO) {
                Log.e("TTTT", "handleMessage------");

            }
        }
    };
    private MySeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_seek_activty);
        mSeekBar = (MySeekBar) findViewById(R.id.seek_progress);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarListener);
    }


    /**
     * 格式化的Builder
     */
    private StringBuilder sFormatBuilder = new StringBuilder();
    /**
     * 格式化的Formatter
     */
    private Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
    /**
     * 格式化的相关属性
     */
    private final Object[] sTimeArgs = new Object[3];

    /**
     * 转换进度值为时间
     *
     * @param secs
     * @return
     */
    private String makeTimeString(int secs) {
        /**
         * %[argument_index$][flags][width]conversion 可选的
         * argument_index 是一个十进制整数，用于表明参数在参数列表中的位置。第一个参数由 "1$"
         * 引用，第二个参数由 "2$" 引用，依此类推。 可选 flags
         * 是修改输出格式的字符集。有效标志集取决于转换类型。 可选 width
         * 是一个非负十进制整数，表明要向输出中写入的最少字符数。 可选 precision
         * 是一个非负十进制整数，通常用来限制字符数。特定行为取决于转换类型。 所需 conversion
         * 是一个表明应该如何格式化参数的字符。给定参数的有效转换集取决于参数的数据类型。
         */
        // id="format">%1$02d:%2$02d:%3$02d</xliff:g>
        sFormatBuilder.setLength(0);
        secs = secs / 1000;
        Object[] timeArgs = sTimeArgs;
        timeArgs[0] = secs / 3600; // 秒
        timeArgs[1] = (secs % 3600) / 60; // 分
        timeArgs[2] = (secs % 3600 % 60) % 60; // 时
        return sFormatter.format("HH:mm:ss", timeArgs).toString().trim();
    }
}
