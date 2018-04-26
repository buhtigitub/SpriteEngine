package com.first.helloworld.engine.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;

import java.util.Random;

/**
 * Created by Tony on 18/03/2016.
 */
public class Sprite {

    private String name;
    private Bitmap image;
    private int pixels[];
    private int x = 0, y = 0, xV = 0, yV = 0, width, height;
    private boolean visible;
    Random r;

    public Sprite(String name, Bitmap image, int width, int height, boolean hasAlpha) {
        this.name = name;

        this.image = image;
        this.image.setHasAlpha(hasAlpha);

        this.width = width;
        this.height = height;
        if (image.getWidth() != width)
            System.out.println("Width mismatch:" + image.getWidth() + ", but expected " + width);
        if (image.getHeight() != height)
            System.out.println("Height mismatch:" + image.getHeight() + ", but expected " + height);

        pixels = new int[width * height];

        System.out.println("Sprite created: " + this.image.toString() + ". w,h: " + width+"," + height);

        this.image.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    /*
    Overlay a sprite.
     */
    public void draw(Sprite sprite, int xOffset, int yOffset, boolean drawAlpha) {
        for (int y = 0; y < sprite.height; y++) {
            int yPixel = y + yOffset;

            if (yPixel < 0 || yPixel >= height) {
                continue;
            }

            for (int x = 0; x < sprite.width; x ++) {
                int xPixel = x + xOffset;

                if (xPixel < 0 || xPixel >= width) {
                    continue;
                }

                int alpha = sprite.pixels[x + y * sprite.width];

                if (alpha < 0 || !sprite.getImage().hasAlpha())
                    pixels[xPixel + yPixel * width] = alpha;
                else if (alpha == 0 && drawAlpha)
                    pixels[xPixel + yPixel * width] = Color.BLACK;

            }
        }
        setImageFromPixels();
    }

    public void updatePos() {
        x += xV;
        y += yV;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImageFromPixels() {
        image.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public int getPix(int i) {
        return pixels[i];
    }

    public void setPix(int i, int val) {
        pixels[i] = val;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[] getPos() {
        int[] pos = {x, y};
        return pos;
    }

    public void setPos(int[] pos) {
        this.x = pos[0];
        this.y = pos[1];
    }

    public void setPos(double[] pos) {
        this.x = (int) pos[0];
        this.y = (int) pos[1];
    }

    public int getXV() {
        return xV;
    }

    public int getYV() {
        return yV;
    }

    public void setXV(int xV) {
        this.xV = xV;
    }

    public void setYV(int yV) {
        this.yV = yV;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public void fill(int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                setPix(x + y * width, color);
            }
        }
        setImageFromPixels();
    }

    public void paddle(int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                setPix(x + y * width, roundedColorForPixelPaddle(color, x, y));
            }
        }
        setImageFromPixels();
    }

    public void fade(int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int og = getPix(x + y * width);
                if (getClass().isAssignableFrom(Screen.class) && (x < 17 || x >= width-17))
                    pixels[(x + y * width)] = Color.BLACK;
                else {
                    double irm = ((color >> 16) & 0xff) / 255.0, igm = ((color >> 8) & 0xff) / 255.0,
                            ibm = (color & 0xff) / 255.0;

                    int ro = (og >> 16) & 0xff, go = (og >> 8) & 0xff, bo = (og) & 0xff;

                    int r = (int) (ro*irm);
                    int g = (int) (go*igm);
                    int b = (int) (bo*ibm);

                    pixels[(x + y * width)] = (color >> 24)<<24 | r << 16 | g << 8 | b;
                }
            }
        }
        setImageFromPixels();
    }

    public void fade160702(int color, int colorBy) {/*
        // FIXME: 07/06/2016: Doesn't fade to non-black colors accurately.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int og = getPix(x + y * width);
                if (getClass().isAssignableFrom(Screen.class) && (x < 17 || x >= width-17))
                    pixels[(x + y * width)] = color;
                else {
                    int ao = (og >> 24) & 0xff, ro = (og >> 16) & 0xff, go = (og >> 8) & 0xff, bo = (og) & 0xff;
                    int an = (color >> 24) & 0xff, rn = (color >> 16) & 0xff, gn = (color >> 8) & 0xff, bn = (color) & 0xff;

                    int r = (int) (ro * 0.9 + rn * 0.1);
                    int g = (int) (go * 0.9 + gn * 0.1);
                    int b = (int) (bo * 0.9 + bn * 0.1);
                    int a = (int) (ao * 0.9 + an * 0.1);

                    pixels[(x + y * width)] = a << 24 | r << 16 | g << 8 | b;
                }
            }
        }
        setImageFromPixels();*/
    }

    public void ball(int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double xC = x-width/2, yC = y - height/2;

                if (width <= 7) {
                    if (xC * xC + yC * yC <= (width / 2) * (width / 2)) {
                        pixels[x + y * width] = roundedColorForPixelBright(color, x, y);
                    }
                    else {
                        pixels[x + y * width] = 0 << 16 | 0 << 8 | 0;
                    }
                }
                else {
                    if (xC * xC + yC * yC < (width / 2) * (width / 2)) {
                        pixels[x + y * width] = roundedColorForPixelBright(color, x, y);
                    }
                    else {
                        pixels[x + y * width] = 0 << 16 | 0 << 8 | 0;
                    }
                }
            }
        }
        setImageFromPixels();
    }

    public void pauseIcon(int color, boolean overlay) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((y == (int) (height * 0.2) || y == (int) (height * 0.8)) &&
                        ((x >= (int) (width * 0.3) && x <= (int) (width * 0.45)) || ((x >= (int) (width * 0.55) && x <= (int) (width * 0.7))))
                        || (y >= (int) (height * 0.2) && y <= (int) (height * 0.8)) &&
                        ((x == (int) (width * 0.3) || x == (int) (width * 0.45)) || ((x == (int) (width * 0.55) || x == (int) (width * 0.7)))))
                    pixels[x + y * width] = 0;
                else if ((y > height * 0.2 && y < height * 0.8) &&
                        ((x > width * 0.3 && x < width * 0.45) || (x > width * 0.55 && x < width * 0.7))) {
                    pixels[x + y * width] = roundedColorForPixel(color, x, y, 5);
                }
                else if (!overlay)
                    pixels[x + y * width] = 0 << 16 | 0 << 8 | 0;
            }
        }
        setImageFromPixels();
    }

    public void playIcon(int color, boolean overlay) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (((y == (int) (x / 1.8) || y == (int) (width - x / 1.8)) || x == (int) (width * 0.3))
                        && (y > height*0.15 && y < height*0.82 && x < width*0.9))
                    pixels[x + y * width] = 0;
                else if (y > x / 1.8 && y < width - x / 1.8 && x > width * 0.3)
                    pixels[x + y * width] = roundedColorForPixel(color, x, y, 5);
                else if (!overlay)
                    pixels[x + y * width] = 0 << 16 | 0 << 8 | 0;
            }
        }
        setImageFromPixels();
    }

    public void iconBackground(int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double xC = x-width/2, yC = y - height/2;
                if (//(width>12 && Math.pow(pixelDisToCentre(x, y), 1.25) < Math.abs(vectorRadius(x, y))) ||
                    ((xC * xC + yC * yC)*1.1 < (width / 2) * (width / 2)// && width <=12)
                    ))
                    pixels[x + y * width] = roundedColorForPixel(color, x, y, 5);
                    //pixels[x + y * width] = (color+roundedColorForPixel(color, x, y))/2;
                else
                    pixels[x + y * width] = 0 << 16 | 0 << 8 | 1;
            }
        }
        setImageFromPixels();
    }

    public void menuItemBackground(int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y > height*0.1+Math.abs(x-width/2)/10 && y < height*0.9-Math.abs(x-width/2)/10
                        && x > width*0.1+Math.abs(y-height/2)/10 && x < width*0.9-Math.abs(y-height/2)/10)
                    pixels[x + y * width] = roundedColorForPixel(color, x, y, 1);
                    //pixels[x + y * width] = color;
                else
                    pixels[x + y * width] = 0 << 16 | 0 << 8 | 1;
            }
        }
        setImageFromPixels();
    }

    public void noise() {
        r = new Random();

        for (int i = 0; i < width*height; i++) {
            setPix(i, r.nextInt());
        }
        setImageFromPixels();
    }

    public int checkColor(int c) {
        if (c > 255)
            return 255;
        else if (c < 0)
            return 0;
        return c;
    }

    public double pixelDisToCentre(int x, int y) {
        return Math.sqrt(Math.pow(width/2-x, 2)+Math.pow(height/2-y, 2));
    }

    public double vectorRadius(int x, int y) {
        /*double angle = Math.toDegrees(Math.atan2(y - height/2, x - width/2));
        System.out.println("name, x, y, angle from centre: " +name+","+x+","+y+","+angle);
        if(angle < 0){
            angle += 360;
        }*/

        ;

        //double[] edgeOfSprite = getIntersectionPoint(new double[][]{{x, y}, {(x-width/2)*pixelDisToCentre(x, y),
        //        (y-height/2)*pixelDisToCentre(x, y)}} );

        //System.out.println(name+"'s supposed intersection point: "+(int)edgeOfSprite[0]+", "+edgeOfSprite[1]);

        if (width > height)
            return Math.sqrt(Math.pow(height/2, 2)+Math.pow(height/2, 2));
        else
            return Math.sqrt(Math.pow(width/2, 2)+Math.pow(width/2, 2));

        //return Math.sqrt(Math.pow(width/2, 2)+Math.pow(height/2, 2));
    }

    public double[] getIntersectionPoint(double[][] line1) {
        Region bounds = new Region(0, 0, width, height);
        if (!bounds.getBounds().intersect((int)line1[0][0], (int)line1[0][0],
                (int)line1[0][0], (int)line1[0][0])) return null;
        double px = line1[0][0],
                py = line1[0][1],
                rx = line1[1][0]-px,
                ry = line1[1][1]-py;
        double qx = bounds.getBounds().left,
                qy = bounds.getBounds().top,
                sx = bounds.getBounds().right-qx,
                sy = bounds.getBounds().bottom-qy;

        double det = sx*ry - sy*rx;
        if (det == 0) {
            return null;
        } else {
            double z = (sx*(qy-py)+sy*(px-qx))/det;
            if (z==0 ||  z==1) return null;  // intersection at end point!
            double[] intersectionPoint = new double[] {(px+z*rx), (py+z*ry)};
            return intersectionPoint;
        }
    }

    public int roundedColorForPixel(int color, int x, int y, int intensity) {
        int a = (color >> 24), r = (color >> 16) & 0xff, g = (color >> 8) & 0xff, b = (color) & 0xff;

        double mul = pixelDisToCentre(x, y) / vectorRadius(x, y) *intensity;
        r+=7*intensity;g+=7*intensity;b+=7*intensity;
        r=(int)(r/mul);g=(int)(g/mul);b=(int)(b/mul);


        r=checkColor(r);g=checkColor(g);b=checkColor(b);

        return a << 24 | r << 16 | g << 8 | b;
    }

    public int roundedColorForPixelBright(int color, int x, int y) {
        double a = (color >> 24), r = (color >> 16) & 0xff, g = (color >> 8) & 0xff, b = (color) & 0xff;

        double mul = pixelDisToCentre(x, y) / vectorRadius(x, y) + 1;
        double r0=r/mul,g0=g/mul,b0=b/mul;

        r+=29;g+=75;b+=75;

        r=r*.5+r0*.5;g=g*.5+g0*.5;b=b*.5+b0*.5;

        r=checkColor((int)r);g=checkColor((int)g);b=checkColor((int)b);

        return (int)a << 24 | (int)r << 16 | (int)g << 8 | (int)b;
    }

    public int roundedColorForPixelPaddle(int color, int x, int y) {
        double a = (color >> 24), r = (color >> 16) & 0xff, g = (color >> 8) & 0xff, b = (color) & 0xff;

        //double mul = pixelDisToCentre(x, y)*30 / vectorRadius(x, y) + 1/2 + 1 -15;
        double mul = 1;
        if (y < width/2) {
            mul = Math.sqrt(Math.pow(x-width/2, 2)+Math.pow(y-width/2, 2)) / Math.sqrt(Math.pow(width/2, 2)+Math.pow(width/2, 2));
        }
        else if (y > height-(width-1)/2) {
            mul = Math.sqrt(Math.pow(x-height/2, 2)+Math.pow(y-height/2, 2)) / Math.sqrt(Math.pow(height/2, 2)+Math.pow(height/2, 2));
        }
        else {
            mul = (Math.abs(x-width/2) / vectorRadius(x, y));
        }
        //mul*=1.5;
        double r0=r/mul,g0=g/mul,b0=b/mul;

        r-=40;g-=40;b-=40;

        r=r*.9+r0*.1;g=g*.9+g0*.1;b=b*.9+b0*.1;

        r=checkColor((int)r);g=checkColor((int)g);b=checkColor((int)b);

        return (int)a << 24 | (int)r << 16 | (int)g << 8 | (int)b;
    }

    public int roundedColorForPixelPaddle_Save(int color, int x, int y) {
        double a = (color >> 24), r = (color >> 16) & 0xff, g = (color >> 8) & 0xff, b = (color) & 0xff;

        //double mul = pixelDisToCentre(x, y)*30 / vectorRadius(x, y) + 1/2 + 1 -15;
        double mul = Math.abs(x-width/2) / vectorRadius(x, y);
        double r0=r/mul,g0=g/mul,b0=b/mul;

        r+=25;g+=25;b+=25;

        r=r*.3+r0*.7;g=g*.3+g0*.7;b=b*.3+b0*.7;

        r=checkColor((int)r);g=checkColor((int)g);b=checkColor((int)b);

        return (int)a << 24 | (int)r << 16 | (int)g << 8 | (int)b;
    }
}