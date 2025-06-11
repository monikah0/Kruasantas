//Monika
/* Enemies or obstacles that chase you
  Walks back and forth(or chases you)
  Hurts you (takes away a life)
  Knows how to draw itself on screen
 */
package com.lecroissantrun;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import com.lecroissantrun.Player;

public class Enemy extends Entity {

private Image image;
private int direction = 1; //1=right -1=left
private int health = 5;
private boolean dead = false;
private long lastAttackTime = 0;
private final long ATTACK_COOLDOWN = 1000;

public Enemy(int x, int y) {

    super(x, y, 32, 32);

}

@Override
    public void update() {
        // Minimal default update — does nothing
    }

    public void updateWithPlayer(Player player) {

    if(dead) return;
        if (player.getX() < x){
            x -= speed;

        }else if(player.getX() > x){
            x += speed;
        }
        if (Math.abs(player.getX() - x) < 5) {
        x = player.getX(); // stay only close
    }
}

@Override
public void render(Graphics g) {
    if (dead) return;
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);

        // Health bar
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 5, (int)(width * (health / 5.0)), 4);
    
}



public Rectangle getBounds() {

    return new Rectangle(x, y, width, height);

}

public void takeDamage() {
    health--;
    if (health <= 0) dead = true;
}

public boolean isDead() {
    return dead;
}

public void tryAttack(Player player) {
    if (dead) return;

    long currentTime = System.currentTimeMillis();
    if (player.getBounds().intersects(getBounds()) && currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
        player.takeDamage();
        lastAttackTime = currentTime;
    }
}


}