package com.first.helloworld.game;

/**
 * Created by Tony on 03/06/2016.
 */
public class Paddle extends GameObject {
    private double yTarget;
    private String name;

    public Paddle(int width, int height) {
        super(width, height);
    }

    public Paddle(GameObject gameObject) {
        super(gameObject.getWidth(), gameObject.getHeight());
    }

    public void setYTarget(double target) {
        yTarget = target;
    }
    
    public void moveTowardsTarget() {
        setYV((yTarget - getMidY())*0.198);
    }

    public void offScreen(int screenWidth, int screenHeight) {
        if (getX() > screenWidth-getWidth()) {
            setX(screenWidth-getWidth());
        }
        else if (getX() <= 0) {
            setX(0);
        }
        if (getY() > screenHeight-getHeight()) {
            setY(screenHeight-getHeight());
        }
        else if (getY() <= 0) {
            setY(0);
        }
    }

    public double getYTarget() {
        return yTarget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}