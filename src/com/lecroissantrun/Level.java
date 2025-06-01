//Monikos
/*
 Holds a list of all entities(player, croissants, enemies, obstacles)
 Tells each one to update (move, check for collisions)
 Manages adding or removing things (like croissant being eaten)
 */
package com.lecroissantrun;

import java.awt.Graphics;
import java.util.ArrayList;

public class Level {

    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Croissant> croissants = new ArrayList<>();

    public Level() {

        loadLevelData();

    }

    private void loadLevelData() {

        obstacles.add(new Obstacle(100, 150));
        enemies.add(new Enemy(200, 100));
        croissants.add(new Croissant(150, 150, false));
        croissants.add(new Croissant(300, 200, true));

    }

    public void update(Player player) {

        for (Enemy e : enemies) e.update();
        croissants.removeIf(c -> player.getBounds().intersects(c.getBounds()) && player.collectCroissant(c));

    }

    public void draw(Graphics g) {

        for (Obstacle o : obstacles) o.draw(g);
        for (Enemy e : enemies) e.draw(g);
        for (Croissant c : croissants) c.draw(g);
    }
}

