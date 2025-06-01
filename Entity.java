package com.lecroissantrun;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Jore
 * A generic game object: knows its x/y position, size, bounding box,
 * and requires subclasses to implement update() and draw().
 */
public abstract class Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public Entity(int x, int y, int width, int height) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }

    public abstract void update();
    public abstract void draw(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
