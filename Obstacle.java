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
    public Obstacle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, width, height);
    }
}

