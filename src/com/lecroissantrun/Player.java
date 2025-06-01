//Monika
/*
 This is the player
 Keeps track of your score and lives
 Moves left/right up/down
 Draws character
 */
package com.lecroissantrun;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Image;

public class Player extends Entity{

    private int score = 0;
    private int lives = 3;
    private Image image;

    public Player(int x, int y) {
        
        super(x, y, 32, 32);
        image = new ImageIcon("assets/player.png").getImage();

    }

    public void move(boolean[] keys) {

        if (keys[0]) y -= speed; //up
        if (keys[1]) y += speed; //down
        if (keys[2]) y -= speed; //left
        if (keys[3]) y += speed; //right

    }

    public void collectCroissant(Croissant c) {

        if (c.isLifeBoost()) lives++;
        else score += 10;

    }

    public void draw(Graphics g) {

        g.drawImage(image, x, y, null);

    }

    public Rectangle getBounds() {

        return new Rectangle(x, y, width, height);

    }

    //getters
    public int getScore() {

        return score;

    }

    public int getLives() {

        return lives;
        
    }
}
