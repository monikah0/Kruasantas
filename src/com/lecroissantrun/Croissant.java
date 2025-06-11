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
    public enum Type{
        SCORE, EXTRA_LIFE
    }

    private Type type;
    private boolean collected = false;

    public Croissant(int x, int y, int width, int height, Type type) {
        super(x, y, width, height);
        this.type = type;
    }
    public Croissant(int x, int y, boolean isLifeBoost) {
        this(x, y, 24, 24, isLifeBoost ? Type.EXTRA_LIFE : Type.SCORE);
    }



    public Type getType() {
        return type;
    }
    public boolean isLifeBoost() {
        return type == Type.EXTRA_LIFE;
    }
    public boolean isCollected() {
        return collected;
    }
    public void collect() {
        collected = true;
    }

    public void update() {
        // Croissants don't move, so no update logic needed
    }

    public void render(Graphics g) {
        if (!collected) {
            g.setColor(type == Type.SCORE ? Color.BLUE : Color.PINK);
            g.fillOval(x, y, width, height);
        }
    }
}
