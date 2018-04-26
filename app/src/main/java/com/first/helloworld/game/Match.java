package com.first.helloworld.game;

/**
 * Created by Tony on 02/07/2016.
 */
public class Match {
    private int scoreTo, timeTo, time, roundResetTimer, matchResetTimer = 0, winnerPaddle;
    private int[] score;
    private boolean roundOn, matchOn, ballTouched;

    public Match(int scoreTo, int timeTo, int players) {
        this.scoreTo = scoreTo;
        this.timeTo = timeTo;
        score = new int[players];
        matchOn = true;
        roundResetTimer = 90;
        matchResetTimer = 0;
    }

    public void update() {
        roundResetTimer--;
        if (matchResetTimer>1) {
            matchResetTimer--;
        }
        // todo: check time elapsed for end of game
    }

    public void scored(int paddle) {
        score[paddle]++;
        roundResetTimer = 90;
        roundOn = false;
        ballTouched = false;
        if (score[paddle]>=scoreTo) {
            matchOn = false;
            winnerPaddle = paddle;
        }
    }

    public int getScore(int paddle) {
        return score[paddle];
    }

    public int getScoreTo() {
        return scoreTo;
    }

    public int getTimeTo () {
        return timeTo;
    }

    public boolean isRoundOn() {
        return roundOn;
    }

    public void setRoundOn(boolean roundOn) {
        this.roundOn = roundOn;
    }

    public int getRoundResetTimer() {
        return roundResetTimer;
    }

    public boolean isMatchOn() {
        return matchOn;
    }

    public int getWinnerPaddle() {
        return winnerPaddle;
    }

    public int getMatchResetTimer() {
        return matchResetTimer;
    }

    public void setMatchResetTimer(int frames) {
        matchResetTimer = frames;
    }

    public void setBallTouched(boolean touched) {
        ballTouched = touched;
    }
}
