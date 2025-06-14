package com.lecroissantrun;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import java.awt.Color;

/**
 * Jore
 * Obstacle.java
 *
 * Static barrier (rock, wall, etc.) that sits at a fixed (x,y),
 * is drawn each frame, and blocks the player from passing through.
 */
public class Obstacle extends Entity {
    private Image wineBarrelImage = new ImageIcon("assets/wine_barrel.png").getImage();
    public Obstacle(int x, int y) {
        super(x, y, 50, 50);
    }

    @Override
    public void update() {
    }

   @Override
   public void render(Graphics g, int cameraX, int cameraY) {
       // Draw the wine barrel image at the obstacle's position, adjusted for camera
       int width = 50; // Assuming a fixed width for the obstacle
       int height = 50; // Assuming a fixed height for the obstacle
    g.drawImage(wineBarrelImage,x - cameraX , y - cameraY, width, height, null);
   }
}
