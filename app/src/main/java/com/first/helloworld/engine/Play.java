package com.first.helloworld.engine;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.first.helloworld.engine.graphics.Sprite;
import com.first.helloworld.game.Ball;
import com.first.helloworld.game.Collision;
import com.first.helloworld.game.Icon;
import com.first.helloworld.game.GameObject;
import com.first.helloworld.game.Match;
import com.first.helloworld.game.Paddle;
import com.first.helloworld.game.Frame;
import com.first.helloworld.game.menu.MenuList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 01/06/2016.
 */
public class Play {
    private int time, screenWidth, screenHeight, frameIndex = 0, prevFrameIndex = 0, x = 0, y = 0;
    private boolean paused;
    final float scaleFactorX, scaleFactorY;

    private Ball ball;
    private Paddle player, opponent;
    private Icon pause;
    List<Frame> frames = new ArrayList<>();

    Match match;

    // Hungry Hungry Hippos meets Halo 2 meets Dragonball Z.
    // Frame - screenPosition, current screen & last screen variables? An instance would hold all
    // sprites and menuLists for a menu screen (or game screen). Pass only current and last used
    // menuScreen sprites and menuLists to Screen.

    /* todo: powerup ideas - spawn multiple balls for opponent to hit, shoot powerup, bounce on
       todo: rising xpBar sprite.
     */

    // todo: use this engine to make b-mtron and slime soccer for Android; would require multiplayer.

    public Play(int screenWidth, int screenHeight, final float sFX, final float sFY) {
        scaleFactorX = sFX;
        scaleFactorY = sFY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        System.out.println("sFX, sFY: " + sFX + ", " + sFY);

        ball = new Ball(11);
        ball.setPos((screenWidth-ball.getWidth())/2, (screenHeight-ball.getHeight())/2);
        player = new Paddle(5, 30);
        player.setPos(12, (screenHeight-player.getHeight())/2);
        opponent = new Paddle(5, 30);
        opponent.setPos(screenWidth-12-opponent.getWidth(), (screenHeight-opponent.getHeight())/2);

        pause = new Icon(20, 20, false, true);
        pause.setPos(new int[]{(screenWidth-pause.getWidth())/2, 5});
        //pause.setBColor(Color.rgb(66, 140, 125));
        pause.setBColor(Color.rgb(101, 216, 251));
        //pause.setBColor(Color.rgb(155, 255, 251));
        pause.setFColor(Color.rgb(240, 198, 16));

        createFrames();
    }

    public GameObject[] tick(boolean isTouching, float[] input) {
        if (isTouching) {
            isTouching = !checkIconsTouched(new int[]{(int) input[0], (int) input[1]});
            iconActions();

            if (isTouching)
                isTouching = !checkMenuTouched(new int[]{(int) input[0], (int) input[1]});
            menuActions();
        }

        if (!paused) {
            time ++;

            if (isTouching) {
                if (input[0] <= screenWidth / 2)
                    player.setYTarget(input[1]);
                else
                    opponent.setYTarget(input[1]);
            }

            player.moveTowardsTarget();
            opponent.moveTowardsTarget();

            player.updatePos();
            opponent.updatePos();
            player.offScreen(screenWidth, screenHeight);
            opponent.offScreen(screenWidth, screenHeight);

            // todo: gametypes
            if (ball.isIntersecting(player)) {
                ball.setVel(new Collision().paddle(ball, player));
            }
            else if (ball.isIntersecting(opponent)) {
                ball.setVel(new Collision().paddle(ball, opponent));
            }

            ball.speedCheck(5);

            if (match!=null) {
                match.update();
                if (!match.isMatchOn()) {
                    if (match.getMatchResetTimer()==0) {
                        MenuList gameOver = new MenuList(Color.rgb(160, 160, 210), Color.argb(160, 255, 255, 255), 12, true, false);
                        gameOver.add(((match.getWinnerPaddle() < 1) ? player.getName() : opponent.getName()) + " wins!");
                        gameOver.setPos(new int[]{screenWidth / 2, screenHeight / 3});
                        gameOver.setFramePos(frames.get(1).getPos());
                        frames.get(1).getMenuLists().get(0).setVisible(false);
                        frames.get(1).addMenuList(gameOver);
                        match.setMatchResetTimer(180);
                    }
                    else if (!match.isMatchOn()&&match.getMatchResetTimer()==1) {
                        // todo: Play again or main menu, currently crashes when returning to main menu
                        frames.get(1).removeMenuList(3);
                        frames.get(1).getMenuLists().get(0).setVisible(true);

                        prevFrameIndex = frameIndex;
                        frameIndex = 0;
                        for (Icon icon : getIcons(prevFrameIndex)) {
                            if (icon != null) icon.setVisible(false);
                        }
                        match = null;
                    }
                }
                else if (!match.isRoundOn() && match.getRoundResetTimer() < 0) {
                    ball.setPos((screenWidth - ball.getWidth()) / 2, (screenHeight - ball.getHeight()) / 2);
                    ball.resetVel();
                    match.setRoundOn(true);
                }
            }

            if (ball.isScored(screenWidth, screenHeight)) {
                if (match != null && match.isRoundOn()) {
                    if (ball.getX() < screenWidth / 2) {
                        match.scored(1);
                        frames.get(1).getMenuLists().get(2).setMenuItem(0, opponent.getName()+": "+match.getScore(1));

                    }
                    else {
                        match.scored(0);
                        frames.get(1).getMenuLists().get(1).setMenuItem(0, player.getName()+": "+match.getScore(0));
                    }
                }
            }
            ball.updatePos();
        }

        return getObjects();
    }

    public GameObject[] getObjects() {
        GameObject[] objects = new GameObject[3];
        objects[0] = ball;
        objects[1] = player;
        objects[2] = opponent;
        return objects;
    }

    public void setObjects(GameObject[] gameObjects) {
        ball = new Ball(gameObjects[0]);
        player = new Paddle(gameObjects[1]);
        opponent = new Paddle(gameObjects[2]);
    }

    public Icon[] getIcons(int frameIndex) {
        if (frameIndex < 0) {
            Icon[] icons = new Icon[1];
            icons[0] = pause;
            return icons;
        }
        else if (frameIndex == 0) {
            return null;
        }
        else {
            Icon[] icons = new Icon[1];
            icons[0] = pause;
            return icons;
        }
    }

    public void setIcons(Icon[] icons) {
        pause = icons[0];
    }

    public boolean checkIconsTouched(int[] input) {
        boolean anIconWasTouched = false;
        for (Icon icon : getIcons(-1)) {
            if (icon.getVisible()) {
                icon.setTouched(icon.isIntersecting(input, icon.getLargeDimensions()));
                if (!anIconWasTouched)
                    anIconWasTouched = icon.getTouched();
            }
        }
        return anIconWasTouched;
    }

    public void iconActions() {
        if (pause.getTouched()) {
            setPaused(!paused);
            if (paused) {
                frames.get(1).getIconSprites().get(0).iconBackground(pause.getBColor());
                frames.get(1).getIconSprites().get(0).playIcon(pause.getFColor(), true);
            }
            else {
                frames.get(1).getIconSprites().get(0).iconBackground(pause.getBColor());
                frames.get(1).getIconSprites().get(0).pauseIcon(pause.getFColor(), true);
            }
        }
    }

    public void menuActions() {
        int f = 0;
        for (Frame frame: frames) {
            if (f <= 0) {
                if (frame.getMenuLists().get(0).getIndex() == 0) {
                    // Single Player
                    System.out.println("Clicked 'Single Player'.");
                }
                else if (frame.getMenuLists().get(0).getIndex() == 1) {
                    // Multiplayer
                    prevFrameIndex = frameIndex;
                    frameIndex = 1;
                    for (Icon icon : getIcons(prevFrameIndex)) {
                        icon.setVisible(false);
                    }
                    for (Icon icon : getIcons(frameIndex)) {
                        icon.setVisible(true);
                    }
                    System.out.println("Clicked 'Multiplayer'.");
                    match = new Match(3, 0, 2);

                    player.setName("Player 1");
                    opponent.setName("Player 2");
                    frames.get(1).getMenuLists().get(1).setMenuItem(0, player.getName()+": "+match.getScore(0));
                    frames.get(1).getMenuLists().get(2).setMenuItem(0, opponent.getName()+": "+match.getScore(1));

                    frames.get(1).getMenuLists().get(0).setMenuItem(0, "First to "+match.getScoreTo());
                    if (match.getTimeTo() > 0) {
                            frames.get(1).getMenuLists().get(0).setMenuItem(1, ""+match.getScoreTo());
                    }
                    else
                        frames.get(1).getMenuLists().get(0).setMenuItem(1, "");
                }
                else if (frame.getMenuLists().get(0).getIndex() == 2) {
                    // Online
                    System.out.println("Clicked 'Online'.");
                }
                frame.getMenuLists().get(0).setIndex(-1);
            }
            f++;
        }
    }

    private void createFrames() {
        frames = new ArrayList<>();

        Frame frame = new Frame(1, 0, screenWidth, screenHeight);

        MenuList mainMenu = new MenuList(Color.rgb(160, 160, 210), Color.argb(180, 81, 126, 167), 10, true, true);
        mainMenu.add("Single Player");
        mainMenu.add("Multiplayer");
        mainMenu.add("Online");
        mainMenu.setPos(new int[]{screenWidth/2, screenHeight/2});
        mainMenu.setBackground(new Sprite("menu button", Bitmap.createBitmap(mainMenu.getWidth(), mainMenu.getHeight()/mainMenu.getLength(), Bitmap.Config.ARGB_8888),
                mainMenu.getWidth(), mainMenu.getHeight()/mainMenu.getLength(), true));
        mainMenu.getBackground().menuItemBackground(mainMenu.getBackgroundColor());
        mainMenu.setFramePos(frame.getPos());
        frame.addMenuList(mainMenu);

        frames.add(frame);


        frame = new Frame(1, 1, screenWidth, screenHeight);

        frame.addGameObjectSprite(new Sprite("ball", Bitmap.createBitmap(ball.getWidth(), ball.getHeight(), Bitmap.Config.ARGB_8888),
                ball.getWidth(), ball.getHeight(), true));
        frame.getGameObjectSprites().get(0).ball(Color.rgb(255, 255, 209));
        ball.setFramePos(frame.getPos());

        frame.addGameObjectSprite(new Sprite("player", Bitmap.createBitmap(player.getWidth(), player.getHeight(), Bitmap.Config.ARGB_8888),
                player.getWidth(), player.getHeight(), true));
        frame.getGameObjectSprites().get(1).paddle(Color.LTGRAY);
        player.setFramePos(frame.getPos());

        frame.addGameObjectSprite(new Sprite("opponent", Bitmap.createBitmap(opponent.getWidth(), opponent.getHeight(), Bitmap.Config.ARGB_8888),
                opponent.getWidth(), opponent.getHeight(), true));
        frame.getGameObjectSprites().get(2).paddle(Color.DKGRAY);
        opponent.setFramePos(frame.getPos());

        frame.addIconSprite(new Sprite("pause", Bitmap.createBitmap(pause.getWidth(), pause.getHeight(), Bitmap.Config.ARGB_8888),
                pause.getWidth(), pause.getHeight(), true));
        frame.getIconSprites().get(0).iconBackground(pause.getBColor());
        frame.getIconSprites().get(0).pauseIcon(pause.getFColor(), true);
        pause.setFramePos(frame.getPos());

        MenuList matchInfo = new MenuList(Color.rgb(160, 160, 210), Color.argb(160, 255, 255, 255), 7, true, false);
        matchInfo.add("First to +match.getScoreTo()");
        matchInfo.add("match.getTime()");
        matchInfo.setPos(new int[]{screenWidth/2, screenHeight/4});
        matchInfo.setFramePos(frame.getPos());
        frame.addMenuList(matchInfo);

        MenuList playerScore = new MenuList(Color.rgb(160, 160, 210), Color.argb(160, 255, 255, 255), 7, false, false);
        playerScore.add("Player 1: 0");
        playerScore.setPos(new int[]{5, 10});
        playerScore.setFramePos(frame.getPos());
        frame.addMenuList(playerScore);

        MenuList opponentScore = new MenuList(Color.rgb(160, 160, 210), Color.argb(160, 255, 255, 255), 7, false, false);
        opponentScore.add("Opponent: 0");
        opponentScore.setPos(new int[]{(int) (screenWidth-opponentScore.getTextPaint().measureText("Opponent: 52")), 10});
        opponentScore.setFramePos(frame.getPos());
        frame.addMenuList(opponentScore);

        frames.add(frame);
    }

    public List<Sprite> getGameObjectSprites(int frameIndex) {
        if (frameIndex < -1) {
            List<Sprite> gameObjectSpritesReturn = frames.get(this.frameIndex).getGameObjectSprites();
            return gameObjectSpritesReturn;
        }
        else if (frameIndex < 0) {
            List<Sprite> gameObjectSpritesReturn = frames.get(0).getGameObjectSprites();
            for (int i = 1; i < frames.size(); i++) {
                gameObjectSpritesReturn.addAll(frames.get(i).getGameObjectSprites());
            }
            return gameObjectSpritesReturn;
        }
        else {
            List<Sprite> gameObjectSpritesReturn = frames.get(frameIndex).getGameObjectSprites();
            return gameObjectSpritesReturn;
        }
    }

    public List<Sprite> getIconSprites() {
        List<Sprite> iconSpritesReturn = frames.get(frameIndex).getIconSprites();

        if (frameIndex != prevFrameIndex) {
            iconSpritesReturn.addAll(frames.get(prevFrameIndex).getIconSprites());
        }

        return iconSpritesReturn;
    }

    public List<MenuList> getMenuLists() {
        if (frameIndex != prevFrameIndex) {
            List<MenuList> menuListReturn = frames.get(frameIndex).getMenuLists();
            if (prevFrameIndex!=1) { //todo: fix performance hit when also passing the menuList of the gameFrame.(pause too)
                menuListReturn.addAll(frames.get(prevFrameIndex).getMenuLists());
            }
            return menuListReturn;
        }
        else
            return frames.get(frameIndex).getMenuLists();
    }

    public boolean checkMenuTouched(int[] input) {
        boolean aMenuItemWasTouched = false;
        for (MenuList menuList : frames.get(frameIndex).getMenuLists()) {
            if (menuList.isClickable()) {
                menuList.setIndex(menuList.getIntersection(input));
                if (!aMenuItemWasTouched)
                    aMenuItemWasTouched = (menuList.getIndex() >= 0);
            }
        }
        return aMenuItemWasTouched;
    }

    public boolean getPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public String getDebugString() {
        return "frame("+getFrameOffsetX()+", "+getFrameOffsetY()+"), playerF:"+player.getFrameX()+", "+player.getFrameY()
                +"), playerPos("+player.getX()+", "+player.getY()+")";
    }

    public int getWidth() {
        return screenWidth;
    }

    public int getHeight() {
        return screenHeight;
    }

    public int getFrameOffsetX() {
        return frames.get(frameIndex).getX();
    }

    public int getFrameOffsetY() {
        return frames.get(frameIndex).getY();
    }

    public int getX() {
        int step = screenWidth/30;
        if (Math.abs(getFrameOffsetX()-x) >= step) {
            return x = (x>getFrameOffsetX())?x-step:x+step;
        }
        else return x;
    }

    public int getY() {
        int step = screenHeight/30;
        if (Math.abs(getFrameOffsetY()-y) >= step) {
            return y = (y > getFrameOffsetY()) ? y - step : y + step;
        }
        else return y;
    }
}