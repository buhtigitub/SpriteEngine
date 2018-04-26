package com.first.helloworld.engine.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;

import com.first.helloworld.R;
import com.first.helloworld.game.GameObject;
import com.first.helloworld.game.Icon;
import com.first.helloworld.game.menu.MenuList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tony on 18/03/2016.
 */
public class Screen extends Sprite {

    private Sprite background;
    private Map<String, Sprite> gameObjectSprites = new LinkedHashMap<>(), iconSprites = new LinkedHashMap<>();
    private boolean paused = false;
    private int offsetX = 0, offsetY = 0;

    public Screen(Context context, int width, int height) {
        super("screen", Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888), width, height, true);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        //opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

        background = new Sprite("background", BitmapFactory.decodeResource(context.getResources(), R.drawable.xpbar), 147, 11, false);
        background.setXV(3);
        background.setYV(3);
    }

    public void addGameObjectSprites(List<Sprite> spritesToAdd) {
        for (Sprite sprite : spritesToAdd) {
            gameObjectSprites.put(sprite.getName(), sprite);
        }
        for (Sprite sprite : gameObjectSprites.values()) {
            System.out.println("Game object '"+sprite.getName()+"' sprite received by engine.");
        }
    }

    public void addIconSprites(List<Sprite> spritesToAdd) {
        for (Sprite sprite : spritesToAdd) {
            iconSprites.put(sprite.getName(), sprite);
        }
        for (Sprite sprite : iconSprites.values()) {
            System.out.println("Icon '"+sprite.getName()+"' sprite received by engine.");
        }
    }

    public void updateIconSprite(List<Sprite> sprites) {
        for (Sprite sprite : sprites) {
            iconSprites.remove(sprite.getName());
            iconSprites.put(sprite.getName(), sprite);
        }
    }

    public void updateOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
	
	b:60000ms => ms:b
	100.73:60000ms, 

    /* 47743214
        Updating game objects, graphical effects & dynamic animation.
         */
    public void updatePositions(GameObject[] objects, Icon[] icons, boolean paused) {
        this.paused = paused;

        if (!paused) {
            background.updatePos();
            if (background.getX() >= getWidth() - background.getWidth()) {
                background.setXV(-2);
            } else if (background.getX() <= 0) {
                background.setXV(2);
            }
            if (background.getY() > getHeight() - background.getHeight()) {
                background.setYV(-3);
            } else if (background.getY() <= 0) {
                background.setYV(3);
            }
        }
        int i = 0;
        for (Sprite sprite : gameObjectSprites.values()) {
            sprite.setX((int) (objects[i].getX() + (objects[i].getFrameX() - offsetX)));
            sprite.setY((int) (objects[i].getY() + (objects[i].getFrameY() - offsetY)));
            i++;
        }

        i = 0;
        for (Sprite sprite : iconSprites.values()) {
            sprite.setX(icons[i].getX() + (icons[i].getFrameX() - offsetX));
            sprite.setY(icons[i].getY() + (icons[i].getFrameY() - offsetY));
            i++;
        }
    }

    public void drawTo(List<MenuList> menuLists, int frameOffsetX, int frameOffsetY) {
        if (!paused) fade(Color.rgb(135, 185, 205));

        draw(background, background.getX(), background.getY(), false);

        for (Sprite sprite : gameObjectSprites.values()) {
            draw(sprite, sprite.getX(), sprite.getY(), false);
        }

        for (Sprite sprite : iconSprites.values()) {
            draw(sprite, sprite.getX(), sprite.getY(), paused);
        }

        for (MenuList menuList : menuLists){
            if (menuList.getBackground() == null) {
                continue;
            }
            int drawnHeight = 5;
            for (String menuItem : menuList.getMenuItems()) {
                draw(menuList.getBackground(), menuList.getX() - menuList.getWidth()/2 + menuList.getFrameX() - frameOffsetX,
                        menuList.getY() + menuList.getFrameY() - frameOffsetY - menuList.getHeight()/2 + drawnHeight, true);
                Rect bounds = new Rect();
                menuList.getTextPaint().getTextBounds(menuItem, 0, menuItem.length(), bounds);
                drawnHeight += bounds.height()*1.5;
            }
        }
    }
}