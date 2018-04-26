package com.first.helloworld.game.menu;

import android.graphics.Paint;
import android.graphics.Rect;

import com.first.helloworld.engine.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 11/06/2016.
 */
public class MenuList {

    private int x, y, width, height, bColor, index, frameX, frameY;
    private Paint textPaint = new Paint();
    private boolean visible = true;
    private List<String> menuItems = new ArrayList<>();
    private Sprite background;
    private boolean centered, clickable;

    public MenuList(int bColor, int textColor, int textSize, boolean centered, boolean clickable) {
        this.bColor = bColor;
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        this.centered = centered;
        this.clickable = clickable;
    }

    public void add(String item) {
        menuItems.add(item);
        setSize();
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

    public void setSize() {
        int largestWidth = 0, totalHeight = 0;
        for (String text : menuItems) {
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            totalHeight += bounds.height()*1.5;
            System.out.println(bounds.height());
            if (bounds.width() > largestWidth)
                largestWidth = bounds.width();
        }

        width = (int) (largestWidth*1.5);
        height = totalHeight;
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    public List<String> getMenuItems() {
        return menuItems;
    }

    public void setMenuItem(int index, String string) {
        menuItems.set(index, string);
    }

    public Sprite getBackground() {
        return background;
    }

    public void setBackground(Sprite background) {
        this.background = background;
    }

    public int getBackgroundColor() {
        return bColor;
    }

    public int getLength() {
        return menuItems.size();
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIntersection(int[] input) {
        int selectedItem = -1, i = 0, processedHeight = 0;
        for (String text : menuItems) {
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            Rect a = new Rect(x, y + processedHeight, x + width, y + bounds.height() + processedHeight);
            if (a.contains(input[0], input[1])) {
                selectedItem = i;
            }

            processedHeight += bounds.height()*1.5;
            i++;
        }
        return selectedItem;
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

    public boolean isCentered() {
        return centered;
    }

    public boolean isClickable() {
        return clickable;
    }
}