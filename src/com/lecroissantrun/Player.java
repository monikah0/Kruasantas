package com.lecroissantrun;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
<<<<<<< HEAD
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
=======
import java.awt.Rectangle;
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc

public class Player extends Entity {
    private int score = 0;
    private int lives = 3;
    private int maxHealth = 10;
    private int currentHealth = 10;
    private boolean attacking = false;
    private long lastAttackTime = 0;
    private final long ATTACK_COOLDOWN = 500;
<<<<<<< HEAD
    private double velocityY = 0;
    private boolean isJumping = false;
    private final double gravity = 0.5;
    private final double jumpStrength = -10;
    private Image playerImage = new ImageIcon("assets/player.png").getImage();
    private Image weaponImage = new ImageIcon("assets/baguette.png").getImage();
    private long swingStart = 0;
    private final int SWING_DURATION = 200;
    private Clip swingClip;




    public Player(int x, int y) {
        super(x, y, 50, 50);
        speed = 3; // Reasonable movement speed
        loadSwingSound();
    }

    private void loadSwingSound() {
    try {
        AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File("assets/swing.wav"));
        swingClip = AudioSystem.getClip();
        swingClip.open(audioInput);
        
        FloatControl gainControl = (FloatControl) swingClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(+3.0f); 


        
    } catch (Exception e) {
        System.err.println("Error loading swing sound: " + e.getMessage());
    }
}

    public void move(boolean[] keys, ArrayList<Obstacle> obstacles, int levelWidth) {
=======
    private final int GAME_WIDTH = 800;

    public Player(int x, int y) {
        super(x, y, 32, 32);
        speed = 3; // Reasonable movement speed
    }

    public void move(boolean[] keys) {
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
        int oldX = x;
        
        // Horizontal movement
        if (keys[2] && !keys[3]) { // A key (left)
            x -= speed;
        }
        if (keys[3] && !keys[2]) { // D key (right)
            x += speed;
        }
<<<<<<< HEAD

        velocityY += gravity;
        y += velocityY;

        Rectangle futureBounds = new Rectangle(x, y, width, height);;
        for (Obstacle obstacle : obstacles){
            Rectangle oBounds = obstacle.getBounds();

            boolean falling = velocityY > 0;
            if(falling && futureBounds.intersects(oBounds) && (y + height - velocityY) <= oBounds.y){
                y = oBounds.y - height; // Snap to the top of the obstacle
                velocityY = 0;
                isJumping = false;
                break;
            }
        }

        if (y >= 415){
            y = 415;
            velocityY = 0;
            isJumping = false;
        }
=======
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
        
        // Boundary checking
        if (x < 0) {
            x = 0;
        }
<<<<<<< HEAD
        if (x > levelWidth - width) {
            x = levelWidth - width;
        }
        
        
=======
        if (x > GAME_WIDTH - width) {
            x = GAME_WIDTH - width;
        }
        
        // Keep player on ground level
        y = 430;
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
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
<<<<<<< HEAD
    public void render(Graphics g, int cameraX, int cameraY) {
        Graphics2D g2d = (Graphics2D) g;
        
        
        
        g.drawImage(playerImage, x - cameraX, y - cameraY, width, height, null);

        //weapon rendering 
        

        // Draw baguette (positioned to the right of the player)
        int baguetteX = x + width - 4 - cameraX;;
        int baguetteY = y + 10 - cameraY;
        
        // If swinging, rotate the baguette
        if (attacking && System.currentTimeMillis() - swingStart < SWING_DURATION) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(baguetteX + 12, baguetteY + 4); // center of baguette
            g2.rotate(Math.toRadians(45)); // swing angle
            g2.drawImage(weaponImage, -12, -4, 40, 20, null);
            g2.dispose();
        } else {
            g.drawImage(weaponImage, baguetteX, baguetteY, 40,20, null);
        }


=======
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
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
        
        // Health bar
        int healthBarWidth = 32;
        int healthBarHeight = 4;
        int healthBarY = y - 8;
        
        // Health bar background
        g.setColor(Color.RED);
<<<<<<< HEAD
        g.fillRect(x - cameraX, healthBarY - cameraY, healthBarWidth, healthBarHeight);
=======
        g.fillRect(x, healthBarY, healthBarWidth, healthBarHeight);
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
        
        // Health bar foreground
        g.setColor(Color.GREEN);
        int currentHealthWidth = (int)(healthBarWidth * ((double)currentHealth / maxHealth));
<<<<<<< HEAD
        g.fillRect(x - cameraX, healthBarY - cameraY, currentHealthWidth, healthBarHeight);
        
        // Health bar border
        g.setColor(Color.BLACK);
        g.drawRect(x - cameraX, healthBarY - cameraY, healthBarWidth, healthBarHeight);
=======
        g.fillRect(x, healthBarY, currentHealthWidth, healthBarHeight);
        
        // Health bar border
        g.setColor(Color.BLACK);
        g.drawRect(x, healthBarY, healthBarWidth, healthBarHeight);
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
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
<<<<<<< HEAD
            swingStart = currentTime;
            playSound();
=======
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
        }
    }

    public void resetAttack() {
        attacking = false;
    }

    public void addScore(int points) {
        score += points;
    }
<<<<<<< HEAD

    public void jump() {
        if (!isJumping){
            velocityY = jumpStrength;
            isJumping = true;
        }
    }

    private void playSound() {
    if (swingClip != null) {
        if (swingClip.isRunning()) {
            swingClip.stop();
        }
        swingClip.setFramePosition(0); // rewind
        swingClip.start();
    }
}

public void addLife() {
    lives++;
}


=======
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
}