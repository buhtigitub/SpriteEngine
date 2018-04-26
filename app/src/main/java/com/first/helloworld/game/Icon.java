package com.first.helloworld.game;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.first.helloworld.engine.graphics.Sprite;

/**
 * Created by Tony on 06/06/2016.
 */
public class Icon {

    private int x, y, width, height, bColor, fColor, frameX, frameY;
    private boolean visible, touched, largeDimensions;

    /*  The 'visible' constructor argument must be passed as false so that it's not indiscriminately
    *   clickable before the frame is changed from the initial program state.
    * */
    public Icon(int width, int height, boolean visible, boolean largeDimensions) {
        this.width = width;
        this.height = height;
        this.visible = visible;
        this.largeDimensions = largeDimensions;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public boolean getLargeDimensions() {
        return largeDimensions;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getPos() {
        int[] pos = {x, y};
        return pos;
    }

    public void setPos(int[] pos) {
        this.x = pos[0];
        this.y = pos[1];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isIntersecting(int[] input, boolean largeDimensions) {
        Rect a;
        if (!largeDimensions)
            a = new Rect(x, y, x + width, y + height);
        else
            a = new Rect(x - width*2, y - height*2, x + width * 2, y + height * 2);
        return a.contains(input[0], input[1]);
    }

    public int getBColor() {
        return bColor;
    }

    public void setBColor(int color) {
        bColor = color;
    }

    public int getFColor() {
        return fColor;
    }

    public void setFColor(int color) {
        fColor = color;
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