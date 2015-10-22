package com.azz.azrain;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.azz.azbarrage.BarrageView;
import com.azz.azbarrage.R;

import java.util.Random;


public class RainActivity extends Activity {
    //两两弹幕之间的间隔时间
    public static final int DELAY_TIME = 800;

    private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        System.exit(0);
    }
}
