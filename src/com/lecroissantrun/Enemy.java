package com.lecroissantrun;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;

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

    public Enemy(int x, int y) {
        super(x, y, 32, 32);
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
        y = 430;
        
        // Try to attack player if close enough
        tryAttack(player);
    }

    @Override
    public void render(Graphics g) {
        if (dead) return;
        
        // Enemy body
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
        
        // Enemy outline
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, width, height);
        
        // Simple angry face
        g.setColor(Color.BLACK);
        g.fillRect(x + 6, y + 8, 6, 4);   // Left angry eye
        g.fillRect(x + 20, y + 8, 6, 4);  // Right angry eye
        g.fillRect(x + 12, y + 22, 8, 2); // Angry mouth
        
        // Health bar
        int healthBarWidth = 32;
        int healthBarHeight = 4;
        int healthBarY = y - 8;
        
        // Health bar background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, healthBarY, healthBarWidth, healthBarHeight);
        
        // Health bar foreground
        g.setColor(Color.GREEN);
        int currentHealthWidth = (int)(healthBarWidth * ((double)health / 3.0));
        g.fillRect(x, healthBarY, currentHealthWidth, healthBarHeight);
        
        // Health bar border
        g.setColor(Color.BLACK);
        g.drawRect(x, healthBarY, healthBarWidth, healthBarHeight);
        
        // Direction indicator (small arrow)
        g.setColor(Color.YELLOW);
        if (direction == 1) {
            // Right arrow
            g.fillPolygon(new int[]{x + width - 8, x + width - 2, x + width - 8}, 
                         new int[]{y + 2, y + 6, y + 10}, 3);
        } else {
            // Left arrow
            g.fillPolygon(new int[]{x + 8, x + 2, x + 8}, 
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