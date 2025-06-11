//Monikos
/*
 Holds a list of all entities(player, croissants, enemies, obstacles)
 Tells each one to update (move, check for collisions)
 Manages adding or removing things (like croissant being eaten)
*/
package com.lecroissantrun;

import java.awt.Graphics;
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
        obstacles.add(new Obstacle(0, 430));

        int[] enemySpawnX = {100, 200, 300, 400, 500}; // evenly spaced
        for (int x : enemySpawnX) {
            enemies.add(new Enemy(x, 430));
        }

        int[] scoreCroissantX = {150, 250, 350, 450, 550}; // evenly spaced
        for (int x : scoreCroissantX) {
            croissants.add(new Croissant(x, 430, false));
        }

        int[] lifeCroissantX = {320, 530}; // life croissants
        for (int x : lifeCroissantX) {
            croissants.add(new Croissant(x, 430, true));
        }
    }

    public void update(Player player) {
        // Handle croissant collection
        Iterator<Croissant> iterator = croissants.iterator();
        while (iterator.hasNext()) {
            Croissant c = iterator.next();
            if (player.getBounds().intersects(c.getBounds()) && !c.isCollected()) {
                player.collectCroissant(c);
                c.collect();
                iterator.remove();
            }
        }

        // Handle enemies
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy e = enemyIterator.next();
            if (!e.isDead()) {
                e.updateWithPlayer(player);
 

                if (player.isAttacking() && player.getBounds().intersects(e.getBounds())) {
                    e.takeDamage();
                }

                if (!player.isAttacking() && player.getBounds().intersects(e.getBounds())) {
                    player.takeDamage();
                }

                e.tryAttack(player);
            }
        }

        player.resetAttack(); // Reset attack state after processing
    }

    public void render(Graphics g) {
        for (Obstacle o : obstacles) o.render(g);
        for (Enemy e : enemies) e.render(g);
        for (Croissant c : croissants) c.render(g);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
