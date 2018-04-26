package com.first.helloworld.game;

import java.util.Random;

/**
 * Created by Tony on 02/06/2016.
 */
public class Ball extends GameObject {
    Random r;

    public Ball(int diameter) {
        super(diameter, diameter);
        resetVel();
    }

    public Ball(GameObject gameObject) {
        super(gameObject.getWidth(), gameObject.getHeight());
    }

    public boolean isScored(int screenWidth, int screenHeight) {
        if (getY() > screenHeight-getHeight()) {
            setYV(-Math.abs(getYV()));
        }
        else if (getY() <= 0) {
            setYV(Math.abs(getYV()));
        }
        if (getX() >= screenWidth*1.125) {
            return true;
        }
        else if (getX() <= -screenWidth/8) {
            return true;
        }
        return false;
    }

    public void resetVel() {
        r = new Random();
        setXV(4.5*((r.nextInt(2)<1)?1:-1));
        setYV(0.2*((r.nextInt(2)<1)?1:-1));
    }
}
