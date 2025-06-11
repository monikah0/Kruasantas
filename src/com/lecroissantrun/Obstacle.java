package com.lecroissantrun;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Jore
 * Obstacle.java
 *
 * Static barrier (rock, wall, etc.) that sits at a fixed (x,y),
 * is drawn each frame, and blocks the player from passing through.
 */
public class Obstacle extends Entity {
    public Obstacle(int x, int y) {
        super(x, y, 32, 32);
    }

    @Override
    public void update() {
    }

   @Override
   public void render(Graphics g){
    g.setColor(Color.DARK_GRAY);
    g.fillRect(x, y, width, height);
   }
}
