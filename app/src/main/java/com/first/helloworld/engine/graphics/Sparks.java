package com.first.helloworld.engine.graphics;

import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Tony on 04/07/2016.
 */
public class Sparks {

    private float[] x, y, xV, yV;
    private int boundX, boundY;
    private Paint paint;
    private Random r = new Random();

    public Sparks(float xO, float yO, int n, Paint paint, int boundX, int boundY) {
        x = new float[n];
        y = new float[n];

        xV = new float[n];
        yV = new float[n];

        this.paint = paint;
        this.boundX = boundX;
        this.boundY = boundY;

        for (int i = 0; i < n; i++) {
            x[i] = xO;
            y[i] = yO;

            xV[i] = r.nextInt(80) / 5 - 8;
            yV[i] = r.nextInt(80) / 5 - 8;
        }
    }

    public void update() {
        for (int i = 0; i < x.length; i++) {
            if (x[i]+xV[i]<0) {
                xV[i] = (float) (Math.abs(xV[i])*0.5);
                yV[i] *= 0.8;
            }
            else if (x[i]+xV[i]>boundX) {
                xV[i] = (float) (-Math.abs(xV[i])*0.5);
                yV[i] *= 0.8;
            }
            if (y[i]+yV[i]<0) {
                yV[i] = (float) (Math.abs(yV[i])*0.5);
                xV[i] *= 0.8;
            }
            else if (y[i]+yV[i]>boundY) {
                yV[i] = (float) (-Math.abs(yV[i])*0.5);
                xV[i] *= 0.8;
            }
            x[i] += xV[i];
            y[i] += yV[i];
        }

        paint.setAlpha((paint.getAlpha()>4)?paint.getAlpha()-5:paint.getAlpha());
    }

    public int getArrayLength() {
        return x.length;
    }

    public float getX(int index) {
        return x[index];
    }

    public float getY(int index) {
        return y[index];
    }

    public float getXV(int index) {
        return xV[index];
    }

    public float getYV(int index) {
        return yV[index];
    }

    public Paint getPaint() {
        return paint;
    }
}
