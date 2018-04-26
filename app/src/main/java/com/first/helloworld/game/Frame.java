package com.first.helloworld.game;

import com.first.helloworld.engine.graphics.Sprite;
import com.first.helloworld.game.menu.MenuList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 28/06/2016.
 */
public class Frame {
    // Frame - screenPosition, current screen & last screen variables? An instance would hold all
    // sprites and menuLists for a menu screen (or game screen). Pass only current and last used
    // menuScreen sprites and menuLists to Screen.

    // tl;dr - Handles what should be displayed in various program states.

    private int x = 0, y = 0, sW = 0, sH = 0;
    List<Sprite> gameObjectSprites = new ArrayList<>();
    List<Sprite> iconSprites = new ArrayList<>();
    List<MenuList> menuLists = new ArrayList<>();

    public Frame(int x, int y, int sW, int sH) {
        this.x = x;
        this.y = y;
        this.sW = sW;
        this.sH = sH;
    }

    public List<Sprite> getGameObjectSprites() {
        return gameObjectSprites;
    }

    public List<Sprite> getIconSprites() {
        return iconSprites;
    }

    public List<MenuList> getMenuLists() {
        return menuLists;
    }

    public void addGameObjectSprite(Sprite gameObjectSprite) {
        gameObjectSprites.add(gameObjectSprite);
    }

    public void addIconSprite(Sprite iconSprite) {
        iconSprites.add(iconSprite);
    }

    public void addMenuList(MenuList menuList) {
        menuLists.add(menuList);
    }

    public void removeMenuList(int index) {
        menuLists.remove(index);
    }

    public int getX() {
        return x*sW;
    }

    public int getY() {
        return y*sH;
    }

    public int[] getPos() {
        return new int[]{x*sW, y*sH};
    }
}
