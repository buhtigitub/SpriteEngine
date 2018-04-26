package com.first.helloworld.game;

import android.graphics.Rect;

/**
 * Created by Tony on 02/06/2016.
 */
public class GameObject {
    private double x, y, xV, yV, xA, yA;
    private int width, height, frameX = 0, frameY = 0;

    public GameObject(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /*
    Physics.
     */
    public void updatePos() {
        x += xV;
        y += yV;
        xV += xA;
        yV += yA;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double[] getPos() {
        double[] pos = {x, y};
        return pos;
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getXV() {
        return xV;
    }

    public double getYV() {
        return yV;
    }

    public void setXV(double xV) {
        this.xV = xV;
    }

    public void setYV(double yV) {
        this.yV = yV;
    }

    public void setVel(double[] vel) {
        this.xV = vel[0];
        this.yV = vel[1];
    }

    public double getXA() {
        return xA;
    }

    public double getYA() {
        return yA;
    }

    public void setXA(double xA) {
        this.xA = xA;
    }

    public void setYA(double yA) {
        this.yA = yA;
    }

    public void speedCheck(int max) {
        if (getXV() > max)
            setXV(max);
        else if (getXV() < -max)
            setXV(-max);

        if (getYV() > max)
            setYV(max);
        else if (getYV() < -max)
            setYV(-max);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isIntersecting(GameObject o) {
        Rect a = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        Rect b = new Rect((int) o.getX(), (int) o.getY(), (int) o.getX() + o.getWidth(), (int) o.getY() + o.getHeight());
        return a.intersect(b);
    }

    public double getMidX() {
        return x + (width / 2);
    }

    public double getMidY() {
        return y + (height / 2);
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
    }

    public void setFramePos(int[] framePos) {
        this.frameX = framePos[0];
        this.frameY = framePos[1];
    }
}
