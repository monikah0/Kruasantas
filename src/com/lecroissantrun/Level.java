<<<<<<< HEAD

=======
// Level.java - Fixed version
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
package com.lecroissantrun;

import java.awt.Graphics;
import java.awt.Rectangle;  // Added missing import
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Level {
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Croissant> croissants = new ArrayList<>();
    private int levelNumber;
    private int levelWidth;
    private int height = 570;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.levelWidth = 800 + (levelNumber - 1) * 400; // Level gets wider
        loadLevelData();
    }

    private void loadLevelData() {
<<<<<<< HEAD
    Random rand = new Random();

    // Generate random barrel stairs with life croissants
    int numStructures = levelNumber; // More structures per level
    int stairHeight = 4 + levelNumber;   // Taller stairs per level
    int barrelSize = 50;

    ArrayList<Integer> usedPositions = new ArrayList<>();

    for (int s = 0; s < numStructures; s++) {
        int baseX;

        while (true) {
            baseX = 100 + rand.nextInt(levelWidth - 300);
            boolean farEnough = true;

            for (int used : usedPositions) {
                if (Math.abs(baseX - used) < 100) {
                    farEnough = false;
                    break;
                }
            }

            if (farEnough) {
                usedPositions.add(baseX);
                break;
            }
        }

        // Add stair-shaped barrels
        for (int i = 0; i < stairHeight; i++) {
            int x = baseX + i * (barrelSize / 2);
            int y = 430 - (i + 1) * barrelSize;
            obstacles.add(new Obstacle(x, y));
        }

        // Croissant at the top
        int croissantX = baseX + (stairHeight - 1) * (barrelSize / 2);
        int croissantY = 430 - stairHeight * barrelSize - 24;
        croissants.add(new Croissant(croissantX, croissantY, true));
=======
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
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
    }

    // Enemies scale with level
    // Define boundaries for safe zone and boulangerie zone
    int safeZoneWidth = 200;
    int boulangerieZoneWidth = 200;

    // Enemies scale with level
    int enemyCount = 5 * levelNumber;
    for (int i = 0; i < enemyCount; i++) {
        int x;
        do {
            x = 100 + rand.nextInt(levelWidth - 200);
        } while (x < safeZoneWidth); // Skip spawning in safe zone
        enemies.add(new Enemy(x, 430));
    }

    // Score croissants scale with level
    int scoreCroissants = 4 + 2 * (levelNumber - 1);
    for (int i = 0; i < scoreCroissants; i++) {
        int x;
        do {
            x = 100 + rand.nextInt(levelWidth - 200);
        } while (x < safeZoneWidth || x > levelWidth - boulangerieZoneWidth); // Avoid start & end
        croissants.add(new Croissant(x, 400, false));
    }

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

<<<<<<< HEAD
    public void render(Graphics g, int cameraX, int cameraY) {
        // Render obstacles first (background)
        for (Obstacle obstacle : obstacles) {
            obstacle.render(g, cameraX, cameraY);
=======
    public void render(Graphics g) {
        // Render obstacles first (background)
        for (Obstacle obstacle : obstacles) {
            obstacle.render(g);
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
        }
        
        // Render croissants
        for (Croissant croissant : croissants) {
            if (!croissant.isCollected()) {
<<<<<<< HEAD
                croissant.render(g, cameraX, cameraY);
=======
                croissant.render(g);
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
            }
        }
        
        // Render enemies last (foreground)
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
<<<<<<< HEAD
                enemy.render(g, cameraX, cameraY);
=======
                enemy.render(g);
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
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
<<<<<<< HEAD

    public int getWidth() {
        return levelWidth;
    }
    public int getHeight(){
        return height;
    }
=======
>>>>>>> a1461594f8bc25d9c1aaa625640b272a6ca3b3bc
}
