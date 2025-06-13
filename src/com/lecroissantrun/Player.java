package com.lecroissantrun;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player extends Entity {
    private int score = 0;
    private int lives = 3;
    private int maxHealth = 10;
    private int currentHealth = 10;
    private boolean attacking = false;
    private long lastAttackTime = 0;
    private final long ATTACK_COOLDOWN = 500;
    private final int GAME_WIDTH = 800;

    public Player(int x, int y) {
        super(x, y, 32, 32);
        speed = 3; // Reasonable movement speed
    }

    public void move(boolean[] keys) {
        int oldX = x;
        
        // Horizontal movement
        if (keys[2] && !keys[3]) { // A key (left)
            x -= speed;
        }
        if (keys[3] && !keys[2]) { // D key (right)
            x += speed;
        }
        
        // Boundary checking
        if (x < 0) {
            x = 0;
        }
        if (x > GAME_WIDTH - width) {
            x = GAME_WIDTH - width;
        }
        
        // Keep player on ground level
        y = 430;
    }

    public void collectCroissant(Croissant c) {
        if (c.isLifeBoost()) {
            lives++;
        } else {
            score += 10;
        }
    }

    @Override
    public void update() {
        // Reset attack state after cooldown
        if (attacking && System.currentTimeMillis() - lastAttackTime > 200) {
            attacking = false;
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Player body
        if (attacking) {
            g.setColor(Color.YELLOW); // Flash yellow when attacking
        } else {
            g.setColor(Color.WHITE);
        }
        g.fillRect(x, y, width, height);
        
        // Player outline
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        
        // Simple face
        g.setColor(Color.BLACK);
        g.fillOval(x + 8, y + 8, 4, 4);   // Left eye
        g.fillOval(x + 20, y + 8, 4, 4);  // Right eye
        g.fillOval(x + 14, y + 20, 4, 2); // Mouth
        
        // Health bar
        int healthBarWidth = 32;
        int healthBarHeight = 4;
        int healthBarY = y - 8;
        
        // Health bar background
        g.setColor(Color.RED);
        g.fillRect(x, healthBarY, healthBarWidth, healthBarHeight);
        
        // Health bar foreground
        g.setColor(Color.GREEN);
        int currentHealthWidth = (int)(healthBarWidth * ((double)currentHealth / maxHealth));
        g.fillRect(x, healthBarY, currentHealthWidth, healthBarHeight);
        
        // Health bar border
        g.setColor(Color.BLACK);
        g.drawRect(x, healthBarY, healthBarWidth, healthBarHeight);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters
    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAttacking() {
        return attacking;
    }

    // Actions
    public void takeDamage() {
        currentHealth--;
        if (currentHealth <= 0) {
            lives--;
            if (lives > 0) {
                // Respawn
                currentHealth = maxHealth;
                x = 60;
                y = 430;
            }
        }
    }

    public void attack() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            attacking = true;
            lastAttackTime = currentTime;
        }
    }

    public void resetAttack() {
        attacking = false;
    }

    public void addScore(int points) {
        score += points;
    }
}