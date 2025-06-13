// Level.java - Fixed version
package com.lecroissantrun;

import java.awt.Graphics;
import java.awt.Rectangle;  // Added missing import
import java.util.ArrayList;
import java.util.Iterator;

public class Level {
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Croissant> croissants = new ArrayList<>();

    public Level() {
        loadLevelData();
    }

    private void loadLevelData() {
        // Add some obstacles (but not blocking the path completely)
        obstacles.add(new Obstacle(150, 398)); // Small platform
        obstacles.add(new Obstacle(300, 398));
        obstacles.add(new Obstacle(450, 398));
        obstacles.add(new Obstacle(600, 398));

        // Add enemies at strategic positions
        enemies.add(new Enemy(120, 430));
        enemies.add(new Enemy(250, 430));
        enemies.add(new Enemy(380, 430));
        enemies.add(new Enemy(520, 430));
        enemies.add(new Enemy(650, 430));

        // Add score croissants
        croissants.add(new Croissant(100, 400, false));  // Score croissant
        croissants.add(new Croissant(200, 400, false));
        croissants.add(new Croissant(350, 400, false));
        croissants.add(new Croissant(500, 400, false));
        croissants.add(new Croissant(620, 400, false));

        // Add life boost croissants (fewer and more valuable)
        croissants.add(new Croissant(280, 380, true));   // Life boost (higher up)
        croissants.add(new Croissant(480, 380, true));   // Life boost (higher up)
    }

    public void update(Player player) {
        // Update all enemies
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                enemy.update();
                enemy.updateWithPlayer(player);
            }
        }

        // Handle croissant collection
        Iterator<Croissant> croissantIterator = croissants.iterator();
        while (croissantIterator.hasNext()) {
            Croissant croissant = croissantIterator.next();
            if (!croissant.isCollected() && 
                player.getBounds().intersects(croissant.getBounds())) {
                player.collectCroissant(croissant);
                croissant.collect();
                croissantIterator.remove();
            }
        }

        // Handle combat - simplified to avoid duplicate attacks
        for (Enemy enemy : enemies) {
            if (enemy.isDead()) continue;
            
            // Enemy attacks player when they collide (but not during player attack)
            if (!player.isAttacking() && 
                player.getBounds().intersects(enemy.getBounds())) {
                enemy.tryAttack(player);
            }
        }
        
        // Update player
        player.update();
    }

    public void render(Graphics g) {
        // Render obstacles first (background)
        for (Obstacle obstacle : obstacles) {
            obstacle.render(g);
        }
        
        // Render croissants
        for (Croissant croissant : croissants) {
            if (!croissant.isCollected()) {
                croissant.render(g);
            }
        }
        
        // Render enemies last (foreground)
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                enemy.render(g);
            }
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Croissant> getCroissants() {
        return croissants;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }
    
    // Method to check if all enemies are defeated (bonus win condition)
    public boolean allEnemiesDefeated() {
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                return false;
            }
        }
        return true;
    }
    
    // Method to get remaining croissants count
    public int getRemainingCroissants() {
        int count = 0;
        for (Croissant croissant : croissants) {
            if (!croissant.isCollected()) {
                count++;
            }
        }
        return count;
    }
}
