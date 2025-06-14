package com.lecroissantrun;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Jore
 * A generic game object: knows its x/y position, size, bounding box,
 * and requires subclasses to implement update() and render().
 */
public abstract class Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int dx, dy;
    protected int speed = 5;

    public Entity(int x, int y, int width, int height) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
        this.dx     = 0;
        this.dy     = 0;
    }
     public java.awt.Rectangle getBounds(){
        return new java.awt.Rectangle(x, y, width, height);
     }

     public boolean intersects(Entity other) {
       return this.getBounds().intersects(other.getBounds());
   }

  public abstract void update();
  public abstract void render(java.awt.Graphics g, int cameraX, int cameraY);

  public void draw(java.awt.Graphics g) {
    render(g, 0,0);
  }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}



