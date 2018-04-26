package com.first.helloworld.engine;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.first.helloworld.Panel;
import com.first.helloworld.game.GameObject;

/**
 * Created by Tony on 07/03/2016.
 */
public class MainThread extends Thread {

    private int maxFps = 30, fps;
    private SurfaceHolder surfaceHolder;
    private Panel panel;
    private boolean running;
    private static Canvas canvas;
    private Play play;

    public MainThread(SurfaceHolder surfaceHolder, Panel panel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
    }

    @Override
    public void run() {
        long previousTime = System.nanoTime();
        double delta = 0;
        int frames = 0;
        int updates = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            canvas = null;
            double ns = 1000000000.0 / maxFps;
            long currentTime = System.nanoTime();
            delta += (currentTime - previousTime) / ns;
            previousTime = currentTime;
            if (frames > maxFps) {
                previousTime = System.nanoTime();
                delta = 0;
                frames = 0;
                updates = 0;
                timer = System.currentTimeMillis();
            }
            if (delta >= 1) {
                try {
                    canvas = this.surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        GameObject[] objects = play.tick(panel.getIsTouchingScreen(),
                                panel.getLastTouched());
                        panel.setIsTouchingScreen(false);
                        panel.update(objects, play.getIcons(-1), play.getIconSprites(),
                                play.getMenuLists(), play.getX(), play.getY(),
                                play.getPaused());
                        panel.setFps(fps);
                        panel.setDebugString(play.getDebugString());
                        this.panel.draw(canvas);
                    }
                }
                catch(Exception e) {}
                finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                        catch (Exception e) {e.printStackTrace();}
                    }
                }
                updates ++;
                delta --;
                frames++;
            }

            while (System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                fps = frames;
                frames = 0;
                updates = 0;
            }
        }
    }

    //@Override
    public void run2() {
        /*
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/ maxFps;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    GameObject[] objects = play.tick(panel.getIsTouchingScreen(), panel.getLastTouched());
                    panel.update(objects, play.getIcons(-1), play.getIconSprites(),
                            play.getMenuLists(), play.getFrameOffsetX(), play.getFrameOffsetY(),
                            play.getPaused());
                    panel.setFps(fps);
                    panel.setDebugString(play.getDebugString());
                    this.panel.draw(canvas);
                    panel.setIsTouchingScreen(false);
                }
            }
            catch(Exception e) {}
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            }
            catch(Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount ++;

            if (frameCount == maxFps) {
                //fps = 1000/(totalTime/frameCount/1000000);
                frameCount = 0;
                totalTime = 0;

                System.out.println("fps: "+ fps);
            }
        }
        */
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public Play getPlay() {
        return play;
    }

    public void createPlay(int w, int h, final float sFX, final float sFY) {
        if (play == null)
            play = new Play(w, h, sFX, sFY);
        panel.addSpritesToScreen(play.getGameObjectSprites(-1), true);
        panel.addSpritesToScreen(play.getIconSprites(), false);
    }

    public GameObject[] getPlayObjects() {
        return play.getObjects();
    }

    public void setPlayObjects(GameObject[] gameObjects) {
        play.setObjects(gameObjects);
    }
}