package com.first.helloworld;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import com.first.helloworld.engine.MainThread;
import com.first.helloworld.engine.Play;
import com.first.helloworld.engine.graphics.Screen;
import com.first.helloworld.engine.graphics.Sparks;
import com.first.helloworld.engine.graphics.Sprite;
import com.first.helloworld.game.GameObject;
import com.first.helloworld.game.Icon;
import com.first.helloworld.game.menu.MenuList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 07/03/2016.
 */

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

    //public static final int WIDTH = 800, HEIGHT = 337;
    public static final int WIDTH = 320, HEIGHT = 180;
    private MainThread thread;
    private Screen screen;
    private Sparks sparks;
    private boolean paused, touchedScreen;
    private float[] lastTouched;
    private int fps, frameOffsetX = 0, frameOffsetY = 0;
    private String debugString;

    List<MenuList> menuLists = new ArrayList<>();

    public Panel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        screen = new Screen(context, WIDTH, HEIGHT);
        screen.fill(0);

        lastTouched = new float[2];
        touchedScreen = false;

        setFocusable(true);
    }

    public void addSpritesToScreen(List<Sprite> spritesToAdd, boolean gameObjects) {
        if (gameObjects)
            screen.addGameObjectSprites(spritesToAdd);
        else
            screen.addIconSprites(spritesToAdd);
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        Play play = thread.getPlay();
        GameObject[] gameObjects = thread.getPlayObjects();

        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("Screen width", play.getWidth());
        bundle.putInt("Screen height", play.getHeight());
        bundle.putIntArray("Screen pixels", screen.getPixels());
        bundle.putInt("Number of objects", gameObjects.length);
        int i = 0;
        for(GameObject gameObject : gameObjects) {
            bundle.putString("Object"+i+" name", gameObject.getClass().toString());
            bundle.putInt("Object"+i+" width", gameObject.getWidth());
            bundle.putInt("Object"+i+" height", gameObject.getHeight());
            bundle.putDouble("Object"+i+" x", gameObject.getX());
            bundle.putDouble("Object"+i+" y", gameObject.getY());
            bundle.putDouble("Object"+i+" xV", gameObject.getXV());
            bundle.putDouble("Object"+i+" yV", gameObject.getYV());
            bundle.putDouble("Object"+i+" xA", gameObject.getXA());
            bundle.putDouble("Object"+i+" yA", gameObject.getYA());
            i++;
        }

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        System.out.println("onRestoreInstanceState() called.");
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            thread.createPlay(getScreenWidth(), getScreenHeight(), (float) getWidth() / getScreenWidth(), (float) getHeight() / getScreenHeight());
            screen.setPixels(bundle.getIntArray("Screen pixels"));

            GameObject[] gameObjects = new GameObject[bundle.getInt("Number of objects")];
            int i = 0;
            for(GameObject gameObject : gameObjects) {
                gameObject.setWidth(bundle.getInt("Object"+i+" width"));
                gameObject.setHeight(bundle.getInt("Object"+i+" height"));
                gameObject.setX(bundle.getDouble("Object"+i+" x"));
                gameObject.setY(bundle.getDouble("Object"+i+" y"));
                gameObject.setXV(bundle.getDouble("Object"+i+" xV"));
                gameObject.setYV(bundle.getDouble("Object"+i+" yV"));
                gameObject.setXA(bundle.getDouble("Object"+i+" xA"));
                gameObject.setYA(bundle.getDouble("Object"+i+" yA"));
                i++;
            }

            thread.setPlayObjects(gameObjects);

            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("Surface destroyed, thread to be joined.");
        //paused = true;

        boolean retry = true;//thread no longer joins when app is in background.
        while (!retry) {
            try {
                thread.join();
                thread.setRunning(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        lastTouched[0] = (getWidth()+30)/2;
        lastTouched[1] = (getHeight()-30)/2;

        thread.createPlay(getScreenWidth(), getScreenHeight(), (float) getWidth() / getScreenWidth(), (float) getHeight() / getScreenHeight());

        if (!thread.isRunning()) {
            thread.start();
            thread.setRunning(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchedScreen = true;
        lastTouched[0] = event.getX();
        lastTouched[1] = event.getY();

        Paint p =  new Paint();
        p.setColor(Color.WHITE);
        sparks = new Sparks(lastTouched[0], lastTouched[1], 15, p, getWidth(), getHeight());

        return super.onTouchEvent(event);
    }

    public float[] getLastTouched() {
        return new float[]{lastTouched[0]*WIDTH/getWidth(), lastTouched[1]*HEIGHT/getHeight()};
    }

    public boolean getIsTouchingScreen() {
        return touchedScreen;
    }

    public void setIsTouchingScreen(boolean isTouchingScreen) {
        this.touchedScreen = isTouchingScreen;
    }

    public void update(GameObject[] objects, Icon[] icons, List<Sprite> iconSprites,
                       List<MenuList> menuLists, int frameOffsetX, int frameOffsetY, boolean paused) {
        this.frameOffsetX = frameOffsetX;this.frameOffsetY=frameOffsetY;
        screen.updateOffset(frameOffsetX, frameOffsetY);
        screen.updatePositions(objects, icons, paused);
        screen.updateIconSprite(iconSprites);

        this.menuLists = menuLists;
        this.paused = paused;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = (float) getWidth() / WIDTH;
        final float scaleFactorY = (float) getHeight() / HEIGHT;
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            screen.drawTo(menuLists, frameOffsetX, frameOffsetY);

            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(screen.getImage(), screen.getX(), screen.getY(), null);

            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setTextSize(7);
            //canvas.drawText("debug: "+debugString, 5, 10, p);
            canvas.drawText(fps+" fps", 5, getScreenHeight()-8, p);

            canvas.restoreToCount(savedState);
        }

        for (MenuList menuList : menuLists){
            if (menuList.getVisible()) {
                int drawnHeight = 0;
                Paint p = new Paint(menuList.getTextPaint());
                p.setTextSize(p.getTextSize() * scaleFactorX);
                for (String menuItem : menuList.getMenuItems()) {
                    canvas.drawText(menuItem, (menuList.getX() + menuList.getFrameX() - frameOffsetX) * scaleFactorX
                                    - ((menuList.isCentered()) ? p.measureText(menuItem) / 2 : 0),
                            (menuList.getY() + menuList.getFrameY() - frameOffsetY) * scaleFactorY - menuList.getHeight() / 2 + drawnHeight, p);
                    Rect bounds = new Rect();
                    p.getTextBounds(menuItem, 0, menuItem.length(), bounds);
                    drawnHeight += bounds.height() * 1.75;
                }
            }
        }

        if (sparks != null) {
            sparks.update();
            for (int i = 0; i < sparks.getArrayLength(); i++) {
                canvas.drawLine(sparks.getX(i), sparks.getY(i), sparks.getX(i)+ sparks.getXV(i)*2,
                        sparks.getY(i)+sparks.getYV(i)*2, sparks.getPaint());
            }
        }
    }

    public int getScreenWidth() {
        return WIDTH;
    }

    public int getScreenHeight() {
        return HEIGHT;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setDebugString(String s) {
        debugString = s;
    }
}