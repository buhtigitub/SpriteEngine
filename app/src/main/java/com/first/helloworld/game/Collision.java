package com.first.helloworld.game;

/**
 * Created by Tony on 02/07/2016.
 */
public class Collision {

    public double[] paddle(Ball ball, Paddle paddle) {
        double bMidX = ball.getMidX(), pMidX = paddle.getMidX(), bMidY = ball.getMidY(), pMidY = paddle.getMidY();
        if (bMidX == pMidX && ((bMidY < paddle.getY() && ball.getYV() > 0) || bMidY > paddle.getY() && ball.getYV() < 0)) {
            ball.setYV(Math.abs(ball.getYV()) * ((bMidY - pMidY > 0) ? 1 : -1));
            System.out.println("ball hit edge of paddle");
        }
        else {
            ball.setXV(Math.abs(ball.getXV()) * ((bMidX - pMidX > 0) ? 1 : -1));
            ball.setYV(ball.getYV() + paddle.getYV() / 5);
        }
        return new double[]{ball.getXV(), ball.getYV()};
    }
}
