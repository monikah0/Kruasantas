package com.lecroissantrun;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Jore
 * Croissant.java
 *
 * A yummy treat that sits at a fixed (x,y).  When the player touches it,
 * it awards points (and possibly an extra life), then “disappears.”
 */
public class Croissant extends Entity {
    private final int points;
    private final boolean extraLife;
    private boolean collected;

    public Croissant(int x, int y, int width, int height, int points, boolean extraLife) {
        super(x, y, width, height);
        this.points    = points;
        this.extraLife = extraLife;
        this.collected = false;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        if (collected) {
            return;
        }
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, width, height);
        g.setColor(Color.DARK_GRAY);
        g.drawOval(x, y, width, height);
    }

    public void onPickup(Player player) {
        if (collected) {
            return;
        }
        collected = true;
        player.addScore(points);
        if (extraLife) {
            player.addExtraLife();
        }
    }

    public boolean isCollected() {
        return collected;
    }
}
