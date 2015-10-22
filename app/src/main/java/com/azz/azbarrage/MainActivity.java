package com.azz.azbarrage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import java.util.Random;


public class MainActivity extends Activity {
    //两两弹幕之间的间隔时间
    public static final int DELAY_TIME = 500;

    private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //读取文字资源
        final String[] texts = getResources().getStringArray(R.array.default_text_array);

        //设置宽高全屏
        final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final Handler handler = new Handler();
        Runnable createBarrageView = new Runnable() {
            @Override
            public void run() {
                //新建一条弹幕，并设置文字
                final BarrageView barrageView = new BarrageView(MainActivity.this);
                barrageView.setText(texts[random.nextInt(texts.length)]); //随机设置文字
                addContentView(barrageView, lp);

                //发送下一条消息
                handler.postDelayed(this, DELAY_TIME);
            }
        };
        handler.post(createBarrageView);
    }
}
