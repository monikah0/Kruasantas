package com.lecroissantrun;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Enemy extends Entity {
    private int health = 3; // Reduced from 5 to make game more balanced
    private boolean dead = false;
    private long lastAttackTime = 0;
    private final long ATTACK_COOLDOWN = 1500; // 1.5 seconds between attacks
    private int originalX; // Store original position for patrolling
    private int direction = 1; // 1 for right, -1 for left
    private boolean isChasing = false;
    private final int CHASE_RANGE = 100;
    private final int PATROL_RANGE = 50;
    private Image enemyImage = new ImageIcon("assets/enemy.png").getImage();    

    public Enemy(int x, int y) {
        super(x, y, 50, 50);
        this.originalX = x;
        this.speed = 1; // Slower than player
    }

    @Override
    public void update() {
        if (dead) return;
        
        // Simple patrol behavior when not chasing
        if (!isChasing) {
            patrol();
        }
    }

    private void patrol() {
        x += direction * speed;
        
        // Turn around at patrol boundaries
        if (x <= originalX - PATROL_RANGE || x >= originalX + PATROL_RANGE) {
            direction *= -1;
        }
        
        // Keep within screen bounds
        if (x < 0) {
            x = 0;
            direction = 1;
        }
        if (x > 768) {
            x = 768;
            direction = -1;
        }
    }

    public void updateWithPlayer(Player player) {
        if (dead) return;
        
        int distanceToPlayer = Math.abs(player.getX() - x);
        
        // Check if player is within chase range
        if (distanceToPlayer <= CHASE_RANGE) {
            isChasing = true;
            // Move towards player
            if (player.getX() < x) {
                x -= speed;
                direction = -1;
            } else if (player.getX() > x) {
                x += speed;
                direction = 1;
            }
        } else {
            isChasing = false;
        }
        
        // Keep enemy on ground
        y = 415;
        
        // Try to attack player if close enough
        tryAttack(player);
    }

    @Override
    public void render(Graphics g, int cameraX, int cameraY) {
        if (dead) return;
        
        g.drawImage(enemyImage, x - cameraX, y - cameraY, width, height,null);
        // Health bar
        int healthBarWidth = 32;
        int healthBarHeight = 4;
        int healthBarY = y - 8;
        
        // Health bar background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x - cameraX, healthBarY - cameraY, healthBarWidth, healthBarHeight);
        
        // Health bar foreground
        g.setColor(Color.GREEN);
        int currentHealthWidth = (int)(healthBarWidth * ((double)health / 3.0));
        g.fillRect(x - cameraX, healthBarY - cameraY, currentHealthWidth, healthBarHeight);
        
        // Health bar border
        g.setColor(Color.BLACK);
        g.drawRect(x - cameraX, healthBarY - cameraY, healthBarWidth, healthBarHeight);
        
        // Direction indicator (small arrow)
        g.setColor(Color.YELLOW);
        if (direction == 1) {
            // Right arrow
            g.fillPolygon(new int[]{x + width - 8 - cameraX, x + width - 2 - cameraX, x + width - 8 - cameraX}, 
                         new int[]{y + 2, y + 6, y + 10}, 3);
        } else {
            // Left arrow
            g.fillPolygon(new int[]{x + 8 - cameraX, x + 2 - cameraX, x + 8 - cameraX}, 
                         new int[]{y + 2, y + 6, y + 10}, 3);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void takeDamage() {
        health--;
        if (health <= 0) {
            dead = true;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void tryAttack(Player player) {
        if (dead) return;

        long currentTime = System.currentTimeMillis();
        if (player.getBounds().intersects(getBounds()) && 
            currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            player.takeDamage();
            lastAttackTime = currentTime;
        }
    }

    public boolean isChasing() {
        return isChasing;
    }
}