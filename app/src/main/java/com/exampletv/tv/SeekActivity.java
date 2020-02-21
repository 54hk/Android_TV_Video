package com.exampletv.tv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekActivity extends AppCompatActivity {

    private TextView tv_sb;
    private SeekBar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_sb = (TextView) findViewById(R.id.tv_sb);
        sb = (SeekBar) findViewById(R.id.sb);
        //SeekBar的监听事件
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //监听点击时
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("TTTT", "开始");
                tv_sb.setText("开始");
            }

            //监听滑动时
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("TTTT", "变化" + progress);
                tv_sb.setText("进度条" + progress);
            }

            //监听停止时
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("TTTT", "结束");
                tv_sb.setText("结束");
            }
        });
    }
}
