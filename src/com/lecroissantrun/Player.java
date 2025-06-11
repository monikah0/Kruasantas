//Monika
/*
 This is the player
 Keeps track of your score and lives
 Moves left/right up/down
 Draws character
 */
package com.lecroissantrun;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Image;

public class Player extends Entity{

    private int score = 0;
    private int lives = 3;
    private Image image;
    private int maxHealth = 10;
    private int currentHealth = 10;
    private boolean attacking = false;
    private long lastAttackTime = 0;
    private long ATTACK_COOLDOWN = 500;


    public Player(int x, int y) {
        
        super(x, y, 32, 32);
        image = new ImageIcon("assets/player.png").getImage();

    }

    public void move(boolean[] keys) {

        if (keys[2]) x -= speed; //left
        if (keys[3]) x += speed; //right

        if (x<0){
            x=0;
        }
        if(x > 768) {
            x = 768;
        }
        y= 430; // Keep player at fixed y position

    }

    public void collectCroissant(Croissant c) {

        if (c.isLifeBoost()) lives++;
        else score += 10;

    }

    @Override
    public void update() {
    // No specific update logic for Player, movement is handled in move()
  }    

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);

        g.setColor(Color.GREEN);
        g.fillRect(x, y - 10, (int)(32*(currentHealth/(double)maxHealth)), 5); // Health bar background
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

    public void takeDamage(){
        currentHealth--;
        if(currentHealth <= 0) {
            lives--;
            currentHealth = maxHealth;
            x = 60;
            y = 430;
        }
    }

    public int getHealth(){
        return currentHealth;
    }
    public int getMaxHealth() {
        return maxHealth;
    }

    public void attack(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            attacking = true;
            lastAttackTime = currentTime;
        }
    }
    public boolean isAttacking() {
        return attacking;
    }
    public void resetAttack(){
        attacking = false;
    }

    public int getX() {
    return x;
}

    public int getY() {
        return y;
    }



}
