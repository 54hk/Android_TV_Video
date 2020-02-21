package com.exampletv.tv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exampletv.tv.aliplay.AliPlayActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    private void initView() {

        BorderView border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);

        RelativeLayout relativeLayout = findViewById(R.id.main);
        border.attachTo(relativeLayout);

        TextView text1 = findViewById(R.id.tv1);
        text1.setOnClickListener(this);
        TextView text2 = findViewById(R.id.tv2);
        text2.setOnClickListener(this);
        TextView tv3 = findViewById(R.id.tv3);
        tv3.setOnClickListener(this);
        TextView tv4 = findViewById(R.id.tv4);
        tv4.setOnClickListener(this);


        RelativeLayout relativeLayout2 = findViewById(R.id.main2);
        border.attachTo(relativeLayout2);
        RelativeLayout relativeLayout3 = findViewById(R.id.main3);
        border.attachTo(relativeLayout3);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv1:
                intent.setClass(this, AliPlayActivity.class);
                startActivity(intent);
                toast("tv1 --- 监听事件");
                break;
            case R.id.tv2:
                intent.setClass(this, MySeekActivty.class);
                startActivity(intent);
                toast("tv2 --- 监听事件");
                break;
            case R.id.tv3:
                intent.setClass(this, SeekActivity.class);
                toast("tv3 --- 监听事件");
                break;
            case R.id.tv4:
                toast("tv4 -- 监听事件");
                break;
        }
    }

    public void toast(String mgs) {
        Toast.makeText(mContext, mgs, Toast.LENGTH_SHORT).show();
    }
}
