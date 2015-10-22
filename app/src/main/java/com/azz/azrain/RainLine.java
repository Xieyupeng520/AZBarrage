package com.azz.azrain;

import java.util.Random;

/**
 * Created by AZZ on 15/10/20 21:13.
 */
public class RainLine {
    private Random random = new Random();

    private int startX;
    private int startY;
    private int stopX;
    private int stopY;

    private int deltaX = 20;
    private int deltaY = 30;

    private int maxX; //x最大范围
    private int maxY; //y最大范围


    public RainLine(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
//        resetRandom();
        initRandom();
    }

    public void initRandom() {

        startX = random.nextInt(maxX);
        startY = random.nextInt(maxY);

        deltaX = random.nextInt(20);
        deltaY = 20 + random.nextInt(30);

        stopX = startX + deltaX;
        stopY = startY + deltaY;
    }

    /**
     * 随机初始化
     */
    public void resetRandom() {
        if (random.nextBoolean()) { //随机 true, 雨点从x轴出来
            startY = 0;
            startX = random.nextInt(maxX);
            deltaX = random.nextInt(20);
        } else { //随机 false，雨点从y轴出来
            startX = 0;
            startY = random.nextInt(maxY);
            deltaY = 20 + random.nextInt(30);
        }
        stopX = startX + deltaX;
        stopY = startY + deltaY;
    }
    /**
     * 下雨
     */
    public void rain() {
        startX += deltaX;
        stopX += deltaX;
        startY += deltaY;
        stopY += deltaY;
    }

    /**
     * @return 是否出界
     */
    public boolean outOfBounds() {
        if (getStartY() >= maxY || getStartX() >= maxX) {
            resetRandom();
            return true;
        }
        return false;
    }

    public int getStartX() {
        return startX;
    }

    public RainLine setStartX(int startX) {
        this.startX = startX;
        return this;
    }

    public int getStartY() {
        return startY;
    }

    public RainLine setStartY(int startY) {
        this.startY = startY;
        return this;
    }

    public int getStopX() {
        return stopX;
    }

    public RainLine setStopX(int stopX) {
        this.stopX = stopX;
        return this;
    }

    public int getStopY() {
        return stopY;
    }

    public RainLine setStopY(int stopY) {
        this.stopY = stopY;
        return this;
    }
}
