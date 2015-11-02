package com.azz.azbarrage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by AZZ on 15/10/18 19:43.
 */
public class BarrageView extends TextView {

    private static final String TAG = "BarrageView";

    private Paint paint = new Paint(); //画布参数

    private Random random = new Random(); //随机数

    private RollThread rollThread; //滚动线程

    private int textSize = 30; //字体大小
    public static final int TEXT_MIN = 12;
    public static final int TEXT_MAX = 60;
    //字体颜色
    private int color = 0xffffffff;

    private int windowWidth; //屏幕宽
    private int windowHeight; //屏幕高

    private int posX; //x坐标
    private int posY = textSize; //y坐标

    private OnRollEndListener mOnRollEndListener;

    public BarrageView(Context context) {
        super(context);
        init();
    }

    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    protected void init() {
        //1.设置文字大小
//        textSize = TEXT_MIN + random.nextInt(TEXT_MAX - TEXT_MIN);
        paint.setTextSize(textSize);

        //2.设置文字颜色
        color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        paint.setColor(color);

        //3.得到屏幕宽高
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        windowWidth = rect.width();
        windowHeight = rect.height();

        //4.设置x为屏幕宽
        posX = windowWidth;

        //5.设置y为屏幕高度内内随机，需要注意的是，文字是以左下角为起始点计算坐标的，所以要加上TextSize的大小
        posY = textSize + random.nextInt(windowHeight - textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText(getShowText(), posX, posY, paint);
        if (rollThread == null) {
            rollThread = new RollThread();
            rollThread.start();
        }
    }

    /**
     * @return 显示的文字
     */
    private String getShowText() {
        if (getText() != null && !getText().toString().isEmpty()) {
            return getText().toString();
        } else {
            return getResources().getString(R.string.default_text);
        }
    }


    /**
     * 动画逻辑处理
     */
    private void animLogic() {
        posX -= 8;
    }

    private boolean needStopRollThread() {
        if (posX <= -paint.measureText(getShowText())) {
            return true;
        }
        return false;
    }

    class RollThread extends Thread {
        private Object mPauseLock; //线程锁
        private boolean mPauseFlag; //标签：是否暂停

        RollThread() {
            mPauseLock = new Object();
            mPauseFlag = false;
        }
        @Override
        public void run() {
            while (true) {
                //15/11/01更新：check pause,解决按Home后一分钟以上回到程序会发生满屏线程阻塞
                checkPause();

                //1.动画逻辑
                animLogic();
                //2.绘制图像
                postInvalidate();
                //3.延迟，不然会造成执行太快动画一闪而过
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //关闭线程逻辑判断
                if (needStopRollThread()) {
                    Log.d(TAG, "线程停止-" + getShowText());

                    if (mOnRollEndListener != null) {
                        mOnRollEndListener.onRollEnd();
                    }
                    post(new Runnable() { //从父类中移除本view
                        @Override
                        public void run() {
                            ((ViewGroup) BarrageView.this.getParent()).removeView(BarrageView.this);
                        }
                    });
                    break;
                }
            }
        }
        public void onPause() {
            synchronized (mPauseLock) {
                mPauseFlag = true;
            }
        }
        public void onResume() {
            synchronized (mPauseLock) {
                mPauseFlag = false;
                Log.i(TAG, "线程恢复-" + getShowText());
//                mPauseLock.notifyAll();
                mPauseLock.notify();
            }
        }
        private void checkPause() {
            synchronized (mPauseLock) {
                if (mPauseFlag) {
                    try {
                        Log.e(TAG, "线程挂起-" + getShowText());
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param onRollEndListener 设置滚动结束监听器
     */
    public void setOnRollEndListener(OnRollEndListener onRollEndListener) {
        this.mOnRollEndListener = onRollEndListener;
    }

    /**
     * 滚动结束接听器
     */
    interface OnRollEndListener {
        void onRollEnd();
    }

    /**
     * 15/11/01 测试按Home后一分钟以上回到程序会发生满屏线程阻塞
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (rollThread == null) {
            return;
        }
        if (View.GONE == visibility) {
            rollThread.onPause();
        } else {
            rollThread.onResume();
        }

    }

}